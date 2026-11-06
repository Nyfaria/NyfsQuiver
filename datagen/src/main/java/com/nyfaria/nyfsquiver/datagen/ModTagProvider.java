package com.nyfaria.nyfsquiver.datagen;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.TagInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.CompletableFuture;

public class ModTagProvider {

    public static class ModItemTags extends KeyTagProvider<Item> {
        public ModItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, Registries.ITEM, lookupProvider, Constants.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(TagInit.QUIVER).add(BuiltInRegistries.ITEM.getResourceKey(ItemInit.QUIVER.get()).orElseThrow());
            tag(TagInit.QUIVER_ITEMS).addTag(ItemTags.ARROWS).add(BuiltInRegistries.ITEM.getResourceKey(Items.FIREWORK_ROCKET).orElseThrow());
        }
    }

    public static class ModEnchantTags extends KeyTagProvider<Enchantment> {
        public ModEnchantTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, Registries.ENCHANTMENT, lookupProvider, Constants.MODID);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(EnchantmentTags.NON_TREASURE).add(Constants.CYCLING);
        }
    }
}
