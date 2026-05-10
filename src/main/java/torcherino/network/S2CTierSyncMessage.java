package torcherino.network;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import torcherino.Torcherino;
import torcherino.api.Tier;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
public record S2CTierSyncMessage(Map<ResourceLocation, Tier> tiers) implements CustomPacketPayload {
    public static final ResourceLocation ID = Torcherino.getRl("torcherino_tier_sync");

    public S2CTierSyncMessage(FriendlyByteBuf buffer) {
        this(decode(buffer).tiers());
    }

    public static void write(S2CTierSyncMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.tiers.size());
        message.tiers.forEach((name, tier) -> S2CTierSyncMessage.writeTier(name, tier, buffer));
    }

    public static S2CTierSyncMessage decode(FriendlyByteBuf buffer) {
        Map<ResourceLocation, Tier> localTiers = new HashMap<>();
        int count = buffer.readInt();
        for (int i = 0; i < count; i++) {
            Pair<ResourceLocation, Tier> entry = S2CTierSyncMessage.readTier(buffer);
            localTiers.put(entry.getFirst(), entry.getSecond());
        }
        return new S2CTierSyncMessage(localTiers);
    }



    private static Pair<ResourceLocation, Tier> readTier(FriendlyByteBuf buffer) {
        return new Pair<>(buffer.readResourceLocation(), new Tier(buffer.readInt(), buffer.readInt(), buffer.readInt()));
    }

    private static void writeTier(ResourceLocation name, Tier tier, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(name).writeInt(tier.maxSpeed()).writeInt(tier.xzRange()).writeInt(tier.yRange());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        write(this, buffer);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
