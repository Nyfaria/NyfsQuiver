package com.nyfaria.nyfsquiver.mixin;


import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.init.TagInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ArrowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import com.nyfaria.nyfsquiver.items.QuiverStorageManager;

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


	@Shadow public AbstractArrowEntity.PickupStatus pickup;

	@Shadow public abstract boolean isNoPhysics();

	@Shadow public int shakeTime;

	/**
	 * @author
	 */
	@Overwrite
	public void playerTouch(PlayerEntity player)
	{

		if (this.pickup == AbstractArrowEntity.PickupStatus.ALLOWED && !((AbstractArrowEntity)(Object)this).level.isClientSide && (this.inGround || this.isNoPhysics()) && this.shakeTime <= 0) {
			boolean flag;
			ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem,player)
					.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
			if(!quiverStack.isEmpty()) {
				ItemStack stack = QuiverHolderAttacher.getQuiverHolderUnwrap(quiverStack).getInventory().getStackInSlot(quiverStack.getOrCreateTag().getInt(""));
				if (stack.getItem() == this.getPickupItem().getItem() && stack.getCount() < 64 && !quiverStack.isEmpty()) {
					stack.setCount(stack.getCount() + 1);
					((AbstractArrowEntity)(Object)this).remove();
					return;
				}

				if ((this.getPickupItem().getItem().is(TagInit.QUIVER_ITEMS)) && !quiverStack.isEmpty()) {
					QuiverInventory boop = QuiverItem.getInventory(quiverStack);
					for (int i = 0; i < boop.getSlots(); i++) {
						if (boop.getStackInSlot(i).getItem() == this.getPickupItem().getItem() && boop.getStackInSlot(i).getCount() < 64) {
							boop.getStackInSlot(i).setCount(boop.getStackInSlot(i).getCount() + 1);
							((AbstractArrowEntity)(Object)this).remove();
							return;
						}

						if (boop.getStackInSlot(i).isEmpty()) {
							boop.setStackInSlot(i, this.getPickupItem());
							((AbstractArrowEntity)(Object)this).remove();
							return;
						}
					}
				}
			}
			flag = (this.pickup == AbstractArrowEntity.PickupStatus.ALLOWED ||
					this.pickup == AbstractArrowEntity.PickupStatus.CREATIVE_ONLY
							&& player.abilities.instabuild || this.isNoPhysics() &&
					((AbstractArrowEntity)(Object)this).getOwner().getUUID() == player.getUUID());
			if (this.pickup == AbstractArrowEntity.PickupStatus.ALLOWED
					&& !player.inventory.add(this.getPickupItem())) {
				flag = false;
			}

			if (flag) {
				player.take((AbstractArrowEntity)(Object)this, 1);
				((AbstractArrowEntity)(Object)this).remove();
			}

		}

	}

}