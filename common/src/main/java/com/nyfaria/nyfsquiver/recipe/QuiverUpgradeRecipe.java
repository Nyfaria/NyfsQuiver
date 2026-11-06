package com.nyfaria.nyfsquiver.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.NormalCraftingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

import java.util.List;

public class QuiverUpgradeRecipe extends NormalCraftingRecipe {
    public static final MapCodec<QuiverUpgradeRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
                            ShapedRecipePattern.MAP_CODEC.forGetter(o -> o.pattern),
                            QuiverType.CODEC.fieldOf("input_type").forGetter(o -> o.input),
                            QuiverType.CODEC.fieldOf("result_type").forGetter(o -> o.result)
                    )
                    .apply(i, QuiverUpgradeRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, QuiverUpgradeRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC,
            o -> o.bookInfo,
            ShapedRecipePattern.STREAM_CODEC,
            o -> o.pattern,
            QuiverType.STREAM_CODEC,
            o -> o.input,
            QuiverType.STREAM_CODEC,
            o -> o.result,
            QuiverUpgradeRecipe::new
    );
    public static final RecipeSerializer<QuiverUpgradeRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final ShapedRecipePattern pattern;
    private final QuiverType input;
    private final QuiverType result;

    public QuiverUpgradeRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ShapedRecipePattern pattern, QuiverType input, QuiverType result) {
        super(commonInfo, bookInfo);
        this.pattern = pattern;
        this.input = input;
        this.result = result;
    }

    @Override
    public RecipeSerializer<QuiverUpgradeRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(this.pattern.ingredients());
    }

    public boolean matches(CraftingInput input, Level level) {
        if (!this.pattern.matches(input)) {
            return false;
        }
        return input.items().stream()
                .filter(stack -> stack.is(ItemInit.QUIVER.get()))
                .findFirst()
                .map(stack -> this.input.equals(stack.get(DataComponentInit.QUIVER_TYPE.get())))
                .orElse(false);
    }

    public ItemStack assemble(CraftingInput input) {
        ItemStack itemstack = input.items().stream().filter(stack -> stack.is(ItemInit.QUIVER.get())).findFirst().orElse(ItemStack.EMPTY);
        ItemStack result = itemstack.copy();
        result.set(DataComponentInit.QUIVER_TYPE.get(), this.result);
        return result;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new ShapedCraftingRecipeDisplay(
                        this.pattern.width(),
                        this.pattern.height(),
                        this.pattern.ingredients().stream().map(e -> e.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(),
                        new SlotDisplay.ItemStackSlotDisplay(this.result.defaultTemplate()),
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
                )
        );
    }
}
