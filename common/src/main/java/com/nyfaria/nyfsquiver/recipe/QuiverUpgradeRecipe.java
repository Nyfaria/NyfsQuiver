package com.nyfaria.nyfsquiver.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.RecipeSerializerInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;


public class QuiverUpgradeRecipe implements CraftingRecipe {
    final ShapedRecipePattern pattern;
    final QuiverType input;
    final QuiverType result;
    final String group;
    final CraftingBookCategory category;
    final boolean showNotification;

    public QuiverUpgradeRecipe(String pGroup, CraftingBookCategory pCategory, ShapedRecipePattern pPattern, QuiverType input, QuiverType pResult, boolean pShowNotification) {
        this.group = pGroup;
        this.category = pCategory;
        this.pattern = pPattern;
        this.input = input;
        this.result = pResult;
        this.showNotification = pShowNotification;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.QUIVER.get();
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return new ItemStack(ItemInit.QUIVER.get());
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.pattern.ingredients();
    }

    @Override
    public boolean showNotification() {
        return this.showNotification;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= this.pattern.width() && pHeight >= this.pattern.height();
    }

    public boolean matches(CraftingInput pInput, Level pLevel) {
        return this.pattern.matches(pInput);
    }

    public ItemStack assemble(CraftingInput pInput, HolderLookup.Provider pRegistries) {
        ItemStack itemstack = pInput.items().stream().filter(stack->stack.is(ItemInit.QUIVER.get())).findFirst().get();
        ItemStack result = itemstack.copy();
        result.set(DataComponentInit.QUIVER_TYPE.get(), this.result);
        return result;
    }

    public int getWidth() {
        return this.pattern.width();
    }

    public int getHeight() {
        return this.pattern.height();
    }

    @Override
    public boolean isIncomplete() {
        NonNullList<Ingredient> nonnulllist = this.getIngredients();
        return nonnulllist.isEmpty() || nonnulllist.stream().filter(p_151277_ -> !p_151277_.isEmpty()).anyMatch(p_151273_ -> p_151273_.getItems().length == 0);
    }

    public static class Serializer implements RecipeSerializer<QuiverUpgradeRecipe> {
        public static final MapCodec<QuiverUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340778_ -> p_340778_.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(p_311729_ -> p_311729_.group),
                                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_311732_ -> p_311732_.category),
                                ShapedRecipePattern.MAP_CODEC.forGetter(p_311733_ -> p_311733_.pattern),
                                QuiverType.CODEC.fieldOf("input_type").forGetter(p_311730_ -> p_311730_.input),
                                QuiverType.CODEC.fieldOf("result_type").forGetter(p_311730_ -> p_311730_.result),
                                Codec.BOOL.optionalFieldOf("show_notification", Boolean.valueOf(true)).forGetter(p_311731_ -> p_311731_.showNotification)
                        )
                        .apply(p_340778_, QuiverUpgradeRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, QuiverUpgradeRecipe> STREAM_CODEC = StreamCodec.of(
                QuiverUpgradeRecipe.Serializer::toNetwork, QuiverUpgradeRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<QuiverUpgradeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, QuiverUpgradeRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static QuiverUpgradeRecipe fromNetwork(RegistryFriendlyByteBuf p_319998_) {
            String s = p_319998_.readUtf();
            CraftingBookCategory craftingbookcategory = p_319998_.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(p_319998_);
            QuiverType input = QuiverType.STREAM_CODEC.decode(p_319998_);
            QuiverType result = QuiverType.STREAM_CODEC.decode(p_319998_);
            boolean flag = p_319998_.readBoolean();
            return new QuiverUpgradeRecipe(s, craftingbookcategory, shapedrecipepattern, input, result, flag);
        }

        private static void toNetwork(RegistryFriendlyByteBuf p_320738_, QuiverUpgradeRecipe p_320586_) {
            p_320738_.writeUtf(p_320586_.group);
            p_320738_.writeEnum(p_320586_.category);
            ShapedRecipePattern.STREAM_CODEC.encode(p_320738_, p_320586_.pattern);
            QuiverType.STREAM_CODEC.encode(p_320738_, p_320586_.input);
            QuiverType.STREAM_CODEC.encode(p_320738_, p_320586_.result);
            p_320738_.writeBoolean(p_320586_.showNotification);
        }
    }
}
