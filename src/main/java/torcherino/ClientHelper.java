package torcherino;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import torcherino.particle.TorcherinoParticleTypes;

import java.util.function.Supplier;

public class ClientHelper {
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(TorcherinoParticleTypes.Normal_Torcherino_Flame.get(), FlameParticle.Provider::new);
        event.registerSpriteSet(TorcherinoParticleTypes.Compressed_Torcherino_Flame.get(), FlameParticle.Provider::new);
        event.registerSpriteSet(TorcherinoParticleTypes.Double_Compressed_Torcherino_Flame.get(), FlameParticle.Provider::new);
    }

    public static void registerCutout(Supplier<? extends Block> block){
        Minecraft.getInstance().submitAsync(() -> {
            ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
        });
    }
}
