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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleSmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class SmithingQuiverRecipe extends SimpleSmithingRecipe {
    public static final MapCodec<SmithingQuiverRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                            Ingredient.CODEC.fieldOf("template").forGetter(o -> o.template),
                            QuiverType.CODEC.fieldOf("base").forGetter(o -> o.base),
                            Ingredient.CODEC.fieldOf("addition").forGetter(o -> o.addition),
                            QuiverType.CODEC.fieldOf("result").forGetter(o -> o.result)
                    )
                    .apply(i, SmithingQuiverRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SmithingQuiverRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.template,
            QuiverType.STREAM_CODEC,
            o -> o.base,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.addition,
            QuiverType.STREAM_CODEC,
            o -> o.result,
            SmithingQuiverRecipe::new
    );
    public static final RecipeSerializer<SmithingQuiverRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final Ingredient template;
    private final QuiverType base;
    private final Ingredient addition;
    private final QuiverType result;

    public SmithingQuiverRecipe(Recipe.CommonInfo commonInfo, Ingredient pTemplate, QuiverType pBase, Ingredient pAddition, QuiverType pResult) {
        super(commonInfo);
        this.template = pTemplate;
        this.base = pBase;
        this.addition = pAddition;
        this.result = pResult;
    }

    @Override
    public boolean matches(SmithingRecipeInput pInput, Level pLevel) {
        return this.template.test(pInput.template()) && this.base.equals(pInput.base().get(DataComponentInit.QUIVER_TYPE.get())) && this.addition.test(pInput.addition());
    }

    public ItemStack assemble(SmithingRecipeInput pInput) {
        ItemStack itemstack = pInput.base().copy();
        itemstack.set(DataComponentInit.QUIVER_TYPE.get(), this.result);
        return itemstack;
    }

    @Override
    public Optional<Ingredient> templateIngredient() {
        return Optional.of(this.template);
    }

    @Override
    public Ingredient baseIngredient() {
        return Ingredient.of(ItemInit.QUIVER.get());
    }

    @Override
    public Optional<Ingredient> additionIngredient() {
        return Optional.of(this.addition);
    }

    @Override
    public RecipeSerializer<SmithingQuiverRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(List.of(Optional.of(this.template), Optional.of(this.baseIngredient()), Optional.of(this.addition)));
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new SmithingRecipeDisplay(
                        this.template.display(),
                        new SlotDisplay.ItemStackSlotDisplay(this.base.defaultTemplate()),
                        this.addition.display(),
                        new SlotDisplay.ItemStackSlotDisplay(this.result.defaultTemplate()),
                        new SlotDisplay.ItemSlotDisplay(Items.SMITHING_TABLE)
                )
        );
    }
}
