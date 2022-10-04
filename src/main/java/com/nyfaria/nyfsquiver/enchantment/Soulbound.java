package com.nyfaria.nyfsquiver.enchantment;

import com.nyfaria.nyfsquiver.config.NQConfig;
import com.nyfaria.nyfsquiver.init.EnchantmentInit;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class Soulbound extends Enchantment {

    public Soulbound(Rarity rarity, EquipmentSlot... applicableSlots) {
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
        return stack.getItem() instanceof TieredItem || stack.getItem() instanceof ArmorItem;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return NQConfig.INSTANCE.cyclingEnchantTable.get();
    }
}
