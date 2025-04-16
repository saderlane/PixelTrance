package saderlane.pixeltrance;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.util.Hand;
import saderlane.pixeltrance.client.audio.TranceAudioHandler;
import saderlane.pixeltrance.client.data.ClientTranceState;
import saderlane.pixeltrance.client.visual.ClientScreenPullHandler;
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

					boolean hasInducer = buf.readBoolean();
					Integer inducerId = hasInducer ? buf.readInt() : null;

					// Update client-side trance cache on the render thread
					client.execute(() -> {
						ClientTranceState.setTrance(trance);
						ClientTranceState.setFocus(focus);
						ClientTranceState.setFocusLocked(focusLocked);

						if (inducerId != null) {
							ClientTranceState.setInducerEntityId(inducerId);
						} else {
							ClientTranceState.clearInducerEntityID();
						}
					});
				}
		);

		// Register for handling packets from item sync
		ClientPlayNetworking.registerGlobalReceiver(
				saderlane.pixeltrance.network.ItemActivationS2CPacket.ID,
				(client, handler, buf, responseSender) -> {
					Hand hand = buf.readEnumConstant(Hand.class);
					boolean isActive = buf.readBoolean();

					client.execute(() -> {
						var player = client.player;
						if (player == null) return;

						var stack = player.getStackInHand(hand);
						String itemName = stack.getName().getString();
						player.sendMessage(
								net.minecraft.text.Text.literal(itemName + (isActive ? " Activated" : " Deactivated")),
								true
						);
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

		});

		// Initialize screen pull effect
		ClientScreenPullHandler.init();

	}
}