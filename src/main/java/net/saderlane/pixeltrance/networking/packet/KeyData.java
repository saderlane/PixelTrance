package net.saderlane.pixeltrance.networking.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.saderlane.pixeltrance.PixelTrance;

public record KeyData(String name, int value) implements CustomPacketPayload {

    // registers name of the packet for the server to interpret
    public static final Type<KeyData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID,"key_data"));

    // Encodes and decodes the data in packet
    public static final StreamCodec<ByteBuf, KeyData> STREAM_CODEC = StreamCodec.composite(
            // Identifying data
            ByteBufCodecs.STRING_UTF8,
            KeyData::name,

            // What format the data is in
            ByteBufCodecs.VAR_INT,
            KeyData::value,

            KeyData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
