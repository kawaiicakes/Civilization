package io.github.kawaiicakes.civilization.api.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

import java.util.HashSet;
import java.util.Set;

/**
 * Class concerned with translating Hexagonal Efficient Coordinate System coordinates to & from
 * Minecraft pos values. A hex tile represents an area of 12 chunks which is owned by the nation
 * that has claimed it.
 * <br><br>
 * As the shape formed by a 12-chunk hex has no discrete centre in terms of <code>ChunkPos</code>,
 * the bottom right chunk bordering the center is arbitrarily defined as the tile's local origin;
 * (0, 0). Accordingly, the global origin is the <code>ChunkPos</code> of the tile's local origin.
 */
public class HexTilePos {
    /**
     * Tile ZERO is the tile whose global origin is equal with its local; that is, tile (0, 0, 0).
     */
    public static final HexTilePos ZERO = new HexTilePos(true, 0, 0);

    private boolean arrayZero;
    private int row;
    private int col;

    public HexTilePos(boolean isArrayZero, int row, int col) {
        this.arrayZero = isArrayZero;
        this.row = row;
        this.col = col;
    }

    /**
     * Returns a set of <code>ChunkPos</code> using several helper methods & HECS math.
     * @return  the 12 element set of <code>ChunkPos</code> constituting this hex tile.
     */
    public Set<ChunkPos> toCorrespondingChunkPositions() {
        Set<ChunkPos> toReturn = new HashSet<>();
    }

    /**
     * Static utility method calculating the global origin of the hex in terms of a <code>ChunkPos</code>. We know that
     * the <code>ChunkPos</code> is always some multiple of 4 on the x-axis and 6 on the z-axis within the same array.
     * Given that tile ZERO is defined at (0, 0, 0) in terms of both global and local origin, the global origin in
     * terms of a <code>ChunkPos</code> of any tile in array zero may be described with the formula (4row, 6col).
     * <br><br>
     * Conversely, tile (1, 0, 0) has a global origin offset from tile (0, 0, 0); a <code>ChunkPos</code> (2, 3).
     * Accordingly, the global origin in terms of a <code>ChunkPos</code> of any tile in array one may be described
     * with the formula (4row + 2, 6col + 3).
     * @param hexTilePos    the <code>HexTilePos</code> you are calculating for
     * @return  the global origin of the hex tile in terms of a <code>ChunkPos</code>.
     */
    public static ChunkPos hexToChunkPos(HexTilePos hexTilePos) {
        return hexTilePos.arrayZero
                ? new ChunkPos(4 * hexTilePos.row, 6 * hexTilePos.col)
                : new ChunkPos((4 * hexTilePos.row) + 2, (6 * hexTilePos.col) + 3);
    }
}
