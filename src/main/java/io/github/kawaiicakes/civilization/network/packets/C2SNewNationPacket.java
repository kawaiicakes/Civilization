package io.github.kawaiicakes.civilization.network.packets;

import io.github.kawaiicakes.civilization.api.nations.LevelNation;
import io.github.kawaiicakes.civilization.api.nations.NationManager;
import io.github.kawaiicakes.civilization.api.network.SimplePacket;
import io.github.kawaiicakes.civilization.api.utils.CivNBT;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;

public class C2SNewNationPacket extends SimplePacket {
    private final LevelNation levelNation;

    /**
     * Sends an instance of this with the given <code>LevelNation</code> to the server
     * when passed as the argument in <code>SimpleChannel</code>.
     * <br><br>
     * For security reasons, the validity of the levelNation to create should only
     * be checked on the server.
     * @param levelNation    the <code>LevelNation</code> to send to the server.
     */
    public C2SNewNationPacket(LevelNation levelNation) {
        super();
        this.levelNation = levelNation;
    }

    public C2SNewNationPacket(FriendlyByteBuf buf) {
        super(buf);
        this.levelNation = CivNBT.fromNBT(Objects.requireNonNull(buf.readNbt()));
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(CivNBT.fromNation(this.levelNation));
    }

    @Override
    public void onReceipt(NetworkEvent.Context context) {
        ServerLevel level = Objects.requireNonNull(context.getSender()).getLevel();

        if (NationManager.createNation(level, this.levelNation)) {
            context.getSender().sendSystemMessage(Component.literal("Successfully founded levelNation " + this.levelNation.nationName()
                    + " with UUID " + this.levelNation.nationUUID()));
        } else {
            context.getSender().sendSystemMessage(Component.literal("Invalid levelNation!"));
        }
    }
}