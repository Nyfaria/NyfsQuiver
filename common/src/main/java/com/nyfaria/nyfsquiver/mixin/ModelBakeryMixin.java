package com.nyfaria.nyfsquiver.mixin;

import com.google.common.collect.Sets;
import com.nyfaria.nyfsquiver.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Shadow @Final private Map<ResourceLocation, UnbakedModel> unbakedCache;
    @Shadow @Final private  Map<ResourceLocation, UnbakedModel> topLevelModels;

    @Shadow public abstract UnbakedModel getModel(ResourceLocation modelLocation);

    @Shadow protected abstract void registerModelAndLoadDependencies(ModelResourceLocation modelLocation, UnbakedModel model);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Collection;forEach(Ljava/util/function/Consumer;)V"))
    private void zenith$initCustomModels(BlockColors blockColors, ProfilerFiller profilerFiller, Map map, Map map2, CallbackInfo ci){
        Set<ModelResourceLocation> additionalModels = new HashSet<>();
        Set<ResourceLocation> locs = Minecraft.getInstance().getResourceManager().listResources("models", loc -> loc.getPath().contains("/quiver/") && loc.getPath().endsWith(".json"))
                .keySet();
        for (ResourceLocation s : locs) {
            String path = s.getPath().substring("models/".length(), s.getPath().length() - ".json".length());
            additionalModels.add(new ModelResourceLocation(Constants.loc(s.getNamespace(), path), "standalone"));
        }
        for (ModelResourceLocation rl : additionalModels) {
            UnbakedModel unbakedmodel = this.getModel(rl.id());
            this.registerModelAndLoadDependencies(rl, unbakedmodel);
        }

    }
}