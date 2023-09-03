package io.github.kawaiicakes.civilization.events;

import io.github.kawaiicakes.civilization.Civilization;
import io.github.kawaiicakes.civilization.client.KeyBinds;
import io.github.kawaiicakes.civilization.screen.MainScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class PlayerEvents {
    @Mod.EventBusSubscriber(modid = Civilization.MOD_ID, value = Dist.CLIENT)
    public static class PlayerForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBinds.CIV_MENU.consumeClick()) {
                Minecraft.getInstance().setScreen(new MainScreen());
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
}