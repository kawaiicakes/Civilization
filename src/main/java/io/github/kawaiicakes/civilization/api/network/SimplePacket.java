package io.github.kawaiicakes.civilization.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Extending classes are intended to be sent to either client or server via
 * <code>SimpleChannel#send</code> and <code>SimpleChannel#sendToServer</code>
 * respectively. Extending classes must be registered with <code>SimpleChannel
 * #messageBuilder</code> during Forge common setup.
 * <br><br>
 * What the packet communicates and how it does so is dependent on implementation
 * of this class.
 */
public abstract class SimplePacket {
    /**
     * The constructor functions as the decoder; it is called when the recipient receives the
     * <code>FriendlyByteBuf</code> from the network. It translates the <code>FriendlyByteBuf</code>
     * to a new instance of this class; where it is then consumed on the main thread of the receiving
     * side.
     * @param buf   the <code>FriendlyByteBuf</code> received over the <code>SimpleChannel</code>
     *              instance.
     */
    public SimplePacket(FriendlyByteBuf buf) {

    }

    /**
     * Used when registering this packet in <code>#messageBuilder</code>. Override
     * this and return the side this packet is intended to be sent to.
     * @return  the <code>NetworkDirection</code> indicating to which side this
     *          packet will be sent.
     */
    public abstract NetworkDirection getRecipient();

    /**
     * This is the encoder and is called as soon as the packet is sent; it runs on the main thread.
     * Do not try to access sided stuff from here. Its job is to take some data, write it
     * in to the <code>FriendlyByteBuf</code>, then pass it over the network.
     * @param buf  the <code>FriendlyByteBuf</code> to which the data is to be written.
     */
    public abstract void toBytes(FriendlyByteBuf buf);

    /**
     * The code inside this method will be executed on the side it is received. Use
     * <code>context</code> to access stuff like the level on the executing side.
     */
    public abstract void onReceipt(NetworkEvent.Context context);

    /**
     * Call inside of <code>SimpleChannel#messageBuilder</code> as the consumer
     * on main thread.
     * @param contextSupplier the <code>Supplier</code> given by the network.
     */
    public final boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> onReceipt(context));
        return false;
    }
}
