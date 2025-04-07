package saderlane.pixeltrance;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.entity.LivingEntity;
import saderlane.pixeltrance.client.audio.TranceAudioHandler;
import saderlane.pixeltrance.client.data.ClientTranceState;
import saderlane.pixeltrance.client.item.PocketWatchClientHandler;
import saderlane.pixeltrance.client.visual.ScreenPullHandler;
import saderlane.pixeltrance.network.TranceSyncS2CPacket;


public class PixelTranceClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		// Register handler for trance_sync packets from the server
		ClientPlayNetworking.registerGlobalReceiver(
				TranceSyncS2CPacket.ID,
				(client, handler, buf, responseSender) -> {
					float trance = buf.readFloat();
					float focus = buf.readFloat();
					boolean focusSessionActive = buf.readBoolean();
					int targetId = buf.readInt();


					// Update client-side trance cache on the render thread
					client.execute(() -> {
						// Capture old state BEFORE updating
						boolean oldState = ClientTranceState.getFocusSessionActive();

						ClientTranceState.setTrance(trance);
						ClientTranceState.setFocus(focus);
						ClientTranceState.setFocusSessionActive(focusSessionActive);

						// Resolve entity by ID
						if (targetId != -1 && client.world != null) {
							var entity = client.world.getEntityById(targetId);
							if (entity instanceof LivingEntity living) {
								ClientTranceState.setHypnoticTarget(living);
							}
						} else {
							ClientTranceState.setHypnoticTarget(null);
						}

						PocketWatchClientHandler.showFocusSessionMessage(oldState, focusSessionActive);


					});
				}
		);


		// Register client tick event
		ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
			if (minecraftClient.player == null) return;

			// Get current trance and focus level from the synced client state
			float trance = ClientTranceState.getTrance();
			float focus = ClientTranceState.getFocus();

			// Update audio based on current trance
			TranceAudioHandler.updateTranceSound(trance, 0);

			// === Screen Pull for Focus Lock ===
			ScreenPullHandler.tick();
		});


	}
}