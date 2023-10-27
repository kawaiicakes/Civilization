package io.github.kawaiicakes.civilization.api.data;

import net.minecraft.nbt.Tag;

/**
 * Implementing classes are data handlers for capability data.
 */
public interface CivCapability<T extends Tag> {
    /**
     * Writes the data from the implementing class to the caller.
     * @param tag the type of <code>T</code> passed by the caller for modification.
     */
    void writeNBT(T tag);

    /**
     * Intended to read the data from the passed tag into the implementing object.
     * @param tag the type of <code>T</code> passed by the caller to be deserialized into this.
     */
    void readNBT(T tag);
}
