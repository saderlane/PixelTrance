package net.saderlane.pixeltrance.hypno;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.saderlane.pixeltrance.PixelTrance;

public class ModInfluenceSources {

    public static final ResourceKey<Registry<InfluenceSource>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "influence_source"));

    public static final Registry<InfluenceSource> REGISTRY = new RegistryBuilder<>(REGISTRY_KEY)
            .sync(true)
            .create();


    public static final DeferredRegister<InfluenceSource> INFLUENCE_SOURCES =
            DeferredRegister.create(REGISTRY, PixelTrance.MOD_ID);

    public static DeferredHolder<InfluenceSource, InfluenceSource> NONE =
            INFLUENCE_SOURCES.register("none", () -> new InfluenceSource(0,0));

    public static DeferredHolder<InfluenceSource, InfluenceSource> POCKET_WATCH =
            INFLUENCE_SOURCES.register("pocket_watch", () -> new InfluenceSource(7,3));

    public static DeferredHolder<InfluenceSource, InfluenceSource> SPIRALITE_LAMP =
            INFLUENCE_SOURCES.register("spiralite_lamp", () -> new InfluenceSource(4,2));

    private static void registerRegistry(NewRegistryEvent event) {
        event.register(REGISTRY);
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(ModInfluenceSources::registerRegistry);
        INFLUENCE_SOURCES.register(eventBus);
    }

}
