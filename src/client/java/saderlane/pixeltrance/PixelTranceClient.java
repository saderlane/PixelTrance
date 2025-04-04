package saderlane.pixeltrance;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import saderlane.pixeltrance.data.ClientTranceState;
import saderlane.pixeltrance.network.TranceSyncS2CPacket;


public class PixelTranceClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		// Register handler for trance_sync packets from the server
		ClientPlayNetworking.registerGlobalReceiver(
				TranceSyncS2CPacket.ID,
				(client, handler, buf, responseSender) -> {
					float trance = buf.readFloat();

					// Update client-side trance cache on the render thread
					client.execute(() -> {
						ClientTranceState.setTrance(trance);
					});
				}
		);

	}
}