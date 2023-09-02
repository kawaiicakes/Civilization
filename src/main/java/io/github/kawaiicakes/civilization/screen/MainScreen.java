package io.github.kawaiicakes.civilization.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.kawaiicakes.civilization.Civilization;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class MainScreen extends Screen {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/menu/main.png");
    public static final Component MAIN_SCREEN_NAME = Component.translatable("menu.civilization.main_menu");
    public static final Component BUTTON_INFO = Component.translatable("menu.button.civilization.player_info");
    private int leftPos;
    private int topPos;
    private static final int imageWidth = 208;
    private static final int imageHeight = 166;

    public MainScreen() {
        super(MAIN_SCREEN_NAME);

    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - imageWidth) / 2;
        this.topPos = (this.height - imageHeight) / 2;
        this.addRenderableWidget(new Button(0, 0, 90, 20, BUTTON_INFO, pButton -> {
            // TODO: make this send a C2S packet
            Civilization.LOGGER.info("Click!");
        }));
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        renderBgTexture(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
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
        this.blit(pPoseStack, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
    }
}
