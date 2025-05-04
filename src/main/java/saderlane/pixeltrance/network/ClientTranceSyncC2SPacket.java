package saderlane.pixeltrance.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import saderlane.pixeltrance.PixelTrance;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.data.TranceData;

public class ClientTranceSyncC2SPacket {

    // Unique identifier for the packet
    public static final Identifier ID = new Identifier(PixelTrance.MOD_ID,"client_trance_sync");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            float trance = buf.readFloat();
            float focus = buf.readFloat();

            server.execute(() -> {
                if (!(player instanceof TranceDataAccess access)) return;
                TranceData data = access.getTranceData();
                data.setTrance(trance);
                data.setFocus(focus);

                TranceSyncS2CPacket.send(player, trance, focus, data.getFocusLocked(),
                        data.getTranceInducer(), data.getFocusInducer());
            });
        });
    }

}
