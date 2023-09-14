package io.github.kawaiicakes.civilization.network.packets;

import io.github.kawaiicakes.civilization.api.network.SimplePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Simple test implementation of <code>SimplePacket</code>. Does not convey any data other than itself.
 */
public class C2STestPacket extends SimplePacket {
    public C2STestPacket() {
        super();
    }

    public C2STestPacket(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {

    }

    @Override
    public void onReceipt(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        assert player != null;
        Objects.requireNonNull(context.getSender()).getLevel().explode(
                player,
                DamageSource.OUT_OF_WORLD,
                null,
                player.blockPosition().getX(),
                player.blockPosition().getY(),
                player.blockPosition().getZ(),
                50,
                true,
                Explosion.BlockInteraction.BREAK
        );
    }
}
