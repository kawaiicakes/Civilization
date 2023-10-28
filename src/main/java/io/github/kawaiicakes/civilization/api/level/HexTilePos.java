package io.github.kawaiicakes.civilization.api.level;

import io.github.kawaiicakes.civilization.api.data.CivSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

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
public class HexTilePos implements CivSerializable<IntArrayTag> {
    /**
     * Tile ZERO is the tile whose global origin is equal with its local; that is, tile (0, 0, 0). If you are unfamiliar
     * with HECS, it is recommended you read up on it so this class and tile notation makes sense.
     */
    public static final HexTilePos ZERO = new HexTilePos(true, 0, 0);
    private final boolean arrayZero;
    private final int row;
    private final int col;

    public HexTilePos(boolean isArrayZero, int row, int col) {
        this.arrayZero = isArrayZero;
        this.row = row;
        this.col = col;
    }

    public HexTilePos(int[] arr) {
        if (arr.length != 3) {
            throw new IllegalArgumentException("Argument length must be 3!");
        } else if (arr[0] > 2 || arr[0] < 0) {
            throw new IllegalArgumentException("HECS array may only be 0 or 1!");
        } else {
            this.arrayZero = arr[0] == 0;
            this.row = arr[1];
            this.col = arr[2];
        }
    }

    public HexTilePos(IntArrayTag tag) {
        this(tag.getAsIntArray());
    }

