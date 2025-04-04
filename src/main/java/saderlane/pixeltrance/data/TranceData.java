package saderlane.pixeltrance.data;

import java.lang.Math;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import net.minecraft.server.network.ServerPlayerEntity;
import saderlane.pixeltrance.network.TranceSyncS2CPacket;

/*
 * Stores trance-related data for a single player
 * Saved to and loaded from NBT
 */
public class TranceData {

    // Trance value for player
    private float trance = 0.0f;

    private final PlayerEntity owner;

    public TranceData(PlayerEntity owner) {
        this.owner = owner;
    }

    // Return current trance value
    public float getTrance() {
        return trance;
    }

    // Set trance value but does not allow it to exceed 0 and 100
    // 0 - fully awake
    // 100 - deeply hypnotized
    public void setTrance(float value) {
        this.trance = clamp(value,0f,100f);
        syncToPlayer();
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

    // Sync the server data with the player
    private void syncToPlayer() {
        if (owner instanceof ServerPlayerEntity serverPlayer) {
            TranceSyncS2CPacket.send(serverPlayer, trance);
        }
    }

}
