package com.nyfaria.nyfsquiver.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(PacketBuffer.class)
public abstract class EpicFightModMixin {


    @Shadow @Nullable public abstract CompoundNBT readNbt();

    @Shadow public abstract byte readByte();

    @Shadow public abstract int readVarInt();

    @Shadow public abstract boolean readBoolean();

    @Inject(method = "readItem", at=@At("HEAD"),cancellable = true)
    private void fixYoShitEpicFightMod(CallbackInfoReturnable<ItemStack> cir){
        if(ModList.get().isLoaded("epicfight")) {
            if (!this.readBoolean()) {
                cir.setReturnValue(ItemStack.EMPTY);
            } else {
                int i = this.readVarInt();
                int j = this.readByte();
                ItemStack itemstack = new ItemStack(Item.byId(i), j);
                itemstack.readShareTag(this.readNbt());
                cir.setReturnValue(itemstack);
            }
        }
    }
}
