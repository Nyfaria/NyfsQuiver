package com.nyfaria.nyfsquiver.datagen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.recipe.QuiverUpgradeRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.AdvancementRewards.Builder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuiverRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final QuiverType inputType;
    private final QuiverType resultType;
    private final List<String> rows;
    private final Map<Character, Ingredient> key;
    private final Map<String, Criterion<?>> criteria;
    @Nullable
    private String group;
    private boolean showNotification;


    public QuiverRecipeBuilder(RecipeCategory category, QuiverType inputType, QuiverType result) {
        this.rows = Lists.newArrayList();
        this.key = Maps.newLinkedHashMap();
        this.criteria = new LinkedHashMap<>();
        this.showNotification = true;
        this.category = category;
        this.result = ItemInit.QUIVER.get();
        this.count = 1;
        this.resultType = result;
        this.inputType = inputType;
    }

    public static QuiverRecipeBuilder quiver(RecipeCategory category, QuiverType inputType, QuiverType result) {
        return new QuiverRecipeBuilder(category, inputType ,result);
    }

    public QuiverRecipeBuilder define(Character pSymbol, TagKey<Item> pTag) {
        return this.define(pSymbol, Ingredient.of(pTag));
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

    public QuiverRecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        this.criteria.put(pName, pCriterion);
        return this;
    }

    public QuiverRecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    public QuiverRecipeBuilder showNotification(boolean pShowNotification) {
        this.showNotification = pShowNotification;
        return this;
    }

    public Item getResult() {
        return this.result;
    }

    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId) {
        ShapedRecipePattern shapedrecipepattern = this.ensureValid(pId);
        Advancement.Builder advancement$builder = pRecipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId)).rewards(Builder.recipe(pId)).requirements(Strategy.OR);
        Map<String,Criterion<?>> var10000 = this.criteria;
        Objects.requireNonNull(advancement$builder);
        var10000.forEach(advancement$builder::addCriterion);
        QuiverUpgradeRecipe shapedrecipe = new QuiverUpgradeRecipe((String)Objects.requireNonNullElse(this.group, ""), RecipeBuilder.determineBookCategory(this.category), shapedrecipepattern, this.inputType, this.resultType, this.showNotification);
        pRecipeOutput.accept(pId, shapedrecipe, advancement$builder.build(pId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private ShapedRecipePattern ensureValid(ResourceLocation pLoaction) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(pLoaction));
        } else {
            return ShapedRecipePattern.of(this.key, this.rows);
        }
    }
}
