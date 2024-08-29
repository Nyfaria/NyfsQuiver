package com.nyfaria.nyfsquiver.codec;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record QuiverFabricCodec(CompoundTag stack) {
    public static final StreamCodec<RegistryFriendlyByteBuf, QuiverFabricCodec> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.COMPOUND_TAG,
                    QuiverFabricCodec::stack,
                    QuiverFabricCodec::new
            );
}
