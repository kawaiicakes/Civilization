package io.github.kawaiicakes.civilization.network.packets;

import io.github.kawaiicakes.civilization.api.nations.Nation;
import io.github.kawaiicakes.civilization.api.nations.NationManager;
import io.github.kawaiicakes.civilization.api.network.SimplePacket;
import io.github.kawaiicakes.civilization.api.utils.CivNBT;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;

public class C2SNewNationPacket extends SimplePacket {
    private final Nation nation;

    /**
     * Sends an instance of this with the given <code>Nation</code> to the server
     * when passed as the argument in <code>SimpleChannel</code>.
     * <br><br>
     * For security reasons, the validity of the nation to create should only
     * be checked on the server.
     * @param nation    the <code>Nation</code> to send to the server.
     */
    public C2SNewNationPacket(Nation nation) {
        super();
        this.nation = nation;
    }

    public C2SNewNationPacket(FriendlyByteBuf buf) {
        super(buf);
        this.nation = CivNBT.fromNBT(Objects.requireNonNull(buf.readNbt()));
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(CivNBT.fromNation(this.nation));
    }

    @Override
    public void onReceipt(NetworkEvent.Context context) {
        ServerLevel level = Objects.requireNonNull(context.getSender()).getLevel();

        if (NationManager.createNation(level, this.nation)) {
            context.getSender().sendSystemMessage(Component.literal("Successfully founded nation " + this.nation.nationName()
                    + " with UUID " + this.nation.nationUUID()));
        } else {
            context.getSender().sendSystemMessage(Component.literal("Invalid nation!"));
        }
    }
}