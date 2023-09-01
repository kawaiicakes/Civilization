package io.github.kawaiicakes.civilization.network.packets;

import io.github.kawaiicakes.civilization.api.utils.NamedUUID;
import io.github.kawaiicakes.civilization.nation.capabilities.PlayerNationCapsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class NationC2SPacket {
    private final int diplomacyScore;
    //private final NamedUUID nation;

    public NationC2SPacket(int diplomacyScore) {
        this.diplomacyScore = diplomacyScore;
        //this.nation = nation;
    }

    public NationC2SPacket(FriendlyByteBuf buf) {
        this.diplomacyScore = buf.readInt();
        //this.nation = new NamedUUID(UUID.randomUUID(), Component.empty()); // TODO
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.diplomacyScore);
        //buf.writeBlockPos(this.nation);
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
                //handler.setNation(this.nation);
                // TEMPORARY DEBUG SHIT
                List<NamedUUID> cityList = handler.getCities();
                cityList.add(new NamedUUID(UUID.randomUUID(), Component.empty()));
                player.sendSystemMessage(Component.literal("added new city UUID to cap"));
                handler.setCities(cityList);
            });
        });
        return false;
    }
}
