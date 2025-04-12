package saderlane.pixeltrance.data;

import java.lang.Math;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import net.minecraft.server.network.ServerPlayerEntity;
import saderlane.pixeltrance.logic.FocusLockConditions;
import saderlane.pixeltrance.network.TranceSyncS2CPacket;
import saderlane.pixeltrance.util.PTLog;

/*
 * Stores trance-related data for a single entity
 * Saved to and loaded from NBT
 */
public class TranceData {

    // Set trance final variables
    private static float TRANCE_DECAY_AMOUNT = 0.05f;
    private static final int TRANCE_DECAY_INTERVAL_TICKS = 40; // Every 2 seconds

    // Set focus final variables
    private static final float FOCUS_DECAY_RATE = 0.15f;
    private static final float FOCUS_BUILD_THRESHOLD = 30f;
    private static final float FOCUS_LOCK_THRESHOLD = 80f;


    private final LivingEntity owner; // Tracks the entity being tranced/focus locked


    // Variables:
    private float trance = 0.0f; // Trance value for entity
    private int ticksSinceLastDecay = 0;

    private float focus = 0f;              // Current focus value (0–100)
    private boolean focusLocked = false;   // Is the entity in focus lock state?
    private boolean focusSessionActive = false;

    private LivingEntity hypnoticTarget = null; //determine if the entity is a valid hypnotic target





    // Return the entity
    public TranceData(LivingEntity owner) {
        this.owner = owner;
    }


    // === Server->Client Syncing

    // Sync the server data with the player
    private void syncToPlayer() {
        if (owner instanceof ServerPlayerEntity serverPlayer) {
            TranceSyncS2CPacket.send(serverPlayer, trance, focus, focusSessionActive, hypnoticTarget);
        }
    }






    // === Trance Methods ===

    // Set the trance value between 0 and 100
    public void setTrance(float value) {
        this.trance = clamp(value,0f,100f);
        syncToPlayer();
    }

    // Return current trance value
    public float getTrance() {
        return trance;
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

    // == Trance Decay and Build Methods ==

    // Called once per tick to handle trance decay over time
    //  Must be hooked externally
    public void tick() {
        // If trance is 0, do nothing
        if (trance <= 0) return;
        ticksSinceLastDecay++;

        if (ticksSinceLastDecay >= TRANCE_DECAY_INTERVAL_TICKS) {
            ticksSinceLastDecay = 0;
            if (trance > 0f) {
                trance = clamp(trance - TRANCE_DECAY_AMOUNT, 0f, 100f);
                syncToPlayer();
            }
        }
    }




    // === Focus Lock Methods ===
    public void tickFocus(boolean shouldBuild, LivingEntity target, float focusRate) {
        float previousFocus = focus;

        if (shouldBuild)
        {

            PTLog.info(owner.getName().getString() + " is building focus. Target: " +
                    (target != null ? target.getName().getString() : "null") +
                    ", Focus: " + focus);


            focus = clamp(focus + focusRate, 0f, 100f);

            // Store the hypnotic target if it's valid and not already set
            if (hypnoticTarget == null && target != null)
            {
                this.hypnoticTarget = target;
            }
        } else
        {
            focus = clamp(focus - FOCUS_DECAY_RATE, 0f, 100f);
        }

        if (!shouldBuild || focus < FOCUS_BUILD_THRESHOLD || hypnoticTarget == null) {
            hypnoticTarget = null;
        }

        // Handle focus lock transitions
        if (!focusLocked && focus >= FOCUS_LOCK_THRESHOLD)
        {
            focusLocked = true;
            PTLog.info(owner.getName().getString() + " has entered Focus Lock!");
        }

        if (focusLocked && focus <= 0f)
        {
            focusLocked = false;
            PTLog.info(owner.getName().getString() + " has broken out of Focus Lock.");
        }

        if (focus != previousFocus)
        {
            syncToPlayer();
        }
    }


    public void setFocusSessionActive(boolean active) {
        this.focusSessionActive = active;
        syncToPlayer();
    }

    public boolean isFocusLocked() {
        return focusLocked;
    }

    public boolean isFocusSessionActive() {
        return focusSessionActive;
    }

    public float getFocus() {
        return focus;
    }




    // === Hypnotic Target Methods ===
    public void setHypnoticTarget(LivingEntity entity) {
        this.hypnoticTarget = entity;
    }

    public LivingEntity getHypnoticTarget() {
        return this.hypnoticTarget;
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
