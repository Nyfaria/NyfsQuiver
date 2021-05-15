package com.nyfaria.nyfsquiver.compat.curios;

import net.minecraft.entity.player.PlayerEntity;

public class CuriosOff {
	
    public boolean isLoaded(){
        return false;
    }

    public boolean increaseQuiverSlot(PlayerEntity player, int Direction){
        return false;
    }

	public boolean decreaseQuiverSlot(PlayerEntity player, int Direction) {
		return false;
	}

} 
