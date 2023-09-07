package io.github.kawaiicakes.civilization.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.kawaiicakes.civilization.api.screen.AbstractGUI;
import io.github.kawaiicakes.civilization.api.screen.BlitRenderDefinition;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class MainPlayerScreen extends AbstractGUI {
    private static final Component MAIN_SCREEN = Component.translatable("menu.civilization.main_menu");

    /**
     * Characteristics of tabs in this screen
     */
    private static final short tabWidth = 155;
    private static final byte tabHeight = 28;
    private static final short tabBlitUOffset = 277;
    private static final short tabSelectedBlitVOffset = 104;
    private static final short tabSelectedBottomBlitVOffset = 132; // Renders 'flush' with the background
    private static final short tabUnselectedBlitVOffset = 76;

    public MainPlayerScreen() {
        // This call to the super constructor essentially establishes the background texture and blitting info
        super(
                MAIN_SCREEN,
                new ResourceLocation(MOD_ID, "textures/gui/menu/main.png"),
                new BlitRenderDefinition(0, 0, 277, 218, 800, 256)
        );

    }
}
