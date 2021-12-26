package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.enchantment.MeldEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, NyfsQuiver.MOD_ID);

    //public static final RegistryObject<Enchantment> MELD_ENCHANTMENT = ENCHANTMENTS.register("meld_enchant", ()-> new MeldEnchantment(Enchantment.Rarity.COMMON));
}
