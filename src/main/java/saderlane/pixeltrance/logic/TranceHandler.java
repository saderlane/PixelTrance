package saderlane.pixeltrance.logic;


import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import saderlane.pixeltrance.api.Inducer;
import saderlane.pixeltrance.api.MobInducerWrapper;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.registry.InducerRegistry;
import saderlane.pixeltrance.util.GazeUtils;
import saderlane.pixeltrance.util.PTLog;

// Applies trance buildup if subjects hold mutual gaze with inducer
public class TranceHandler {

    public static void tickForSubject(ServerWorld world, LivingEntity subject) {
        if(!(subject instanceof TranceDataAccess access)) return;

        var tranceData = access.getTranceData();

        for(InducerRegistry.ActiveInducer active : InducerRegistry.getActiveInducers()) {
            Inducer inducer = active.inducer();
            LivingEntity holder = active.holder();

            LivingEntity inducerEntity = null;

            if (holder != null) {
                if (subject == holder) continue; // can't self hypnotize
                if (holder.getWorld() != world) continue;

                inducerEntity = holder;
            }
            else if (inducer instanceof MobInducerWrapper wrapper) {
                var mob = wrapper.getMob();

                if (subject == mob) continue; // can't self hypnotize
                if (mob.getWorld() != world) continue;

                inducerEntity = mob;
            }

            if (inducerEntity == null) continue;

            // === Mutual Gaze Check ===
            if (!GazeUtils.mutualGaze(subject, inducerEntity, 0.95, 8)) continue;

            // Apply trance on defined interval
            if (world.getTime() % inducer.getTranceInterval() == 0) {
                tranceData.addTrance(inducer.getTranceRate());

                PTLog.info("Subject " + subject.getName().getString() +
                        " gained trance from mutual gaze with " + inducerEntity.getName().getString());
            }

        }
    }

}
