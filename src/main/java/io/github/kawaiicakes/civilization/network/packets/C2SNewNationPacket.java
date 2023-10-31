package io.github.kawaiicakes.civilization.network.packets;

import io.github.kawaiicakes.civilization.api.NationManager;
import io.github.kawaiicakes.civilization.api.network.SimplePacket;
import io.github.kawaiicakes.civilization.data.CivNation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;

import static io.github.kawaiicakes.civilization.Civilization.CHAT_HEADER;

public class C2SNewNationPacket extends SimplePacket {
    private final CivNation civNation;

    /**
     * Sends an instance of this with the given <code>CivLevelNation</code> to the server
     * when passed as the argument in <code>SimpleChannel</code>.
     * <br><br>
     * For security reasons, the validity of the civLevelNation to create should only
     * be checked on the server.
     * @param civNation    the <code>CivLevelNation</code> to send to the server.
     */
    public C2SNewNationPacket(CivNation civNation) {
        this.civNation = civNation;
    }

    public C2SNewNationPacket(FriendlyByteBuf buf) {
        this.civNation = new CivNation(Objects.requireNonNull(buf.readAnySizeNbt()));
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.civNation.serializeNBT());
    }

    @Override
    public void onReceipt(NetworkEvent.Context context) {
        if (NationManager.foundNation(this.civNation)) {
            Objects.requireNonNull(context.getSender()).sendSystemMessage(
                    CHAT_HEADER().append(Component.translatable("chat.civilization.nation_founded_client", this.civNation.name).setStyle(Style.EMPTY))
            );
        } else {
            Objects.requireNonNull(context.getSender()).sendSystemMessage(
                    CHAT_HEADER().append(Component.translatable("chat.civilization.invalid_nation").setStyle(Style.EMPTY))
            );
        }
    }
}