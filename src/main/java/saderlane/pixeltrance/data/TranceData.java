package saderlane.pixeltrance.data;

import java.lang.Math;

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

    // Trance value for entity
    private float trance = 0.0f;

    private final LivingEntity owner;

    public TranceData(LivingEntity owner) {
        this.owner = owner;
    }

    // Return current trance value
    public float getTrance() {
        return trance;
    }

    // === Trance Value Modifiers ===

    // Set trance value but does not allow it to exceed 0 and 100
    // 0 - fully awake
    // 100 - deeply hypnotized
    public void setTrance(float value) {
        this.trance = clamp(value,0f,100f);
        syncToPlayer();
    }

    // Utility method to clamp a float value between min and max
    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    // Increase trance by <amount>
    public void addTrance(float amount) {
        setTrance(trance + amount);
    }

    // Decrease trance by <amount>
    // May decay over time, from damage, player input, etc.
    public void decay(float amount) {
        setTrance(trance - amount);
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


    // === Server->Client Syncing

    // Sync the server data with the player
    private void syncToPlayer() {
        if (owner instanceof ServerPlayerEntity serverPlayer) {
            TranceSyncS2CPacket.send(serverPlayer, trance, focus);
        }
    }


    // === Trance Decay and Cooldown ===
    private static final int DECAY_INTERVAL_TICKS = 40; // 20 ticks = 1 second
    private static final int DECAY_AMOUNT = 1; // Amount of trance lost per decay tick

    // Internal counter to track time between decay
    private int ticksSinceLastDecay = 0;

    // Called once per tick to handle trance decay over time
    //  Must be hooked externally
    public void tick() {
        // If trance is 0, do nothing
        if (trance <= 0) return;

        ticksSinceLastDecay++;

        // Apply decay every DECAY_INTERVAL_TICKS
        if (ticksSinceLastDecay >= DECAY_INTERVAL_TICKS)
        {
            decay(DECAY_AMOUNT);
            ticksSinceLastDecay = 0; // Reset counter
            PTLog.info("Trance for " + owner.getName().getString() + " is now " + trance);
        }
    }



    // === Focus Lock Fields ===
    private float focus = 0f;              // Current focus value (0–100)
    private boolean focusLocked = false;   // Is the entity in focus lock state?

    private static final float FOCUS_LOCK_THRESHOLD = 100f;
    private static final float FOCUS_BUILD_RATE = 0.7f; // Change per tick when conditions are met
    private static final float FOCUS_DECAY_RATE = 0.5f; // If broken (no line of sight, etc.)

    public void tickFocus(boolean shouldBuild) {
        if (shouldBuild) {
            focus = clamp(focus + FOCUS_BUILD_RATE, 0f, 100f);
            // PTLog.info(owner.getName().getString() + " focus: " + focus + " (locked?: " + focusLocked);
        } else {
            focus = clamp(focus - FOCUS_DECAY_RATE, 0f, 100f);
        }

        if (!focusLocked && focus >= FOCUS_LOCK_THRESHOLD) {
            focusLocked = true;
            PTLog.info(owner.getName().getString() + " has entered Focus Lock!");
            // Additional behavior here: increase trance gain, etc.
        }

        if (focusLocked && focus <= 0f) {
            focusLocked = false;
            // PTLog.info(owner.getName().getString() + " has broken out of Focus Lock.");
        }
    }

    public boolean isFocusLocked() {
        return focusLocked;
    }

    public float getFocus() {
        return focus;
    }

}
