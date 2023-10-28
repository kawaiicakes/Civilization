package io.github.kawaiicakes.civilization.api.data;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.Tag;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Implementing classes are expected to be serialized/deserialized frequently due to their usage in this mod.
 * Specifically, implementing classes encode the data they convey to a format extending <code>Tag</code>.
 * Deserialization is to be handled by either the implementing class' constructor or some static method. This is
 * because Records; which this abstraction was primarily designed for, are shallowly immutable and thus deserialization
 * from a non-static context becomes much harder to perform. There's probably a way around this but l m a o.
 * <br><br>
 * To future Ashley and to any modders: either in the implementing class, or another class, cache static codecs for any
 * class implementing this interface. Then reference the static values accordingly in the implementing methods. I do
 * this because I'm not sure if keeping instance methods or fields explicitly containing the <code>Codec</code> would
 * noticeably impact performance. This is therefore just a precaution, if a misguided one...
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface CivSerializable<T extends Tag> {
    /**
     * Serializes this object into an instance of the type parameter. As mentioned earlier, it is advised that
     * implementations reference a static method/field.
     * @return this serialized into an instance of the type parameter.
     */
    T serializeNBT();
}
