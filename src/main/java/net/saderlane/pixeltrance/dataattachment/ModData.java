package net.saderlane.pixeltrance.dataattachment;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.saderlane.pixeltrance.PixelTrance;

import java.util.function.Supplier;

public class ModData {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PixelTrance.MOD_ID);

    public static final Supplier<AttachmentType<Integer>> TRANCE = ATTACHMENT_TYPES.register("trance",
            () -> AttachmentType.<Integer>builder(() -> 0).serialize(Codec.INT).build());

    public static final Supplier<AttachmentType<Integer>> FOCUS = ATTACHMENT_TYPES.register("focus",
            () -> AttachmentType.<Integer>builder(() -> 0).serialize(Codec.INT).build());

    public static final Supplier<AttachmentType<Long>> LAST_INFLUENCED = ATTACHMENT_TYPES.register("last_influenced",
            () -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).build());



    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }

}

