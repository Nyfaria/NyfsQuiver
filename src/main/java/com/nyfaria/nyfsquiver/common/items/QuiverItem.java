package com.nyfaria.nyfsquiver.common.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.api.distmarker.Dist;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.client.ClientProxy;
import com.nyfaria.nyfsquiver.client.curios.QuiverCurio;
import com.nyfaria.nyfsquiver.common.CommonProxy;
import com.nyfaria.nyfsquiver.common.containers.QuiverContainer;

import java.util.List;

public class QuiverItem extends Item
{
	
	public QuiverType type;

	public QuiverItem(QuiverType type) {
        super(type.isEnabled() ? new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_SEARCH) : new Item.Properties().stacksTo(1));
        this.type = type;
        this.setRegistryName(type.getRegistryName());
		
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			tooltip.add(new StringTextComponent("Advanced Tooltip"));
		} else {
			// "Hold " + "\u00A7e" + "Shift" + "\u00A77" + " for More Information"
			tooltip.add(new TranslationTextComponent("Hold " + "\u00A7e" + "Shift" + "\u00A77" + " for More Information"));
		}
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn,PlayerEntity playerIn, Hand handIn) {

        ItemStack stack = playerIn.getItemInHand(handIn);
        if(!playerIn.isShiftKeyDown()){
            if(!worldIn.isClientSide() && stack.getItem() instanceof QuiverItem){
                int bagSlot = handIn == Hand.MAIN_HAND ? playerIn.inventory.selected : -1;
                CommonProxy.openQuiverInventory(stack, playerIn, bagSlot);
            }
        }else if(worldIn.isClientSide){
            ClientProxy.openScreen(stack.getItem().getName(stack).getContents(), stack.getDisplayName().getContents());
        }
        return ActionResult.success(stack);
		
	}
		

    public static class ContainerProvider implements INamedContainerProvider {
        private int inventoryIndex;
        private ITextComponent displayName;
        private int bagSlot;
        private QuiverInventory inventory;

        public ContainerProvider(ITextComponent displayName, int bagSlot, int inventoryIndex, QuiverInventory inventory){
            this.inventoryIndex = inventoryIndex;
            this.displayName = displayName;
            this.bagSlot = bagSlot;
            this.inventory = inventory;
        }

        @Override
        public ITextComponent getDisplayName(){
            return this.displayName;
        }

        @Nullable
        @Override
        public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player){
            return new QuiverContainer(id, playerInv, this.bagSlot, this.inventoryIndex, this.inventory.rows, this.inventory.bagsInThisBag, this.inventory.bagsThisBagIsIn, this.inventory.layer);
        }
    }
    
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return NyfsQuiver.createProvider(new QuiverCurio(stack));
	}
}
