package io.github.kawaiicakes.civilization.events;

import io.github.kawaiicakes.civilization.Civilization;
import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.client.KeyBinds;
import io.github.kawaiicakes.civilization.network.CivPacketHandler;
import io.github.kawaiicakes.civilization.network.packets.C2SNewNationPacket;
import io.github.kawaiicakes.civilization.client.screen.MainPlayerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.kawaiicakes.civilization.Civilization.LOGGER;
import static io.github.kawaiicakes.civilization.api.nations.NationManager.NULLARIA;

public class PlayerEvents {
    @Mod.EventBusSubscriber(modid = Civilization.MOD_ID, value = Dist.CLIENT)
    public static class PlayerForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBinds.CIV_MENU.consumeClick()) {
                Minecraft.getInstance().setScreen(new MainPlayerScreen());
                CivPacketHandler.sendToServer(new C2SNewNationPacket(NULLARIA));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = Civilization.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class PlayerModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinds.CIV_MENU);
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && !(event.getLevel().isClientSide())) {
            HexTilePos hex = HexTilePos.blockToHexPos(event.getPos());
            LOGGER.info(hex.getPrettyCoordinates());
            player.sendSystemMessage(Component.literal("Placed block in hex " + hex.getPrettyCoordinates()));
        }
    }
}
