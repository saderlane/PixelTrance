package saderlane.pixeltrance.logic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import saderlane.pixeltrance.PixelTrance;
import saderlane.pixeltrance.api.HypnoticSource;

/*
 * This processor scans all players and their items to apply trance and focus effects
 * from any source that implements the HypnoticSource interface.
 * Replaces item-specific handlers like PocketWatchServerEffects.
 */
public class HypnoticSourceProcessor {

    public static void tick(MinecraftServer server) {
        for (ServerWorld world : server.getWorlds()) {
            for (ServerPlayerEntity source : world.getPlayers()) {

                // Check both hands for potential hypnotic items
                for (ItemStack stack : new ItemStack[] {
                        source.getMainHandStack(), source.getOffHandStack()
                }) {
                    if (!(stack.getItem() instanceof HypnoticSource hypnotic)) continue;

                    // Loop through all players (or potentially all entities) to check for observers
                    for (ServerPlayerEntity observer : world.getPlayers()) {
                        if (observer == source) continue;

                        if (!hypnotic.shouldAffect(observer, source)) continue;

                        if (hypnotic.getFocusStrength() > 0f) {
                            PixelTrance.FOCUS_SOURCE_HANDLER.addSource(
                                    observer,
                                    new FocusEffectSource(source, hypnotic.getFocusStrength(), hypnotic.getFocusInterval())
                            );
                        }

                        if (hypnotic.getTranceStrength() > 0f) {
                            PixelTrance.TRANCE_SOURCE_HANDLER.addSource(
                                    observer,
                                    new TranceEffectSource(source, hypnotic.getTranceStrength(), hypnotic.getTranceInterval())
                            );
                        }
                    }
                }
            }
        }
    }
}
