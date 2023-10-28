package io.github.kawaiicakes.civilization.network.packets;

import io.github.kawaiicakes.civilization.api.network.SimplePacket;
import io.github.kawaiicakes.civilization.capabilities.data.CivNation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;

public class C2SNewNationPacket extends SimplePacket {
    //private final CivNation civNation;

    /**
     * Sends an instance of this with the given <code>CivLevelNation</code> to the server
     * when passed as the argument in <code>SimpleChannel</code>.
     * <br><br>
     * For security reasons, the validity of the civLevelNation to create should only
     * be checked on the server.
     * @param civNation    the <code>CivLevelNation</code> to send to the server.
     */
    public C2SNewNationPacket(CivNation civNation) {
        super();
        //this.civNation = civNation;
    }

    public C2SNewNationPacket(FriendlyByteBuf buf) {
        super(buf);
        //this.civNation = new CivNation();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        //buf.writeNbt(this.civNation.serializeNBT());
    }

    @Override
    public void onReceipt(NetworkEvent.Context context) {
        ServerLevel level = Objects.requireNonNull(context.getSender()).getLevel();
        /*
        if (NationManager.createNation(level, this.civNation)) {
            context.getSender().sendSystemMessage(
                    CHAT_HEADER().append(Component.translatable("chat.civilization.nation_founded_client", this.civNation.name()).setStyle(Style.EMPTY))
            );
        } else {
            context.getSender().sendSystemMessage(
                    CHAT_HEADER().append(Component.translatable("chat.civilization.invalid_nation").setStyle(Style.EMPTY))
            );
        }

         */
    }
}