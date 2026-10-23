package net.saderlane.pixeltrance.networking;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.saderlane.pixeltrance.client.ClientHypnoCache;
import net.saderlane.pixeltrance.networking.packet.HypnoDataS2C;

// Handle packets Client -> Server
public class ClientPayloadHandler {

    // ---ON SERVER---

    // Whenever HypnoData syncs 2 client -> set the cache
    public static void HypnoDataSync2Cache(HypnoDataS2C packet, IPayloadContext context) {
        ClientHypnoCache.set(packet.trance(), packet.focus());
    }
}
