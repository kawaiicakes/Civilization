package io.github.kawaiicakes.civilization.api.nbt;

import com.mojang.logging.annotations.FieldsAreNonnullByDefault;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;

/**
 * Implementing classes are essentially codecs for some NBT data specifically made for this mod. Implementing classes
 * are expected to contain codecs for <code>ListTag</code>s. Deserialization is to be handled by the constructor.
 * As many NBT data structures are expected to be nested by this mod, this abstraction makes life much easier.
 */
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class CivSerializable<T extends Tag> implements NBTSerializable<T> {
    /**
     *  Implementing methods serialize a <code>ListTag</code> containing the elements of
     *  a <code>Collection</code>. Bear in mind that no precautions are taken to prevent duplicate
     *  entries. Use accordingly.
     * @param uCollection Any <code>Collection</code> of type <code>U</code>.
     * @return  a <code>ListTag</code> containing the nbt-serialized instances of <code>U</code>.
     * @param <U>   the type extending <code>NBTSerializable</code>. Any instance of <code>U</code>
     *              may call <code>#serializeNBT</code> to return a subclass of <code>Tag</code>.
     */
    public <U extends NBTSerializable<?>> ListTag collectionToListTag(Collection<U> uCollection) {
        ListTag listTag = new ListTag();

        uCollection.forEach(u -> {
            if (u != null) listTag.add(u.serializeNBT());
        });

        return listTag;
    }

    /**
     * Implementations deserialize a <code>ListTag</code> into a <code>Set</code> of type <code>T</code>.
     * @param listTag   the <code>ListTag</code> to convert. Ensure <code>pTagType</code> corresponds to the type
     *                  parameter of <code>NBTSerializable</code> in <code>U</code>.
     * @return          a <code>List</code> of type <code>U</code>.
     * @param <U>       a class implementing <code>NBTSerializable</code>.
     */
    public abstract List<? extends NBTSerializable<?>> listTagToList(ListTag listTag);

    @Override
    public abstract boolean equals(Object obj);
}
