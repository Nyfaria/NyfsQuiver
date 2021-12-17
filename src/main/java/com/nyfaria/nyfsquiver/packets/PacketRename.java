package com.nyfaria.nyfsquiver.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraftforge.network.NetworkEvent;

public class PacketRename {

    private String name;

    public PacketRename(String name){
        this.name = name;
    } 

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBoolean(this.name != null);
        if(this.name != null)
            buffer.writeUtf(this.name);
    }

    public static PacketRename decode(FriendlyByteBuf buffer){
        return new PacketRename(buffer.readBoolean() ? buffer.readUtf(32767) : null);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
        contextSupplier.get().setPacketHandled(true);

        Player player = contextSupplier.get().getSender();
        if(player != null){
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

            if(stack.isEmpty() || !(stack.getItem() instanceof QuiverItem))
                stack = player.getItemInHand(InteractionHand.OFF_HAND);
            if(stack.isEmpty() || !(stack.getItem() instanceof QuiverItem))
                return;

            stack.setHoverName(new TextComponent(this.name));
        }
    }

}