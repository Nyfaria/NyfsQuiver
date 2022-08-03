package com.nyfaria.nyfsquiver.enchantment;

import com.nyfaria.nyfsquiver.config.NQConfig;
import com.nyfaria.nyfsquiver.init.EnchantmentInit;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class Quinfinity extends Enchantment {

    public Quinfinity(Rarity rarity, EquipmentSlot... applicableSlots) {
        super(rarity, EnchantmentInit.QUIVER, applicableSlots);
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        if(other.equals(Enchantments.MENDING)){
            return false;
        }
        return super.checkCompatibility(other);
    }

    @Override
    public int getMinCost(int level) {
        return 30;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        if(NQConfig.INSTANCE.cyclingEnchantTable.get()){
            return stack.getItem() instanceof QuiverItem;
        }else {
            return false;
        }
    }

    @Override
    public boolean isAllowedOnBooks() {
        if(NQConfig.CONFIG_SPEC.isLoaded())
        return NQConfig.INSTANCE.cyclingEnchantTable.get();
        return false;
    }
}
