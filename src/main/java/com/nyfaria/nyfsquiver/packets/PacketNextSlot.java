package com.nyfaria.nyfsquiver.packets;


import com.nyfaria.nyfsquiver.items.QuiverStorageManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketNextSlot {
	public static int direction;
	 public PacketNextSlot(int way){
	 direction = way;   
	 }

	    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
	        contextSupplier.get().setPacketHandled(true);

	        PlayerEntity player = contextSupplier.get().getSender();
	        if(player != null){
	            if(!QuiverStorageManager.increaseQuiverSlot(player,direction)){

	                
	            }
	        }
	    }
}
