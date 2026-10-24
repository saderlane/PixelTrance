package net.saderlane.pixeltrance.ai;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.saderlane.pixeltrance.PixelTrance;

public class ModActivities {

    public static final DeferredRegister<Activity> ACTIVITIES =
            DeferredRegister.create(BuiltInRegistries.ACTIVITY, PixelTrance.MOD_ID);

    public static final Holder<Activity> ENTRANCED_ACTIVITY = ACTIVITIES.register("entranced",
            () -> new Activity(PixelTrance.MOD_ID + ":ENTRANCED")
    );

    public static final Holder<Activity> FOCUS_LOCKED_ACTIVITY = ACTIVITIES.register("focus_locked",
            () -> new Activity(PixelTrance.MOD_ID + ":FOCUS_LOCKED")
    );

    public static void register(IEventBus eventBus) {
        ACTIVITIES.register(eventBus);
    }

    public static void attachTo(Brain<?> brain) {
        brain.addActivity(FOCUS_LOCKED_ACTIVITY.value(), ImmutableList.of());
        brain.addActivity(ENTRANCED_ACTIVITY.value(), ImmutableList.of());
    }
}
