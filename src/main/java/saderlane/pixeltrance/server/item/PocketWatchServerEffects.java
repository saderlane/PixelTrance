package saderlane.pixeltrance.server.item;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.item.ModItems;
import saderlane.pixeltrance.sound.TranceSounds;

public class PocketWatchServerEffects {

    private static int tickCounter = 0;

    // Called every server tick from mod initializer
    public static void tick(MinecraftServer server) {
        tickCounter++;

        for (ServerWorld world : server.getWorlds()) {
            for (ServerPlayerEntity player : world.getPlayers()) {

                // Must have trance data and be in an active focus session
                if (!(player instanceof TranceDataAccess tranceUser)) continue;
                var tranceData = tranceUser.getTranceData();

                if (!tranceData.isFocusSessionActive()) continue;

                // Must be holding the Pocket Watch
                boolean holdingWatch = false;
                for (Hand hand : Hand.values()) {
                    if (player.getStackInHand(hand).isOf(ModItems.POCKET_WATCH)) {
                        holdingWatch = true;
                        break;
                    }
                }

                if (!holdingWatch) continue;

                // Every 20 ticks (~1 second)
                if (tickCounter % 20 == 0)
                {
                    // Play the ticking sound for all players around this one
                    world.playSound(
                            null, // null = audible to all players near position
                            player.getX(), player.getY(), player.getZ(),
                            TranceSounds.WATCH_TICK,
                            SoundCategory.PLAYERS,
                            0.6f, // volume
                            1.0f  // pitch
                    );
                }


            }
        }

        // Reset tickCounter to avoid overflow (optional safety)
        if (tickCounter > 1000000) tickCounter = 0;

    }
}
