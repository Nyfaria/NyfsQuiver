package com.nyfaria.nyfsquiver.common.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nyfaria.nyfsquiver.NQConfig_Client;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.client.ClientProxy;
import com.nyfaria.nyfsquiver.client.render.model.OldQuiverModel;
import com.nyfaria.nyfsquiver.client.render.model.QuiverModel;
import com.nyfaria.nyfsquiver.common.CommonProxy;
import com.nyfaria.nyfsquiver.common.containers.QuiverContainer;
import com.nyfaria.nyfsquiver.init.TagInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.antlr.v4.runtime.misc.NotNull;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.Nullable;
import java.util.List;

//import com.nyfaria.nyfsquiver.client.curios.QuiverCurio;

public class QuiverItem extends Item implements ICurioItem {

	private Object model;
	public QuiverType type;

	public QuiverItem(QuiverType type) {
		super(type.getFireProof() ? new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_COMBAT).fireResistant() : new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_COMBAT));
		this.type = type;
		this.setRegistryName(type.getRegistryName());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@NotNull ItemStack stack, World worldIn, @NotNull List<ITextComponent> tooltip, @NotNull ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {

			tooltip.add(new StringTextComponent("Advanced Tooltip coming soon(tm)"));

		} else {
			// "Hold " + "\u00A7e" + "Shift" + "\u00A77" + " for More Information"
			tooltip.add(
					new TranslationTextComponent("Hold " + "\u00A7e" + "Shift" + "\u00A77" + " for More Information"));
		}
	}

	@Override
	public ActionResult<ItemStack> use(@NotNull World worldIn, PlayerEntity playerIn, @NotNull Hand handIn) {

		ItemStack stack = playerIn.getItemInHand(handIn);
		if (!playerIn.isShiftKeyDown()) {
			if (!worldIn.isClientSide() && stack.getItem() instanceof QuiverItem) {
				int bagSlot = handIn == Hand.MAIN_HAND ? playerIn.inventory.selected : -1;
				CommonProxy.openQuiverInventory(stack, playerIn, bagSlot);
			}
		} else if (worldIn.isClientSide) {
			ClientProxy.openScreen(stack.getItem().getName(stack).getContents(), stack.getDisplayName().getContents());
		}
		return ActionResult.success(stack);
		// playerIn.blockPosition().relative(playerIn.getDirection());

	}
	

	public static class ContainerProvider implements INamedContainerProvider {
		private final int inventoryIndex;
		private final ITextComponent displayName;
		private final int bagSlot;
		private final QuiverInventory inventory;

		public ContainerProvider(ITextComponent displayName, int bagSlot, int inventoryIndex,
				QuiverInventory inventory) {
			this.inventoryIndex = inventoryIndex;
			this.displayName = displayName;
			this.bagSlot = bagSlot;
			this.inventory = inventory;
		}

		@Override
		public @NotNull ITextComponent getDisplayName() {
			return this.displayName;
		}

		@Nullable
		@Override
		public Container createMenu(int id, @NotNull PlayerInventory playerInv, @NotNull PlayerEntity player) {
			return new QuiverContainer(id, playerInv, this.bagSlot, this.inventoryIndex, this.inventory.rows,
					this.inventory.columns);
		}
	}

	//@Nullable
	//@Override
	//public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
	//	return NyfsQuiver.createProvider(new QuiverCurio(stack));
	//}



	@Override
	public void render(String identifier, int index, MatrixStack matrixStack,
					   IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity living,
					   float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
					   float netHeadYaw, float headPitch, ItemStack stack) {
		ICurio.RenderHelper.translateIfSneaking(matrixStack, living);
		ICurio.RenderHelper.rotateIfSneaking(matrixStack, living);

		ItemStack arrowsE = CuriosApi.getCuriosHelper()
				.findEquippedCurio(NyfsQuiver.arrow_predicate, living)
				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right)
				.orElse(ItemStack.EMPTY);

		ResourceLocation QUIVER_TEXTURE;
		matrixStack.mulPose(Vector3f.YN.rotationDegrees(180));
		QUIVER_TEXTURE = new ResourceLocation(NyfsQuiver.MOD_ID,
				"textures/back/" + type.getRegistryName() + ".png");
		if (!(this.model instanceof QuiverModel)) {
			this.model = new QuiverModel<>(arrowsE == ItemStack.EMPTY);
		}
		QuiverModel<?> quiverModel = (QuiverModel<?>) this.model;

		quiverModel.displayArrows = arrowsE == ItemStack.EMPTY;
		IVertexBuilder vertexBuilder = ItemRenderer
				.getFoilBuffer(renderTypeBuffer, quiverModel.renderType(QUIVER_TEXTURE), false,
						stack.hasFoil());
		quiverModel.renderToBuffer(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
						1.0F);

	}

	@Override
	public boolean canRender(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
		return !livingEntity.isInvisible();
	}

	@Override
	public void onEquip(SlotContext slotContext, ItemStack newStack,ItemStack quiverItem) {
		if (quiverItem.getOrCreateTag().contains("nyfsquiver:invIndex")
				&& quiverItem.getOrCreateTag().contains("nyfsquiver:slotIndex")) {

		} else {

			CompoundNBT compound  = quiverItem.getOrCreateTag();
			compound.putInt("nyfsquiver:invIndex", QuiverStorageManager.createInventoryIndex(this.type));
			compound.putInt("nyfsquiver:slotIndex", 0);
			quiverItem.setTag(compound);
		}
		ItemStack stack = QuiverStorageManager.getInventory(quiverItem.getOrCreateTag().getInt("nyfsquiver:invIndex"))
				.getStackInSlot(quiverItem.getOrCreateTag().getInt("nyfsquiver:slotIndex"));
		ItemStack checkStack = CuriosApi.getCuriosHelper()
				.findEquippedCurio(NyfsQuiver.arrow_predicate, slotContext.getWearer())
				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right)
				.orElse(ItemStack.EMPTY);
		if (checkStack == ItemStack.EMPTY) {
			CuriosApi.getCuriosHelper().getCuriosHandler(slotContext.getWearer()).map(ICuriosItemHandler::getCurios)
					.map(stringICurioStacksHandlerMap -> stringICurioStacksHandlerMap.get("arrows"))
					.map(ICurioStacksHandler::getStacks)
					.ifPresent(curioStackHandler -> curioStackHandler.setStackInSlot(0, stack));
			QuiverStorageManager.getInventory(quiverItem.getOrCreateTag().getInt("nyfsquiver:invIndex"))
					.setStackInSlot(quiverItem.getOrCreateTag().getInt("nyfsquiver:slotIndex"), ItemStack.EMPTY);
		}
	}


	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack quiverItem) {
		quiverItem.getOrCreateTag().putBoolean("equipped", false);
		System.out.println("Quiver UnEquipped");
		ItemStack stack = CuriosApi.getCuriosHelper()
				.findEquippedCurio(item -> item.getItem().is(TagInit.QUIVER_ITEMS), slotContext.getWearer())
				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right)
				.orElse(ItemStack.EMPTY);
/*		if(stack.isEmpty()) {
			stack = CuriosApi.getCuriosHelper()
					.findEquippedCurio(item -> item.getItem() instanceof FireworkRocketItem, slotContext.getWearer())
					.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right)
					.orElse(ItemStack.EMPTY);
		}*/
		QuiverStorageManager.getInventory(quiverItem.getOrCreateTag().getInt("nyfsquiver:invIndex"))
				.setStackInSlot(quiverItem.getOrCreateTag().getInt("nyfsquiver:slotIndex"), stack);
		CuriosApi.getCuriosHelper().getCuriosHandler(slotContext.getWearer()).map(ICuriosItemHandler::getCurios)
				.map(stringICurioStacksHandlerMap -> stringICurioStacksHandlerMap.get("arrows"))
				.map(ICurioStacksHandler::getStacks)
				.ifPresent(curioStackHandler -> curioStackHandler.setStackInSlot(0, ItemStack.EMPTY));

	}
}

