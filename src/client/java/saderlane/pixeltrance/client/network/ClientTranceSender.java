package saderlane.pixeltrance.client.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import saderlane.pixeltrance.network.ClientTranceSyncC2S;

public class ClientTranceSender {
    public static void send(float newTrance, float newFocus) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeFloat(newTrance);
        buf.writeFloat(newFocus);
        ClientPlayNetworking.send(ClientTranceSyncC2S.ID, buf);
    }
}
