package com.nyfaria.nyfsquiver.packets;

import com.nyfaria.nyfsquiver.items.QuiverStorageManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketMaxLayers {

    private final int maxLayers;

    public PacketMaxLayers(int maxLayers) {
        this.maxLayers = maxLayers;
    }

    public static PacketMaxLayers decode(FriendlyByteBuf buffer) {
        return new PacketMaxLayers(buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.maxLayers);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().setPacketHandled(true);

        QuiverStorageManager.maxLayers = this.maxLayers;
    }
}
