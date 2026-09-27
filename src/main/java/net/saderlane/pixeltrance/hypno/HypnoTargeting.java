package net.saderlane.pixeltrance.hypno;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.saderlane.pixeltrance.dev.PTLog;

import java.util.Comparator;
import java.util.List;

public class HypnoTargeting {


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
}
