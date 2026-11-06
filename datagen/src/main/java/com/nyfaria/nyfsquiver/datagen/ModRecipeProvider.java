package com.nyfaria.nyfsquiver.datagen;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.api.RegistryAccessAccess;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.QuiverInit;
import com.nyfaria.nyfsquiver.recipe.SmithingQuiverRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer, HolderLookup.Provider holderLookup) {
        ItemStack itemStack = new ItemStack(ItemInit.QUIVER.get());
        QuiverType leather = holderLookup.lookup(QuiverInit.QUIVER_TYPES.key()).get().get(Constants.LEATHER_QUIVER).get().value();
        QuiverType iron = holderLookup.lookup(QuiverInit.QUIVER_TYPES.key()).get().get(Constants.IRON_QUIVER).get().value();
        QuiverType gold = holderLookup.lookup(QuiverInit.QUIVER_TYPES.key()).get().get(Constants.GOLD_QUIVER).get().value();
        QuiverType diamond = holderLookup.lookup(QuiverInit.QUIVER_TYPES.key()).get().get(Constants.DIAMOND_QUIVER).get().value();
        QuiverType netherite = holderLookup.lookup(QuiverInit.QUIVER_TYPES.key()).get().get(Constants.NETHERITE_QUIVER).get().value();
        itemStack.set(DataComponentInit.QUIVER_TYPE.get(), leather);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, itemStack)
                .define('L', Items.LEATHER)
                .define('F', Items.FEATHER)
                .pattern("  F")
                .pattern(" L ")
                .pattern("L  ")
                .unlockedBy("has_item", has(Items.LEATHER))
                .save(consumer);
        QuiverRecipeBuilder.quiver(RecipeCategory.MISC, leather, iron)
                .define('I', Items.IRON_INGOT)
                .define('L', ItemInit.QUIVER.get())
                .define('F', Items.FEATHER)
                .pattern("IIF")
                .pattern("ILI")
                .pattern("III")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer, Constants.modLoc("iron_quiver"));
        QuiverRecipeBuilder.quiver(RecipeCategory.MISC, iron, gold)
                .define('G', Items.GOLD_INGOT)
                .define('L', ItemInit.QUIVER.get())
                .define('F', Items.FEATHER)
                .pattern("GGF")
                .pattern("GLG")
                .pattern("GGG")
                .unlockedBy("has_item", has(Items.GOLD_INGOT))
                .save(consumer, Constants.modLoc("gold_quiver"));
        QuiverRecipeBuilder.quiver(RecipeCategory.MISC, gold, diamond)
                .define('D', Items.DIAMOND)
                .define('L', ItemInit.QUIVER.get())
                .define('F', Items.FEATHER)
                .pattern("DDF")
                .pattern("DLD")
                .pattern("DDD")
                .unlockedBy("has_item", has(Items.DIAMOND))
                .save(consumer, Constants.modLoc("diamond_quiver"));
        ResourceLocation pRecipeId = Constants.modLoc("netherite_quiver");
        Advancement.Builder advancement$builder = consumer.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                .rewards(AdvancementRewards.Builder.recipe(pRecipeId))
                .requirements(AdvancementRequirements.Strategy.OR);
        consumer.accept(pRecipeId,
                new SmithingQuiverRecipe(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        diamond,
                        Ingredient.of(Items.NETHERITE_INGOT),
                        netherite),
                        advancement$builder.build(pRecipeId.withPrefix("recipes/misc/")));
    }
}
