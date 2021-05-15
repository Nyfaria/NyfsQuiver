package com.nyfaria.nyfsquiver.packets;

import com.nyfaria.nyfsquiver.common.CommonProxy;
import com.nyfaria.nyfsquiver.common.items.QuiverItem;
import com.nyfaria.nyfsquiver.compat.Compatibility;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketPreviousSlot {
	public static int direction;
	 public PacketPreviousSlot(int way){
	 direction = way;   
	 }

	    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
	        contextSupplier.get().setPacketHandled(true);

	        PlayerEntity player = contextSupplier.get().getSender();
	        if(player != null){
	            //PlayerInventory inventory = player.inventory;
	        	System.out.println(direction);
	            if(!Compatibility.CURIOS.decreaseQuiverSlot(player,direction)){

	                
	            }
	        }
	    }
}
