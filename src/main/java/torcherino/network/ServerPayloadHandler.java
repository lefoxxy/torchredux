package torcherino.network;

import com.mojang.logging.LogUtils;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.slf4j.Logger;
import torcherino.Torcherino;
import torcherino.block.entity.TorcherinoBlockEntity;

public class ServerPayloadHandler {
    private static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ServerPayloadHandler getInstance() {
        return INSTANCE;
    }

    public static void handleValueUpdate(final ValueUpdateMessage message, PlayPayloadContext context) {
        context.player().ifPresent(player -> context.workHandler().execute(() -> {
            if (player.level().getBlockEntity(message.pos()) instanceof TorcherinoBlockEntity blockEntity
                    && !blockEntity.readClientData(message.xRange(), message.zRange(), message.yRange(), message.speed(), message.redstoneMode())) {
                LOGGER.error("Data received from {} ({}) is invalid.", player.getName().getString(), player.getStringUUID());
            }
        }));
    }
}
