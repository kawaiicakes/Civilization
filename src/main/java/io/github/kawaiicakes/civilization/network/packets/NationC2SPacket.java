package io.github.kawaiicakes.civilization.network.packets;

import io.github.kawaiicakes.civilization.api.utils.NamedUUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class NationC2SPacket {
    private final int diplomacyScore;
    private final NamedUUID nation;

    public NationC2SPacket(int diplomacyScore, NamedUUID nation) {
        this.diplomacyScore = diplomacyScore;
        this.nation = nation;
    }

    public NationC2SPacket(FriendlyByteBuf buf) {
        this.diplomacyScore = buf.readInt();
        this.nation = new NamedUUID(UUID.randomUUID(), Component.empty()); // TODO
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.diplomacyScore);
        //buf.writeBlockPos(this.nation);
    }

    // TODO
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        return false;
    }
}
