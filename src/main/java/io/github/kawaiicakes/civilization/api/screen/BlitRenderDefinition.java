package io.github.kawaiicakes.civilization.api.screen;

import org.jetbrains.annotations.Nullable;

/**
 * Used for defining important coordinates and values for blit drawing.
 * Mainly a convenience record class for the above purposes.
 * automatically.
 * @param leftPos       the <code>Integer</code> in screen relative terms of the leftmost screen coordinate to render to.
 * @param topPos        the <code>Integer</code> in screen relative terms of the topmost screen coordinate to render to.
 * @param blitUOffset    the int in absolute terms representing the leftmost (x) u-coordinate to blit from.
 * @param blitVOffset    the int in absolute terms representing the topmost (y) v-coordinate to blit from.
 * @param blitUWidth     the int in absolute terms representing the number of pixels to blit along the (x) u-axis.
 * @param blitVHeight    the int in absolute terms representing the number of pixels to blit along the (y) v-axis.
 */
public record BlitRenderDefinition(
        @Nullable Integer leftPos,
        @Nullable Integer topPos,
        int blitUOffset,
        int blitVOffset,
        int blitUWidth,
        int blitVHeight
        ) {

    /**
     * This method is handy when rendering things that change appearance based on some action.
     * @param blitNewVOffset    the int representing the new blit start y-position.
     * @return  a new <code>BlitRenderDefinition</code> identical to the calling instance,
     * save for the blit start y-position.
     */
    public BlitRenderDefinition blitFromNewY(int blitNewVOffset) {
        return new BlitRenderDefinition(
                this.leftPos,
                this.topPos,
                this.blitUOffset,
                blitNewVOffset,
                this.blitUWidth,
                this.blitVHeight
        );
    }

    /**
     * This method is handy when you need to change where a blit should be rendered.
     * @param leftPos   the int representing the new leftmost position to render to in screen relative terms.
     * @param topPos    the int representing the new topmost position to render to in screen relative terms.
     * @return          an identical instance of this, but with a new leftPos and topPos.
     */
    public BlitRenderDefinition renderAtNewPos(int leftPos, int topPos) {
        return new BlitRenderDefinition(
                leftPos,
                topPos,
                this.blitUOffset,
                this.blitVOffset,
                this.blitUWidth,
                this.blitVHeight
        );
    }
}
