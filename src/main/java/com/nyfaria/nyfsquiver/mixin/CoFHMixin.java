package com.nyfaria.nyfsquiver.mixin;


import cofh.core.compat.curios.CuriosProxy;
import cofh.lib.capability.CapabilityArchery;
import com.nyfaria.nyfsquiver.init.TagInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ShootableItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import cofh.lib.util.helpers.ArcheryHelper;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Iterator;
import java.util.function.Predicate;

import static cofh.lib.capability.CapabilityArchery.AMMO_ITEM_CAPABILITY;

@Mixin(ArcheryHelper.class)
public abstract class CoFHMixin
{

	@Shadow
    public static boolean isArrow(ItemStack stack) {
		return false;
	}


    @Shadow
    public static boolean isSimpleArrow(ItemStack stack) {
        return false;
    }

    private static ItemStack findAmmo(PlayerEntity shooter) {

        ItemStack offHand = shooter.getMainHandItem();
        ItemStack mainHand = shooter.getOffhandItem();
		ItemStack quiver = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem().is(TagInit.QUIVER_ITEMS),shooter)
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





    /**
     * @author
     */
    @Overwrite
    public static ItemStack findAmmo(PlayerEntity shooter, ItemStack weapon) {
        ItemStack offHand = shooter.getOffhandItem();
        ItemStack mainHand = shooter.getMainHandItem();
        ItemStack quiver = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem().is(TagInit.QUIVER_ITEMS),shooter)
                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);

        Predicate<ItemStack> isHeldAmmo = weapon.getItem() instanceof ShootableItem ? ((ShootableItem)weapon.getItem()).getSupportedHeldProjectiles() : (i) -> {
            return false;
        };
        Predicate<ItemStack> isAmmo = weapon.getItem() instanceof ShootableItem ? ((ShootableItem)weapon.getItem()).getAllSupportedProjectiles() : (i) -> {
            return false;
        };
        if(!quiver.isEmpty()) {
            return quiver;
        }
        if (!(Boolean)offHand.getCapability(CapabilityArchery.AMMO_ITEM_CAPABILITY).map((cap) -> {
            return !cap.isEmpty(shooter);
        }).orElse(false) && !isHeldAmmo.test(offHand)) {
            if (!(Boolean)mainHand.getCapability(CapabilityArchery.AMMO_ITEM_CAPABILITY).map((cap) -> {
                return !cap.isEmpty(shooter);
            }).orElse(false) && !isHeldAmmo.test(mainHand)) {
                ItemStack[] retStack = new ItemStack[]{ItemStack.EMPTY};
                CuriosProxy.getAllWorn(shooter).ifPresent((c) -> {
                    for(int i = 0; i < c.getSlots(); ++i) {
                        ItemStack slot = c.getStackInSlot(i);
                        if ((Boolean)slot.getCapability(CapabilityArchery.AMMO_ITEM_CAPABILITY).map((cap) -> {
                            return !cap.isEmpty(shooter);
                        }).orElse(false) || isAmmo.test(slot)) {
                            retStack[0] = slot;
                        }
                    }

                });
                if (!retStack[0].isEmpty()) {
                    return retStack[0];
                } else {
                    Iterator var7 = shooter.inventory.items.iterator();

                    ItemStack slot;
                    do {
                        if (!var7.hasNext()) {
                            return ItemStack.EMPTY;
                        }

                        slot = (ItemStack)var7.next();
                    } while(!(Boolean)slot.getCapability(CapabilityArchery.AMMO_ITEM_CAPABILITY).map((cap) -> {
                        return !cap.isEmpty(shooter);
                    }).orElse(false) && !isAmmo.test(slot));

                    return slot;
                }
            } else {
                return mainHand;
            }
        } else {
            return offHand;
        }
    }











    /**
     * @author
     */
    @Overwrite
    public static ItemStack findArrows(PlayerEntity shooter) {
        ItemStack offHand = shooter.getOffhandItem();
        ItemStack mainHand = shooter.getMainHandItem();
        ItemStack quiver = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem().is(TagInit.QUIVER_ITEMS),shooter)
                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
        if(!quiver.isEmpty()) {
            return quiver;
        }
        if (isSimpleArrow(offHand)) {
            return offHand;
        } else if (isSimpleArrow(mainHand)) {
            return mainHand;
        } else {
            for(int i = 0; i < shooter.inventory.getContainerSize(); ++i) {
                ItemStack stack = shooter.inventory.getItem(i);
                if (isSimpleArrow(stack)) {
                    return stack;
                }
            }

            return ItemStack.EMPTY;
        }
    }
}