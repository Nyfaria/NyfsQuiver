package com.nyfaria.nyfsquiver;

import com.google.gson.JsonObject;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Map;


public class QuiverRecipe extends ShapedRecipe {

	public QuiverRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn){
		super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
	}

	@Override
	public ItemStack assemble(CraftingInventory inv){
		for(int index = 0; index < inv.getContainerSize(); index++) {
			ItemStack stack = inv.getItem(index);
			if(!stack.isEmpty() && stack.getItem() instanceof QuiverItem){
				ItemStack result = this.getResultItem().copy();
				result.setTag(stack.getTag());
				if(stack.hasCustomHoverName())
					result.setHoverName(stack.getHoverName());
				for(Map.Entry<Enchantment,Integer> enchant : EnchantmentHelper.getEnchantments(stack).entrySet())
					result.enchant(enchant.getKey(), enchant.getValue());
				QuiverInventory oldInventory = QuiverHolderAttacher.getQuiverHolderUnwrap(stack).getInventory();
				for (int slot = 0; slot < oldInventory.getStacks().size(); slot++) {
					QuiverHolderAttacher.getQuiverHolderUnwrap(result).getInventory().insertItem(slot, oldInventory.getStackInSlot(slot), false);
				}
				return result;
			}
		}
		ItemStack quiverItem = this.getResultItem().copy();


		return quiverItem;
	}

	@Override
	public IRecipeSerializer<?> getSerializer(){
		return super.getSerializer();
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<QuiverRecipe> {
		@Override
		public QuiverRecipe fromJson(ResourceLocation recipeId, JsonObject json){
			ShapedRecipe recipe = IRecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
			return new QuiverRecipe(recipeId, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem());
		}

		@Nullable
		@Override
		public QuiverRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer){
			ShapedRecipe recipe = IRecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer);
			return new QuiverRecipe(recipeId, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem());
		}

		@Override
		public void toNetwork(PacketBuffer buffer, QuiverRecipe recipe){
			IRecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
		}
	}
	}

