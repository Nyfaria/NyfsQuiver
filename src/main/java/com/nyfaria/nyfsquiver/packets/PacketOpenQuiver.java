package com.nyfaria.nyfsquiver.packets;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.items.QuiverStorageManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenQuiver {
	 public PacketOpenQuiver(){
	 }

	    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
	        contextSupplier.get().setPacketHandled(true);

	        Player player = contextSupplier.get().getSender();
	        if(player != null){
	            if(!QuiverStorageManager.openQuiver(player)){

	                
	            }
	        }
	    }
}
