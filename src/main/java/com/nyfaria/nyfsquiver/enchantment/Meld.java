package com.nyfaria.nyfsquiver.enchantment;

import com.nyfaria.nyfsquiver.config.NQConfig;
import com.nyfaria.nyfsquiver.init.EnchantmentInit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class Meld extends Enchantment {

    public Meld(Rarity p_44676_, EquipmentSlot... p_44678_) {
        super(p_44676_, EnchantmentCategory.WEARABLE, p_44678_);
    }

    public static boolean shouldRender(ItemStack stack, LivingEntity living) {
        if (living.isInvisible()) {
            return !EnchantmentHelper.getEnchantments(stack).containsKey(EnchantmentInit.MELD.get());
        }
        return true;
    }

    @Override
    public int getMinCost(int eLevel) {
        return 10 * eLevel;
    }

    @Override
    public int getMaxCost(int eLevel) {
        return getMinCost(eLevel) + 30;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return NQConfig.INSTANCE.meldingEnchantTable.get();
    }

    @Override
    public boolean isAllowedOnBooks() {
        if(NQConfig.CONFIG_SPEC.isLoaded()) {
            return NQConfig.INSTANCE.meldingEnchantTable.get();
        }
        return false;
    }
}
