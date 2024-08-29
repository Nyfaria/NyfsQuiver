package com.nyfaria.nyfsquiver.init;

import com.mojang.serialization.Codec;
import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.api.ArrowAction;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.item.component.QuiverContainerContents;
import com.nyfaria.nyfsquiver.registration.RegistrationProvider;
import com.nyfaria.nyfsquiver.registration.RegistryObject;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;

public class DataComponentInit {
    public static RegistrationProvider<DataComponentType<?>> DATA_COMPONENTS = RegistrationProvider.get(BuiltInRegistries.DATA_COMPONENT_TYPE, Constants.MODID);

    public static final RegistryObject<DataComponentType<?>,DataComponentType<QuiverType>> QUIVER_TYPE = DATA_COMPONENTS.register("quiver_type", ()-> DataComponentType.<QuiverType>builder()
            .persistent(QuiverType.CODEC)
            .networkSynchronized(ByteBufCodecs.fromCodecWithRegistries(QuiverType.CODEC))
            .build());
    public static final RegistryObject<DataComponentType<?>,DataComponentType<ArrowAction>> ARROW_ACTION = DATA_COMPONENTS.register("arrow_action", ()-> DataComponentType.<ArrowAction>builder()
            .persistent(ArrowActionInit.ACTION_REGISTRY.byNameCodec())
            .networkSynchronized(ByteBufCodecs.fromCodecWithRegistries(ArrowActionInit.ACTION_REGISTRY.byNameCodec()))
            .build());
    public static final RegistryObject<DataComponentType<?>, DataComponentType<QuiverContainerContents>> QUIVER_CONTENTS = DATA_COMPONENTS.register("quiver_contents", ()-> DataComponentType.<QuiverContainerContents>builder()
            .persistent(QuiverContainerContents.CODEC)
            .networkSynchronized(QuiverContainerContents.STREAM_CODEC)
            .build());
    public static final RegistryObject<DataComponentType<?>, DataComponentType<Integer>> CURRENT_SLOT = DATA_COMPONENTS.register("current_slot", ()-> DataComponentType.<Integer>builder()
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.INT)
            .build());

    public static void loadClass() {
    }
}
