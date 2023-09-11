package io.github.kawaiicakes.civilization.api.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Similar to <code>ImageButton</code>, but allows for more flexible rendering. Also works with
 * <code>BlitRenderDefinition</code>s.
 */
public class DynamicButton extends Button {
    private final ResourceLocation textureLocation;
    private final int textureWidth;
    private final int textureHeight;

    protected BlitRenderDefinition renderDefinition;
    /**
     * This field describes the active "state" of the button. The state may be tied to the value of
     * <code>$blitVOffset</code> in this object's <code>$renderDefinition</code>. This will in turn
     * change the appearance of the rendered object.
     */
    private String state;

    @ParametersAreNonnullByDefault
    public DynamicButton(BlitRenderDefinition renderDefinition, ResourceLocation textureLocation,
                         OnPress onPress) {
        this(renderDefinition, textureLocation, Component.empty(), onPress, NO_TOOLTIP, 256, 256);
    }

    @ParametersAreNonnullByDefault
    public DynamicButton(BlitRenderDefinition renderDefinition, ResourceLocation textureLocation,
                         OnPress onPress, int textureWidth, int textureHeight) {
        this(renderDefinition, textureLocation, Component.empty(), onPress, NO_TOOLTIP, textureWidth, textureHeight);
    }

    @ParametersAreNonnullByDefault
    public DynamicButton(BlitRenderDefinition renderDefinition, ResourceLocation textureLocation,
                         Component title, OnPress onPress, OnTooltip onTooltip, int textureWidth, int textureHeight) {
        super(renderDefinition.leftPos(), renderDefinition.topPos(), renderDefinition.blitUWidth(),
                renderDefinition.blitVHeight(), title, onPress, onTooltip);
        this.textureLocation = textureLocation;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public void setPosition(int leftPos, int topPos) {
        this.renderDefinition.renderAtNewPos(leftPos, topPos);
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public void reRender(int newBlitVOffset) {
        this.renderDefinition = this.renderDefinition.blitFromNewY(newBlitVOffset);
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.textureLocation);

        RenderSystem.enableDepthTest();
        blit(pPoseStack, this.x, this.y, (float) this.renderDefinition.blitUOffset(), (float) renderDefinition.blitVOffset(), this.width,
                this.height, this.textureWidth, this.textureHeight);

        if (this.isHovered) {
            this.renderToolTip(pPoseStack, pMouseX, pMouseY);
        }
    }
}
