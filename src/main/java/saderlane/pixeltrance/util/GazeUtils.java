package saderlane.pixeltrance.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

// Utility for tracking what an entity is looking at
public class GazeUtils {

    // Check if subject is looking at the target
    /*
     * @param subject The entity who is being hypnotized (the viewer)
     * @param inducerEntity The living entity that is the inducer
     * @param threshold Dot product threshold (1.0 = perfect aim, 0.95 ≈ 18°, 0.9 ≈ 26° cone)
     * @return true if the subject is looking at the inducer entity
    */
    public static boolean subjectLookingAtInducer(LivingEntity subject, LivingEntity inducerEntity, double angleThreshold, double maxDistance) {
        if (subject == null || inducerEntity == null) return false;
        if (subject.getWorld() != inducerEntity.getWorld()) return false;

        // Make sure gaze is within range
        double distanceSq = subject.squaredDistanceTo(inducerEntity);
        if (distanceSq > maxDistance * maxDistance) return false;

        // Make sure subject is looking at inducer
        Vec3d subjectEye = subject.getEyePos();
        Vec3d subjectLook = subject.getRotationVec(1.0F).normalize();

        Vec3d inducerEye = inducerEntity.getEyePos();
        Vec3d toInducer = inducerEye.subtract(subjectEye).normalize();

        double dot = subjectLook.dotProduct(toInducer);
        if (dot < angleThreshold) return false;

        // Check line of sight (not obstructed)
        HitResult result = subject.getWorld().raycast(new RaycastContext(
                subjectEye,
                inducerEye,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                subject
        ));

        // If the ray hit a block *before* reaching the inducer, vision is blocked
        if (result.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockResult = (BlockHitResult) result;
            Vec3d hitPos = blockResult.getPos();

            // Check if the block hit is before the inducer
            if (hitPos.squaredDistanceTo(subjectEye) < subjectEye.squaredDistanceTo(inducerEye)) {
                var blockState = subject.getWorld().getBlockState(blockResult.getBlockPos());

                // Allow gaze through transparent or low-opacity blocks
                if (!isTransparentForGaze(blockState)) {
                    return false; // Blocked by solid object
                }
            }
        }

        return true;
    }

     // Determine if inducer is also looking back at the subject.
    public static boolean mutualGaze(LivingEntity subject, LivingEntity inducerEntity, double angleThreshold, double maxDistance) {
        return subjectLookingAtInducer(subject, inducerEntity, angleThreshold, maxDistance) &&
                subjectLookingAtInducer(inducerEntity, subject, angleThreshold, maxDistance);
    }


    // Helper method for marking what blocks are transparent/trance should impact
    public static boolean isTransparentForGaze(BlockState state) {
        return state.isOf(Blocks.GLASS) ||
                state.isOf(Blocks.GLASS_PANE) ||
                state.isOf(Blocks.IRON_BARS) ||
                state.isOf(Blocks.AIR) ||
                state.isOf(Blocks.OAK_FENCE) ||
                state.isOf(Blocks.SPRUCE_FENCE) ||
                state.isOf(Blocks.BIRCH_FENCE) ||
                state.isOf(Blocks.JUNGLE_FENCE) ||
                state.isOf(Blocks.ACACIA_FENCE) ||
                state.isOf(Blocks.DARK_OAK_FENCE) ||
                state.isOf(Blocks.MANGROVE_FENCE) ||
                state.isOf(Blocks.CHERRY_FENCE) ||
                state.isOf(Blocks.BAMBOO_FENCE) ||
                state.isOf(Blocks.CRIMSON_FENCE) ||
                state.isOf(Blocks.WARPED_FENCE);
    }

}
