package io.github.kawaiicakes.civilization.network.packets;

import io.github.kawaiicakes.civilization.api.CityManager;
import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.api.network.SimplePacket;
import io.github.kawaiicakes.civilization.data.CivCity;
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
    private final CivCity civCity;

    /**
     * Sends an instance of this with the given <code>CivCity</code> to the server
     * when passed as the argument in <code>SimpleChannel</code>.
     * @param civCity    the <code>CivCity</code> to send to the server.
     */
    public C2SNewCityPacket(CivCity civCity) {
        this.civCity = civCity;
    }

    public C2SNewCityPacket(FriendlyByteBuf buf) {
        this.civCity = new CivCity(Objects.requireNonNull(buf.readAnySizeNbt()));
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.civCity.serializeNBT());
    }

    @Override
    public void onReceipt(NetworkEvent.Context context) {
        if (context.getSender() == null) {
            LOGGER.info("The packet sender is null! This packet must be sent by a player!");
            return;
        }

        ServerPlayer player = context.getSender();
        if (CityManager.foundCity(player.getLevel(), HexTilePos.chunkToHexPos(player.chunkPosition()), this.civCity)) {
            Objects.requireNonNull(context.getSender()).sendSystemMessage(
                    CHAT_HEADER().append(Component.translatable("chat.civilization.city_founded_client", this.civCity.name).setStyle(Style.EMPTY))
            );
        } else {
            Objects.requireNonNull(context.getSender()).sendSystemMessage(
                    CHAT_HEADER().append(Component.translatable("chat.civilization.invalid_city").setStyle(Style.EMPTY))
            );
        }
    }
}