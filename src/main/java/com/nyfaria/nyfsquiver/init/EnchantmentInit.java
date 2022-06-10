package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.enchantment.Cycling;
import com.nyfaria.nyfsquiver.enchantment.Meld;
import com.nyfaria.nyfsquiver.enchantment.Quinfinity;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, NyfsQuiver.MODID);

    public static final RegistryObject<Enchantment> MELD = ENCHANTMENTS.register("meld_enchant", () -> new Meld(Enchantment.Rarity.VERY_RARE));
    public static final RegistryObject<Enchantment> CYCLING = ENCHANTMENTS.register("cycling_enchant", () -> new Cycling(Enchantment.Rarity.VERY_RARE));
    public static final RegistryObject<Enchantment> QUINFINITY = ENCHANTMENTS.register("quinfinity_enchant", () -> new Quinfinity(Enchantment.Rarity.VERY_RARE));

    public static final EnchantmentCategory QUIVER = EnchantmentCategory.create("quiver", (item) -> item instanceof QuiverItem);
}
