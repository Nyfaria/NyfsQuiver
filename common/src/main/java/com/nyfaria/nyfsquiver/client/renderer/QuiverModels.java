package com.nyfaria.nyfsquiver.client.renderer;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.api.QuiverType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolves the baked models that are drawn on the wearer ("standalone" models registered through
 * {@code ModelEvent.RegisterAdditional} on NeoForge and the model loader on Fabric).
 */
public final class QuiverModels {

    private static final Map<ResourceLocation, ModelResourceLocation> CACHE = new HashMap<>();

    private QuiverModels() {}

    /**
     * @param slot      {@code back} or {@code hip}
     * @param type      the quiver type of the stack
     * @param hasArrows whether the currently selected slot holds ammunition
     */
    public static ModelResourceLocation location(String slot, QuiverType type, boolean hasArrows) {
        String arrows = hasArrows ? "" : "_noarrows";
        return CACHE.computeIfAbsent(type.name().withSuffix("_" + slot + arrows), key ->
                new ModelResourceLocation(
                        Constants.loc(key.getNamespace(), "quiver/" + slot + "/" + type.name().getPath() + arrows),
                        "standalone"
                ));
    }

    public static BakedModel baked(String slot, QuiverType type, boolean hasArrows) {
        return Minecraft.getInstance().getModelManager().getModel(location(slot, type, hasArrows));
    }
}
