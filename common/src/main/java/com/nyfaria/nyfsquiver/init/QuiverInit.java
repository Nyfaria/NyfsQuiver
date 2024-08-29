package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.registration.registries.DatapackRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class QuiverInit {
    public static DatapackRegistry<QuiverType> QUIVER_TYPES = DatapackRegistry.<QuiverType>builder(Constants.modLoc( "quiver_type"))
            .withElementCodec(QuiverType.CODEC)
            .withNetworkCodec(QuiverType.CODEC)
            .withBootstrap(QuiverInit::quiverTypesBootstrap)
            .build();

    public static void quiverTypesBootstrap(BootstrapContext<QuiverType> context) {
        context.register(Constants.LEATHER_QUIVER, new QuiverType( Constants.modLoc("leather"),3,9, false, 576, ArrowActionInit.VANILLA.get()));
        context.register(Constants.IRON_QUIVER, new QuiverType( Constants.modLoc("iron"),4,9, false, 1152, ArrowActionInit.VANILLA.get()));
        context.register(Constants.GOLD_QUIVER, new QuiverType( Constants.modLoc("gold"),5,9, false, 2304, ArrowActionInit.VANILLA.get()));
        context.register(Constants.DIAMOND_QUIVER, new QuiverType( Constants.modLoc("diamond"),6,9, false, 4608, ArrowActionInit.VANILLA.get()));
        context.register(Constants.NETHERITE_QUIVER, new QuiverType( Constants.modLoc("netherite"),7,9, false, 9216, ArrowActionInit.VANILLA.get()));
    }

    public static Registry<QuiverType> getRegistry(RegistryAccess registryAccess) {
        return QUIVER_TYPES.get(registryAccess);
    }
    public static ResourceKey<QuiverType> createKey(ResourceLocation name) {
        return ResourceKey.create(QUIVER_TYPES.key(), name);
    }

    public static void loadClass() {
    }
}
