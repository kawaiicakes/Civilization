package io.github.kawaiicakes.civilization.capabilities.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.kawaiicakes.civilization.api.data.CivSerializable;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record CivNation(
        UUID id,
        String name
) implements CivSerializable<CompoundTag> {
    public static final Codec<CivNation> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExtraCodecs.UUID.fieldOf("id").forGetter(CivNation::id),
                    Codec.STRING.fieldOf("name").forGetter(CivNation::name)
            ).apply(instance, CivNation::new)
    );

    public static final CivNation NULLARIA = new CivNation(
            Util.NIL_UUID,
            "Nullaria"
    );

    @Override
    public CompoundTag serializeNBT() {
        return (CompoundTag) CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElse(new CompoundTag());
    }

    @Nullable
    public static CivNation deserializeNBT(CompoundTag tag) {
        return CODEC.parse(NbtOps.INSTANCE, tag).result().orElse(null);
    }
}
