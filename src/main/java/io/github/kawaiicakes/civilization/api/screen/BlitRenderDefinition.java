package io.github.kawaiicakes.civilization.api.screen;

import org.jetbrains.annotations.Nullable;

/**
 * Used for defining important coordinates and values for blit drawing.
 * Mainly a convenience record class for the above purposes.
 * automatically.
 * @param blitUOffset    the int in absolute terms representing the leftmost (x) u-coordinate to blit from.
 * @param blitVOffset    the int in absolute terms representing the topmost (y) v-coordinate to blit from.
 * @param blitUWidth     the int in absolute terms representing the number of pixels to blit along the (x) u-axis.
 * @param blitVHeight    the int in absolute terms representing the number of pixels to blit along the (y) v-axis.
 * @param textureWidth  the <code>Integer</code> representing the png width in pixels.
 * @param textureHeight the <code>Integer</code> representing the png height in pixels.
 */
public record BlitRenderDefinition(
        int blitUOffset,
        int blitVOffset,
        int blitUWidth,
        int blitVHeight,
        @Nullable Integer textureWidth,
        @Nullable Integer textureHeight
        ) {

    /**
     * This method is handy when rendering things that change appearance based on some action.
     * @param blitNewVOffset    the int representing the new blit start y-position.
     * @return  a new <code>BlitRenderDefinition</code> identical to the calling instance,
     * save for the blit start y-position.
     */
    public BlitRenderDefinition blitFromNewY(int blitNewVOffset) {
        return new BlitRenderDefinition(
                this.blitUOffset,
                blitNewVOffset,
                this.blitUWidth,
                this.blitVHeight,
                this.textureWidth,
                this.textureHeight
        );
    }
}
