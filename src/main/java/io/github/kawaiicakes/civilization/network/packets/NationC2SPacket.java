package io.github.kawaiicakes.civilization.network.packets;

import io.github.kawaiicakes.civilization.nation.capabilities.PlayerNationCapsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class NationC2SPacket {
    private final UUID nation;

    public NationC2SPacket(UUID nation) {
        this.nation = nation;
    }

    public NationC2SPacket(FriendlyByteBuf buf) {
        this.nation = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(this.nation);
    }

    // TODO
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Server only
            ServerPlayer player = context.getSender();
            assert player != null;
            ServerLevel level = player.getLevel();

            player.getCapability(PlayerNationCapsProvider.PLAYER_NATION).ifPresent(handler -> {
                handler.setNation(this.nation);
                player.sendSystemMessage(Component.literal("added random nation to cap"));
            });
        });
        return false;
    }
}
