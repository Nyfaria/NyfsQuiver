package com.nyfaria.nyfsquiver.client.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record QuiverTypeProperty() implements SelectItemModelProperty<Identifier> {
    public static final SelectItemModelProperty.Type<QuiverTypeProperty, Identifier> TYPE = SelectItemModelProperty.Type.create(MapCodec.unit(new QuiverTypeProperty()), Identifier.CODEC);

    @Override
    public @Nullable Identifier get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        QuiverType type = itemStack.get(DataComponentInit.QUIVER_TYPE.get());
        return type != null ? type.name() : null;
    }

    @Override
    public Codec<Identifier> valueCodec() {
        return Identifier.CODEC;
    }

    @Override
    public SelectItemModelProperty.Type<QuiverTypeProperty, Identifier> type() {
        return TYPE;
    }
}
