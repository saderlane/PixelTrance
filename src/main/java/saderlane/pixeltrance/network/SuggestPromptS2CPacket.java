package saderlane.pixeltrance.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import saderlane.pixeltrance.PixelTrance;

public class SuggestPromptS2CPacket {

    // Unique identifier for the packet
    public static final Identifier ID = new Identifier(PixelTrance.MOD_ID,"suggest_prompt");
    private final String message;

    public SuggestPromptS2CPacket(String message) {
        this.message = message;
    }

    public SuggestPromptS2CPacket(PacketByteBuf buf) {
        this.message = buf.readString(256);
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(message);
    }

    public String getMessage() {
        return message;
    }

    public void send(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(message);
        ServerPlayNetworking.send(player, ID, buf);
    }

}
