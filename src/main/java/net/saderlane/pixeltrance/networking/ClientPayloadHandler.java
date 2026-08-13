package net.saderlane.pixeltrance.networking;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.saderlane.pixeltrance.networking.packet.KeyData;

// Handle packets Client -> Server
public class ClientPayloadHandler {

    // ---ON SERVER---

    // Whenever KeyData arrives: do this
    public static void handleDataOnMain(KeyData keyData, IPayloadContext context) {
        EntityType.COW.spawn(((ServerLevel) context.player().level()), context.player().getOnPos(), MobSpawnType.TRIGGERED);
        context.player().sendSystemMessage(Component.literal(keyData.name() + " and it has a value of " + keyData.value()));
    }
}
