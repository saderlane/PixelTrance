package net.saderlane.pixeltrance.hypno;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.saderlane.pixeltrance.dev.PTLog;
import net.saderlane.pixeltrance.util.ModTags;

import java.util.Comparator;
import java.util.List;

public class HypnoTargeting {

    private HypnoTargeting() {}


    public static List<LivingEntity> selectTargets(List<LivingEntity> candidates, Vec3 origin, int slots) {
        candidates.sort(
                Comparator.comparingInt(HypnoData::getFocus).reversed()
                        .thenComparingDouble(candidate -> candidate.distanceToSqr(origin))
        );

        return candidates.size() > slots ? candidates.subList(0, slots) : candidates;
    }

    public static void applyInfluence(LivingEntity subject, InfluenceSource source) {
        int subjectFocus = HypnoData.getFocus(subject);
        int subjectTrance = HypnoData.getTrance(subject);


        if (subjectFocus != HypnoData.MAX)
        {
            HypnoData.addFocus(subject, source.getFocusGain());
            PTLog.debug(source.getName().getString() + " is influencing " + subject.getName().getString()
                    + " (focus " + HypnoData.getFocus(subject) + ")");
        }
        if (subjectFocus == HypnoData.MAX && subjectTrance != HypnoData.MAX) {
            HypnoData.addTrance(subject, source.getTranceGain());
            PTLog.debug(source.getName().getString() + " is influencing " + subject.getName().getString()
                    + " (trance " + HypnoData.getTrance(subject) + ")");
        }
    }

    private static ClipContext gazeClip(Vec3 from, Vec3 to, LivingEntity entity){
        ClipContext context = new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity){
            @Override
            public VoxelShape getBlockShape(BlockState blockState, BlockGetter level, BlockPos pos) {
                if (blockState.is(ModTags.Blocks.UNOBSTRUCTED_GAZE)) {
                    return Shapes.empty();
                }
                return super.getBlockShape(blockState, level, pos);

            }
        };

        return context;
    }



    //================ Block based targeting ================
    public static boolean isValidSubject(LivingEntity candidate, BlockPos pos, Vec3 origin, float radius) {
        if (!candidate.isAlive() || candidate instanceof ArmorStand || candidate.isSpectator()) return false; // If the candidate is dead, return

        if (candidate.distanceToSqr(origin) > radius * radius) return false; // Get sphere(scandelous) instead of box around holder

        return isLookingAt(candidate, pos, radius);
    }

    // Check if entity is looking at the block
    private static boolean isLookingAt(LivingEntity candidate, BlockPos pos, float radius) {
        HitResult hit = customPick(candidate, radius+1);

        return hit instanceof BlockHitResult blockHit && //If it returns BlockHitResult
                blockHit.getType() == HitResult.Type.BLOCK && // Was a block and not a miss
                pos.equals(blockHit.getBlockPos()); // Hit position is where the block is
    }

    public static HitResult customPick(LivingEntity candidate, double hitDistance) {
        Vec3 eyePosition = candidate.getEyePosition();
        Vec3 lookDirection = candidate.getViewVector(1.0f);
        Vec3 lookRange = eyePosition.add(lookDirection.x * hitDistance, lookDirection.y * hitDistance, lookDirection.z * hitDistance);

        ClipContext context = gazeClip(eyePosition, lookRange, candidate);

        return candidate.level().clip(context);
    }



    //================ Item based targeting ================
    public static boolean isValidSubject(LivingEntity candidate, LivingEntity holder, Vec3 origin, float radius) {
        if (candidate == holder || !candidate.isAlive() || candidate instanceof ArmorStand || candidate.isSpectator()) return false; // If the candidate is the holder or dead, return

        if (candidate.distanceToSqr(origin) > radius * radius) return false;

        return isLookingAt(candidate, holder);
    }

    // Check if candidate is looking at the holder
    private static boolean isLookingAt(LivingEntity candidate, LivingEntity holder) {
        Vec3 lookDirection = candidate.getViewVector(1.0F).normalize();
        Vec3 viewLine = new Vec3(holder.getX() - candidate.getX(), holder.getEyeY() - candidate.getEyeY(), holder.getZ() - candidate.getZ());
        double distance = viewLine.length();
        viewLine = viewLine.normalize();
        double d1 = lookDirection.dot(viewLine); // Not sure what this is measuring exactly cause took it from enderman logic
        return d1 > 1.0 - 0.1 / distance ? !gazeObstructed(candidate, holder) : false;
    }

    private static boolean gazeObstructed(LivingEntity candidate, LivingEntity holder) {
        Vec3 candidateEyePosition = candidate.getEyePosition();
        Vec3 holderEyePosition = holder.getEyePosition();

        ClipContext context = gazeClip(candidateEyePosition, holderEyePosition, candidate);

        return holder.level()
                .clip(context)
                .getType() != HitResult.Type.MISS;

    }
}
