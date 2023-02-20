package com.nyfaria.nyfsquiver.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.cap.QuiverHolder;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.events.ClientModEvents;
import com.nyfaria.nyfsquiver.init.EnchantmentInit;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.tooltip.QuiverTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
        super(new Item.Properties().stacksTo(1).durability(type.getDefaultColumns() * type.getDefaultRows()));
        this.type = type;

    }


    public static QuiverInventory getInventory(ItemStack itemStack) {
        return QuiverHolderAttacher.getQuiverHolderUnwrap(itemStack).getInventory();
    }

    public static void useQuiver(ItemStack quiverStack, ServerPlayer player, ItemStack arrowStack) {
        if (EnchantmentHelper.getEnchantments(quiverStack).containsKey(EnchantmentInit.QUINFINITY.get())) {
            int damage = checkQuinfinityValue(arrowStack);
            if (quiverStack.getDamageValue() + damage <= quiverStack.getMaxDamage()) {
                quiverStack.hurt(damage, player.level.random, player);
            }
        }
    }

    public static int checkQuinfinityValue(ItemStack arrowStack) {
        int damage = 16;
        if (arrowStack.is(Items.SPECTRAL_ARROW)) {
            damage = 8;
        } else if (arrowStack.is(Items.ARROW)) {
            damage = 1;
        }
        return damage;
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
            tooltip.add(Component.translatable("tooltip.nyfsquiver",Component.translatable("tooltip.nyfsquiver.shift").withStyle(ChatFormatting.YELLOW)));
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
                NetworkHooks.openScreen((ServerPlayer) playerIn, new QuiverItem.ContainerProvider(stack.getDisplayName(), bagSlot, getInventory(stack)), a -> {
                    a.writeInt(bagSlot);
                    a.writeInt(getInventory(stack).rows);
                    a.writeInt(getInventory(stack).columns);
                    a.writeNbt(getInventory(stack).serializeNBT());
                });
            }
        } else if (worldIn.isClientSide) {
            ClientModEvents.openScreen(stack.getItem().getName(stack).getContents().toString(), stack.getDisplayName().getContents().toString());
        }
        return InteractionResultHolder.sidedSuccess(stack, worldIn.isClientSide);

    }

    @Override
    public boolean isFireResistant() {
        return type.getFireProof();
    }

    public AbstractArrow modifyArrow(AbstractArrow abstractArrow) {
        return abstractArrow;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.create();
            if (getInventory(itemStack) != null) {
                return Optional.of(new QuiverTooltip(getInventory(itemStack).getStacks(), 64, this.type));
            }
        } else {
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        QuiverHolderAttacher.getQuiverHolderUnwrap(stack).deserializeNBT(nbt.getCompound("inventory"), true);
        super.readShareTag(stack, nbt);
    }

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag tag = super.getShareTag(stack);
        if (tag == null) {
            tag = new CompoundTag();
        }
        tag.put("inventory", QuiverHolderAttacher.getQuiverHolderUnwrap(stack).serializeNBT(true));
        return tag;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return type.getDefaultDurability();
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack quiverStack, ItemStack inputItem, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction == ClickAction.SECONDARY) {
            if (inputItem.is(TagInit.QUIVER_ITEMS) && !quiverStack.isEmpty()) {
                QuiverInventory qi = getInventory(quiverStack);
                int slots = qi.getSlots();
                for (int s = 0; s < slots; s++) {
                    ItemStack currentStack = qi.getStackInSlot(s);
                    ItemStack rem2 = inputItem.copy();
                    if (currentStack.getItem() == inputItem.getItem() || currentStack.isEmpty()) {
                        rem2 = qi.insertItem(s, rem2, false);
                    }
                    inputItem.setCount(rem2.getCount());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);
        if (EnchantmentHelper.getEnchantments(stack).containsKey(EnchantmentInit.CYCLING.get())) {
            QuiverHolder quiverHolder = QuiverHolderAttacher.getQuiverHolderUnwrap(stack);
            if (getInventory(stack).getStacks().stream().filter(is -> !is.isEmpty()).count() > 0) {
                if (getInventory(stack).getStackInSlot(quiverHolder.getCurrentSlot()).isEmpty()) {
                    quiverHolder.changeCurrentSlot(1);
                }
            }
        }
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
        public @NotNull
        Component getDisplayName() {
            return this.displayName;
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInv, @NotNull Player player) {
            return new QuiverContainer(id, playerInv, this.bagSlot, this.inventory, this.inventory.rows,
                    this.inventory.columns);
        }

    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if(stack.getDamageValue() > 0){
            if(!EnchantmentHelper.getEnchantments(stack).containsKey(EnchantmentInit.QUINFINITY.get())){
                stack.setDamageValue(0);
            }
        }
    }
}

