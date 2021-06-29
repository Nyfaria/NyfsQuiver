package com.nyfaria.nyfsquiver.mixin;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity.PickupStatus;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ArrowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.common.items.QuiverInventory;
import com.nyfaria.nyfsquiver.common.items.QuiverItem;
import com.nyfaria.nyfsquiver.common.items.QuiverStorageManager;

import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;


@Mixin(AbstractArrowEntity.class)
public abstract class ArrowEntityMixin
{

	@Shadow
	protected boolean inGround;
	@Shadow
	protected abstract ItemStack getPickupItem();


	@Overwrite
	public void playerTouch(PlayerEntity p_70100_1_) 
	{
		
	      if (((AbstractArrowEntity)(Object)this).pickup == AbstractArrowEntity.PickupStatus.ALLOWED && !((AbstractArrowEntity)(Object)this).level.isClientSide && (this.inGround || ((AbstractArrowEntity)(Object)this).isNoPhysics()) && ((AbstractArrowEntity)(Object)this).shakeTime <= 0) {
	    	  boolean flag;
	    	  ItemStack stack = CuriosApi.getCuriosHelper().findEquippedCurio(NyfsQuiver.arrow_predicate,p_70100_1_)
					.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
	    	  ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem,p_70100_1_)
					.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
	    	  if(stack.getItem() == this.getPickupItem().getItem() && stack.getCount() < 64 && !quiverStack.isEmpty()) {
		    	  stack.setCount(stack.getCount()+ 1);
		    	  ((AbstractArrowEntity)(Object)this).remove();
		    	  return;
	    	  }
	    	  if(!(this.getPickupItem().getItem() instanceof ArrowItem) && stack.isEmpty() && !quiverStack.isEmpty()) {
	    		  CuriosApi.getCuriosHelper().getCuriosHandler(p_70100_1_).map(ICuriosItemHandler::getCurios)
	    		  .map(stringICurioStacksHandlerMap -> stringICurioStacksHandlerMap.get("arrows"))
	    		  .map(ICurioStacksHandler::getStacks)
	    		  .ifPresent(curioStackHandler -> curioStackHandler.setStackInSlot(0,this.getPickupItem()));
	    		  ((AbstractArrowEntity)(Object)this).remove();
	    		  return;
	    	  }
	    	  if(!(this.getPickupItem().getItem() instanceof ArrowItem) && !quiverStack.isEmpty()) {
	    		  QuiverInventory boop = QuiverStorageManager.getInventory(quiverStack.getOrCreateTag().getInt("nyfsquiver:invIndex"));
	    		for(int i = 0; i < boop.getSlots(); i++) {
	    			if(i != quiverStack.getOrCreateTag().getInt("nyfsquiver:slotIndex")) {
	    				if(boop.getStackInSlot(i).getItem() == this.getPickupItem().getItem() && boop.getStackInSlot(i).getCount() < 64) 
	    				{
	    					boop.getStackInSlot(i).setCount(boop.getStackInSlot(i).getCount() + 1);
	    					((AbstractArrowEntity)(Object)this).remove();
	    					break;
	    				} 
	    				
	    				if(boop.getStackInSlot(i).isEmpty()) 
	    				{
	    					boop.setStackInSlot(i, this.getPickupItem());
	    					((AbstractArrowEntity)(Object)this).remove();
	    					break;
	    				}
	    			}
	    		}
	    		  
	    		  
	    	  }
	    	  flag = ((AbstractArrowEntity)(Object)this).pickup == AbstractArrowEntity.PickupStatus.ALLOWED || 
	    			  ((AbstractArrowEntity)(Object)this).pickup == AbstractArrowEntity.PickupStatus.CREATIVE_ONLY 
	    			  && p_70100_1_.abilities.instabuild || ((AbstractArrowEntity)(Object)this).isNoPhysics() && 
	    			  ((AbstractArrowEntity)(Object)this).getOwner().getUUID() == p_70100_1_.getUUID();
	    	  if (((AbstractArrowEntity)(Object)this).pickup == AbstractArrowEntity.PickupStatus.ALLOWED 
	    			  && !p_70100_1_.inventory.add(this.getPickupItem())) {
	    		  flag = false;
	    	  }
	    	  
	    	  if (flag) {
	    		  p_70100_1_.take(((AbstractArrowEntity)(Object)this), 1);
	    		  ((AbstractArrowEntity)(Object)this).remove();
	    	  }

	      }

	}

}