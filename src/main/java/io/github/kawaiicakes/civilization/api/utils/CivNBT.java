package io.github.kawaiicakes.civilization.api.utils;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for writing classes to and from NBT data. Especially useful for easy capability shit
 * and networking. Methods are self-explanatory.
 */
public class CivNBT {
    /**
     *  Simple utility method to easily return a <code>ListTag</code> containing the elements of
     *  a <code>Collection</code>. Bear in mind that no precautions are taken to prevent duplicate
     *  entries. Use accordingly.
     * @param tCollection Any <code>Collection</code> of type <code>T</code>.
     * @return  a <code>ListTag</code> containing the nbt-serialized instances of <code>T</code>.
     * @param <T>   the type extending <code>NBTSerializable</code>. Any instance of <code>T</code>
     *              may call <code>#serializeNBT</code> to return a subclass of <code>Tag</code>.
     */
    public static <T extends NBTSerializable<?>> ListTag collectionToListTag(Collection<T> tCollection) {
        ListTag listTag = new ListTag();

        tCollection.forEach(t -> {
            if (t == null) {
                listTag.add(IntTag.valueOf(0));
            } else {
                listTag.add(t.serializeNBT());
            }
        });

        return listTag;
    }

    /**
     * Easily turn a <code>ListTag</code> into a <code>Set</code> of type <code>T</code>. Mainly exists for convenience
     * and formality.
     * @param listTag   the <code>ListTag</code> to convert. Ensure <code>pTagType</code> corresponds to the type
     *                  parameter of <code>NBTSerializable</code> in <code>T</code>.
     * @param mapper    the <code>Function</code> corresponding to the deserialization of <code>T</code>.
     * @return          a <code>Set</code> of type <code>T</code> constructed to the given mapper.
     * @param <T>       a class implementing <code>NBTSerializable</code>.
     */
    public static <T extends NBTSerializable<?>> Set<T> listTagToSet(ListTag listTag,
                                                                     Function<? super Tag, ? extends T> mapper) {
        return listTag.stream().map(mapper).collect(Collectors.toSet());
    }
}
