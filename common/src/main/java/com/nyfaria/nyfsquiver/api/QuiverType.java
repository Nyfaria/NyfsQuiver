package com.nyfaria.nyfsquiver.api;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nyfaria.nyfsquiver.init.ArrowActionInit;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.QuiverInit;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record QuiverType(ResourceLocation name, int rows, int columns, boolean fireProof, int durability, ArrowAction arrowAction) {
    public static Codec<QuiverType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("name").forGetter(QuiverType::name),
            Codec.INT.fieldOf("rows").forGetter(QuiverType::rows),
            Codec.INT.fieldOf("columns").forGetter(QuiverType::columns),
            Codec.BOOL.fieldOf("fireProof").forGetter(QuiverType::fireProof),
            Codec.INT.fieldOf("durability").forGetter(QuiverType::durability),
            Codec.lazyInitialized(()->ArrowActionInit.ARROW_ACTIONS.getRegistry().byNameCodec()).fieldOf("arrowAction").forGetter(QuiverType::arrowAction)
    ).apply(instance, QuiverType::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, QuiverType> STREAM_CODEC = new StreamCodec<>() {
        public QuiverType decode(RegistryFriendlyByteBuf rfbb) {
            return rfbb.readJsonWithCodec(CODEC);
        }

        public void encode(RegistryFriendlyByteBuf registryFriendlyByteBuf, QuiverType quiverType) {
            registryFriendlyByteBuf.writeJsonWithCodec(CODEC, quiverType);
        }
    };

    public static QuiverType byLoc(ResourceLocation name) {
        return QuiverInit.getRegistry(RegistryAccessAccess.ACCESS).get(name);
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
