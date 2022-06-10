package com.nyfaria.nyfsquiver.packets;

import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRename {

    private final String name;

    public PacketRename(String name) {
        this.name = name;
    }

    public static PacketRename decode(FriendlyByteBuf buffer) {
        return new PacketRename(buffer.readBoolean() ? buffer.readUtf(32767) : null);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.name != null);
        if (this.name != null)
            buffer.writeUtf(this.name);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().setPacketHandled(true);

        Player player = contextSupplier.get().getSender();
        if (player != null) {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

            if (stack.isEmpty() || !(stack.getItem() instanceof QuiverItem))
                stack = player.getItemInHand(InteractionHand.OFF_HAND);
            if (stack.isEmpty() || !(stack.getItem() instanceof QuiverItem))
                return;

            stack.setHoverName(Component.literal(this.name));
        }
    }

}