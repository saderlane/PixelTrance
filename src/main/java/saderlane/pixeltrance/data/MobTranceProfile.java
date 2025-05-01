package saderlane.pixeltrance.data;

import net.minecraft.entity.EntityType;

import java.util.function.Predicate;

// Defines how a mob behaves when it is under trance
public class MobTranceProfile {


    private final boolean attentionGrabbed; // Should mob stop and stare at inducer
    private final boolean slowsMovement; // Should mob slow down
    private final Predicate<EntityType<?>> supportCheck; // Predicate to determine if mob can be tranced


    // Constructor
    public MobTranceProfile(boolean attentionGrabbed, boolean slowsMovement, Predicate<EntityType<?>> supportCheck) {
        this.attentionGrabbed = attentionGrabbed;
        this.slowsMovement = slowsMovement;
        this.supportCheck = supportCheck;
    }

    // Returns if mob should stop and stare
    public boolean attentionGrabbed() {
        return attentionGrabbed;
    }

    // Returns if mob movement is slowed
    public boolean shouldSlowMovement() {
        return slowsMovement;
    }

    // Returns if mob is supported and can be tranced
    public boolean isSupported(EntityType<?> type) {
        return supportCheck.test(type);
    }

}
