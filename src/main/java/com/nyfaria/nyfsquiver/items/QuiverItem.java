package com.nyfaria.nyfsquiver.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.events.ClientModEvents;
import com.nyfaria.nyfsquiver.init.EnchantmentInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import net.minecraftforge.fml.network.NetworkHooks;
import org.antlr.v4.runtime.misc.NotNull;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

//import com.nyfaria.nyfsquiver.client.curios.QuiverCurio;

public class QuiverItem extends Item implements ICurioItem {

	public QuiverType type;

	public QuiverItem(QuiverType type) {
		super(new Item.Properties().stacksTo(1).tab(NyfsQuiver.ITEM_GROUP));
		this.type = type;
	}

	public static QuiverInventory getInventory(ItemStack itemStack) {
		return QuiverHolderAttacher.getQuiverHolderUnwrap(itemStack).getInventory();
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
				//QuiverInventory.openQuiverInventory(stack, playerIn, bagSlot);
				NetworkHooks.openGui((ServerPlayerEntity) playerIn, new QuiverItem.ContainerProvider(stack.getDisplayName(), bagSlot, getInventory(stack)), a -> {
					a.writeInt(bagSlot);
					a.writeInt(getInventory(stack).rows);
					a.writeInt(getInventory(stack).columns);
					a.writeNbt(getInventory(stack).serializeNBT());
				});
			}
		} else if (worldIn.isClientSide) {
			ClientModEvents.openScreen(stack.getItem().getName(stack).getContents(), stack.getDisplayName().getContents());
		}
		return ActionResult.sidedSuccess(stack, worldIn.isClientSide);

	}

	@Override
	public boolean isFireResistant() {
		return type.getFireProof();
	}

	public static class ContainerProvider implements INamedContainerProvider {
		private final ITextComponent displayName;
		private final int bagSlot;
		private final QuiverInventory inventory;

		public ContainerProvider(ITextComponent displayName, int bagSlot, QuiverInventory inventory) {
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
			return new QuiverContainer(id, playerInv, this.bagSlot, this.inventory, this.inventory.rows,
					this.inventory.columns);
		}
	}




	@Override
	public void render(String identifier, int index, MatrixStack matrixStack,
					   IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity living,
					   float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
					   float netHeadYaw, float headPitch, ItemStack stack) {
//		if(EnchantmentHelper.getEnchantments(stack).containsKey(EnchantmentInit.MELD_ENCHANTMENT.get()) && living.isInvisible())
//		{
//			return;
//		}

		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		ICurio.RenderHelper.translateIfSneaking(matrixStack, living);
		ICurio.RenderHelper.rotateIfSneaking(matrixStack, living);
		//matrixStack.mulPose(Vector3f.XN.rotationDegrees(180));
		matrixStack.translate(-.75,0.35,-0.145);
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90));
		//matrixStack.translate(0,0.6,-0.65 - (living.getItemBySlot(EquipmentSlot.CHEST).isEmpty() ? 0.0 : 0.05));
		ItemStack arrowsE = QuiverStorageManager.getCurrentSlotStack(stack);
		IBakedModel quiver = itemRenderer.getItemModelShaper().getModelManager().getModel(QuiverModels.getQuiverModel(stack,!arrowsE.isEmpty()));
		IRenderTypeBuffer buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		itemRenderer.render(stack, ItemCameraTransforms.TransformType.HEAD,true,matrixStack,buffer,light,light,quiver);

	}

	@Override
	public boolean canRender(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
		return true;
	}

	@Nullable
	@Override
	public CompoundNBT getShareTag(ItemStack stack) {
		return QuiverHolderAttacher.getQuiverHolderUnwrap(stack).serializeNBT(true);
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
		QuiverHolderAttacher.getQuiverHolderUnwrap(stack).deserializeNBT(nbt, true);
	}
}

