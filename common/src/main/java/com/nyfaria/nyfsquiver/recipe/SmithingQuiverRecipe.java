package com.nyfaria.nyfsquiver.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.RecipeSerializerInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

import java.util.stream.Stream;

public class SmithingQuiverRecipe implements SmithingRecipe {
    final Ingredient template;
    final QuiverType base;
    final Ingredient addition;
    final QuiverType result;

    public SmithingQuiverRecipe(Ingredient pTemplate, QuiverType pBase, Ingredient pAddition, QuiverType pResult) {
        this.template = pTemplate;
        this.base = pBase;
        this.addition = pAddition;
        this.result = pResult;
    }

    public boolean matches(SmithingRecipeInput pInput, Level pLevel) {
        return this.template.test(pInput.template()) && this.base.equals(pInput.base().get(DataComponentInit.QUIVER_TYPE.get())) && this.addition.test(pInput.addition());
    }

    public ItemStack assemble(SmithingRecipeInput pInput, HolderLookup.Provider pRegistries) {
        ItemStack itemstack = pInput.base().copy();
        itemstack.set(DataComponentInit.QUIVER_TYPE.get(), this.result);
        return itemstack;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result.defaultStack();
    }

    @Override
    public boolean isTemplateIngredient(ItemStack pStack) {
        return this.template.test(pStack);
    }

    @Override
    public boolean isBaseIngredient(ItemStack pStack) {
        return this.base.equals(pStack.get(DataComponentInit.QUIVER_TYPE.get()));
    }

    @Override
    public boolean isAdditionIngredient(ItemStack pStack) {
        return this.addition.test(pStack);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.SMITHING_QUIVER.get();
    }

    @Override
    public boolean isIncomplete() {
        return Stream.of(this.template,this.addition).anyMatch(Ingredient::isEmpty);
    }

    public static class Serializer implements RecipeSerializer<SmithingQuiverRecipe> {
        private static final MapCodec<SmithingQuiverRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340782_ -> p_340782_.group(
                                Ingredient.CODEC.fieldOf("template").forGetter(p_301310_ -> p_301310_.template),
                                QuiverType.CODEC.fieldOf("base").forGetter(p_300938_ -> p_300938_.base),
                                Ingredient.CODEC.fieldOf("addition").forGetter(p_301153_ -> p_301153_.addition),
                                QuiverType.CODEC.fieldOf("result").forGetter(p_300935_ -> p_300935_.result)
                        )
                        .apply(p_340782_, SmithingQuiverRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, SmithingQuiverRecipe> STREAM_CODEC = StreamCodec.of(
                SmithingQuiverRecipe.Serializer::toNetwork, SmithingQuiverRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<SmithingQuiverRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SmithingQuiverRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SmithingQuiverRecipe fromNetwork(RegistryFriendlyByteBuf p_320375_) {
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(p_320375_);
            QuiverType ingredient1 = QuiverType.STREAM_CODEC.decode(p_320375_);
            Ingredient ingredient2 = Ingredient.CONTENTS_STREAM_CODEC.decode(p_320375_);
            QuiverType itemstack = QuiverType.STREAM_CODEC.decode(p_320375_);
            return new SmithingQuiverRecipe(ingredient, ingredient1, ingredient2, itemstack);
        }

        private static void toNetwork(RegistryFriendlyByteBuf p_320743_, SmithingQuiverRecipe p_319840_) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(p_320743_, p_319840_.template);
            QuiverType.STREAM_CODEC.encode(p_320743_, p_319840_.base);
            Ingredient.CONTENTS_STREAM_CODEC.encode(p_320743_, p_319840_.addition);
            QuiverType.STREAM_CODEC.encode(p_320743_, p_319840_.result);
        }
    }
}
