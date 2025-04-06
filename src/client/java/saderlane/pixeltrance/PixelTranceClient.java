package saderlane.pixeltrance;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import saderlane.pixeltrance.client.audio.TranceAudioHandler;
import saderlane.pixeltrance.data.ClientTranceState;
import saderlane.pixeltrance.item.PocketWatchClientHandler;
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

					// Update client-side trance cache on the render thread
					client.execute(() -> {
						// Capture old state BEFORE updating
						boolean oldState = ClientTranceState.getFocusSessionActive();

						ClientTranceState.setTrance(trance);
						ClientTranceState.setFocus(focus);
						ClientTranceState.setFocusSessionActive(focusSessionActive);

						PocketWatchClientHandler.showFocusSessionMessage(oldState, focusSessionActive);


					});
				}
		);

		ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
			if (minecraftClient.player == null) return;

			// Get current trance level from the synced client state
			float trance = ClientTranceState.getTrance();

			// Update audio based on current trance
			TranceAudioHandler.updateTranceSound(trance, 0);
		});


	}
}