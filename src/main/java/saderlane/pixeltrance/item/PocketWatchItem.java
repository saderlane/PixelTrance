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
        return 0.5f; // Builds 0.5 focus per interval
    }

    @Override
    public int getFocusInterval() {
        return 10; // Every 10 ticks (~0.5 seconds)
    }

    // === Inducer: Trance Properties ===
    @Override
    public float getTranceRate() {
        return 0.0f; // Not active yet
    }

    @Override
    public int getTranceInterval() {
        return 20;
    }



}
