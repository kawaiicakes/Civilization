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
     * accessing blit info; not for defining render coordinates.
     */
    private static final BlitRenderDefinition TAB_UNSELECTED = new BlitRenderDefinition(null, null,
            277, 76, 155, 28);
    private static final BlitRenderDefinition TAB_SELECTED = TAB_UNSELECTED.blitFromNewY(104);
    private static final BlitRenderDefinition TAB_SELECTED_BOTTOM = TAB_UNSELECTED.blitFromNewY(132);

    public byte activeTab;

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

        this.BLIT_RENDER_LIST = new HashMap<>(); // FIXME unselected texture on bottom tab
        this.addToRenderList("player_profile", TAB_UNSELECTED);
        this.addToRenderList("city_info", TAB_UNSELECTED);
        this.addToRenderList("nation_info", TAB_UNSELECTED);
        this.addToRenderList("reputation_info", TAB_UNSELECTED);

        this.setActiveTab((byte) 0);
    }

    @Override
    public void init() {
        // Renders background texture + tabs to the centre of the screen.
        this.leftPos = (this.width - (this.background.blitUWidth() + TAB_UNSELECTED.blitUWidth())) / 2;
        this.topPos = (this.height - this.background.blitVHeight()) / 2;

        int tabLeftPos = this.leftPos + this.background.blitUWidth() - 4;

        this.repositionBlit("player_profile", tabLeftPos, this.tabTopPosHelper(0));
        this.repositionBlit("city_info", tabLeftPos, this.tabTopPosHelper(1));
        this.repositionBlit("nation_info", tabLeftPos, this.tabTopPosHelper(2));
        this.repositionBlit("reputation_info", tabLeftPos, this.tabTopPosHelper(3));
    }

    public void setActiveTab(byte tab) {
        this.activeTab = tab;
        this.tabHelper();
    }

    /**
     * All this does is return the correct position to render at based on which 'tab' is calling it
     * @param tab   the arbitrary tab number
     * @return  the int representing a screen-relative y-coordinate
     */
    private int tabTopPosHelper(int tab) {
        return (this.topPos + this.background.blitVHeight() - TAB_UNSELECTED.blitVHeight())
                - (tab * (TAB_UNSELECTED.blitVHeight() + 1));
    }

    private void tabHelper() {
        switch (this.activeTab) {
            case 0 -> {
                this.reRender("player_profile", TAB_SELECTED_BOTTOM.blitVOffset());
                this.reRender("city_info", TAB_UNSELECTED.blitVOffset());
                this.reRender("nation_info", TAB_UNSELECTED.blitVOffset());
                this.reRender("reputation_info", TAB_UNSELECTED.blitVOffset());
            }
            case 1 -> {
                this.reRender("player_profile", TAB_UNSELECTED.blitVOffset());
                this.reRender("city_info", TAB_SELECTED.blitVOffset());
                this.reRender("nation_info", TAB_UNSELECTED.blitVOffset());
                this.reRender("reputation_info", TAB_UNSELECTED.blitVOffset());
            }
            case 2 -> {
                this.reRender("player_profile", TAB_UNSELECTED.blitVOffset());
                this.reRender("city_info", TAB_UNSELECTED.blitVOffset());
                this.reRender("nation_info", TAB_SELECTED.blitVOffset());
                this.reRender("reputation_info", TAB_UNSELECTED.blitVOffset());
            }
            case 3 -> {
                this.reRender("player_profile", TAB_UNSELECTED.blitVOffset());
                this.reRender("city_info", TAB_UNSELECTED.blitVOffset());
                this.reRender("nation_info", TAB_UNSELECTED.blitVOffset());
                this.reRender("reputation_info", TAB_SELECTED.blitVOffset());
            }
            default -> throw new IllegalStateException("Active tab does not exist!");
        }
    }
}
