package com.nyfaria.nyfsquiver.datagen;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.TagInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModTagProvider {

    public static class ModItemTags extends TagsProvider<Item>{

        public ModItemTags(PackOutput p_256596_, CompletableFuture<HolderLookup.Provider> p_256513_, @Nullable ExistingFileHelper existingFileHelper) {
            super(p_256596_, Registries.ITEM, p_256513_, Constants.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(TagInit.QUIVER).add(ItemInit.QUIVER.getResourceKey());
            tag(TagInit.QUIVER_ITEMS).addTag(ItemTags.ARROWS).add(Items.FIREWORK_ROCKET.builtInRegistryHolder().getKey());
        }

        public void populateTag(TagKey<Item> tag, Supplier<Item>... items){
            for (Supplier<Item> item : items) {
                tag(tag).add(BuiltInRegistries.ITEM.getResourceKey(item.get()).get());
            }
        }
    }

    public static class ModEnchantTags extends EnchantmentTagsProvider {


        public ModEnchantTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pLookupProvider, Constants.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(EnchantmentTags.NON_TREASURE).add(Constants.CYCLING);
        }
    }

    public static class ModBlockTags extends TagsProvider<Block>{

        public ModBlockTags(PackOutput pGenerator, CompletableFuture<HolderLookup.Provider> p_256513_, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, Registries.BLOCK, p_256513_, Constants.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {

        }
        public  <T extends Block>void populateTag(TagKey<Block> tag, Supplier<?>... items){
            for (Supplier<?> item : items) {
                tag(tag).add(BuiltInRegistries.BLOCK.getResourceKey((Block)item.get()).get());
            }
        }
    }
}
