package net.saderlane.pixeltrance.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.saderlane.pixeltrance.dev.PTLog;
import net.saderlane.pixeltrance.hypno.HypnoData;
import net.saderlane.pixeltrance.hypno.InfluenceSource;
import net.saderlane.pixeltrance.hypno.ModInfluenceSources;
import net.saderlane.pixeltrance.util.ModTags;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

public class SpiraliteLampBlock extends Block {

    // ========== Private Variables ============
    private static final float RADIUS = 8.0f; // How far watch reaches
    private static final int PULSE_IN_TICK = 7; // Ticks between pulses
                                                // 5 = 4 times a second
    public static final BooleanProperty CLICKED = BooleanProperty.create("clicked");


    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide && state.getValue(CLICKED))  {
            level.scheduleTick(pos, this, PULSE_IN_TICK);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(CLICKED, Boolean.valueOf(context.getLevel().hasNeighborSignal(context.getClickedPos())));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean flag = state.getValue(CLICKED);
            if (flag != level.hasNeighborSignal(pos)) {
                if (flag) {
                    level.scheduleTick(pos, this, 4);
                } else {
                    level.setBlock(pos, state.cycle(CLICKED), 2);
                    level.scheduleTick(pos, this, PULSE_IN_TICK); // Start the pulse loop
                }
            }
        }
    }


    @Override
    protected void tick(BlockState state,
                        ServerLevel level,
                        BlockPos pos,
                        RandomSource random) {


        if (!state.getValue(CLICKED)) return;

        // Power is gone: turn off and don't reschedule, which ends the loop
        if (!level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.cycle(CLICKED), 2);
            return;
        }

        // Still powered: pulse and schedule the next tick
        //PTLog.debug("[PixelTrance] Block ticking _PULSING");
        pulse(level, pos);
        level.scheduleTick(pos, this, PULSE_IN_TICK);

        super.tick(state, level, pos, random);
    }

    private static void pulse(ServerLevel level, BlockPos pos) {
        Vec3 origin = pos.getCenter();

        // Build a list of LivingEntities that can be candidates
        List<LivingEntity> candidates = level.getEntitiesOfClass(
                LivingEntity.class, // Get the
                AABB.ofSize(origin, RADIUS*2, RADIUS*2, RADIUS*2), // Set the range that candidates can be found in
                candidate -> isValidSubject(candidate, pos, origin) // Set as candidate if it is a valid subject
        );

        if (candidates.isEmpty()) return;

        int slots = 1; //TODO Make this variable/scale in the future based on perks

        List<LivingEntity> chosen = selectTargets(candidates, origin, slots);
        for (LivingEntity subject : candidates) {

            int subjectFocus = HypnoData.getFocus(subject);
            int subjectTrance = HypnoData.getTrance(subject);

            if (chosen.contains(subject)) {

                HypnoData.markInfluenced(subject, ModInfluenceSources.SPIRALITE_LAMP.get());
                InfluenceSource subjectSource = HypnoData.getInfluenceSource(subject);

                if (subjectFocus != HypnoData.MAX)
                {
                    HypnoData.addFocus(subject, subjectSource.getFocusGain());
                    PTLog.debug(subjectSource.getName().getString() + " is influencing " + subject.getName().getString()
                            + " (focus " + HypnoData.getFocus(subject) + ")");
                }
                if (subjectFocus == HypnoData.MAX && subjectTrance != HypnoData.MAX) {
                    HypnoData.addTrance(subject, subjectSource.getTranceGain());
                    PTLog.debug(subjectSource.getName().getString() + " is influencing " + subject.getName().getString()
                            + " (trance " + HypnoData.getTrance(subject) + ")");
                }

            }
        }
    }


    // Can probably move these to a class later for all hypno-inducing valid objects
    private static boolean isValidSubject(LivingEntity candidate, BlockPos pos, Vec3 origin) {
        if (!candidate.isAlive()) return false; // If the candidate is dead, return

        if (candidate.distanceToSqr(origin) > RADIUS * RADIUS) return false; // Get sphere(scandelous) instead of box around holder

        return isLookingAt(candidate, pos);
    }

    // Check if entity is looking at the block
    private static boolean isLookingAt(LivingEntity candidate, BlockPos pos) {
        HitResult hit = customPick(candidate, RADIUS+1, 1.0f, false);

        return hit instanceof BlockHitResult blockHit && //If it returns BlockHitResult
                blockHit.getType() == HitResult.Type.BLOCK && // Was a block and not a miss
                pos.equals(blockHit.getBlockPos()); // Hit position is where the block is
    }

    private static HitResult customPick(LivingEntity candidate, double hitDistance, float partialTicks, boolean hitFluids) {
        Vec3 eyePosition = candidate.getEyePosition(partialTicks);
        Vec3 lookDirection = candidate.getViewVector(partialTicks);
        Vec3 lookRange = eyePosition.add(lookDirection.x * hitDistance, lookDirection.y * hitDistance, lookDirection.z * hitDistance);

        ClipContext context = new ClipContext(eyePosition, lookRange, ClipContext.Block.OUTLINE, hitFluids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, candidate){
            @Override
            public VoxelShape getBlockShape(BlockState blockState, BlockGetter level, BlockPos pos) {
                if (blockState.is(ModTags.Blocks.UNOBFUSCATED_GAZE)) {
                    return Shapes.empty();
                }
                return super.getBlockShape(blockState, level, pos);

            }
        };

        return candidate.level().clip(context);
    }


    private static List<LivingEntity> selectTargets(List<LivingEntity> candidates, Vec3 origin, int slots) {
        candidates.sort(
                Comparator.comparingInt(HypnoData::getFocus).reversed()
                        .thenComparingDouble(candidate -> candidate.distanceToSqr(origin))
        );

        return candidates.size() > slots ? candidates.subList(0, slots) : candidates;
    }


    public SpiraliteLampBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CLICKED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CLICKED);
    }
}
