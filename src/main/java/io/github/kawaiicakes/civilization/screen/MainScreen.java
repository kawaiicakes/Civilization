package io.github.kawaiicakes.civilization.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class MainScreen extends Screen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/menu/main.png");
    private static final short textureWidth = 800;
    private static final short textureHeight = 256;
    private static final short bgWidth = 277;
    private static final short bgHeight = 218;
    private static final short tabWidth = 155;
    private static final short tabHeight = 28;
    private static final short tabUOffset = 277;
    private static final short tabSelectedVOffset = 104;
    private static final short tabSelectedBottomVOffset = 132;
    private static final short tabUnselectedVOffset = 76;
    private static final byte tabCount = 4;

    private static final Component MAIN_SCREEN_NAME = Component.translatable("menu.civilization.main_menu");
    private static final Component BUTTON_INFO = Component.translatable("menu.button.civilization.player_info");
    private int leftPos;
    private int topPos;
    private byte activeTab = 0;

    public MainScreen() {
        super(MAIN_SCREEN_NAME);

    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - (bgWidth + tabWidth - 4)) / 2;
        this.topPos = (this.height - bgHeight) / 2;

        // this.addRenderableWidget(new Button(leftPos, topPos, Button.SMALL_WIDTH, Button.DEFAULT_HEIGHT, BUTTON_INFO, pButton -> // TODO: make this send a C2S packet Civilization.LOGGER.info("Click!")));
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        renderBgTexture(pPoseStack);

        renderTabs(pPoseStack);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick); //call last?
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == InputConstants.KEY_ESCAPE || pKeyCode == InputConstants.KEY_M) {
            this.onClose();
            return true;
        } else {
            return false;
        }
    }

    private void renderBgTexture(PoseStack pPoseStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.enableBlend();

        //blit(pPoseStack, this.leftPos, this.topPos, 0, 0, bgWidth, bgHeight, 1024, 256);
        blit(pPoseStack, this.leftPos, this.topPos, 0, 0, bgWidth, bgHeight, textureWidth, textureHeight);
        this.renderTabs(pPoseStack);
    }

    private void renderTabs(PoseStack poseStack) {
        for (byte i = 0; i < tabCount; i++) {
            if (this.activeTab == 0 && i == 0) {
                this.renderTab(poseStack, i, tabSelectedBottomVOffset);
            } else if (this.activeTab == i) {
                this.renderTab(poseStack, i, tabSelectedVOffset);
            } else {
                this.renderTab(poseStack, i, tabUnselectedVOffset);
            }
        }
    }

    private void renderTab(PoseStack poseStack, int tab, int vOffset) {
        blit(poseStack, this.leftPos + bgWidth - 4, (this.topPos + bgHeight - tabHeight) - (tab * (tabHeight + 1)),
                tabUOffset, vOffset, tabWidth, tabHeight, textureWidth, textureHeight);
    }
}
