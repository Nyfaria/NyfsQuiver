package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.recipe.QuiverUpgradeRecipe;
import com.nyfaria.nyfsquiver.recipe.SmithingQuiverRecipe;
import com.nyfaria.nyfsquiver.registration.RegistrationProvider;
import com.nyfaria.nyfsquiver.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RecipeSerializerInit {
    public static RegistrationProvider<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistrationProvider.get(Registries.RECIPE_SERIALIZER, Constants.MODID);
    public static RegistryObject<RecipeSerializer<?>, RecipeSerializer<QuiverUpgradeRecipe>> QUIVER = RECIPE_SERIALIZERS.register("shaped_quiver", QuiverUpgradeRecipe.Serializer::new);
    public static RegistryObject<RecipeSerializer<?>, RecipeSerializer<SmithingQuiverRecipe>> SMITHING_QUIVER = RECIPE_SERIALIZERS.register("smithing_quiver", SmithingQuiverRecipe.Serializer::new);

    public static void loadClass() {
    }
}
