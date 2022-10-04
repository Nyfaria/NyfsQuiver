/*
package com.nyfaria.nyfsquiver.mixin;


import cofh.core.compat.curios.CuriosProxy;
import cofh.lib.capability.CapabilityArchery;
import cofh.lib.util.helpers.ArcheryHelper;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Iterator;
import java.util.UUID;
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

*
     * @author Nyfaria
     * @reason COFH is a shit mod


    @Overwrite(remap = false)
    public static ItemStack findAmmo(Player shooter, ItemStack weapon) {
        ItemStack quiver = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.is(TagInit.QUIVER_ITEMS),shooter)
                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
        if(!quiver.isEmpty()) {
            ItemStack quiverStack = QuiverItem.getInventory(quiver).getStackInSlot(QuiverHolderAttacher.getQuiverHolderUnwrap(quiver).getCurrentSlot());
            if(!quiverStack.isEmpty()) {
                return quiverStack;
            }
        }
        ItemStack itemstack = shooter.getOffhandItem();
        ItemStack itemstack1 = shooter.getMainHandItem();
        Item retStack = weapon.getItem();
        Predicate predicate2;
        if (retStack instanceof ProjectileWeaponItem) {
            ProjectileWeaponItem projectileweaponitem = (ProjectileWeaponItem)retStack;
            predicate2 = projectileweaponitem.getSupportedHeldProjectiles();
        } else {
            predicate2 = (i) -> false;
        }

        Predicate<ItemStack> predicate = predicate2;
        Item item1 = weapon.getItem();
        if (item1 instanceof ProjectileWeaponItem) {
            ProjectileWeaponItem projectileweaponitem1 = (ProjectileWeaponItem)item1;
            predicate2 = projectileweaponitem1.getAllSupportedProjectiles();
        } else {
            predicate2 = (i) -> false;
        }

        Predicate<ItemStack> predicate1 = predicate2;
        if (!itemstack.getCapability(CapabilityArchery.AMMO_ITEM_CAPABILITY).map((cap) -> !cap.isEmpty(shooter)).orElse(false) && !predicate.test(itemstack)) {
            if (!itemstack1.getCapability(CapabilityArchery.AMMO_ITEM_CAPABILITY).map((cap) -> !cap.isEmpty(shooter)).orElse(false) && !predicate.test(itemstack1)) {
                ItemStack[] aitemstack = new ItemStack[]{ItemStack.EMPTY};
                CuriosProxy.getAllWorn(shooter).ifPresent((c) -> {
                    for(int i = 0; i < c.getSlots(); ++i) {
                        ItemStack itemstack3 = c.getStackInSlot(i);
                        if (itemstack3.getCapability(CapabilityArchery.AMMO_ITEM_CAPABILITY).map((cap) -> !cap.isEmpty(shooter)).orElse(false) || predicate1.test(itemstack3)) {
                            aitemstack[0] = itemstack3;
                        }
                    }

                });
                if (!aitemstack[0].isEmpty()) {
                    return aitemstack[0];
                } else {
                    for(ItemStack itemstack2 : shooter.getInventory().items) {
                        if (itemstack2.getCapability(CapabilityArchery.AMMO_ITEM_CAPABILITY).map((cap) -> !cap.isEmpty(shooter)).orElse(false) || predicate1.test(itemstack2)) {
                            return itemstack2;
                        }
                    }

                    return ItemStack.EMPTY;
                }
            } else {
                return itemstack1;
            }
        } else {
            return itemstack;
        }
    }






//*
// * @author Nyfaria
// * @reason CoFH is a shit mod that does shit it shouldn't


//    @Overwrite
//    public static ItemStack findAmmo(Player shooter, ItemStack weapon) {
//        ItemStack offHand = shooter.getOffhandItem();
//        ItemStack mainHand = shooter.getMainHandItem();
//        ItemStack quiver = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.is(TagInit.QUIVER_ITEMS),shooter)
//                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
//
//        Predicate<ItemStack> isHeldAmmo = weapon.getItem() instanceof ProjectileWeaponItem ? ((ProjectileWeaponItem)weapon.getItem()).getSupportedHeldProjectiles() : (i) -> {
//            return false;
//        };
//        Predicate<ItemStack> isAmmo = weapon.getItem() instanceof ProjectileWeaponItem ? ((ProjectileWeaponItem)weapon.getItem()).getAllSupportedProjectiles() : (i) -> {
//            return false;
//        };
//        if(!quiver.isEmpty()) {
//            return quiver;
//        }
//        if (!(Boolean)offHand.getCapability(CapabilityArchery.AMMO_ITEM_CAPABILITY).map((cap) -> {
//            return !cap.isEmpty(shooter);
//        }).orElse(false) && !isHeldAmmo.test(offHand)) {
//            if (!(Boolean)mainHand.getCapability(CapabilityArchery.AMMO_ITEM_CAPABILITY).map((cap) -> {
//                return !cap.isEmpty(shooter);
//            }).orElse(false) && !isHeldAmmo.test(mainHand)) {
//                ItemStack[] retStack = new ItemStack[]{ItemStack.EMPTY};
//                CuriosProxy.getAllWorn(shooter).ifPresent((c) -> {
//                    for(int i = 0; i < c.getSlots(); ++i) {
//                        ItemStack slot = c.getStackInSlot(i);
//                        if ((Boolean)slot.getCapability(CapabilityArchery.AMMO_ITEM_CAPABILITY).map((cap) -> {
//                            return !cap.isEmpty(shooter);
//                        }).orElse(false) || isAmmo.test(slot)) {
//                            retStack[0] = slot;
//                        }
//                    }
//
//                });
//                if (!retStack[0].isEmpty()) {
//                    return retStack[0];
//                } else {
//                    Iterator var7 = shooter.getInventory().items.iterator();
//
//                    ItemStack slot;
//                    do {
//                        if (!var7.hasNext()) {
//                            return ItemStack.EMPTY;
//                        }
//
//                        slot = (ItemStack)var7.next();
//                    } while(!(Boolean)slot.getCapability(CapabilityArchery.AMMO_ITEM_CAPABILITY).map((cap) -> {
//                        return !cap.isEmpty(shooter);
//                    }).orElse(false) && !isAmmo.test(slot));
//
//                    return slot;
//                }
//            } else {
//                return mainHand;
//            }
//        } else {
//            return offHand;
//        }
//    }











//
//    @Inject(method = "findAmmo", at = @At("HEAD"),cancellable = true, remap = false)
//    private static void quiverBitches(Player shooter, ItemStack weapon, CallbackInfoReturnable<ItemStack> cir) {
//        ItemStack quiver = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.is(TagInit.QUIVER_ITEMS),shooter)
//                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
//        if(!quiver.isEmpty()) {
//            ItemStack quiverStack = QuiverItem.getInventory(quiver).getStackInSlot(QuiverHolderAttacher.getQuiverHolderUnwrap(quiver).getCurrentSlot());
//            if(!quiverStack.isEmpty()) {
//                cir.setReturnValue(quiverStack);
//            }
//        }
//    }
//
//
//    @Inject(method = "findArrows", at = @At("HEAD"),cancellable = true, remap = false)
//    private static void quiverBitches(Player shooter, CallbackInfoReturnable<ItemStack> cir) {
//        ItemStack quiver = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.is(TagInit.QUIVER_ITEMS),shooter)
//                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
//        if(!quiver.isEmpty()) {
//            ItemStack quiverStack = QuiverItem.getInventory(quiver).getStackInSlot(QuiverHolderAttacher.getQuiverHolderUnwrap(quiver).getCurrentSlot());
//            if(!quiverStack.isEmpty()) {
//                cir.setReturnValue(quiverStack);
//            }
//        }
//    }
}
*/
