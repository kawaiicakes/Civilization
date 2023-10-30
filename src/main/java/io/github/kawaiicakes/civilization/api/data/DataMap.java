package io.github.kawaiicakes.civilization.api.data;

import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper intended to serialize and deserialize <code>Map</code>s for caching in <code>CivCapability</code>
 * instances. Its purpose is to provide the capability a performant, rapid way to look up information from
 * <code>$map</code> while also providing read/write functions to the capability.
 * <br><br>
 * Null values are forbidden from being placed into the map as attempting to serialize a null value to NBT will
 * cause NullPointerExceptions and typically a server crash.
 * @param <K> An object that is in some way tied to <code>V</code>. The chosen object should permit rapid lookup
 *           of the corresponding value.
 * @param <V> An object who must be quickly referenced.
 */
// TODO: look into Int2ObjectArrayMaps, maybe even a custom implementation of Map
public abstract class DataMap<K, V> implements INBTSerializable<ListTag> {
    public static final String ID_KEY = "id";

    protected final Map<K, V> map = new HashMap<>();

    /**
     * @return the int referencing the <code>Tag</code> type of the elements of the <code>ListTag</code>.
     */
    public abstract int tagType();
    @Nullable
    public abstract V get(K key);
    @Nullable
    public abstract V put(K key, V value);
    @NotNull
    public Collection<V> values() {
        return this.map.values();
    }
}
