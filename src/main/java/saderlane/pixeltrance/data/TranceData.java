package saderlane.pixeltrance.data;

import java.lang.Math;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

import net.minecraft.server.network.ServerPlayerEntity;
import saderlane.pixeltrance.network.TranceSyncS2CPacket;
import saderlane.pixeltrance.util.PTLog;

/*
 * Stores trance-related data for a single entity
 * Saved to and loaded from NBT
 */
public class TranceData {

    // Set trance final variables
    private static float TRANCE_DECAY_AMOUNT = 1f; // Amount trance decays
    private static final int TRANCE_DECAY_INTERVAL_TICKS = 40; // Every 2 seconds

    // Set focus final variables
    private static final float FOCUS_DECAY_RATE = 1f; // Amount focus decays
    private static final float FOCUS_DECAY_INTERVAL_TICKS = 40; // Every 2 seconds
    private static final float FOCUS_LOCK_THRESHOLD = 80f; // Threshold for when entity becomes focus locked


    private final LivingEntity subject; // Tracks the entity being tranced/focus locked


    // Variables:
    private float trance = 0.0f; // Trance value for entity
    private int ticksSinceLastTranceDecay = 0;
    private int ticksSinceTranceAdded = 1000;
    private int tranceDecayPauseTicks = 0; // How long to pause trance decay for
    private Entity tranceInducer; // Tracks the current trance inducer for the subject


    private float focus = 0f;              // Focus value for entity
    private boolean focusLocked = false;   // Is the entity in focus lock state
    private int ticksSinceLastFocusDecay = 0;
    private int ticksSinceFocusAdded = 1000;
    private Entity focusInducer; // Tracks the current focus inducer for the subject





    // Return the entity
    public TranceData(LivingEntity subject) {
        this.subject = subject;
    }


    // === Server->Client Syncing

    // Sync the server data with the player
    private void syncToPlayer() {
        if (subject instanceof ServerPlayerEntity serverPlayer) {
            TranceSyncS2CPacket.send(serverPlayer, trance, focus, focusLocked, tranceInducer, focusInducer);
        }
    }


    // Method for just calling a tick if something happens to trance and focus
    public void tick() {
        tickTranceDecay();
        tickFocusDecay();
    }




    // === Trance Methods ===

    // Set the trance value between 0 and 100
    public void setTrance(float value) {
        float clamped = clamp(value, 0f, 100f);

        // If we are newly reaching 100 trance
        if (this.trance < 100f && clamped >= 100f) {
            tranceDecayPauseTicks = 100; // e.g., 5 seconds pause at 20 ticks/sec
        }

        // Safety to clear trance inducer
        if (this.trance <= 0f) {
            this.tranceInducer = null;
        }

        this.trance = clamped;
        PTLog.info("Subject:" + subject.getName().getString() + " Trance: " + trance + " Inducer: " + tranceInducer);
        syncToPlayer();
    }

    // Return current trance value
    public float getTrance() {
        return trance;
    }

    // Increase trance by <amount>
    public void addTrance(float amount) {
        this.ticksSinceTranceAdded = 0;
        setTrance(trance + amount);
    }

    // Decrease trance by <amount>
    // May decay over time, from damage, player input, etc.
    public void tranceDecay(float amount) {
        setTrance(trance - amount);
    }

    // Trance Passive Decay:
    // Called every tick to apply passive trance decay
    public void tickTranceDecay() {
        ticksSinceTranceAdded++;

        if (tranceDecayPauseTicks > 0) {
            tranceDecayPauseTicks--;
            return; // Skip decay while paused
        }

        // Don’t decay if trance was added recently
        if (ticksSinceTranceAdded <= 20) return;

        ticksSinceLastTranceDecay++;
        if (ticksSinceLastTranceDecay >= TRANCE_DECAY_INTERVAL_TICKS) {
            tranceDecay(TRANCE_DECAY_AMOUNT);
            ticksSinceLastTranceDecay = 0;
        }
    }




    // === Focus Methods ===

    // Set focus value between 0 and 100 on the entity X
    public void setFocus(float value) {
        this.focus = clamp(value,0f,100f);
        //PTLog.info(subject.getName().getString() + " Focus: " + focus);

        // Check for focus lock exit
        boolean lockStatus = focusLocked;
        focusLocked = focus >= FOCUS_LOCK_THRESHOLD;

        // Clear inducer if focus drops too low
        if (focus < 30f) {
            focusInducer = null;
        }

        syncToPlayer();
    }

    // Return current focus value
    public float getFocus() {
        return focus;
    }

    // Increase focus by <amount>
    public void addFocus(float amount) {
        this.ticksSinceFocusAdded = 0;
        setFocus(focus + amount);
    }

    // Decrease focus by <amount>
    // May decay over time, from damage, player input, etc.
    public void focusDecay(float amount) {
        setFocus(focus - amount);
    }

    public boolean getFocusLocked() {
        return focusLocked;
    }

    // Focus Passive Decay:
    // Called every tick to apply passive focus decay and update lock state
    public void tickFocusDecay() {
        ticksSinceFocusAdded++;

        if (ticksSinceFocusAdded <= 20) return;

        ticksSinceLastFocusDecay++;
        if (ticksSinceLastFocusDecay >= FOCUS_DECAY_INTERVAL_TICKS) {
            focusDecay(FOCUS_DECAY_RATE);

            ticksSinceLastFocusDecay = 0;
        }
    }


    // === Inducer Methods ===
    public Entity getFocusInducer() {
        return focusInducer;
    }

    public Entity getTranceInducer() {
        return tranceInducer;
    }

    public void setFocusInducer(Entity inducer) {
        this.focusInducer = inducer;
        syncToPlayer(); // ensure the client knows about it
    }

    public void setTranceInducer(Entity inducer) {
        this.tranceInducer = inducer;
        syncToPlayer(); // ensure the client knows about it
    }












    // === NBT Functions ===

    // Saves trance data to NBT compound
    // Used when gave saves a player
    public NbtCompound writeToNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putFloat("Trance", trance);
        return nbt;
    }

    // Loads trance data from NBT
    // Used when game loads a player from disk
    public void readFromNbt(NbtCompound nbt) {
        if (nbt.contains("Trance")) {
            trance = nbt.getFloat("Trance");
        }
    }

    // Utility method to clamp a float value between min and max
    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

}
