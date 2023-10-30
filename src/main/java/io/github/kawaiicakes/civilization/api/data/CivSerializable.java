package io.github.kawaiicakes.civilization.api.data;

import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

/**
 * Subclasses are expected to be serialized/deserialized frequently due to their usage in this mod.
 * Specifically, implementing classes encode the data they convey to a format extending <code>Tag</code>.
 * In particular, this class abstracts away the shared trait that serializable data in this mod has; a
 * name and UUID identifier.
 * <br><br>
 * Implementations are intended to be labelled data conveyors and thus changing its ID should not be a trivial thing.
 * That being said, <code>$id</code> is the only field that is final. This is to help ensure that at runtime, the UUID
 * is not inadvertently changed. This is especially important with respect to security of data during both serialization
 * and deserialization.
 * <br><br>
 * As an aside, I would recommend delegating <code>#deserializeNBT</code> to a static method or few.
 */

/* --- The below are old notes; fall back on these if my current plans turn out to suck major ass ---
 * As making the fields final would necessitate making things work ad hoc - namely by way of coercing implicit
 * usage of static deserialization methods - I've opted to make the ID field protected instead and only write a getter.
 */
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class CivSerializable<T extends Tag> implements INBTSerializable<T> {
    public static final String ID_NBT_KEY = "id";
    public static final String NAME_NBT_KEY = "name";

    private final UUID id;
    public String name = "";

    /**
     * This constructor is intended primarily for usage by the extending class or its builder. Intended for
     * instantiation of an object for caching or serialization. The implementing class should make its constructor
     * public.
     */
    protected CivSerializable(UUID id) {
        this.id = id;
    }

    /**
     * This is constructor is essentially the deserializer. Implementing classes should make their constructor
     * public. Include checks to ensure that the data it queries from the passed tag actually exists and to throw
     * an error otherwise.
     * @param tag the <code>Tag</code> subclass to deserialize and instantiate this class with.
     */
    protected CivSerializable(UUID id, T tag) {
        this.id = id;
        this.deserializeNBT(tag);
    }

    /**
     * This method is internally called by the constructor who doubles as the deserializer. It should ideally
     * include checks to ensure that the data it queries from the passed tag actually exists and to throw an
     * error otherwise.
     * <br><br>
     * It may be called on the same object repeatedly to maintain an instance of some object who has some unchanging
     * field. This may confer a negligible performance benefit but could also contribute to design reinforcing the
     * idea that this is a labelled data conveyor.
     */
    @Override
    public abstract void deserializeNBT(T tag);

    /**
     * There is, at the time of writing, no conceivable reason why this should return anything other than the
     * UUID associated with this object. I'm going to declare this as final to prevent overriding it and
     * potentially weakening the integrity of the design.
     */
    public final UUID id() {
        return this.id;
    }
}
