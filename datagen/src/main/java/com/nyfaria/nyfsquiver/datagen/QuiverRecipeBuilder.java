package com.nyfaria.nyfsquiver.datagen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.recipe.QuiverUpgradeRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class QuiverRecipeBuilder implements RecipeBuilder {
    private final HolderGetter<Item> items;
    private final RecipeCategory category;
    private final QuiverType inputType;
    private final QuiverType resultType;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    @Nullable
    private String group;
    private boolean showNotification = true;

    public QuiverRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, QuiverType inputType, QuiverType result) {
        this.items = items;
        this.category = category;
        this.inputType = inputType;
        this.resultType = result;
    }

    public static QuiverRecipeBuilder quiver(HolderGetter<Item> items, RecipeCategory category, QuiverType inputType, QuiverType result) {
        return new QuiverRecipeBuilder(items, category, inputType, result);
    }

    public QuiverRecipeBuilder define(Character pSymbol, TagKey<Item> pTag) {
        return this.define(pSymbol, Ingredient.of(this.items.getOrThrow(pTag)));
    }

    public QuiverRecipeBuilder define(Character pSymbol, ItemLike pItem) {
        return this.define(pSymbol, Ingredient.of(pItem));
    }

    public QuiverRecipeBuilder define(Character pSymbol, Ingredient pIngredient) {
        if (this.key.containsKey(pSymbol)) {
            throw new IllegalArgumentException("Symbol '" + pSymbol + "' is already defined!");
        } else if (pSymbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(pSymbol, pIngredient);
            return this;
        }
    }

    public QuiverRecipeBuilder pattern(String pPattern) {
        if (!this.rows.isEmpty() && pPattern.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(pPattern);
            return this;
        }
    }

    @Override
    public QuiverRecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        this.advancementBuilder.unlockedBy(pName, pCriterion);
        return this;
    }

    @Override
    public QuiverRecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    public QuiverRecipeBuilder showNotification(boolean pShowNotification) {
        this.showNotification = pShowNotification;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return ResourceKey.create(Registries.RECIPE, this.resultType.name());
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceKey<Recipe<?>> pId) {
        ShapedRecipePattern pattern = ShapedRecipePattern.of(this.key, this.rows);
        QuiverUpgradeRecipe recipe = new QuiverUpgradeRecipe(
                RecipeBuilder.createCraftingCommonInfo(this.showNotification),
                RecipeBuilder.createCraftingBookInfo(this.category, this.group),
                pattern,
                this.inputType,
                this.resultType
        );
        pRecipeOutput.accept(pId, recipe, this.advancementBuilder.build(pRecipeOutput, pId, this.category));
    }
}
