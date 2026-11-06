package com.nyfaria.nyfsquiver.datagen;

import com.google.common.collect.Maps;
import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.QuiverInit;
import com.nyfaria.nyfsquiver.recipe.SmithingQuiverRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        HolderLookup.RegistryLookup<QuiverType> types = this.registries.lookupOrThrow(QuiverInit.QUIVER_TYPES.key());
        QuiverType leather = types.getOrThrow(Constants.LEATHER_QUIVER).value();
        QuiverType iron = types.getOrThrow(Constants.IRON_QUIVER).value();
        QuiverType gold = types.getOrThrow(Constants.GOLD_QUIVER).value();
        QuiverType diamond = types.getOrThrow(Constants.DIAMOND_QUIVER).value();
        QuiverType netherite = types.getOrThrow(Constants.NETHERITE_QUIVER).value();

        ResourceKey<Recipe<?>> leatherId = ResourceKey.create(Registries.RECIPE, Constants.modLoc("quiver"));
        Map<Character, Ingredient> leatherKey = Maps.newLinkedHashMap();
        leatherKey.put('L', Ingredient.of(Items.LEATHER));
        leatherKey.put('F', Ingredient.of(Items.FEATHER));
        RecipeUnlockAdvancementBuilder leatherUnlock = new RecipeUnlockAdvancementBuilder();
        leatherUnlock.unlockedBy("has_item", has(Items.LEATHER));
        this.output.accept(leatherId,
                new ShapedRecipe(
                        RecipeBuilder.createCraftingCommonInfo(true),
                        RecipeBuilder.createCraftingBookInfo(RecipeCategory.MISC, null),
                        ShapedRecipePattern.of(leatherKey, List.of("  F", " L ", "L  ")),
                        leather.defaultTemplate()),
                leatherUnlock.build(this.output, leatherId, RecipeCategory.MISC));

        QuiverRecipeBuilder.quiver(this.items, RecipeCategory.MISC, leather, iron)
                .define('I', Items.IRON_INGOT)
                .define('L', ItemInit.QUIVER.get())
                .define('F', Items.FEATHER)
                .pattern("IIF")
                .pattern("ILI")
                .pattern("III")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(this.output, recipeKey("iron_quiver"));
        QuiverRecipeBuilder.quiver(this.items, RecipeCategory.MISC, iron, gold)
                .define('G', Items.GOLD_INGOT)
                .define('L', ItemInit.QUIVER.get())
                .define('F', Items.FEATHER)
                .pattern("GGF")
                .pattern("GLG")
                .pattern("GGG")
                .unlockedBy("has_item", has(Items.GOLD_INGOT))
                .save(this.output, recipeKey("gold_quiver"));
        QuiverRecipeBuilder.quiver(this.items, RecipeCategory.MISC, gold, diamond)
                .define('D', Items.DIAMOND)
                .define('L', ItemInit.QUIVER.get())
                .define('F', Items.FEATHER)
                .pattern("DDF")
                .pattern("DLD")
                .pattern("DDD")
                .unlockedBy("has_item", has(Items.DIAMOND))
                .save(this.output, recipeKey("diamond_quiver"));

        ResourceKey<Recipe<?>> netheriteId = recipeKey("netherite_quiver");
        RecipeUnlockAdvancementBuilder netheriteUnlock = new RecipeUnlockAdvancementBuilder();
        netheriteUnlock.unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT));
        this.output.accept(netheriteId,
                new SmithingQuiverRecipe(
                        RecipeBuilder.createCraftingCommonInfo(true),
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        diamond,
                        Ingredient.of(Items.NETHERITE_INGOT),
                        netherite),
                netheriteUnlock.build(this.output, netheriteId, RecipeCategory.MISC));
    }

    private static ResourceKey<Recipe<?>> recipeKey(String path) {
        return ResourceKey.create(Registries.RECIPE, Constants.modLoc(path));
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Nyf's Quivers Recipes";
        }
    }
}
