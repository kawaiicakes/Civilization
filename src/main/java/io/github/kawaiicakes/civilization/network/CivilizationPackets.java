package io.github.kawaiicakes.civilization.network;

import io.github.kawaiicakes.civilization.network.packets.NationC2SPacket;
import io.github.kawaiicakes.civilization.network.packets.NationS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class CivilizationPackets {
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

        net.messageBuilder(NationC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(NationC2SPacket::new)
                .encoder(NationC2SPacket::toBytes)
                .consumerMainThread(NationC2SPacket::handle)
                .add();

        net.messageBuilder(NationS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(NationS2CPacket::new)
                .encoder(NationS2CPacket::toBytes)
                .consumerMainThread(NationS2CPacket::handle)
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
