package io.github.kawaiicakes.civilization.api.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

/**
 * Similar to <code>ImageButton</code>, but allows for more flexible rendering. Also works with
 * <code>BlitRenderDefinition</code>s.
 */
public class DynamicButton extends Button {
    private final ResourceLocation textureLocation;
    protected final Map<String, Integer> possibleStates;

    protected BlitRenderDefinition renderDefinition;
    public String activeState;

    @ParametersAreNonnullByDefault
    public DynamicButton(BlitRenderDefinition renderDefinition, ResourceLocation textureLocation,
                         Map<String, Integer> possibleStates, OnPress onPress) {
        this(renderDefinition, textureLocation, possibleStates, Component.empty(), onPress, NO_TOOLTIP);
    }

    @ParametersAreNonnullByDefault
    public DynamicButton(BlitRenderDefinition renderDefinition, ResourceLocation textureLocation,
                         Map<String, Integer> possibleStates, Component title, OnPress onPress, OnTooltip onTooltip) {
        super(renderDefinition.leftPos(), renderDefinition.topPos(), renderDefinition.blitUWidth(),
                renderDefinition.blitVHeight(), title, onPress, onTooltip);
        this.textureLocation = textureLocation;
        this.possibleStates = possibleStates;
    }

    public void setPosition(int leftPos, int topPos) {
        this.renderDefinition.renderAtNewPos(leftPos, topPos);
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.textureLocation);

        int i = this.yTexStart;
        if (!this.isActive()) {
            i += this.yDiffTex * 2;
        } else if (this.isHoveredOrFocused()) {
            i += this.yDiffTex;
        }

        RenderSystem.enableDepthTest();
        blit(pPoseStack, this.x, this.y, (float)this.xTexStart, (float)i, this.width, this.height, this.textureWidth, this.textureHeight);

        if (this.isHovered) {
            this.renderToolTip(pPoseStack, pMouseX, pMouseY);
        }
    }
}
