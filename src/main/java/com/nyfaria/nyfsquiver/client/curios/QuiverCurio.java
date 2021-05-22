package com.nyfaria.nyfsquiver.client.curios;

import com.nyfaria.nyfsquiver.common.items.QuiverStorageManager;

import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class QuiverCurio implements ICurio {

	ItemStack quiverItem;
	public QuiverCurio(ItemStack stack) {
		quiverItem = stack;
	}
	@Override
	public boolean canRightClickEquip() {
		return false;
	}
	@Override
	public void onEquip(SlotContext slotContext, ItemStack newStack) 
	{
		System.out.println("Quiver Equipped");
		ItemStack stack = QuiverStorageManager.getInventory(quiverItem.getOrCreateTag().getInt("nyfsquiver:invIndex")).getStackInSlot(quiverItem.getOrCreateTag().getInt("nyfsquiver:slotIndex"));			
		ItemStack checkStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof ArrowItem,slotContext.getWearer()).map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
		if(checkStack == ItemStack.EMPTY) {
			CuriosApi.getCuriosHelper().getCuriosHandler(slotContext.getWearer()).map(ICuriosItemHandler::getCurios)
		.map(stringICurioStacksHandlerMap -> stringICurioStacksHandlerMap.get("arrows"))
		.map(ICurioStacksHandler::getStacks)
		.ifPresent(curioStackHandler -> curioStackHandler.setStackInSlot(0,stack));
		QuiverStorageManager.getInventory(quiverItem.getOrCreateTag().getInt("nyfsquiver:invIndex")).setStackInSlot(quiverItem.getOrCreateTag().getInt("nyfsquiver:slotIndex"), ItemStack.EMPTY);
		}
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack) {
		System.out.println("Quiver UnEquipped");
		ItemStack stack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof ArrowItem,slotContext.getWearer()).map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
		QuiverStorageManager.getInventory(quiverItem.getOrCreateTag().getInt("nyfsquiver:invIndex")).setStackInSlot(quiverItem.getOrCreateTag().getInt("nyfsquiver:slotIndex"), stack);
		CuriosApi.getCuriosHelper().getCuriosHandler(slotContext.getWearer()).map(ICuriosItemHandler::getCurios)
		.map(stringICurioStacksHandlerMap -> stringICurioStacksHandlerMap.get("arrows"))
		.map(ICurioStacksHandler::getStacks)
		.ifPresent(curioStackHandler -> curioStackHandler.setStackInSlot(0,ItemStack.EMPTY));
	}
	
}
