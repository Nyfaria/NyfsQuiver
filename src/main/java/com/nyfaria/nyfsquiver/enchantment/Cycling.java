package com.nyfaria.nyfsquiver.enchantment;

import com.nyfaria.nyfsquiver.config.NQConfig;
import com.nyfaria.nyfsquiver.init.EnchantmentInit;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class Cycling extends Enchantment {
    public Cycling(Rarity rarity, EquipmentSlot... applicableSlots) {
        super(rarity, EnchantmentInit.QUIVER, applicableSlots);
    }


    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        if(stack.getItem() instanceof QuiverItem) {
            return NQConfig.INSTANCE.cyclingEnchantTable.get();
        }
        return false;
    }

    @Override
    public boolean isAllowedOnBooks() {
        if(NQConfig.CONFIG_SPEC.isLoaded())
        return NQConfig.INSTANCE.cyclingEnchantTable.get();
        return false;
    }


}
