package io.github.kawaiicakes.civilization.commands;

import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.kawaiicakes.civilization.screen.MainPlayerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public class CommandOpenMainMenu {
    public CommandOpenMainMenu(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("civ").then(Commands.literal("main_menu").executes((command) -> openMainMenu(command.getSource()))));
    }

    private static int openMainMenu(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        return 1;
    }
}
