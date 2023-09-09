package io.github.kawaiicakes.civilization.api.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple abstract class offering the ability to easily construct GUIs used for networking.
 * These GUIs are designed to be opened using a keybind setting the screen of the client
 * Minecraft instance to this.
 *<br><br>
 * Each instance consists of a 'primary' background rendered as a texture using blitting,
 * and then other 'modular' components which are functional and/or cosmetic in nature.
 * Coordinates are defined relative to the primary background; which in turn has its
 * coordinates defined accounting for screen size among other variables.
 */
public abstract class AbstractGUI extends Screen {
    /**
     * The texture to blit from.
     */
    protected final ResourceLocation TEXTURE;
    protected final int TEXTURE_WIDTH;
    protected final int TEXTURE_HEIGHT;

    /**
     * This field stores blitting info to ease rendering all of them.
     */
    protected List<BlitRenderDefinition> BLIT_RENDER_LIST;

    /**
     * <code>leftPos</code> and <code>topPos</code> are fields defining the coordinate
     * of the leftmost and topmost point of the primary background texture respectively.
     * <code>leftPos</code> and <code>topPos</code> may be referred to as x or y, respectively.
     * Remember that x increases from left to right, while y actually increases top to bottom.
     * These are not static values. They will change depending on screen size and resolution.
     * Minecraft takes this into account in <code>#init</code>; override it accordingly.
     */
    protected int leftPos;
    protected int topPos;

    /**
     * Contains info regarding the background for rendering purposes.
     */
    public BlitRenderDefinition background;

    /**
     * The constructor is typically called when one needs to render a GUI. The instance is then
     * passed as an argument to <code>Minecraft#setScreen</code>.
     * <br><br>
     * Subclasses should call this and pass the desired background texture info as
     * the argument. This can be kept in a static final field or generated on a
     * method call. The leftPos and topPos of the <code>BlitRenderDefinition</code>
     * may be null here as <code>#init</code> is intended to define these.
     *
     * @param pTitle   the narrated text of the menu as a <code>Component</code>.
     * @param textureLocation the <code>ResourceLocation</code> of the texture to blit from.
     * @param backgroundTexture the <code>BlitRenderDefinition</code> representing the placement and blit info of the
     *                          background texture.
     */
    public AbstractGUI(Component pTitle, ResourceLocation textureLocation, int textureWidth,
                       int textureHeight, BlitRenderDefinition backgroundTexture) {
        super(pTitle);

        this.TEXTURE = textureLocation;
        this.TEXTURE_WIDTH = textureWidth;
        this.TEXTURE_HEIGHT = textureHeight;

        this.background = backgroundTexture;

        this.BLIT_RENDER_LIST = new ArrayList<>();
    }

    /**
     * This is called as soon as a screen is initialized. Screens reinitialize if the
     * window size or resolution changes. Thus, computation of relative coordinates
     * must be made in here.
     * <br><br>
     * Bearing this in mind, <code>#init</code> is responsible for 'setting up' the
     * initial settings of the screen.
     */
    @Override
    public void init() {
        // This is the default. It will render the background texture in the centre of the screen.
        this.leftPos = (this.width - this.background.blitUWidth()) / 2;
        this.topPos = (this.height - this.background.blitVHeight()) / 2;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        // Background first
        this.renderBackground(pPoseStack);

        // Other stuff second
        this.renderBlitList(pPoseStack);

        // Widgets go third
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        // Tooltips last
    }

    @Override
    public void renderBackground(PoseStack pPoseStack) {
        // Renders darkened background
        super.renderBackground(pPoseStack);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.enableBlend();

        BlitRenderDefinition bg = this.background;
        blit(pPoseStack, this.leftPos, this.topPos, bg.blitUOffset(), bg.blitVOffset(), bg.blitUWidth(), bg.blitVHeight(),
                this.TEXTURE_WIDTH, this.TEXTURE_HEIGHT);
    }

    protected void renderBlitList(PoseStack poseStack) {
        BLIT_RENDER_LIST.forEach(blit -> {
            if (blit.leftPos() != null && blit.topPos() != null) {
                this.blit(poseStack, blit.leftPos(), blit.topPos(), blit.blitUOffset(),
                        blit.blitVOffset(), blit.blitUWidth(), blit.blitVHeight());
            } else {
                throw new IllegalArgumentException("leftPos and topPos may not be null!");
            }
        });
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == InputConstants.KEY_M) { // FIXME: this won't change with the keybind
            this.onClose();
            return true;
        } else {
            return super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }
    }
}