    public boolean isArrayZero() {
        return this.arrayZero;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public String getPrettyCoordinates() {
        byte val = (byte) (this.arrayZero ? 0 : 1);
        return "(" + val + ", " + this.row + ", " + this.col + ")";
    }

    public int[] asIntArray() {
        return new int[]{this.arrayZero ? 1 : 0, this.row, this.col};
    }

    /**
     * Returns a set of <code>ChunkPos</code> using several helper methods & HECS math.
     * This set is NOT ordered; nor does it make guarantees as to maintaining an order over time.
     * @return  the 12 element set of <code>ChunkPos</code> constituting this hex tile.
     */
    public Set<ChunkPos> toChunkPos() {
        Set<ChunkPos> toReturn = new HashSet<>();

        ChunkPos localOrigin = hexToGlobalOriginChunkPos(this);

        for (int colNum = -2; colNum < 2; colNum++) {
            for (int rowNum = 0; rowNum < 2; rowNum++) {
                toReturn.add(new ChunkPos(localOrigin.x + colNum, localOrigin.z - rowNum));
            }
        }

        toReturn.add(new ChunkPos(localOrigin.x, localOrigin.z - 2));
        toReturn.add(new ChunkPos(localOrigin.x - 1, localOrigin.z - 2));
        toReturn.add(new ChunkPos(localOrigin.x, localOrigin.z + 1));
        toReturn.add(new ChunkPos(localOrigin.x - 1, localOrigin.z + 1));

        return toReturn;
    }

    /**
     * Returns the <code>ChunkPos</code> of the global origin of this tile.
     * @return  the <code>ChunkPos</code> of the global origin of this tile.
     */
    public ChunkPos toOriginChunkPos() {
        return hexToGlobalOriginChunkPos(this);
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
        ChunkPos chunk = new ChunkPos(blockPos); // Is this call unnecessarily expensive? Should I just perform the bitwise shift here?
        return HexTilePos.chunkToHexPos(chunk);
    }

    /**
     * This method gets the <code>HexTilePos</code> containing the given <code>ChunkPos</code>.
     * It relies on calculating patterns corresponding to geometric properties of a hex grid in Minecraft.
     * Bitwise operations are used frequently as they are both performant and perfect for this job.
     * @param chunkPos the <code>ChunkPos</code> inside of the tile to be given.
     * @return the <code>HexTilePos</code> at the given <code>ChunkPos</code>.
     */
    public static HexTilePos chunkToHexPos(ChunkPos chunkPos) {
        // true only if at this z the tile is in array zero regardless of x.
        if (zInArrayZero(chunkPos.z)) {
            return new HexTilePos(true, zIndependentToTileRow(true, chunkPos.z), xIndependentToTileColumn(true, chunkPos.x));
        } else if (zInArrayOne(chunkPos.z)) {
            return new HexTilePos(false, zIndependentToTileRow(false, chunkPos.z), xIndependentToTileColumn(false, chunkPos.x));
        } else {
            // testAgainstPattern2Skip2 is only meaningful in this context; when it is uncertain if regardless of x, the z
            // implies that we are in either array zero or one with certainty.
            return new HexTilePos(
                    testAgainstPattern2Skip2(chunkPos.x),
                    zDependentToTileRow(testAgainstPattern2Skip2(chunkPos.x), chunkPos.z),
                    xDependentToTileColumn(testAgainstPattern2Skip2(chunkPos.x), chunkPos.x)
            );
        }
    }

    /*
        HELPER METHODS BELOW -
            Magic numbers are derived from the global origins of tiles (0, 0, 0) and (1, 0, 0).
     */

    /**
     * Returns the row number given an x-coordinate of a <code>ChunkPos</code>. The result is meaningless unless
     * the z-coordinate cannot be tested with certainty as being in either array zero or one regardless of x.
     * @param arrayZero coord in array zero?
     * @param xCoord the x coordinate corresponding to a <code>ChunkPos</code>.
     * @return  The int corresponding to the tile row.
     */
    private static int xDependentToTileColumn(boolean arrayZero, int xCoord) {
        return arrayZero ? (xCoord + 1) >> 2 : (xCoord - 1) >> 2;
    }

    /**
     * Returns the row number corresponding to the x-coordinate of a <code>ChunkPos</code>
     * if and only if at any given z-coordinate, one is certain of being in array one or zero regardless of x.
     * @param arrayZero the array of the tile is zero?
     * @param xCoord    int representing the x-coordinate of a <code>ChunkPos</code>.
     * @return  the tile row as int.
     */
    private static int xIndependentToTileColumn(boolean arrayZero, int xCoord) {
        return arrayZero ? (xCoord + 2) >> 2 : xCoord >> 2;
    }

    /**
     * Returns the row number corresponding to the z-coordinate of a <code>ChunkPos</code> if and only if
     * the tile array is uncertain for any given x at this z.
     * <br><br>
     * Again written by Erik of the Create: Aeronautics team.
     * @param arrayZero is the array zero at this x and z?
     * @param zCoord the z-coord as an int.
     * @return the int corresponding to the tile row.
     */
    private static int zDependentToTileRow(boolean arrayZero, int zCoord) {
        if (arrayZero) {
            int a = (((zCoord + 3) & (0x80000000)) >> 31);
            return ((zCoord + 3) - a) / 6 + a;
        } else {
            int a = ((zCoord & (0x80000000)) >> 31);
            return (zCoord - a) / 6 + a;
        }
    }

    /**
     * Returns the row number given a z-coordinate of a <code>ChunkPos</code> if and only if
     * the tile array is certain for any given x at this z. The sum of zCoord and 1 should
     * never be a number that falls outside the pattern 2Skip4.
     * @param arrayZero coord in array zero?
     * @param zCoord the z coordinate corresponding to a <code>ChunkPos</code>.
     * @return  The int corresponding to the tile row.
     */
    private static int zIndependentToTileRow(boolean arrayZero, int zCoord) {
        return arrayZero ? (((zCoord + 1) >> 1) / 3) : (((zCoord - 2) >> 1) / 3);
    }

    /**
     * This method, for any integer zCoord who represents the z-value of a <code>ChunkPos</code>, deduces whether zCoord in the
     * tile (a, zCoord, c) is in array zero or array one regardless of the value of c. An elegant solution graciously
     * written by Eriksonn of the Create: Aeronautics team.
     * <br><br>
     * A positive result is a certainty, but a negative return does not necessarily mean the tile is not of array zero.
     * It is merely indicative that more rigorous testing must be performed.
     * @param zCoord any integer representing the z-value of a <code>ChunkPos</code>.
     * @return  <code>true</code> if a = 0, <code>false</code> if a = 1 in tile (a, zCoord, c)
     */
    private static boolean zInArrayZero(int zCoord) {
        return testAgainstPattern2Skip4(zCoord);
    }

    /**
     * This method is identical to <code>#zInArrayZero</code> including in its caveats; although is shifted left by 3.
     * @param zCoord any integer representing the z-value of a <code>ChunkPos</code>.
     * @return  <code>true</code> if a = 1, <code>false</code> if a = 0 in tile (a, zCoord, c)
     */
    private static boolean zInArrayOne(int zCoord) {
        return zInArrayZero(zCoord - 3);
    }

    /**
     * In regard to binary numbers, 010 must appear twice every two base-10 numbers, we can leverage this fact to
     * create a pattern: -5, -4, -1, 0, 3, 4... This helper method tells us if the argument is in this pattern.
     * @param n an integer.
     * @return  <code>true</code> if integer n lies in pattern - <code>false</code> otherwise.
     */
    private static boolean testAgainstPattern2Skip2(int n) {
        return (((n + 1) >> 1) & 1) != 1;
    }

    /**
     * Helper method using bitwise operations to generate a simple pattern; then tests a passed
     * integer against it. The pattern appears as follows:
     * <br><br>
     * -7, -6, -1, 0, 5, 6...
     * @param n an integer.
     * @return  <code>true</code> if n lands in a "2" cluster, <code>false</code> otherwise.
     */
    private static boolean testAgainstPattern2Skip4(int n) {
        return (((n + 1) >> 1) % 3) == 0;
    }

    @Override
    public @NotNull IntArrayTag serializeNBT() {
        return new IntArrayTag(this.asIntArray());
    }

    /*  8-BIT BINARY REFERENCE SHEET
        0  -> 00000000 > 00000000 > 00000000
        1  -> 00000001 > 00000000 > 00000000
        2  -> 00000010 > 00000001 > 00000000
        3  -> 00000011 > 00000001 > 00000000
        4  -> 00000100 > 00000010 > 00000001
        5  -> 00000101 > 00000010 > 00000001
        6  -> 00000110 > 00000011 > 00000001
        7  -> 00000111 > 00000011 > 00000001
        8  -> 00001000 > 00000100 > 00000010
        9  -> 00001001 > 00000101 > 00000010
        10 -> 00001010 > 00000110 > 00000010
        11 -> 00001011 > 00000111 > 00000010
        12 -> 00001100 > 00001000 > 00000011
        13 -> 00001101 >
        14 -> 00001110 >
        15 -> 00001111 >
        16 -> 00010000 >
     */
}
