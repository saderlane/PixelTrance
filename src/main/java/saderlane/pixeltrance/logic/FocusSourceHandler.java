package saderlane.pixeltrance.logic;

import net.minecraft.entity.LivingEntity;
import saderlane.pixeltrance.api.TranceDataAccess;

import java.util.*;

/*
 * FocusSourceHandler manages all active focus effect sources acting on a tranceable entity.
 * Each source builds focus at its own rate and interval (not necessarily every tick).
 * This allows dynamic sources like candles or mobs to scale differently or pulse over time.
 */
public class FocusSourceHandler {

    // Maps observer UUID → list of focus sources currently influencing them
    private final Map<UUID, List<FocusEffectSource>> activeSources = new HashMap<>();

    // Called once per tick for each tranceable entity
    public void tickForEntity(LivingEntity observer) {
        UUID id = observer.getUuid();

        // Only apply focus if the entity supports TranceData
        if (!(observer instanceof TranceDataAccess access)) return;
        var tranceData = access.getTranceData();

        List<FocusEffectSource> sources = activeSources.get(id);
        if (sources == null || sources.isEmpty()) return;

        float totalFocusThisTick = 0f;

        // Apply pulses from each source (if ready)
        for (FocusEffectSource source : sources) {
            if (source.shouldPulse()) {
                totalFocusThisTick += source.getFocusPerPulse();
            }
        }

        // Build focus if any pulse was applied
        if (totalFocusThisTick > 0f) {
            tranceData.tickFocus(true, null, totalFocusThisTick);
        }
    }

    // Add a focus source for an entity
    public void addSource(LivingEntity observer, FocusEffectSource source) {
        UUID id = observer.getUuid();
        activeSources.computeIfAbsent(id, key -> new ArrayList<>()).add(source);
    }

    // Remove a specific source from an entity
    public void removeSource(LivingEntity observer, FocusEffectSource source) {
        List<FocusEffectSource> sources = activeSources.get(observer.getUuid());
        if (sources != null) {
            sources.remove(source);
            if (sources.isEmpty()) {
                activeSources.remove(observer.getUuid());
            }
        }
    }

    // Clear all sources for an observer (e.g. on death or unload)
    public void clearSources(LivingEntity observer) {
        activeSources.remove(observer.getUuid());
    }
}
