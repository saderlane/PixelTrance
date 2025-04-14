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
					boolean focusLocked = buf.readBoolean();



					// Update client-side trance cache on the render thread
					client.execute(() -> {
						ClientTranceState.setTrance(trance);
						ClientTranceState.setFocus(focus);
						ClientTranceState.setFocusLocked(focusLocked);
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
			//ScreenPullHandler.tick();
		});


	}
}