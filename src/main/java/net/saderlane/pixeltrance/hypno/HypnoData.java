package net.saderlane.pixeltrance.hypno;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.saderlane.pixeltrance.dataattachment.ModData;
import net.saderlane.pixeltrance.networking.packet.HypnoDataS2C;

public final class HypnoData {

    // Min and max trance/focus
    public static final int MIN = 0;
    public static final int MAX = 100;

    public static final int INFLUENCE_GRACE_TICKS = 10;

    public static final int DECAY_INTERVAL = 10; // Ticks per decay

    // Temporarily here, will be specific to each source's decay
    private static final int FOCUS_DECAY = 5;
    private static final int TRANCE_DECAY = 2;

    private HypnoData() {}

    // ======= Trance =======

    public static int getTrance(LivingEntity subject) {
        return subject.getData(ModData.TRANCE);
    }

    public static void setTrance(LivingEntity subject, int value) {
        int clamped = Mth.clamp(value, MIN, MAX);
        if (clamped == getTrance(subject)) return;   // no change -> don't burn a packet
        subject.setData(ModData.TRANCE, clamped);
        sync(subject);
    }

    public static void addTrance(LivingEntity subject, int amount) {
        lastInfluenced(subject);
        setTrance(subject, getTrance(subject) + amount);
    }

    public static void subTrance(LivingEntity subject, int amount) {
        setTrance(subject, getTrance(subject) - amount);
    }

    // ======= Focus =======

    public static int getFocus(LivingEntity subject) {
        return subject.getData(ModData.FOCUS);
    }

    public static void setFocus(LivingEntity subject, int value) {
        int clamped = Mth.clamp(value, MIN, MAX);
        if (clamped == getFocus(subject)) return;
        subject.setData(ModData.FOCUS, clamped);
        sync(subject);
    }

    public static void addFocus(LivingEntity subject, int amount) {
        lastInfluenced(subject);
        setFocus(subject, getFocus(subject) + amount);
    }

    public static void subFocus(LivingEntity subject, int amount) {
        setFocus(subject, getFocus(subject) - amount);
    }

    // Add per tick or every X ticks to this logic
    public static void tickDecay(LivingEntity subject) {
        int focus = getFocus(subject);
        int trance = getTrance(subject);

        if (focus <= 0 && trance <= MIN) return;
        if (recentlyInfluenced(subject)) return;


        // Remove trance if focus is broken
        if (focus > MIN) {
            subFocus(subject, FOCUS_DECAY);
        }
        else {
            subTrance(subject, TRANCE_DECAY);
        }
    }

    // ======= Sync =======
    public static void sync(LivingEntity subject) {
        // If the subject is a ServerPlayer
        if (subject instanceof ServerPlayer player){
            // Send their client the packet
            PacketDistributor.sendToPlayer(player, new HypnoDataS2C(getTrance(subject), getFocus(subject)));
        }
    }

    // ====== Helpers =======

    // Mark the last time the living entity was influenced
    private static void lastInfluenced(LivingEntity subject) {
        subject.setData(ModData.LAST_INFLUENCED, subject.level().getGameTime());
    }

    // If the subject was recently influenced (game time - lastInfluenced > grace period)
    public static boolean recentlyInfluenced(LivingEntity subject) {
        return subject.level().getGameTime() - subject.getData(ModData.LAST_INFLUENCED) > INFLUENCE_GRACE_TICKS;
    }
}