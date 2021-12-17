package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.enchantment.MeldEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, NyfsQuiver.MOD_ID);

    public static final RegistryObject<Enchantment> MELD_ENCHANTMENT = ENCHANTMENTS.register("meld_enchant", ()-> new MeldEnchantment(Enchantment.Rarity.COMMON));
}
