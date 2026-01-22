package com.nyfaria.nyfsquiver.codec;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record QuiverFabricCodec(ItemStack stack) {
    public static final StreamCodec<RegistryFriendlyByteBuf, QuiverFabricCodec> CODEC =
            StreamCodec.composite(
                    ItemStack.STREAM_CODEC,
                    QuiverFabricCodec::stack,
                    QuiverFabricCodec::new
            );
}
