package net.saderlane.pixeltrance.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.networking.ClientPayloadHandler;
import net.saderlane.pixeltrance.networking.packet.KeyData;

@EventBusSubscriber(modid = PixelTrance.MOD_ID)
public class ModEvents {


    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1")
                .executesOn(HandlerThread.MAIN);

        registrar.playToServer(KeyData.TYPE, KeyData.STREAM_CODEC, ClientPayloadHandler::handleDataOnMain);
    }
}
