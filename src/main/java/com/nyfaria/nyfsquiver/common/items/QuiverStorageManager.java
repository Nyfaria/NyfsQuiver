package com.nyfaria.nyfsquiver.common.items;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.nyfaria.nyfsquiver.NQConfig;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.packets.PacketMaxLayers;

public class QuiverStorageManager {
	 		
	private static File directory;
	private static final HashMap<Integer,QuiverInventory> inventories = new HashMap<>();
	    private static int inventoryIndex = 0;
 
	    public static int maxLayers;

	    @SubscribeEvent
	    public static void onWorldSave(WorldEvent.Save event){
	        if(event.getWorld().isClientSide() || !(event.getWorld() instanceof World) || ((World)event.getWorld()).dimension() != World.OVERWORLD)
	            return;
	        save();
	    }

	    @SubscribeEvent
	    public static void onWorldLoad(WorldEvent.Load event){
	        if(event.getWorld().isClientSide() || !(event.getWorld() instanceof World) || ((World)event.getWorld()).dimension() != World.OVERWORLD)
	            return;
	        maxLayers = 0;
	        ServerWorld world = (ServerWorld)event.getWorld();
	        directory = new File(world.getServer().getWorldPath(FolderName.ROOT).toFile(), "nyfsquiver/quivers");
	        load();
	    }

	    public static QuiverInventory getInventory(int index){
	        QuiverInventory inventory = inventories.get(index);
	        if(inventory == null){
	            File file = new File(directory, "inventory" + index + ".nbt");
	            if(file.exists()){
	                inventory = new QuiverInventory(false, index);
	                inventory.load(file);
	                inventories.put(index, inventory);
	            }
	        }
	        return inventory;
	    }

	    public static int createInventoryIndex(QuiverType type){
	        int index = inventoryIndex++;
	        inventories.put(index, new QuiverInventory(false, index, type.getRows(),type.getColumns()));
	        return index;
	    }

	    public static void save(){
	        directory.mkdirs();
	        for(int i : inventories.keySet())
	            inventories.get(i).save(new File(directory, "inventory" + i + ".nbt"));
	    }

	    public static void load(){
			File[] files = directory.listFiles();
			inventories.clear();
			if(files == null)
				files = new File[0];
			int highest = -1;
			for(File file : files){
				String name = file.getName();
				if(!name.startsWith("inventory") || !name.endsWith(".nbt"))
					continue;
				int index;
				try{
					index = Integer.parseInt(name.substring("inventory".length(), name.length() - ".nbt".length()));
				}catch(NumberFormatException e){continue;}
				if(index > highest)
					highest = index;

				// for validation
				QuiverInventory inventory = new QuiverInventory(false, index);
				inventory.load(file);
				inventories.put(index, inventory);
			}

			inventoryIndex = highest + 1;

			// validation
			for(Map.Entry<Integer,QuiverInventory> entry : inventories.entrySet()){
				QuiverInventory inventory = entry.getValue();
				for(ItemStack stack : inventory.getStacks()){
					if(stack.getItem() instanceof QuiverItem && stack.getOrCreateTag().contains("invIndex")){
						int index = stack.getTag().getInt("invIndex");
						if(!inventories.containsKey(index)){
							stack.getTag().remove("invIndex");
							continue;
						}
					}
				}
			}

			for(Map.Entry<Integer,QuiverInventory> entry : inventories.entrySet()){
				QuiverInventory inventory = entry.getValue();
			}

			save();
			inventories.clear();
	    }


	    @SuppressWarnings("resource")
		@SubscribeEvent
	    public static void onJoin(PlayerEvent.PlayerLoggedInEvent e){
	        if(!e.getEntity().getCommandSenderWorld().isClientSide)
	            NyfsQuiver.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)e.getEntityLiving()), new PacketMaxLayers(maxLayers));
	    }

	}

