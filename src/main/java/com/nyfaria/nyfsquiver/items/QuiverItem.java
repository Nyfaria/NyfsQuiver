package com.nyfaria.nyfsquiver.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.cap.QuiverHolder;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.events.ClientModEvents;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.tooltip.QuiverTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.antlr.v4.runtime.misc.NotNull;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;


public class QuiverItem extends Item implements ICurioItem, Wearable {

	public QuiverType type;

//	@Nullable
//	@Override
//	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
//		return new ICapabilityProvider().;
//	}

	public QuiverItem(QuiverType type) {
		super(new Item.Properties().stacksTo(1).tab(NyfsQuiver.ITEM_GROUP));
		this.type = type;
		//inventory = new QuiverInventory(false, 0, type.getRows(),type.getColumns());

	}

	public static QuiverInventory getInventory(ItemStack itemStack){
		return QuiverHolderAttacher.getQuiverHolderUnwrap(itemStack).getInventory();
	}
	@Override
	public boolean isEnchantable(ItemStack p_41456_) {
		return true;
	}

	@Override
	public int getEnchantmentValue() {
		return 1;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@NotNull ItemStack stack, Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {

		} else {
			tooltip.add(new TranslatableComponent("Hold " + "\u00A7e" + "Shift" + "\u00A77" + " for More Information"));
		}
	}

	@Override
	public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {

		ItemStack stack = playerIn.getItemInHand(handIn);
		if (!playerIn.isShiftKeyDown()) {
			if (!worldIn.isClientSide() && stack.getItem() instanceof QuiverItem) {
				int bagSlot = handIn == InteractionHand.MAIN_HAND ? playerIn.getInventory().selected : -1;
				//QuiverInventory.openQuiverInventory(stack, playerIn, bagSlot);
				NetworkHooks.openGui((ServerPlayer) playerIn, new QuiverItem.ContainerProvider(stack.getDisplayName(), bagSlot, getInventory(stack)), a -> {
					a.writeInt(bagSlot);
					a.writeInt(getInventory(stack).rows);
					a.writeInt(getInventory(stack).columns);
					a.writeNbt(getInventory(stack).serializeNBT());
				});
			}
		} else if (worldIn.isClientSide) {
			ClientModEvents.openScreen(stack.getItem().getName(stack).getContents(), stack.getDisplayName().getContents());
		}
		return InteractionResultHolder.sidedSuccess(stack, worldIn.isClientSide);

	}

	@Override
	public boolean isFireResistant() {
		return type.getFireProof();
	}



	@Override
	public void onEquip(SlotContext slotContext, ItemStack newStack,ItemStack quiverItem) {

	}


	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack quiverItem) {

	}
	public AbstractArrow modifyArrow(AbstractArrow abstractArrow){
		return abstractArrow;
	}


	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
		if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			NonNullList<ItemStack> nonnulllist = NonNullList.create();
			if(getInventory(itemStack)!=null) {
				return Optional.of(new QuiverTooltip(getInventory(itemStack).getStacks(), 64, this.type));
			}
		} else {
			return Optional.empty();
		}
		return Optional.empty();
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
		QuiverHolderAttacher.getQuiverHolderUnwrap(stack).deserializeNBT(nbt, true);
	}

	@Nullable
	@Override
	public CompoundTag getShareTag(ItemStack stack) {
		return QuiverHolderAttacher.getQuiverHolderUnwrap(stack).serializeNBT(true);
	}


	@Override
	public boolean overrideOtherStackedOnMe(ItemStack quiverStack, ItemStack inputItem, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
		if (clickAction == ClickAction.SECONDARY) {
			if(inputItem.is(TagInit.QUIVER_ITEMS) && !quiverStack.isEmpty()) {
				QuiverInventory qi = getInventory(quiverStack);
				int slots = qi.getSlots();
				for(int s = 0; s < slots; s++) {
					ItemStack currentStack = qi.getStackInSlot(s);
					ItemStack rem2 = inputItem.copy();
					if(currentStack.getItem() == inputItem.getItem() || currentStack.isEmpty())
					{
						rem2 = qi.insertItem(s, rem2, false);
					}
					inputItem.setCount(rem2.getCount());
				}
				return true;
			}
		}
		return false;
	}

	public static class ContainerProvider implements MenuProvider {
		private final Component displayName;
		private final int bagSlot;
		private final QuiverInventory inventory;

		public ContainerProvider(Component displayName, int bagSlot,
								 QuiverInventory inventory) {
			this.displayName = displayName;
			this.bagSlot = bagSlot;
			this.inventory = inventory;
		}

		@Override
		public @NotNull Component getDisplayName() {
			return this.displayName;
		}

		@Nullable
		@Override
		public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInv, @NotNull Player player) {
			return new QuiverContainer(id, playerInv, this.bagSlot, this.inventory, this.inventory.rows,
					this.inventory.columns);
		}




	}
}

