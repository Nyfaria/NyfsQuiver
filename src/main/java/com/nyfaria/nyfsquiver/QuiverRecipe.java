package com.nyfaria.nyfsquiver;

import com.google.gson.JsonObject;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import javax.annotation.Nullable;
import java.util.Map;


public class QuiverRecipe extends ShapedRecipe {

    public QuiverRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        for (int index = 0; index < inv.getContainerSize(); index++) {
            ItemStack stack = inv.getItem(index);
            if (!stack.isEmpty() && stack.getItem() instanceof QuiverItem) {
                ItemStack result = this.getResultItem().copy();
                result.setTag(stack.getTag());
                if (stack.hasCustomHoverName())
                    result.setHoverName(stack.getHoverName());
                for (Map.Entry<Enchantment, Integer> enchant : EnchantmentHelper.getEnchantments(stack).entrySet())
                    result.enchant(enchant.getKey(), enchant.getValue());
                QuiverInventory oldInventory = QuiverHolderAttacher.getQuiverHolderUnwrap(stack).getInventory();
                for (int slot = 0; slot < oldInventory.getStacks().size(); slot++) {
                    QuiverHolderAttacher.getQuiverHolderUnwrap(result).getInventory().insertItem(slot, oldInventory.getStackInSlot(slot), false);
                }
                return result;
            }
        }
        ItemStack quiverItem = this.getResultItem().copy();

        CompoundTag compound = quiverItem.getOrCreateTag();
        //compound.putInt("invIndex", QuiverStorageManager.createInventoryIndex(QuiverType.BASIC));
        compound.putInt("slotIndex", 0);
        quiverItem.setTag(compound);

        return quiverItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return super.getSerializer();
    }

    public static class Serializer /*extends ForgeRegistryEntry<RecipeSerializer<?>>*/ implements RecipeSerializer<QuiverRecipe> {
        @Override
        public QuiverRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
            return new QuiverRecipe(recipeId, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem());
        }

        @Nullable
        @Override
        public QuiverRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer);
            return new QuiverRecipe(recipeId, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, QuiverRecipe recipe) {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
        }
    }
}

