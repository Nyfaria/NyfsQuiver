package com.nyfaria.nyfsquiver.client.model;

import com.mojang.serialization.MapCodec;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.menu.QuiverContainer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record HasArrowsProperty() implements ConditionalItemModelProperty {
    public static final MapCodec<HasArrowsProperty> MAP_CODEC = MapCodec.unit(new HasArrowsProperty());

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        int slot = itemStack.getOrDefault(DataComponentInit.CURRENT_SLOT.get(), 0);
        return !new QuiverContainer(itemStack).getItem(slot).isEmpty();
    }

    @Override
    public MapCodec<HasArrowsProperty> type() {
        return MAP_CODEC;
    }
}
