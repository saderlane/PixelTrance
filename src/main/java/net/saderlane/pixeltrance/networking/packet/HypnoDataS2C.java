package net.saderlane.pixeltrance.networking.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.saderlane.pixeltrance.PixelTrance;

public record HypnoDataS2C(int trance, int focus) implements CustomPacketPayload {

    // registers ID of the packet to "sync_hypno"
    public static final Type<HypnoDataS2C> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID,"sync_hypno"));

    // Encodes and decodes the data in packet
    public static final StreamCodec<ByteBuf, HypnoDataS2C> STREAM_CODEC = StreamCodec.composite(
            // Send trance data
            ByteBufCodecs.VAR_INT,
            HypnoDataS2C::trance,

            // Send focus data
            ByteBufCodecs.VAR_INT,
            HypnoDataS2C::focus,

            HypnoDataS2C::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
