package torcherino.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import torcherino.Torcherino;

public class Networking {
    public static void register(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(Torcherino.MOD_ID).versioned("2");
        registrar.play(
                OpenScreenMessage.ID,
                OpenScreenMessage::new,
                handlers -> handlers.client(ClientPayloadHandler::handleOpenScreen)
        );
        registrar.play(
                S2CTierSyncMessage.ID,
                S2CTierSyncMessage::new,
                handlers -> handlers.client(ClientPayloadHandler::handleTierSync)
        );
        registrar.play(
                ValueUpdateMessage.ID,
                ValueUpdateMessage::new,
                handlers -> handlers.server(ServerPayloadHandler::handleValueUpdate)
        );
    }
}
