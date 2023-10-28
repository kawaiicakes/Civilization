package io.github.kawaiicakes.civilization.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Extending classes are intended to be sent to either client or server via
 * <code>SimpleChannel#send</code> and <code>SimpleChannel#sendToServer</code>
 * respectively. Extending classes must be registered with <code>SimpleChannel
 * #messageBuilder</code> during Forge common setup.
 * <br><br>
 * What the packet communicates and how it does so is dependent on implementation
 * of this class. You must also create two constructors; one which instantiates
 * this normally for sending, and another which takes a <code>FriendlyByteBuf</code>
 * as an argument and essentially acts as a deserializer.
 */
public abstract class SimplePacket {
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
     * Be aware of the side you are working on at all times.
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
