package com.nyfaria.nyfsquiver.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class MeldEnchantment extends Enchantment {

    public MeldEnchantment(Rarity p_44676_, EquipmentSlotType... p_44678_) {
        super(p_44676_, EnchantmentType.WEARABLE, p_44678_);
    }

    @Override
    public int getMinCost(int p_44616_) {
        return 25;
    }

    @Override
    public int getMaxCost(int p_44619_) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return true;
    }
}
