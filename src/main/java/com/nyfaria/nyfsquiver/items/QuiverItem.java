package com.nyfaria.nyfsquiver.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.events.ClientModEvents;
import com.nyfaria.nyfsquiver.tooltip.QuiverTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.antlr.v4.runtime.misc.NotNull;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;


public class QuiverItem extends Item implements ICurioItem, Wearable {

	public QuiverType type;

	public QuiverItem(QuiverType type) {
		super(new Item.Properties().stacksTo(1).tab(NyfsQuiver.ITEM_GROUP));
		this.type = type;
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
			// "Hold " + "\u00A7e" + "Shift" + "\u00A77" + " for More Information"
			tooltip.add(
					new TranslatableComponent("Hold " + "\u00A7e" + "Shift" + "\u00A77" + " for More Information"));
		}
	}

	@Override
	public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
		return false;
	}

	@Override
	public boolean hasCurioCapability(ItemStack stack) {
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {

		ItemStack stack = playerIn.getItemInHand(handIn);
		if (!playerIn.isShiftKeyDown()) {
			if (!worldIn.isClientSide() && stack.getItem() instanceof QuiverItem) {
				int bagSlot = handIn == InteractionHand.MAIN_HAND ? playerIn.getInventory().selected : -1;
				QuiverInventory.openQuiverInventory(stack, playerIn, bagSlot);
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

	public static class ContainerProvider implements MenuProvider {
		private final int inventoryIndex;
		private final Component displayName;
		private final int bagSlot;
		private final QuiverInventory inventory;

		public ContainerProvider(Component displayName, int bagSlot, int inventoryIndex,
				QuiverInventory inventory) {
			this.inventoryIndex = inventoryIndex;
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
			return new QuiverContainer(id, playerInv, this.bagSlot, this.inventoryIndex, this.inventory.rows,
					this.inventory.columns);
		}
	}


	@Override
	public void onEquip(SlotContext slotContext, ItemStack newStack,ItemStack quiverItem) {
		if (quiverItem.getOrCreateTag().contains("invIndex")
				&& quiverItem.getOrCreateTag().contains("slotIndex")) {

		} else {

			CompoundTag compound  = quiverItem.getOrCreateTag();
			compound.putInt("invIndex", QuiverStorageManager.createInventoryIndex(this.type));
			compound.putInt("slotIndex", 0);
			quiverItem.setTag(compound);
		}
//		ItemStack stack = QuiverStorageManager.getInventory(quiverItem.getOrCreateTag().getInt("invIndex"))
//				.getStackInSlot(quiverItem.getOrCreateTag().getInt("slotIndex"));
//		ItemStack checkStack = CuriosApi.getCuriosHelper()
//				.findEquippedCurio(NyfsQuiver.ARROW_PREDICATE, slotContext.getWearer())
//				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right)
//				.orElse(ItemStack.EMPTY);
//		if (checkStack == ItemStack.EMPTY) {
//			CuriosApi.getCuriosHelper().getCuriosHandler(slotContext.getWearer()).map(ICuriosItemHandler::getCurios)
//					.map(stringICurioStacksHandlerMap -> stringICurioStacksHandlerMap.get("arrows"))
//					.map(ICurioStacksHandler::getStacks)
//					.ifPresent(curioStackHandler -> curioStackHandler.setStackInSlot(0, stack));
//			QuiverStorageManager.getInventory(quiverItem.getOrCreateTag().getInt("invIndex"))
//					.setStackInSlot(quiverItem.getOrCreateTag().getInt("slotIndex"), ItemStack.EMPTY);
//		}
	}


	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack quiverItem) {

	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
		if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			NonNullList<ItemStack> nonnulllist = NonNullList.create();
			QuiverStorageManager.getInventory(itemStack.getOrCreateTag().getInt("invIndex")).getStacks().forEach(nonnulllist::add);
			return Optional.of(new QuiverTooltip(nonnulllist, 64,this.type));
		} else {
			return Optional.empty();
		}
	}
}

