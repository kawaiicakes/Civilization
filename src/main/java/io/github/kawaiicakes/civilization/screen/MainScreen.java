package io.github.kawaiicakes.civilization.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MainScreen extends Screen {
    public static final Component MAIN = Component.translatable("menu.civilization.main_menu");

    public MainScreen() {
        super(MAIN);

    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
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
}
