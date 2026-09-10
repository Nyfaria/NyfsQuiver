package com.nyfaria.nyfsquiver.compat;

import com.google.common.collect.HashMultimap;
import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.init.ItemInit;
import io.wispforest.accessories.api.AccessoriesAPI;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.Accessory;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Every Accessories API reference of the mod is contained in this class.
 * <p>
 * Accessories is a soft dependency, so this class may only ever be loaded while the Accessories
 * mod is actually installed. Gameplay code therefore never touches it directly but goes through
 * {@link com.nyfaria.nyfsquiver.util.QuiverEquipment}.
 */
public final class AccessoriesCompat {

    public static final String MOD_ID = "accessories";

    private static final ResourceLocation ONE_QUIVER = Constants.modLoc("one_quiver");
    private static final String QUIVER_BACK = "quiver_back";
    private static final String QUIVER_HIP = "quiver_hip";

    private AccessoriesCompat() {}

    /**
     * Registers the accessory behavior of the quiver item. This used to happen implicitly by
     * extending {@code AccessoryItem}, which is not possible anymore now that Accessories is
     * optional. Must be called after item registration.
     */
    public static void registerAccessory() {
        AccessoriesAPI.registerAccessory(ItemInit.QUIVER.get(), new QuiverAccessory());
    }

    /**
     * @return the quiver currently equipped in one of the mod's Accessories slots, or an empty
     * stack when there is none.
     */
    public static ItemStack getEquippedQuiver(Player player) {
        AccessoriesCapability capability = AccessoriesCapability.get(player);
        if (capability == null) {
            return ItemStack.EMPTY;
        }
        SlotEntryReference reference = capability.getFirstEquipped(ItemInit.QUIVER.get());
        return reference == null ? ItemStack.EMPTY : reference.stack();
    }

    /**
     * Keeps the mod's own two slots mutually exclusive: equipping a quiver in the back slot
     * removes the hip slot (and the other way around), so only one quiver can be worn.
     */
    private static final class QuiverAccessory implements Accessory {

        private static String otherSlot(SlotReference reference) {
            return reference.slotName().equals(QUIVER_BACK) ? QUIVER_HIP : QUIVER_BACK;
        }

        private static HashMultimap<String, AttributeModifier> modifiers(SlotReference reference) {
            HashMultimap<String, AttributeModifier> map = HashMultimap.create();
            map.put(otherSlot(reference), new AttributeModifier(ONE_QUIVER, -1, AttributeModifier.Operation.ADD_VALUE));
            return map;
        }

        @Override
        public void onEquip(ItemStack stack, SlotReference reference) {
            reference.capability().addTransientSlotModifiers(modifiers(reference));
        }

        @Override
        public void onUnequip(ItemStack stack, SlotReference reference) {
            reference.capability().removeSlotModifiers(modifiers(reference));
        }
    }
}
