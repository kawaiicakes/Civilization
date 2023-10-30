package io.github.kawaiicakes.civilization.api.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementations of this are intended to be used as the caps providers for implementations of <code>CivCapability</code>.
 * A lot of this is likely going to be boilerplate in any implementations you see.
 * See {@link io.github.kawaiicakes.civilization.capabilities.CivLevelCapability.Provider } for an example.
 * @param <T> the <code>CivCapability</code> subclass for which this provides.
 * @param <U> the type extending <code>Tag</code> in the type parameter for <code>T</code>.
 */
public abstract class CivCapabilityProvider<T extends CivCapability<U>, U extends Tag> implements ICapabilitySerializable<U> {
    protected T capability = null;
    protected final LazyOptional<T> lazyHandler = LazyOptional.of(this::create);

    /**
     * <code>$capability</code> is to be referenced here. If null, then instantiate <code>T</code> and return.
     * Otherwise, return this field.
     */
    protected abstract T create();

    /**
     * This should return a static field in the implementing class containing the <code>CapabilityToken</code>
     * associated with it.
     * @return the <code>Capability</code> of type <code>T</code>.
     */
    public abstract Capability<T> getCap();

    @Override
    public @NotNull <V> LazyOptional<V> getCapability(@NotNull Capability<V> cap, @Nullable Direction side) {
        if (cap == getCap()) {
            return lazyHandler.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public void deserializeNBT(U nbt) {
        this.create().readNBT(nbt);
    }

    /*
    An example of how this works. This is commented here in case shit changes and I need to look at the ez default
    implementation of these methods

    @Override
    public U serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        // Write data from the capability into the tag here
        this.create().writeNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(U nbt) {
        // Read and deserialize data here
        this.create().readNBT(nbt);
    }
     */
}
