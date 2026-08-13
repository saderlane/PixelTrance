package net.saderlane.pixeltrance.hypno;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import net.saderlane.pixeltrance.dataattachment.ModData;
import net.saderlane.pixeltrance.networking.packet.HypnoDataS2C;

public final class HypnoData {

    public static final int MIN = 0;
    public static final int MAX = 100;

    private HypnoData() {}

    // ======= Trance =======

    public static int getTrance(ServerPlayer player) {
        return player.getData(ModData.TRANCE);
    }

    public static void setTrance(ServerPlayer player, int value) {
        int clamped = Mth.clamp(value, MIN, MAX);
        if (clamped == getTrance(player)) return;   // no change -> don't burn a packet
        player.setData(ModData.TRANCE, clamped);
        sync(player);
    }

    public static void addTrance(ServerPlayer player, int amount) {
        setTrance(player, getTrance(player) + amount);
    }

    public static void subTrance(ServerPlayer player, int amount) {
        setTrance(player, getTrance(player) - amount);
    }

    // ======= Focus =======

    public static int getFocus(ServerPlayer player) {
        return player.getData(ModData.FOCUS);
    }

    public static void setFocus(ServerPlayer player, int value) {
        int clamped = Mth.clamp(value, MIN, MAX);
        if (clamped == getFocus(player)) return;
        player.setData(ModData.FOCUS, clamped);
        sync(player);
    }

    public static void addFocus(ServerPlayer player, int amount) {
        setFocus(player, getFocus(player) + amount);
    }

    public static void subFocus(ServerPlayer player, int amount) {
        setFocus(player, getFocus(player) - amount);
    }

    // ======= Sync =======

    /**
     * Push the authoritative values down to the owning client.
     * Called automatically by every setter; call it manually only at the
     * points where the client's mirror starts out empty (login, respawn,
     * dimension change).
     */
    public static void sync(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new HypnoDataS2C(getTrance(player), getFocus(player)));
    }
}