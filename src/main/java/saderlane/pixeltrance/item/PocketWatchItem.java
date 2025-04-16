package saderlane.pixeltrance.item;


import net.minecraft.item.Item;
import saderlane.pixeltrance.api.Inducer;


public class PocketWatchItem extends Item implements Inducer {

    public PocketWatchItem(Settings settings) {
        super(settings);
    }

    // === Inducer: Focus Properties ===
    @Override
    public float getFocusRate() {
        return 1f; // Builds  focus per interval
    }

    @Override
    public int getFocusInterval() {
        return 20; // Every X ticks
    }

    // === Inducer: Trance Properties ===
    @Override
    public float getTranceRate() {
        return 1f;
    }

    @Override
    public int getTranceInterval() {
        return 20;
    }


    // === Inducer: Custom Properties ===
    @Override
    public boolean requiresLineOfSight() {
        return true;
    }



}
