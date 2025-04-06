package saderlane.pixeltrance.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;

import io.netty.buffer.Unpooled;
import saderlane.pixeltrance.PixelTrance;

public class TranceSyncS2CPacket {

    // Unique identifier for the packet
    public static final Identifier ID = new Identifier(PixelTrance.MOD_ID,"trance_sync");

    // Send given trance and focus value to specific player
    public static void send(ServerPlayerEntity player, float trance, float focus, boolean focusSessionActive) {
        PacketByteBuf buf = new PacketByteBuf(io.netty.buffer.Unpooled.buffer());
        buf.writeFloat(trance);
        buf.writeFloat(focus);
        buf.writeBoolean(focusSessionActive);

        ServerPlayNetworking.send(player,ID, buf);
    }

}
