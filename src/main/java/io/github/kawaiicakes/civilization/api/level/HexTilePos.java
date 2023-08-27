package io.github.kawaiicakes.civilization.api.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

import java.util.HashSet;
import java.util.Set;

/**
 * Class concerned with translating Hexagonal Efficient Coordinate System (HECS) coordinates to & from
 * Minecraft pos values. A hex tile represents an area of 12 chunks which is owned by the nation
 * that has claimed it.
 * <br><br>
 * As the shape formed by a 12-chunk hex has no discrete centre in terms of <code>ChunkPos</code>,
 * the bottom right chunk bordering the center is arbitrarily defined as the tile's local origin;
 * (0, 0). Accordingly, the global origin is the <code>ChunkPos</code> of the tile's local origin.
 */
public class HexTilePos {
    /**
     * Tile ZERO is the tile whose global origin is equal with its local; that is, tile (0, 0, 0). If you are unfamiliar
     * with HECS, it is recommended you read up on it so this class makes sense and tile notation makes sense.
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
    public Set<ChunkPos> toChunkPositions() {
        Set<ChunkPos> toReturn = new HashSet<>();

        toReturn.add(hexToGlobalOriginChunkPos(this));
        // TODO: continue

        return toReturn;
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
    public static ChunkPos hexToGlobalOriginChunkPos(HexTilePos hexTilePos) {
        return hexTilePos.arrayZero
                ? new ChunkPos(4 * hexTilePos.row, 6 * hexTilePos.col)
                : new ChunkPos((4 * hexTilePos.row) + 2, (6 * hexTilePos.col) + 3);
    }

    /**
     * Use this static method to determine what <code>BlockPos</code> belongs to which tile.
     * @param blockPos the <code>BlockPos</code> inside of the returned <code>HexTilePos</code>
     * @return  the <code>HexTilePos</code> containing the <code>BlockPos</code> instance.
     */
    public static HexTilePos blockToHexPos(BlockPos blockPos) {
        ChunkPos chunk = new ChunkPos(blockPos.getX(), blockPos.getZ());
        return HexTilePos.chunkToHexPos(chunk);
    }

    /**
     *
     * @param chunkPos
     * @return
     */
    public static HexTilePos chunkToHexPos(ChunkPos chunkPos) { // TODO: local variables for bitwise operations?
        // true only if at this z the tile is in array zero regardless of x.
        if (zInArrayZero(chunkPos.z)) {
            return new HexTilePos(true, (chunkPos.x + 2) >> 2, (((chunkPos.z + 2) >> 1) / 3));
        } else if (zInArrayOne(chunkPos.z)) {
            return new HexTilePos(false, chunkPos.x >> 2, (((chunkPos.z - 1) >> 1) / 3));
        } else {
            return xInArrayZero(chunkPos.x) ? new HexTilePos(true, (chunkPos.x + 2) >> 2, (((chunkPos.z + 2) >> 1) / 3))
                    : new HexTilePos(false, chunkPos.x >> 2, (((chunkPos.z - 1) >> 1) / 3));
        }
    }

    /**
     * As 010 must appear twice every two base-10 numbers, we can leverage this fact to
     * create a pattern, then test whether a <code>ChunkPos</code> is in array one or zero.
     * <br><br>
     * This method is only meaningful if it is uncertain whether the tile is in array zero or one
     * at a given z-coordinate using existing methods.
     * @param xCoord an int representing the x-coordinate of a <code>ChunkPos</code>.
     * @return  <code>true</code> if in array zero, <code>false</code> if in array one.
     */
    private static boolean xInArrayZero(int xCoord) {
        return ((xCoord + 1) & 2) != 2;
    }

    /**
     * This method, for any integer zCoord who represents the z-value of a <code>ChunkPos</code>, deduces whether zCoord in the
     * tile (a, zCoord, c) is in array zero or array one regardless of the value of c. An elegant solution graciously
     * written by Eriksonn of the Create: Aeronautics team.
     * <br><br>
     * A negative return does not necessarily mean the tile is not of array zero. Rather, more rigorous testing must
     * be performed.
     * @param zCoord any integer representing the z-value of a <code>ChunkPos</code>.
     * @return  <code>true</code> if a = 0, <code>false</code> if a = 1 in tile (a, zCoord, c)
     */
    private static boolean zInArrayZero(int zCoord) {
        return ((zCoord >> 1) % 3) == 0;
    }

    /**
     * This method is identical to <code>#zInArrayZero</code> including in its caveats; although is x-shifted.
     *
     * @param zCoord any integer representing the z-value of a <code>ChunkPos</code>.
     * @return  <code>true</code> if a = 1, <code>false</code> if a = 0 in tile (a, zCoord, c)
     */
    private static boolean zInArrayOne(int zCoord) {
        return (((zCoord - 3) >> 1) % 3) == 0;
    }
    /*
        0  -> 00000 >> 2 = 00000 < 00000
        1  -> 00001 >> 2 = 00000 < 00000
        2  -> 00010 >> 2 = 00000 < 00001
        3  -> 00011 >> 2 = 00000 < 00001
        4  -> 00100 >> 2 = 00001 < 00010
        5  -> 00101 >> 2 = 00001 < 00010
        6  -> 00110 >> 2 = 00001 < 00011
        7  -> 00111 >> 2 = 00001 < 00011
        8  -> 01000 >> 2 = 00010 < 00100
     */
}
