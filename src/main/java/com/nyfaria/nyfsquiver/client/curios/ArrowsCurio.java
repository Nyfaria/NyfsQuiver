package com.nyfaria.nyfsquiver.client.curios;

import net.minecraft.entity.LivingEntity;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class ArrowsCurio implements ICurio {

	@Override
	public DropRule getDropRule(LivingEntity livingEntity) {
	    return DropRule.ALWAYS_KEEP;
	}
	
	@Override
	public boolean canEquip(String identifier, LivingEntity livingEntity) {
		return false;
	}

	@Override
	public boolean canUnequip(String identifier, LivingEntity livingEntity) {
		return false;
	}

	@Override
	public boolean canRightClickEquip() {
		return false;
	}
}
