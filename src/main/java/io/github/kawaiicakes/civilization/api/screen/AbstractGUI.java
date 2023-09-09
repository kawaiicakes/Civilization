package io.github.kawaiicakes.civilization.api.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

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
     * This field stores blitting info to ease rendering all of them. Blit info may be named to keep track of them.
     */
    protected Map<String, BlitRenderDefinition> BLIT_RENDER_LIST;

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
    protected BlitRenderDefinition background;

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
    protected AbstractGUI(Component pTitle, ResourceLocation textureLocation, int textureWidth,
                       int textureHeight, BlitRenderDefinition backgroundTexture) {
        super(pTitle);

        this.TEXTURE = textureLocation;
        this.TEXTURE_WIDTH = textureWidth;
        this.TEXTURE_HEIGHT = textureHeight;

        this.background = backgroundTexture;
    }

    /**
     * This is called as soon as a screen is initialized. Screens reinitialize if the
     * window size or resolution changes. Thus, computation of relative coordinates
     * must be made in here.
     * <br><br>
     * Bearing this in mind, <code>#init</code> is responsible for 'setting up' the
     * initial settings of the screen. Rendering blit info using <code>#addToRenderList</code>
     * will probably mostly take place in here.
     */
    @Override
    public void init() {
        // This is the default. It will render the background texture in the centre of the screen.
        this.leftPos = (this.width - this.background.blitUWidth()) / 2;
        this.topPos = (this.height - this.background.blitVHeight()) / 2;
    }

    /**
     * Called every frame to render stuff to the screen.
     * @param pPoseStack    the <code>PoseStack</code>
     * @param pMouseX   the int representing the x-coordinate of the mouse in screen-relative terms.
     * @param pMouseY   the int representing the y-coordinate of the mouse in screen-relative terms
     * @param pPartialTick  the number of ticks
     */
    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        // Since rendering may be dependent on a few criteria, 'check' methods should be called first.

        // Background second
        this.renderBackground(pPoseStack);

        // Other stuff third
        this.renderBlitList(pPoseStack);

        // Widgets go fourth
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        // Tooltips last
    }

    /**
     * Blits and renders the default background.
     * @param pPoseStack    none of your beeswax
     */
    @Override
    public void renderBackground(@NotNull PoseStack pPoseStack) {
        // Renders darkened background
        super.renderBackground(pPoseStack);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.enableBlend();

        BlitRenderDefinition bg = this.background;

        pPoseStack.pushPose();
        pPoseStack.translate(0, 0, -30); // Ensures the background is drawn behind everything
        blit(pPoseStack, this.leftPos, this.topPos, bg.blitUOffset(), bg.blitVOffset(), bg.blitUWidth(), bg.blitVHeight(),
                this.TEXTURE_WIDTH, this.TEXTURE_HEIGHT);
        pPoseStack.popPose();
    }

    /**
     * Add an identifiable blit to be rendered to the render list. This really only exists for convenience.
     * @param name  the String representing the name of the render.
     * @param blitInfo  the <code>BlitRenderDefinition</code> of the render.
     */
    public final void addToRenderList(String name, BlitRenderDefinition blitInfo) {
        this.BLIT_RENDER_LIST.put(name, blitInfo);
    }

    /**
     * Convenience method for changing solely the appearance of an existing blit.
     * @param name  the String representing the name of the blit to re-render.
     * @param newVOffset    the int representing the new V offset to draw from.
     */
    public final void reRender(String name, int newVOffset) {
        this.BLIT_RENDER_LIST.put(name, this.BLIT_RENDER_LIST.get(name).blitFromNewY(newVOffset));
    }

    /**
     * When called, blits the contents of <code>BLIT_RENDER_LIST</code> to the screen.
     * @param poseStack don't worry about this lmao
     */
    protected void renderBlitList(PoseStack poseStack) {
        this.BLIT_RENDER_LIST.forEach((_name, blit) -> {
            if (blit.leftPos() != null && blit.topPos() != null) {
                blit(poseStack, blit.leftPos(), blit.topPos(), blit.blitUOffset(),
                        blit.blitVOffset(), blit.blitUWidth(), blit.blitVHeight(), TEXTURE_WIDTH, TEXTURE_HEIGHT);
            } else {
                throw new IllegalArgumentException("leftPos and topPos may not be null!");
            }
        });
    }

    public final void repositionBlit(String name, int leftPos, int topPos) {
        this.BLIT_RENDER_LIST.put(name, this.BLIT_RENDER_LIST.get(name).renderAtNewPos(leftPos, topPos));
    }

    // TODO: wtf are scan codes and modifiers???
    /**
     * Automatically called every time a key is pressed inside the screen.
     * @param pKeyCode  the integer equal to the corresponding key pressed
     * @param pScanCode the integer equal to ?
     * @param pModifiers    the integer equal to ?
     * @return  true or false depending on which key is pressed.
     */
    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == InputConstants.KEY_M) { // FIXME: this won't change with the keybind
            this.onClose();
            return true;
        } else {
            return super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }
    }

    /**
     * Use this method to determine if the cursor is touching a defined rectangle.
     * Assume params are screen-relative unless specified.
     * @param pX The int defining the leftmost part of the area to check.
     * @param pY The int defining the topmost part of the area to check.
     * @param pWidth    The int defining the width of the rectangle. (absolute)
     * @param pHeight   The int defining the height of the rectangle. (absolute)
     * @param pMouseX   The double describing the mouse's x position.
     * @param pMouseY   The double describing the mouse's y position.
     * @return  <code>true</code> if the given mouse coordinates are inside the rectangle. <code>false</code> otherwise.
     */
    protected boolean isHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        pMouseX -= i;
        pMouseY -= j;
        return pMouseX >= (double)(pX - 1) && pMouseX < (double)(pX + pWidth + 1) && pMouseY >= (double)(pY - 1) && pMouseY < (double)(pY + pHeight + 1);
    }
}
