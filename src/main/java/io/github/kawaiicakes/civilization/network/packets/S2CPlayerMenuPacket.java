package io.github.kawaiicakes.civilization.network.packets;

import io.github.kawaiicakes.civilization.api.network.SimplePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * Sends up-to-date info from the level to the client when queried. Done from the server end so a client may not
 * query arbitrary things (like the nation info of another player).
 */
public class S2CPlayerMenuPacket extends SimplePacket {
    public S2CPlayerMenuPacket() {
        super();
    }

    public S2CPlayerMenuPacket(FriendlyByteBuf buf) {

    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {

    }

    @Override
    public void onReceipt(NetworkEvent.Context context) {

    }
}
