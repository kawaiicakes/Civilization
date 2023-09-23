package io.github.kawaiicakes.civilization.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.kawaiicakes.civilization.api.screen.BlitRenderDefinition;
import io.github.kawaiicakes.civilization.api.screen.DynamicButton;
import io.github.kawaiicakes.civilization.api.screen.SimpleGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    protected UUID nation;
    protected List<UUID> cities;
    protected int reputation;

    protected int tabLeftPos;

    protected int xMouse;
    protected int yMouse;

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
        return this.activeTab;
    }

    @Override
    public void init() {
        // Renders background texture + tabs to the centre of the screen.
        this.leftPos = (this.width - (this.background.blitUWidth() + TAB_UNSELECTED.blitUWidth())) / 2;
        this.topPos = (this.height - this.background.blitVHeight()) / 2;

        this.tabLeftPos = this.leftPos + this.background.blitUWidth() - 4;

        this.initializeTabs();
    }

    // Place black box at 14 25 relative to 0 0 of background
    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.setTabState();
        this.xMouse = pMouseX;
        this.yMouse = pMouseY;
        this.renderActiveMenu(pPoseStack, pMouseX, pMouseY, pPartialTick);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void renderBackground(@NotNull PoseStack pPoseStack) {
        super.renderBackground(pPoseStack);

        this.blit(pPoseStack, LOGO.renderAtNewPos(
                this.leftPos + this.background.blitUWidth()
                        + ((TAB_UNSELECTED.blitUWidth() - LOGO.blitUWidth() - 4) / 2),
                this.topPos + 20)
        );

        if (Objects.equals(this.activeTab, "player_info")) {
            // FIXME: move this to #renderOverviewMenu. The problem is doing so makes the model dark for some reason
            renderEntityInInventory(this.leftPos + 51, this.topPos + 75, (float) (this.leftPos + 51) - this.xMouse, (float) (this.topPos + 75 - 50) - this.yMouse, this.minecraft.player);
        }
    }

    private void renderActiveMenu(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        switch (this.activeTab) {
            case "player_info" -> this.renderOverviewMenu(pPoseStack, pMouseX, pMouseY, pPartialTick);
            case "nation_info" -> this.renderNationMenu(pPoseStack, pMouseX, pMouseY, pPartialTick);
            case "city_info" -> this.renderCityMenu(pPoseStack, pMouseX, pMouseY, pPartialTick);
            case "reputation_info" -> this.renderReputationMenu(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }
    }

    private static void renderEntityInInventory(int pPosX, int pPosY, float pMouseX, float pMouseY, LivingEntity pLivingEntity) {
        float f = (float)Math.atan(pMouseX / 40.0F);
        float f1 = (float)Math.atan(pMouseY / 40.0F);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(pPosX, pPosY, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale(80F, 80F, 80F);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        posestack1.mulPose(quaternion);
        float f2 = pLivingEntity.yBodyRot;
        float f3 = pLivingEntity.getYRot();
        float f4 = pLivingEntity.getXRot();
        float f5 = pLivingEntity.yHeadRotO;
        float f6 = pLivingEntity.yHeadRot;
        pLivingEntity.yBodyRot = 180.0F + f * 20.0F;
        pLivingEntity.setYRot(180.0F + f * 40.0F);
        pLivingEntity.setXRot(-f1 * 20.0F);
        pLivingEntity.yHeadRot = pLivingEntity.getYRot();
        pLivingEntity.yHeadRotO = pLivingEntity.getYRot();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(pLivingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880);
        });
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        pLivingEntity.yBodyRot = f2;
        pLivingEntity.setYRot(f3);
        pLivingEntity.setXRot(f4);
        pLivingEntity.yHeadRotO = f5;
        pLivingEntity.yHeadRot = f6;

        posestack.popPose(); // If this is moved to the bottom of the method everything glitches out

        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
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

            this.addRenderableWidget(new TabButton(topPos, tabNames[tab]));
        }
    }

    private void setTabState() {
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

    private void renderOverviewMenu(PoseStack poseStack, int xMouse, int yMouse, float tickDelta) {
        //renderEntityInInventory(this.leftPos + 51, this.topPos + 75, 80, (float) (this.leftPos + 51) - this.xMouse, (float) (this.topPos + 75 - 50) - this.yMouse, this.minecraft.player);
    }

    private void renderNationMenu(PoseStack poseStack, int xMouse, int yMouse, float tickDelta) {

    }

    private void renderCityMenu(PoseStack poseStack, int xMouse, int yMouse, float tickDelta) {

    }

    private void renderReputationMenu(PoseStack poseStack, int xMouse, int yMouse, float tickDelta) {

    }

    private class TabButton extends DynamicButton {
        private final String tabName;

        public TabButton(int topPos, String tabName) {
            super(
                    TAB_UNSELECTED.renderAtNewPos(MainPlayerScreen.this.tabLeftPos, topPos),
                    MainPlayerScreen.this.textureLocation,
                    Component.translatable("menu.civilization." + tabName),
                    (pButton) -> MainPlayerScreen.this.setActiveTab(tabName),
                    ((pButton, pPoseStack, pMouseX, pMouseY) ->
                            MainPlayerScreen.this.renderTooltip(
                                    pPoseStack,
                                    Component.translatable("menu.civilization." + tabName + ".tooltip"),
                                    pMouseX,
                                    pMouseY
                            )
                    ),
                    MainPlayerScreen.this.textureWidth,
                    MainPlayerScreen.this.textureHeight
            );

            this.tabName = tabName;
        }

        @Override
        public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            pPoseStack.pushPose();
            RenderSystem.enableDepthTest();
            switch (this.getState()) {
                case "selected" ->
                        this.renderSexyTab(pPoseStack, pMouseX, pMouseY, pPartialTick, TAB_SELECTED_OFFSET, 5, "d9fa75");
                case "selected_bottom" ->
                        this.renderSexyTab(pPoseStack, pMouseX, pMouseY, pPartialTick, TAB_SELECTED_BOTTOM_OFFSET, 5, "d9fa75");
                case "unselected" ->
                        this.renderSexyTab(pPoseStack, pMouseX, pMouseY, pPartialTick, TAB_UNSELECTED.blitVOffset(), -1, "747474");
                default ->
                        this.renderSexyTab(pPoseStack, pMouseX, pMouseY, pPartialTick, 0, -1, "747474");
            }
            RenderSystem.disableDepthTest();
            pPoseStack.popPose();
        }

        public void renderSexyTab(PoseStack poseStack, int xMouse, int yMouse, float tickDelta, int blitVOffset, int blitOffset, String hexColor) {
            poseStack.pushPose();
            this.renderDefinition = this.renderDefinition.blitFromNewY(blitVOffset);
            poseStack.translate(0, 0, blitOffset);
            super.renderButton(poseStack, xMouse, yMouse, tickDelta);
            drawString(poseStack, Minecraft.getInstance().font, Component.translatable("menu.civilization." + this.tabName),
                    this.x + 10, this.y + (this.renderDefinition.blitVHeight() / 2) - 5, Integer.parseInt(hexColor, 16));
            poseStack.popPose();
        }
    }
}