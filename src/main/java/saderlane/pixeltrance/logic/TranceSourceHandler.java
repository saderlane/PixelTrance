package saderlane.pixeltrance.logic;

import net.minecraft.entity.LivingEntity;
import saderlane.pixeltrance.api.TranceDataAccess;

import java.util.*;

/*
 * Tracks and manages trance effect sources that apply trance over time.
 * Each source can apply trance at its own tick interval and with a custom amount.
 * This handler should be ticked per-entity each tick.
 */
public class TranceSourceHandler {

    // Stores a list of active trance sources per observer (the one receiving trance)
    private final Map<UUID, List<TranceEffectSource>> activeSources = new HashMap<>();

    // Called every tick for a tranceable entity (e.g. player or mob)
    public void tickForEntity(LivingEntity observer) {
        UUID id = observer.getUuid();

        // Only proceed if the entity supports TranceData
        if (!(observer instanceof TranceDataAccess access)) return;

        var tranceData = access.getTranceData();

        // Get all active trance sources affecting this entity
        List<TranceEffectSource> sources = activeSources.get(id);
        if (sources == null || sources.isEmpty()) return;

        float totalTranceThisTick = 0f;

        // Check each source: if it's ready to pulse, add its trance contribution
        for (TranceEffectSource source : sources) {
            if (source.shouldPulse()) {
                totalTranceThisTick += source.getTrancePerPulse();
            }
        }

        // If any trance was added this tick, apply it using tickTrance
        if (totalTranceThisTick > 0f) {
            tranceData.tickTrance(true); // will apply TRANCE_BUILD_RATE
        }
    }

    // Adds a trance source for the given observer
    public void addSource(LivingEntity observer, TranceEffectSource source) {
        UUID id = observer.getUuid();
        activeSources.computeIfAbsent(id, key -> new ArrayList<>()).add(source);
    }

    // Clears all trance sources for the given observer
    public void clearSources(LivingEntity observer) {
        activeSources.remove(observer.getUuid());
    }

    // Removes a specific trance source for the observer
    public void removeSource(LivingEntity observer, TranceEffectSource source) {
        List<TranceEffectSource> sources = activeSources.get(observer.getUuid());
        if (sources != null) {
            sources.remove(source);
            if (sources.isEmpty()) {
                activeSources.remove(observer.getUuid());
            }
        }
    }
}
