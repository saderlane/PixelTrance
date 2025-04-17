package saderlane.pixeltrance.client.item;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import saderlane.pixeltrance.client.audio.LoopingTickingSoundInstance;
import saderlane.pixeltrance.network.ItemActivationS2CPacket;
import saderlane.pixeltrance.sound.TranceSounds;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Handles client-side logic for the Pocket Watch item:
/*
 * Activation Sound
 * Ticking audio loop
 * Action bar message
*/
public class PocketWatchClientHandler {

    // Tracks which players (by UUID) have active ticking sounds.
    private static final Map<UUID, LoopingTickingSoundInstance> activeTickingSounds = new HashMap<>();

    // Registers the packet receiver that handles Pocket Watch activation sync.
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                ItemActivationS2CPacket.ID,
                (client, handler, buf, responseSender) -> {
                    // Read data from packet
                    Hand hand = buf.readEnumConstant(Hand.class);
                    boolean isActive = buf.readBoolean();

                    // Apply on main client thread
                    client.execute(() -> {
                        PlayerEntity player = client.player;
                        if (player == null) return;

                        // Display activation/deactivation message
                        var stack = player.getStackInHand(hand);
                        String itemName = stack.getName().getString();
                        player.sendMessage(
                                net.minecraft.text.Text.literal(itemName + (isActive ? " Activated" : " Deactivated")),
                                true
                        );

                        // Handle ticking sound logic
                        if (isActive) {
                            startTicking(player);
                        } else {
                            stopTicking(player);
                        }
                    });
                }
        );
    }

    // Starts the ticking sound for a given inducer (player).
    // Only plays the sound if one isn’t already active for that player.
    private static void startTicking(PlayerEntity inducer) {
        UUID id = inducer.getUuid();
        SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();

        // Don't start another if it's already playing
        if (activeTickingSounds.containsKey(id)) return;

        // Create and play a new positional ticking sound instance
        var sound = new LoopingTickingSoundInstance(TranceSounds.WATCH_TICK, inducer, 0.6f);
        activeTickingSounds.put(id, sound);
        soundManager.play(sound);
    }

    // Stops the ticking sound for a given inducer (player), if active.
    private static void stopTicking(PlayerEntity inducer) {
        UUID id = inducer.getUuid();
        LoopingTickingSoundInstance sound = activeTickingSounds.remove(id);
        if (sound != null) {
            sound.stop(); // Stops the sound instance
        }
    }

    // Stops all ticking sounds and clears the tracking map.
    public static void clearAll() {
        for (LoopingTickingSoundInstance sound : activeTickingSounds.values()) {
            sound.stop();
        }
        activeTickingSounds.clear();
    }


}