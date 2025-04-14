package saderlane.pixeltrance.registry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import saderlane.pixeltrance.api.Inducer;
import saderlane.pixeltrance.api.MobInducerWrapper;

import java.util.ArrayList;
import java.util.List;

public class InducerRegistry {

    private static final List<ActiveInducer> activeInducers = new ArrayList<>();

    // === Called once per tick ===

    // Scan for inducer items in players' hands
    public static void scanHeldItems(List<? extends PlayerEntity> players) {
        for (PlayerEntity player : players) {
            for (Hand hand : Hand.values()) {
                ItemStack stack = player.getStackInHand(hand);
                var item = stack.getItem();
                if (item instanceof Inducer inducer) {
                    activeInducers.add(new ActiveInducer(player, inducer));
                }
            }
        }
    }

    // Add mob-based inducers (like pink sheep)
    public static void addMobInducer(Inducer inducer) {
        // We assume the inducer has world/context internally
        activeInducers.add(new ActiveInducer(null, inducer));
    }

    // === Access ===

    public static List<ActiveInducer> getActiveInducers() {
        return activeInducers;
    }

    public static void clear() {
        activeInducers.clear();
    }

    // === Data Structure ===

    public record ActiveInducer(PlayerEntity holder, Inducer inducer) { }
}
