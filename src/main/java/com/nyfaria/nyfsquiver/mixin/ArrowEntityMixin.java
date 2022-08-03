package com.nyfaria.nyfsquiver.mixin;

import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;


@Mixin(AbstractArrow.class)
public abstract class ArrowEntityMixin extends Projectile {

    @Shadow
    public int shakeTime;
    @Shadow
    public AbstractArrow.Pickup pickup;
    @Shadow
    protected boolean inGround;


    protected ArrowEntityMixin(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    @Shadow
    protected abstract ItemStack getPickupItem();

    @Shadow
    public abstract boolean isNoPhysics();


//    @Inject(method = "tryPickup", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
//    public void addToQuiver(Player player, CallbackInfoReturnable<Boolean> cir) {
//        if (!getPickupItem().is(TagInit.QUIVER_ITEMS)) return;
//        ItemStack quiverStack = CuriosApi.getCuriosHelper().findFirstCurio(player, item -> item.getItem() instanceof QuiverItem).orElseThrow().stack();
//        if (quiverStack.isEmpty()) {
//            return;
//        } else {
//            ItemStack stack = QuiverItem.getInventory(quiverStack).insertItem(0, getPickupItem(), false);
//            if(stack.isEmpty()) {
//                cir.setReturnValue(true);
//            }
//        }
//    }


    /**
     * @author Nyfaria
     * @reason pick up arrows
     */
    @Overwrite
    public void playerTouch(Player player) {

        if (this.pickup == AbstractArrow.Pickup.ALLOWED && !level.isClientSide && (this.inGround || this.isNoPhysics()) && this.shakeTime <= 0) {
            boolean flag;
            ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem, player)
                    .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
            if (!quiverStack.isEmpty()) {
                ItemStack stack = QuiverHolderAttacher.getQuiverHolderUnwrap(quiverStack).getInventory().getStackInSlot(quiverStack.getOrCreateTag().getInt(""));
                if (stack.getItem() == this.getPickupItem().getItem() && stack.getCount() < 64 && !quiverStack.isEmpty()) {
                    stack.setCount(stack.getCount() + 1);
                    this.remove(RemovalReason.DISCARDED);
                    return;
                }

                if ((this.getPickupItem().is(TagInit.QUIVER_ITEMS)) && !quiverStack.isEmpty()) {
                    QuiverInventory boop = QuiverItem.getInventory(quiverStack);
                    for (int i = 0; i < boop.getSlots(); i++) {
                        if (boop.getStackInSlot(i).getItem() == this.getPickupItem().getItem() && boop.getStackInSlot(i).getCount() < 64) {
                            boop.getStackInSlot(i).setCount(boop.getStackInSlot(i).getCount() + 1);
                            this.remove(RemovalReason.DISCARDED);
                            return;
                        }

                        if (boop.getStackInSlot(i).isEmpty()) {
                            boop.setStackInSlot(i, this.getPickupItem());
                            this.remove(RemovalReason.DISCARDED);
                            return;
                        }
                    }
                }
            }
            flag = (this.pickup == AbstractArrow.Pickup.ALLOWED ||
                    this.pickup == AbstractArrow.Pickup.CREATIVE_ONLY
                            && player.getAbilities().instabuild || this.isNoPhysics() &&
                    this.getOwner().getUUID() == player.getUUID());
            if (this.pickup == AbstractArrow.Pickup.ALLOWED
                    && !player.getInventory().add(this.getPickupItem())) {
                flag = false;
            }

            if (flag) {
                player.take(this, 1);
                this.remove(RemovalReason.DISCARDED);
            }

        }

    }

}