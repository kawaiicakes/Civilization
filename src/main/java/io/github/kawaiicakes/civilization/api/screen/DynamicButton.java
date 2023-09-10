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
    private final int textureWidth;
    private final int textureHeight;


    protected final Map<String, Integer> possibleStates;

    protected BlitRenderDefinition renderDefinition;
    public String activeState;

    @ParametersAreNonnullByDefault
    public DynamicButton(BlitRenderDefinition renderDefinition, ResourceLocation textureLocation,
                         Map<String, Integer> possibleStates, OnPress onPress) {
        this(renderDefinition, textureLocation, possibleStates, Component.empty(), onPress, NO_TOOLTIP, 256, 256);
    }

    @ParametersAreNonnullByDefault
    public DynamicButton(BlitRenderDefinition renderDefinition, ResourceLocation textureLocation,
                         Map<String, Integer> possibleStates, OnPress onPress, int textureWidth, int textureHeight) {
        this(renderDefinition, textureLocation, possibleStates, Component.empty(), onPress, NO_TOOLTIP, textureWidth, textureHeight);
    }

    @ParametersAreNonnullByDefault
    public DynamicButton(BlitRenderDefinition renderDefinition, ResourceLocation textureLocation,
                         Map<String, Integer> possibleStates, Component title, OnPress onPress, OnTooltip onTooltip, int textureWidth, int textureHeight) {
        super(renderDefinition.leftPos(), renderDefinition.topPos(), renderDefinition.blitUWidth(),
                renderDefinition.blitVHeight(), title, onPress, onTooltip);
        this.textureLocation = textureLocation;
        this.possibleStates = possibleStates;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public void setPosition(int leftPos, int topPos) {
        this.renderDefinition.renderAtNewPos(leftPos, topPos);
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.textureLocation);

        int i = renderDefinition.blitVOffset();
        if (!this.isActive()) { // TODO this clause should determine what to blit at based on active state
        } else if (this.isHoveredOrFocused()) {
        }

        RenderSystem.enableDepthTest();
        blit(pPoseStack, this.x, this.y, (float) this.renderDefinition.blitUOffset(), (float) i, this.width,
                this.height, this.textureWidth, this.textureHeight);

        if (this.isHovered) {
            this.renderToolTip(pPoseStack, pMouseX, pMouseY);
        }
    }
}
