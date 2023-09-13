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
import net.minecraft.client.gui.Font;
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

        renderEntityInInventory(this.leftPos + 51, this.topPos + 75, 30, (float)(this.leftPos + 51) - this.xMouse, (float)(this.topPos + 75 - 50) - this.yMouse, this.minecraft.player);
    }

    // Place black box at 14 25 relative to 0 0 of background
    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderTabs();
        this.xMouse = pMouseX;
        this.yMouse = pMouseY;
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    public void renderNationTab(PoseStack poseStack, int xMouse, int yMouse, float tickDelta) {
        if (this.nation == null) {
            // TODO
        }
    }

    public static void renderEntityInInventory(int pPosX, int pPosY, int pScale, float pMouseX, float pMouseY, LivingEntity pLivingEntity) {
        float f = (float)Math.atan((double)(pMouseX / 40.0F));
        float f1 = (float)Math.atan((double)(pMouseY / 40.0F));
        renderEntityInInventoryRaw(pPosX, pPosY, pScale, f, f1, pLivingEntity);
    }

    public static void renderEntityInInventoryRaw(int pPosX, int pPosY, int pScale, float angleXComponent, float angleYComponent, LivingEntity pLivingEntity) {
        float f = angleXComponent;
        float f1 = angleYComponent;
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double)pPosX, (double)pPosY, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale((float)pScale, (float)pScale, (float)pScale);
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
        posestack.popPose();
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
                                this.x + 10, this.y + (this.renderDefinition.blitVHeight() / 2) - 5, Integer.parseInt(hexColor, 16));
                        poseStack.popPose();
                    }
                }
        );
    }
}