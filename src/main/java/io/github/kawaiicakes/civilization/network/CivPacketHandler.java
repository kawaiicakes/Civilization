package io.github.kawaiicakes.civilization.network;

import io.github.kawaiicakes.civilization.network.packets.C2SNewCityPacket;
import io.github.kawaiicakes.civilization.network.packets.C2SNewNationPacket;
import io.github.kawaiicakes.civilization.network.packets.S2CPlayerMenuPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class CivPacketHandler {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0") // checks for packet compatibility
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(S2CPlayerMenuPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(S2CPlayerMenuPacket::new)
                .encoder(S2CPlayerMenuPacket::toBytes)
                .consumerMainThread(S2CPlayerMenuPacket::handle)
                .add();

        net.messageBuilder(C2SNewNationPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SNewNationPacket::new)
                .encoder(C2SNewNationPacket::toBytes)
                .consumerMainThread(C2SNewNationPacket::handle)
                .add();

        net.messageBuilder(C2SNewCityPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(C2SNewCityPacket::new)
                .encoder(C2SNewCityPacket::toBytes)
                .consumerMainThread(C2SNewCityPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static <MSG> void sendToClients(MSG msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }
}
