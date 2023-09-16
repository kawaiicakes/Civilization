package io.github.kawaiicakes.civilization.network.packets;

import io.github.kawaiicakes.civilization.api.nations.CivLevelNation;
import io.github.kawaiicakes.civilization.api.nations.NationManager;
import io.github.kawaiicakes.civilization.api.network.SimplePacket;
import io.github.kawaiicakes.civilization.api.utils.CivNBT;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;

public class C2SNewNationPacket extends SimplePacket {
    private final CivLevelNation civLevelNation;

    /**
     * Sends an instance of this with the given <code>CivLevelNation</code> to the server
     * when passed as the argument in <code>SimpleChannel</code>.
     * <br><br>
     * For security reasons, the validity of the civLevelNation to create should only
     * be checked on the server.
     * @param civLevelNation    the <code>CivLevelNation</code> to send to the server.
     */
    public C2SNewNationPacket(CivLevelNation civLevelNation) {
        super();
        this.civLevelNation = civLevelNation;
    }

    public C2SNewNationPacket(FriendlyByteBuf buf) {
        super(buf);
        this.civLevelNation = CivNBT.fromNBT(Objects.requireNonNull(buf.readNbt()));
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.civLevelNation.serializeNBT());
    }

    @Override
    public void onReceipt(NetworkEvent.Context context) {
        ServerLevel level = Objects.requireNonNull(context.getSender()).getLevel();

        if (NationManager.createNation(level, this.civLevelNation)) {
            context.getSender().sendSystemMessage(Component.literal("Successfully founded civLevelNation " + this.civLevelNation.nationName()
                    + " with UUID " + this.civLevelNation.nationUUID()));
        } else {
            context.getSender().sendSystemMessage(Component.literal("Invalid civLevelNation!"));
        }
    }
}