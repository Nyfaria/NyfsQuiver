package com.nyfaria.nyfsquiver.core.events;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.common.items.QuiverInventory;
import com.nyfaria.nyfsquiver.common.items.QuiverItem;
import com.nyfaria.nyfsquiver.common.items.QuiverStorageManager;

import com.nyfaria.nyfsquiver.init.TagInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;


@EventBusSubscriber(modid = NyfsQuiver.MOD_ID, bus = Bus.FORGE)
public class EventHandler {
	

	
	
	
	@SubscribeEvent
	public static void arrowPickup(final EntityItemPickupEvent e) {
		ItemStack toPickup = e.getItem().getItem();
		PlayerEntity player = e.getPlayer();
		ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem,player)
				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);

		if(toPickup.getItem().is(TagInit.QUIVER_ITEMS) && !quiverStack.isEmpty()) {
			if (!CuriosApi.getCuriosHelper().findEquippedCurio(NyfsQuiver.arrow_predicate, player).
					map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY).isEmpty()) {
				CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(icurioitemhandler -> {
					ItemStack rem = toPickup.copy();
					ICurioStacksHandler iCurioStacksHandler = icurioitemhandler.getCurios().get("arrows");	
					
					IDynamicStackHandler iDynamicStackHandler = iCurioStacksHandler.getStacks();

					rem = iDynamicStackHandler.insertItem(0, rem, false);
					toPickup.setCount(rem.getCount());
					/*if (toPickup.getCount() > rem.getCount()) {
						if (rem.isEmpty()) {
							iDynamicStackHandler.insertItem(0, toPickup, false);
							toPickup.setCount(0);
							e.setCanceled(true);
						} 
						else 
						{
							toPickup.setCount(rem.getCount());
						}
					}*/
				});
			}
			else if (CuriosApi.getCuriosHelper().findEquippedCurio(NyfsQuiver.arrow_predicate, player).
					map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY).isEmpty()) {
				//ItemStack boople = toPickup.copy();
				System.out.println("beeple " + toPickup);
				CuriosApi.getCuriosHelper().getCuriosHandler(player).map(ICuriosItemHandler::getCurios)
				.map(stringICurioStacksHandlerMap -> stringICurioStacksHandlerMap.get("arrows"))
				.map(ICurioStacksHandler::getStacks)
				.ifPresent(curioStackHandler -> curioStackHandler.setStackInSlot(0,toPickup.copy()));
				toPickup.setCount(0);
			}
			QuiverInventory qi = QuiverStorageManager.getInventory(quiverStack.getOrCreateTag().getInt("nyfsquiver:invIndex"));
			int slots = qi.getSlots();
			int cSlot = quiverStack.getOrCreateTag().getInt("nyfsquiver:slotIndex");
			for(int s = 0; s < slots; s++) {
				if(s != cSlot) {
					ItemStack currentStack = qi.getStackInSlot(s);
					ItemStack rem2 = toPickup.copy();
					if(currentStack.getItem() == toPickup.getItem()) 
					{
						rem2 = qi.insertItem(s, rem2, false);
					}
					toPickup.setCount(rem2.getCount());
				}
			}
		}
	}
}

