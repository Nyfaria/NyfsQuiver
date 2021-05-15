package com.nyfaria.nyfsquiver.core.events;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import net.minecraftforge.api.distmarker.Dist;
import com.nyfaria.nyfsquiver.common.items.QuiverItem;
import com.nyfaria.nyfsquiver.common.items.QuiverStorageManager;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

@EventBusSubscriber(modid = NyfsQuiver.MOD_ID, bus = Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public void pickupItem(EntityItemPickupEvent event) {
        System.out.println("Item picked up!");
        Optional<ImmutableTriple<String,Integer,ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() 
          	  instanceof QuiverItem, event.getPlayer());
        QuiverStorageManager.getInventory(optional.get().right.getOrCreateTag().getInt("nyfsquiver:invIndex")).insertItem(0, event.getItem().getItem(), false);
    }
	@SubscribeEvent
	public static void arrowNocked(ArrowNockEvent event) {
		if(!event.getWorld().isClientSide) {
			System.out.println("Boop");
			Optional<ImmutableTriple<String,Integer,ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() 
            	  instanceof QuiverItem, event.getPlayer());
			//event.getPlayer().inventory.add(
			if(optional.isPresent()) {
				ItemStack stack = QuiverStorageManager.getInventory(optional.get().right.getOrCreateTag().getInt("nyfsquiver:invIndex")).getStackInSlot(0);
				System.out.println(optional.get().right.getOrCreateTag().getInt("nyfsquiver:invIndex"));
				//event.setAction(new ActionResult<ItemStack>(ActionResultType.PASS,stack));
        //  );
			}
		}
	}
	
}

