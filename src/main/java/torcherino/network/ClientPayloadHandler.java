package torcherino.network;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.slf4j.Logger;
import torcherino.Torcherino;
import torcherino.TorcherinoImpl;
import torcherino.api.TorcherinoAPI;
import torcherino.block.entity.TorcherinoBlockEntity;

public class ClientPayloadHandler {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

    public static ClientPayloadHandler getInstance() {
        return INSTANCE;
    }

    public static void handleOpenScreen(final OpenScreenMessage message, final PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> OpenScreenMessage.openTorcherinoScreen(message));
    }

    public static void handleTierSync(final S2CTierSyncMessage message, PlayPayloadContext context) {
        context.workHandler().execute(() -> ((TorcherinoImpl) TorcherinoAPI.INSTANCE).setRemoteTiers(message.tiers()));
    }

    public static void handleValue(final ValueUpdateMessage message, PlayPayloadContext context) {
        context.player().ifPresent(player -> context.workHandler().execute(() -> {
            if (player.level().getBlockEntity(message.pos()) instanceof TorcherinoBlockEntity blockEntity
                    && !blockEntity.readClientData(message.xRange(), message.zRange(), message.yRange(), message.speed(), message.redstoneMode())) {
                LOGGER.error("Data received from {} ({}) is invalid.", player.getName().getString(), player.getStringUUID());
            }
        }));
    }
}
