package io.github.kawaiicakes.civilization.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.kawaiicakes.civilization.Civilization;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

/**
 * Jesus this code is messy.
 */
// FIXME: hover areas change when changing window size.
// FIXME: relative v. absolute screen coordinates causing weird fuck shit? store conversions as fields?
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
    private static final short logoWidth = 132;
    private static final byte logoHeight = 64;
    private static final byte logoVOffset = 8;

    private static final Component MAIN_SCREEN_NAME = Component.translatable("menu.civilization.main_menu");
    private static final Component TAB_PLAYER_INFO = Component.translatable("menu.button.civilization.player_info");
    private int leftPos;
    private int topPos;
    private byte activeTab = 0;

    public MainScreen() {
        // Screen title is used for narration!
        super(MAIN_SCREEN_NAME);
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - (bgWidth + tabWidth - 4)) / 2;
        this.topPos = (this.height - bgHeight) / 2;

        this.addRenderableWidget(new ImageButton(30, 30, tabWidth, tabHeight, textureWidth, textureHeight, TEXTURE, pButton -> {
            Civilization.LOGGER.info("CLICKING");}));

        // TODO: make a new GUI component for this...
        // this.addRenderableWidget(new Button(leftPos, topPos, Button.SMALL_WIDTH, Button.DEFAULT_HEIGHT, BUTTON_INFO, pButton -> // TODO: make this send a C2S packet Civilization.LOGGER.info("Click!")));
    }

    // Check L785 in CreativeModeInventoryScreen
    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        // TODO: Text may be rendered using this.font
        this.renderBackground(pPoseStack);
        this.renderBgTexture(pPoseStack, pMouseX, pMouseY);
        this.renderTabs(pPoseStack, pMouseX, pMouseY);

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

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.isHovering(this.leftPos + bgWidth - 4, (this.topPos + bgHeight - tabHeight), tabWidth, tabHeight, pMouseX, pMouseY)) {
            if (this.activeTab == 3) {
                this.activeTab = 0;
            } else {
                this.activeTab++;
            }
            return true;
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    private void renderBgTexture(PoseStack pPoseStack, int xMouse, int yMouse) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.enableBlend();

        // Renders background
        blit(pPoseStack, this.leftPos, this.topPos, 0, 0, bgWidth, bgHeight, textureWidth, textureHeight);
        // Renders logo
        blit(pPoseStack, this.leftPos + bgWidth + ((tabWidth - logoWidth - 4) / 2), this.topPos + 20, tabUOffset, logoVOffset,
                logoWidth, logoHeight, textureWidth, textureHeight);
    }

    private void renderTabs(PoseStack poseStack, int xMouse, int yMouse) {
        for (byte i = 0; i < tabCount; i++) {
            if (this.activeTab == 0 && i == 0) {
                this.renderTab(poseStack, i, tabSelectedBottomVOffset);
            } else if (this.activeTab == i) {
                this.renderTab(poseStack, i, tabSelectedVOffset);
            } else {
                this.renderTab(poseStack, i, tabUnselectedVOffset);
            }
        }
        for (byte i = 0; i < tabCount; i++) {
            this.isHoveringTab(poseStack, i, xMouse, yMouse);
        }
    }

    private void renderTab(PoseStack poseStack, int tab, int vOffset) {
        blit(poseStack, this.leftPos + bgWidth - 4, (this.topPos + bgHeight - tabHeight) - (tab * (tabHeight + 1)),
                tabUOffset, vOffset, tabWidth, tabHeight, textureWidth, textureHeight);
    }

    private void isHoveringTab(PoseStack poseStack, byte tab, int xMouse, int yMouse) {
        int y = (this.height - bgHeight) / 2; // Why is this necessary? Why isn't x necessary?
        if (this.isHovering(this.leftPos + bgWidth - 4, (this.topPos + bgHeight - tabHeight) - (tab * (tabHeight + 1)) - y,
                tabWidth, tabHeight, xMouse, yMouse)) {
            this.renderTooltip(poseStack, TAB_PLAYER_INFO, xMouse, yMouse);
        };
    }

    /**
     * Use this method to determine if the cursor is hovering over a defined rectangle.
     * @param pX The int defining the leftmost part of the area to check.
     * @param pY The int defining the topmost part of the area to check.
     * @param pWidth    The int defining the width of the rectangle.
     * @param pHeight   The int defining the height of the rectangle.
     * @param pMouseX   The double describing the mouse's x position.
     * @param pMouseY   The double describing the mouse's y position.
     * @return  <code>true</code> if the given mouse coordinates are inside the rectangle. <code>false</code> otherwise.
     */
    private boolean isHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        pMouseX -= i;
        pMouseY -= j;
        return pMouseX >= (double)(pX - 1) && pMouseX < (double)(pX + pWidth + 1) && pMouseY >= (double)(pY - 1) && pMouseY < (double)(pY + pHeight + 1);
    }
}
