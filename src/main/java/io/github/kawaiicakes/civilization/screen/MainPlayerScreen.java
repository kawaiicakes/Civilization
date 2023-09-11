package io.github.kawaiicakes.civilization.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.kawaiicakes.civilization.api.screen.BlitRenderDefinition;
import io.github.kawaiicakes.civilization.api.screen.DynamicButton;
import io.github.kawaiicakes.civilization.api.screen.SimpleGUI;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class MainPlayerScreen extends SimpleGUI {
    protected static final BlitRenderDefinition LOGO = new BlitRenderDefinition(
            0, 0,
            277, 8, 132, 64
    );

    /**
     * Characteristics of tabs in this screen. leftPos and topPos left null as this is only here for convenience in
     * accessing blit info; not for defining render coordinates.
     */
    protected static final BlitRenderDefinition TAB_UNSELECTED = new BlitRenderDefinition(0, 0,
            277, 76, 155, 28);
    protected static final short TAB_SELECTED_OFFSET = 104;
    protected static final short TAB_SELECTED_BOTTOM_OFFSET = 132;

    protected String activeTab;

    protected int tabLeftPos;

    public MainPlayerScreen() {
        // This call to the super constructor essentially establishes the texture location and the blitting info
        // for the main part of the background
        super(
                Component.translatable("menu.civilization.main_menu"),
                new ResourceLocation(MOD_ID, "textures/gui/menu/main.png"),
                800,
                256,
                // leftPos and topPos are declared null as where to render the background can only be determined in #init
                new BlitRenderDefinition(0, 0,
                        0, 0, 277, 218)
        );

        this.setActiveTab("player_info");
    }

    @Override
    public void init() {
        // Renders background texture + tabs to the centre of the screen.
        this.leftPos = (this.width - (this.background.blitUWidth() + TAB_UNSELECTED.blitUWidth())) / 2;
        this.topPos = (this.height - this.background.blitVHeight()) / 2;

        this.tabLeftPos = this.leftPos + this.background.blitUWidth() - 4;

        this.initializeTabs();
    }



    @Override
    public void renderBackground(@NotNull PoseStack pPoseStack) {
        super.renderBackground(pPoseStack);

        // logo
        // FIXME: relativize topPos?
        this.blit(pPoseStack, LOGO.renderAtNewPos(
                this.leftPos + this.background.blitUWidth()
                        + ((TAB_UNSELECTED.blitUWidth() - LOGO.blitUWidth() - 4) / 2),
                this.topPos + 20)
        );
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    /**
     * The tabs of this screen and what they do are defined here.
     * <code>$blitVOffset</code> of the <code>BlitRenderDefinition</code> is irrelevant here
     * as the tab has not been rendered yet. The correct v-offset will be assigned elsewhere.
     */
    public void initializeTabs() {
        int tabTopPosDefault = (this.topPos + this.background.blitVHeight() - TAB_UNSELECTED.blitVHeight());
        String[] tabNames = {"player_info", "city_info", "nation_info", "reputation_info"};

        for (int tab = 0; tab < 4; tab++) {
            int topPos = tabTopPosDefault - (tab * (TAB_UNSELECTED.blitVHeight()));

            this.createTab(topPos, tabNames[tab]);
        }
    }

    @Override
    protected void renderTooltip(PoseStack pPoseStack, ItemStack pItemStack, int pMouseX, int pMouseY) {
        super.renderTooltip(pPoseStack, pItemStack, pMouseX, pMouseY);
    }

    public void renderTabs(PoseStack poseStack) {

    }

    public void setActiveTab(String tab) {
        this.activeTab = tab;
    }

    public String getActiveTab() {
        return activeTab;
    }

    /**
     * Small helper method to cut down on repeatedly typing the same arguments.
     */
    private void createTab(int topPos, String tabName) {
        this.addRenderableWidget(
                new DynamicButton(
                        TAB_UNSELECTED.renderAtNewPos(this.tabLeftPos, topPos),
                        this.textureLocation,
                        Component.translatable("menu.civilization." + tabName),
                        (pButton) -> this.setActiveTab(tabName),
                        ((pButton, pPoseStack, pMouseX, pMouseY) ->
                                this.renderTooltip(
                                        pPoseStack,
                                        Component.translatable("menu.civilization." + tabName + ".tooltip"),
                                        pMouseX,
                                        pMouseY
                                )
                        ),
                        this.textureWidth,
                        this.textureHeight
                )
        );
    }
}