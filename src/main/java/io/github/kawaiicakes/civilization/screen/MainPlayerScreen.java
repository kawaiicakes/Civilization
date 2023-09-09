package io.github.kawaiicakes.civilization.screen;

import io.github.kawaiicakes.civilization.api.screen.AbstractGUI;
import io.github.kawaiicakes.civilization.api.screen.BlitRenderDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class MainPlayerScreen extends AbstractGUI {
    private static final Component MAIN_SCREEN = Component.translatable("menu.civilization.main_menu");

    /**
     * Characteristics of tabs in this screen. leftPos and topPos left null as this is only here for convenience in
     * accessing blit info.
     */
    private static final BlitRenderDefinition TAB_UNSELECTED = new BlitRenderDefinition(null, null,
            277, 76, 155, 28);
    private static final BlitRenderDefinition TAB_SELECTED = TAB_UNSELECTED.blitFromNewY(104);
    private static final BlitRenderDefinition TAB_SELECTED_BOTTOM = TAB_UNSELECTED.blitFromNewY(132);

    public MainPlayerScreen() {
        // This call to the super constructor essentially establishes the texture location and the blitting info
        // for the main part of the background
        super(
                MAIN_SCREEN,
                new ResourceLocation(MOD_ID, "textures/gui/menu/main.png"),
                800,
                256,
                // leftPos and topPos are declared null as where to render the background can only be determined in #init
                new BlitRenderDefinition(null, null,
                        0, 0, 277, 218)
        );

        this.BLIT_RENDER_LIST = new HashMap<>();
    }

    @Override
    public void init() {
        // Renders background texture + tabs to the centre of the screen.
        this.leftPos = (this.width - (this.background.blitUWidth() + TAB_UNSELECTED.blitUWidth())) / 2;
        this.topPos = (this.height - this.background.blitVHeight()) / 2;
    }
}
