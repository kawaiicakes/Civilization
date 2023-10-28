package io.github.kawaiicakes.civilization.commands;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class CommandEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new CommandOpenMainMenu(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}
