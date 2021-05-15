package com.nyfaria.nyfsquiver.compat.curios;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Optional;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import com.nyfaria.nyfsquiver.common.items.QuiverItem;
import com.nyfaria.nyfsquiver.common.CommonProxy;

import top.theillusivec4.curios.api.CuriosApi;

public class CuriosOn extends CuriosOff {
	
    @Override
    public boolean isLoaded(){
        return true;
    }

    @Override
    public boolean increaseQuiverSlot(PlayerEntity player, int Direction){
        Optional<ImmutableTriple<String,Integer,ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() 
    	  instanceof QuiverItem, player);
        optional.ifPresent(triple ->
            CommonProxy.nextSlot(triple.getRight(), player, 1)
        );
        return optional.isPresent(); 
    }
    @Override
    public boolean decreaseQuiverSlot(PlayerEntity player, int Direction){
        Optional<ImmutableTriple<String,Integer,ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() 
    	  instanceof QuiverItem, player);
        optional.ifPresent(triple ->
            CommonProxy.nextSlot(triple.getRight(), player, -1)
        );
        return optional.isPresent(); 
    }
}
