package io.github.kawaiicakes.civilization.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.kawaiicakes.civilization.api.screen.BlitRenderDefinition;
import io.github.kawaiicakes.civilization.api.screen.DynamicButton;
import io.github.kawaiicakes.civilization.api.screen.SimpleGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

    public void setActiveTab(String tab) {
        this.activeTab = tab;
    }

    public String getActiveTab() {
        return activeTab;
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
        this.renderTabs();
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    /**
     * The tabs of this screen and what they do are defined here.
     * <code>$blitVOffset</code> of the <code>BlitRenderDefinition</code> is technically irrelevant here
     * as the tab has not been rendered yet. The correct v-offset may be assigned elsewhere.
     */
    private void initializeTabs() {
        int tabTopPosDefault = (this.topPos + this.background.blitVHeight() - TAB_UNSELECTED.blitVHeight());
        String[] tabNames = {"player_info", "city_info", "nation_info", "reputation_info"};

        for (int tab = 0; tab < 4; tab++) {
            int topPos = tabTopPosDefault - (tab * (TAB_UNSELECTED.blitVHeight() + 1));

            this.createTab(topPos, tabNames[tab]);
        }
    }

    private void renderTabs() {
        String[] tabNames = {"player_info", "city_info", "nation_info", "reputation_info"};

        for (int tab = 0; tab < 4; tab++) {
            if (Objects.equals(this.activeTab, tabNames[tab]) && tab == 0) {
                ((DynamicButton) this.renderables.get(tab)).setState("selected_bottom");
            } else if (Objects.equals(this.activeTab, tabNames[tab]) && tab != 0) {
                ((DynamicButton) this.renderables.get(tab)).setState("selected");
            } else {
                ((DynamicButton) this.renderables.get(tab)).setState("unselected");
            }
        }
    }

    /**
     * Small helper method to cut down on repeatedly typing the same arguments.
     */
    private void createTab(int topPos, String tabName) {
        Font pFont = Minecraft.getInstance().font;

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
                ) {
                    // TODO: consider extracting this to a new subclass.
                    @Override
                    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
                        RenderSystem.enableDepthTest();
                        switch (this.getState()) {
                            case "selected" ->
                                    this.renderSexyTab(pPoseStack, pMouseX, pMouseY, pPartialTick, TAB_SELECTED_OFFSET, 5, tabName, "d9fa75");
                            case "selected_bottom" ->
                                    this.renderSexyTab(pPoseStack, pMouseX, pMouseY, pPartialTick, TAB_SELECTED_BOTTOM_OFFSET, 5, tabName, "d9fa75");
                            case "unselected" ->
                                    this.renderSexyTab(pPoseStack, pMouseX, pMouseY, pPartialTick, TAB_UNSELECTED.blitVOffset(), -1, tabName, "747474");
                            default ->
                                    this.renderSexyTab(pPoseStack, pMouseX, pMouseY, pPartialTick, 0, -1, tabName, "747474");
                        }
                        RenderSystem.disableDepthTest();
                    }

                    public void renderSexyTab(PoseStack poseStack, int xMouse, int yMouse, float tickDelta, int blitVOffset, int blitOffset, String tabName, String hexColor) {
                        poseStack.pushPose();
                        this.renderDefinition = this.renderDefinition.blitFromNewY(blitVOffset);
                        poseStack.translate(0, 0, blitOffset);
                        super.renderButton(poseStack, xMouse, yMouse, tickDelta);
                        drawString(poseStack, pFont, Component.translatable("menu.civilization." + tabName),
                                this.x + (this.renderDefinition.blitUWidth() / 2) - 6, this.y + (this.renderDefinition.blitVHeight() / 2) - 5, Integer.parseInt(hexColor, 16));
                        poseStack.popPose();
                    }
                }
        );
    }
}