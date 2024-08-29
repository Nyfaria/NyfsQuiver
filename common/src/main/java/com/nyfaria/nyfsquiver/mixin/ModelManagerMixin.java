package com.nyfaria.nyfsquiver.mixin;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.client.model.QuiverModel;
import net.minecraft.client.resources.model.AtlasSet;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ModelManager.class)
public class ModelManagerMixin {

    @Inject(method = "loadModels", at = @At(value = "INVOKE", target = "net/minecraft/util/profiling/ProfilerFiller.popPush (Ljava/lang/String;)V", ordinal = 1))
    private void loadModels$Inject$AfterLoadModels(ProfilerFiller pProfilerFiller, Map<ResourceLocation, AtlasSet.StitchResult> pAtlasPreparations, ModelBakery pModelBakery, CallbackInfoReturnable<ModelManager.ReloadState> cir) {
        ModelResourceLocation key = new ModelResourceLocation(Constants.modLoc("quiver"), "inventory");
        BakedModel oldModel = pModelBakery.getBakedTopLevelModels().get(key);
        if (oldModel != null) {
            pModelBakery.getBakedTopLevelModels().put(key, new QuiverModel(oldModel, pModelBakery));
        }
    }
}
