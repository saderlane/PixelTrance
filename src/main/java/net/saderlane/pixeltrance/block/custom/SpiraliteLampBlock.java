package net.saderlane.pixeltrance.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.saderlane.pixeltrance.hypno.HypnoData;
import net.saderlane.pixeltrance.hypno.HypnoTargeting;
import net.saderlane.pixeltrance.hypno.InfluenceSource;
import net.saderlane.pixeltrance.hypno.ModInfluenceSources;

import javax.annotation.Nullable;
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
                candidate -> HypnoTargeting.isValidSubject(candidate, pos, origin, RADIUS) // Set as candidate if it is a valid subject
        );

        if (candidates.isEmpty()) return;

        int slots = 1; //TODO Make this variable/scale in the future based on perks

        List<LivingEntity> chosen = HypnoTargeting.selectTargets(candidates, origin, slots);
        for (LivingEntity subject : candidates) {

            if (chosen.contains(subject)) {

                HypnoData.markInfluenced(subject, ModInfluenceSources.SPIRALITE_LAMP.get());

                HypnoTargeting.applyInfluence(subject,ModInfluenceSources.SPIRALITE_LAMP.get());

            }
        }
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
