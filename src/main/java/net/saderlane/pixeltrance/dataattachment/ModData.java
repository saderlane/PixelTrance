package net.saderlane.pixeltrance.dataattachment;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.hypno.InfluenceSource;
import net.saderlane.pixeltrance.hypno.ModInfluenceSources;

import java.util.UUID;
import java.util.function.Supplier;

public class ModData {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PixelTrance.MOD_ID);

    // Tracks the codec for the trance
    public static final Supplier<AttachmentType<Float>> TRANCE = ATTACHMENT_TYPES.register("trance",
            () -> AttachmentType.<Float>builder(() -> 0.0f).serialize(Codec.FLOAT).build());

    // Tracks the codec for the focus
    public static final Supplier<AttachmentType<Float>> FOCUS = ATTACHMENT_TYPES.register("focus",
            () -> AttachmentType.<Float>builder(() -> 0.0f).serialize(Codec.FLOAT).build());

    // Tracks the codec for the last time entity was influenced
    public static final Supplier<AttachmentType<Long>> LAST_INFLUENCED = ATTACHMENT_TYPES.register("last_influenced",
            () -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).build());

    // Tracks the codec for the last thing that influenced an entity
    public static final Supplier<AttachmentType<InfluenceSource>> LAST_INFLUENCE_SOURCE = ATTACHMENT_TYPES.register("last_influence_source",
            () -> AttachmentType.<InfluenceSource>builder(() -> ModInfluenceSources.NONE.get())
                    .serialize(ModInfluenceSources.REGISTRY.byNameCodec()).build());

    // Tracks the codec for the UUID of the trance inducer
    public static final Supplier<AttachmentType<UUID>> INFLUENCE_SOURCE_UUID = ATTACHMENT_TYPES.register("influence_source_uuid",
            () -> AttachmentType.<UUID>builder(() -> Util.NIL_UUID)
                    .serialize(UUIDUtil.CODEC).build());

    // Tracks the codec for the Vec3 of the trance inducer
    public static final Supplier<AttachmentType<Vec3>> INFLUENCE_SOURCE_VEC3 = ATTACHMENT_TYPES.register("influence_source_vec3",
            () -> AttachmentType.<Vec3>builder(() -> Vec3.ZERO).serialize(Vec3.CODEC).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }

}

