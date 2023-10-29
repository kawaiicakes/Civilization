package io.github.kawaiicakes.civilization.network.packets;

import io.github.kawaiicakes.civilization.api.CivManager;
import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.api.network.SimplePacket;
import io.github.kawaiicakes.civilization.capabilities.data.CivCity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;

import static io.github.kawaiicakes.civilization.Civilization.CHAT_HEADER;
import static io.github.kawaiicakes.civilization.Civilization.LOGGER;

/**
 * The sender of this packet must be a <code>ServerPlayer</code> instance, or it will not work.
 */
public class C2SNewCityPacket extends SimplePacket {
    private final CivCity civNation;

    /**
     * Sends an instance of this with the given <code>CivLevelNation</code> to the server
     * when passed as the argument in <code>SimpleChannel</code>.
     * @param civNation    the <code>CivLevelNation</code> to send to the server.
     */
    public C2SNewCityPacket(CivCity civNation) {
        this.civNation = civNation;
    }

    public C2SNewCityPacket(FriendlyByteBuf buf) {
        this.civNation = CivCity.deserializeNBT(Objects.requireNonNull(buf.readAnySizeNbt()));
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.civNation.serializeNBT());
    }

    @Override
    public void onReceipt(NetworkEvent.Context context) {
        if (context.getSender() == null) {
            LOGGER.info("The packet sender is null! This packet must be sent by a player!");
            return;
        }

        ServerPlayer player = context.getSender();
        if (CivManager.foundCity(player.getLevel(), HexTilePos.chunkToHexPos(player.chunkPosition()), this.civNation)) {
            Objects.requireNonNull(context.getSender()).sendSystemMessage(
                    CHAT_HEADER().append(Component.translatable("chat.civilization.city_founded_client", this.civNation.name()).setStyle(Style.EMPTY))
            );
        } else {
            Objects.requireNonNull(context.getSender()).sendSystemMessage(
                    CHAT_HEADER().append(Component.translatable("chat.civilization.invalid_city").setStyle(Style.EMPTY))
            );
        }
    }
}