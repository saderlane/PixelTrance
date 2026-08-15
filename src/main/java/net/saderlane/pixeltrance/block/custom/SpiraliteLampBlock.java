package net.saderlane.pixeltrance.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.saderlane.pixeltrance.dev.PTLog;
import net.saderlane.pixeltrance.hypno.HypnoData;

import java.util.Comparator;
import java.util.List;

public class SpiraliteLampBlock extends Block {

    // ========== Private Variables ============
    private static final float RADIUS = 8.0f; // How far watch reaches
    private static final int PULSE_IN_TICK = 7; // Ticks between pulses
    // 5 = 4 times a second
    private static final int FOCUS_GAIN = 2;
    private static final int TRANCE_GAIN = 1;

    public static final BooleanProperty CLICKED = BooleanProperty.create("clicked");


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CLICKED);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide()) {
            boolean currentState = state.getValue(CLICKED);
            level.setBlockAndUpdate(pos, state.setValue(CLICKED, !currentState));
            level.scheduleTick(pos, this, 0);
        }

        return InteractionResult.SUCCESS;
    }


    @Override
    protected void tick(BlockState state,
                        ServerLevel level,
                        BlockPos pos,
                        RandomSource random) {


        level.scheduleTick(pos, this, 0);
        boolean currentState = state.getValue(CLICKED);
        if (!currentState) return;

        if (level.getGameTime() % PULSE_IN_TICK == 0) {
            //PTLog.debug("[PixelTrance] Block ticking _PULSING");
            pulse(level, pos);
        }

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
                if (subjectFocus <= HypnoData.MAX)
                {
                    HypnoData.addFocus(subject, FOCUS_GAIN);
                    PTLog.debug("[PixelTrance] Influencing " + subject.getName().getString()
                            + " (focus " + HypnoData.getFocus(subject) + ")");
                }
                if (subjectFocus >= HypnoData.MAX && subjectTrance <= HypnoData.MAX) {
                    HypnoData.addTrance(subject, TRANCE_GAIN);
                    PTLog.debug("[PixelTrance] Influencing " + subject.getName().getString()
                            + " (trance " + HypnoData.getTrance(subject) + ")");
                }

            }
        }
    }


    // Can probably move these to a class later for all hypno-inducing valid objects
    private static boolean isValidSubject(LivingEntity candidate, BlockPos pos, Vec3 origin) {
        if (!candidate.isAlive()) return false; // If the candidate is the holder or dead, return

        if (candidate.distanceToSqr(origin) > RADIUS * RADIUS) return false; // Get sphere(scandelous) instead of box around holder

        return true;
    }

    private static List<LivingEntity> selectTargets(List<LivingEntity> candidates, Vec3 origin, int slots) {
        candidates.sort(
                Comparator.comparingInt(HypnoData::getFocus).reversed()
                        .thenComparingDouble(candidate -> candidate.distanceToSqr(origin))
        );

        return candidates.size() > slots ? candidates.subList(0, slots) : candidates;
    }





    public SpiraliteLampBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CLICKED, false));
    }
}
