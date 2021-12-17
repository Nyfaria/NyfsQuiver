package com.nyfaria.nyfsquiver.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class MeldEnchantment extends Enchantment {

    public MeldEnchantment(Rarity p_44676_, EquipmentSlot... p_44678_) {
        super(p_44676_, EnchantmentCategory.WEARABLE, p_44678_);
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
