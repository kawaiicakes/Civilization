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
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/menu/main.png");
    public static final Component MAIN = Component.translatable("menu.civilization.main_menu");
    private int leftPos;
    private int topPos;
    private static final int imageWidth = 1080;
    private static final int imageHeight = 720;

    public MainScreen() {
        super(MAIN);

    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - imageWidth) / 2;
        this.topPos = (this.height - imageHeight) / 2;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderBgTexture(pPoseStack);
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
        blit(pPoseStack, 0, 0, 0, 0, imageWidth, imageHeight, this.width, this.height);
    }
}
