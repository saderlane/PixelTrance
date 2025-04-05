package saderlane.pixeltrance;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import saderlane.pixeltrance.client.audio.TranceAudioHandler;
import saderlane.pixeltrance.data.ClientTranceState;
import saderlane.pixeltrance.network.TranceSyncS2CPacket;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import com.mojang.blaze3d.systems.RenderSystem;


public class PixelTranceClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		// Register handler for trance_sync packets from the server
		ClientPlayNetworking.registerGlobalReceiver(
				TranceSyncS2CPacket.ID,
				(client, handler, buf, responseSender) -> {
					float trance = buf.readFloat();
					float focus = buf.readFloat();

					// Update client-side trance cache on the render thread
					client.execute(() -> {
						ClientTranceState.setTrance(trance);
						ClientTranceState.setFocus(focus);
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