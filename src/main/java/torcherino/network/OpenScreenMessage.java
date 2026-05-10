package torcherino.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import torcherino.Torcherino;
import torcherino.block.entity.TorcherinoBlockEntity;
import torcherino.client.screen.TorcherinoScreen;

@SuppressWarnings("ClassCanBeRecord")
public record OpenScreenMessage(BlockPos pos, String title, int xRange, int zRange, int yRange, int speed, int redstoneMode) implements CustomPacketPayload {
    public static final ResourceLocation ID = Torcherino.getRl("open_torcherino_screen");

    public OpenScreenMessage(final FriendlyByteBuf buf) {
        this(buf.readBlockPos(),
                buf.readUtf(),
                buf.readInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readInt()
        );
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeUtf(title);
        buffer.writeInt(xRange);
        buffer.writeInt(zRange);
        buffer.writeInt(yRange);
        buffer.writeInt(speed);
        buffer.writeInt(redstoneMode);
    }

    public static void openTorcherinoScreen(OpenScreenMessage message) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.submitAsync(() -> {
            if (minecraft.player != null && minecraft.player.level().getBlockEntity(message.pos()) instanceof TorcherinoBlockEntity blockEntity) {
                TorcherinoScreen screen = new TorcherinoScreen(Component.translatable(message.title()), message.xRange(), message.zRange(), message.yRange(),
                        message.speed(), message.redstoneMode(), blockEntity.getBlockPos(), blockEntity.getTier());
                minecraft.setScreen(screen);
            }
        });
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
