package com.nyfaria.nyfsquiver.mixin;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ArrowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import cofh.lib.util.helpers.ArcheryHelper;
import top.theillusivec4.curios.api.CuriosApi;

import static cofh.lib.capability.CapabilityArchery.AMMO_ITEM_CAPABILITY;

@Mixin(ArcheryHelper.class)
public class CoFHMixin 
{

	@Shadow
    public static boolean isArrow(ItemStack stack) {
		return false;
	}
	 
	
	@Overwrite
    public static ItemStack findAmmo(PlayerEntity shooter) {

        ItemStack offHand = shooter.getMainHandItem();
        ItemStack mainHand = shooter.getOffhandItem();
		ItemStack quiver = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof ArrowItem,shooter)
				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);

		if(!quiver.isEmpty()) {
			return quiver;
		}
		
        if (offHand.getCapability(AMMO_ITEM_CAPABILITY).map(cap -> !cap.isEmpty(shooter)).orElse(false) || isArrow(offHand)) {
            return offHand;
        }
        if (mainHand.getCapability(AMMO_ITEM_CAPABILITY).map(cap -> !cap.isEmpty(shooter)).orElse(false) || isArrow(mainHand)) {
            return mainHand;
        }
        for (ItemStack slot : shooter.inventory.items) {
            if (slot.getCapability(AMMO_ITEM_CAPABILITY).map(cap -> !cap.isEmpty(shooter)).orElse(false) || isArrow(slot)) {
                return slot;
            }
        }
        return ItemStack.EMPTY;
    }
}