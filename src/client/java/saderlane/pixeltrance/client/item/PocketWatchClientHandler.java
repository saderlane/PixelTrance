package saderlane.pixeltrance.client.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class PocketWatchClientHandler {

    public static void showFocusSessionMessage(boolean oldState, boolean newState) {
        // Only show a message if the session state changed
        if (oldState != newState && MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(
                    Text.literal(newState ? "Focus Session: Activated" : "Focus Session: Deactivated"),
                    true
            );
        }
    }

}
