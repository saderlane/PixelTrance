package net.saderlane.pixeltrance.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.saderlane.pixeltrance.hypno.HypnoData;

public class HypnotizedEffect extends MobEffect {
    private static final int PULSE_IN_TICK = 17; // Ticks between pulses


    public HypnotizedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {

        if (HypnoData.getTrance(livingEntity) < 100) {
            HypnoData.addTrance(livingEntity, 3);
            return true;
        }

        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % PULSE_IN_TICK == 0;
    }
}
