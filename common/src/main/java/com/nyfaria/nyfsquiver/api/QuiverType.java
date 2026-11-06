package com.nyfaria.nyfsquiver.api;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nyfaria.nyfsquiver.init.ArrowActionInit;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.QuiverInit;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public record QuiverType(Identifier name, int rows, int columns, boolean fireProof, int durability, ArrowAction arrowAction) {
    public static Codec<QuiverType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("name").forGetter(QuiverType::name),
            Codec.INT.fieldOf("rows").forGetter(QuiverType::rows),
            Codec.INT.fieldOf("columns").forGetter(QuiverType::columns),
            Codec.BOOL.fieldOf("fireProof").forGetter(QuiverType::fireProof),
            Codec.INT.fieldOf("durability").forGetter(QuiverType::durability),
            Codec.lazyInitialized(()->ArrowActionInit.ARROW_ACTIONS.getRegistry().byNameCodec()).fieldOf("arrowAction").forGetter(QuiverType::arrowAction)
    ).apply(instance, QuiverType::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, QuiverType> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public static QuiverType byLoc(Identifier name) {
        return QuiverInit.getRegistry(RegistryAccessAccess.ACCESS).getValue(name);
    }

    public ItemStackTemplate defaultTemplate() {
        return new ItemStackTemplate(ItemInit.QUIVER.get(), DataComponentPatch.builder().set(DataComponentInit.QUIVER_TYPE.get(), this).build());
    }

    public ItemStack defaultStack(){
        ItemStack itemStack = new ItemStack(ItemInit.QUIVER.get());
        itemStack.set(DataComponentInit.QUIVER_TYPE.get(), this);
        return itemStack;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof QuiverType) {
            QuiverType quiverType = (QuiverType) obj;
            return quiverType.name.equals(this.name);
        }
        return false;
    }
    public String getTranslationKey(){
        return "quiver." + name.getNamespace() + "." + name.getPath();
    }
}
