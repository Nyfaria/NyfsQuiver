package com.nyfaria.nyfsquiver.mixin;


import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.init.TagInit;

import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import com.nyfaria.nyfsquiver.items.QuiverStorageManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Predicate;


@Mixin(Player.class)
public class PlayerEntityMixin extends LivingEntity
{
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
		super(p_20966_, p_20967_);
	}

	@Inject(method = "getProjectile",at = @At(value="HEAD"),cancellable = true)
	private void checkQuiver(ItemStack shootable, CallbackInfoReturnable<ItemStack> cir)
	{
		//if(level.isClientSide())
		//	return;
		
		if (!(shootable.getItem() instanceof ProjectileWeaponItem)) {
			return;
		}
		Predicate<ItemStack> predicate = ((ProjectileWeaponItem)shootable.getItem()).getSupportedHeldProjectiles();
		ItemStack itemStack;
		if(!CuriosApi.getCuriosHelper().findEquippedCurio(NyfsQuiver.QUIVER_PREDICATE,(Player)(Object)this).isPresent())
			return;

		ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(NyfsQuiver.QUIVER_PREDICATE,(Player)(Object)this).get().right;
		if(quiverStack.isEmpty()){
			return;
		}

		QuiverInventory quiverInventory = QuiverItem.getInventory(quiverStack);
		itemStack = quiverInventory.getStackInSlot(QuiverHolderAttacher.getQuiverHolderUnwrap(quiverStack).getCurrentSlot());
//		ItemStack stack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.is(TagInit.QUIVER_ITEMS),(Player)(Object)this)
//				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);


		if (predicate.test(itemStack)) {
			if(level.isClientSide()){
				cir.setReturnValue(ItemStack.EMPTY);
			}else {
				cir.setReturnValue(itemStack);
			}
		}
		
	}

	@Shadow
	public Iterable<ItemStack> getArmorSlots() {
		return null;
	}

	@Shadow
	public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
		return null;
	}

	@Shadow
	public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

	}

	@Shadow
	public HumanoidArm getMainArm() {
		return null;
	}
}