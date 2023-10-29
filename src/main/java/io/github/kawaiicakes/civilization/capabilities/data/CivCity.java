package io.github.kawaiicakes.civilization.capabilities.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.kawaiicakes.civilization.api.data.CivSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.ExtraCodecs;

import java.util.UUID;

public record CivCity(
    UUID id,
    String name
) implements CivSerializable<CompoundTag> {
    public static final Codec<CivCity> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExtraCodecs.UUID.fieldOf("id").forGetter(CivCity::id),
                    Codec.STRING.fieldOf("name").forGetter(CivCity::name)
            ).apply(instance, CivCity::new)
    );

    @Override
    public CompoundTag serializeNBT() {
        return (CompoundTag) CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElse(new CompoundTag());
    }

    public static CivCity deserializeNBT(CompoundTag nbt) {
        return new CivCity(nbt.getUUID("id"), nbt.getString("name"));
    }
}
