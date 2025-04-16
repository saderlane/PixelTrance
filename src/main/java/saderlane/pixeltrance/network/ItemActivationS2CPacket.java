package saderlane.pixeltrance.network;


import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

// Packet to sync when an inducer item is activated
public class ItemActivationS2CPacket {

    public static final Identifier ID = new Identifier("pixeltrance", "item_activation");

    // Sends the activation state of an item in the specified hand to the client.
    /*
     * player    The server-side player
     * hand      The hand holding the item (main/off-hand)
     * isActive  Whether the item is now active
    */
    public static void send(ServerPlayerEntity player, Hand hand, boolean isActive) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeEnumConstant(hand);
        buf.writeBoolean(isActive);

        ServerPlayNetworking.send(player, ID, buf);
    }


}
