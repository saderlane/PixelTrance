package saderlane.pixeltrance.logic;


import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import saderlane.pixeltrance.api.Inducer;
import saderlane.pixeltrance.api.MobInducerWrapper;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.registry.InducerRegistry;
import saderlane.pixeltrance.util.GazeUtils;
import saderlane.pixeltrance.util.PTLog;

// Applies passive focus gain to subjects based on proximity to active inducers.
public class FocusHandler {

    // Called every tick per subject
    public static void tickForSubject(ServerWorld world, LivingEntity subject) {
        if (!(subject instanceof TranceDataAccess access)) return;

        var tranceData = access.getTranceData();

        for (InducerRegistry.ActiveInducer active : InducerRegistry.getActiveInducers()) {
            Inducer inducer = active.inducer();
            LivingEntity holder = active.holder(); // May be null (for mobs)

            if (holder != null) {

                if (subject == holder) continue; // can't self hypnotize
                if (holder.getWorld() != world) continue;

                // If this inducer requires line of sight, subject must be looking at the holder
                if (inducer.requiresLineOfSight() &&
                        !GazeUtils.subjectLookingAtInducer(subject, holder, 0.95, 6.0)) continue;

                if (world.getTime() % inducer.getFocusInterval() == 0) {
                    tranceData.addFocus(inducer.getFocusRate());

                    PTLog.info("Subject " + subject.getName().getString() +
                            " received +" + inducer.getFocusRate() + " focus from inducer held by " +
                            holder.getName().getString());
                }
            }
            else if (inducer instanceof MobInducerWrapper wrapper) {
                var mob = wrapper.getMob();

                if (subject == mob) continue; // can't self hypnotize
                if (mob.getWorld() != world) continue;

                // Gaze check: subject must be actively looking at the inducer
                if (!GazeUtils.subjectLookingAtInducer(subject, mob, 0.95, 6.0)) continue;

                // Only apply focus on valid tick interval
                if (world.getTime() % inducer.getFocusInterval() == 0) {
                    tranceData.addFocus(inducer.getFocusRate());
                    PTLog.info("Subject " + subject.getName().getString() +
                            " received +" + inducer.getFocusRate() + " focus from inducer mob: " +
                            mob.getName().getString());
                }
            }
        }
    }
}