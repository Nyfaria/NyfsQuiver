package com.nyfaria.nyfsquiver.item;

import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.client.ClientUtil;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.item.tooltip.QuiverTooltip;
import com.nyfaria.nyfsquiver.menu.QuiverContainer;
import com.nyfaria.nyfsquiver.menu.QuiverMenu;
import com.nyfaria.nyfsquiver.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Consumer;

public class QuiverItem extends Item {

    public QuiverItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        Services.PLATFORM.openQuiverMenu(new SimpleMenuProvider((pContainerId, pInventory, pPlayer1) -> {
            return new QuiverMenu(pContainerId, pInventory, new QuiverContainer(pPlayer.getItemInHand(pUsedHand)));
        }, Component.literal("Quiver")),pPlayer,pPlayer.getItemInHand(pUsedHand));
        return InteractionResult.SUCCESS;
    }

    @Override
    public Component getName(ItemStack pStack) {
        QuiverType type = pStack.get(DataComponentInit.QUIVER_TYPE.get());
        return Component.translatable(type.getTranslationKey());
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay pDisplay, Consumer<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pDisplay, pTooltipComponents, pTooltipFlag);
        if (!ClientUtil.isShiftDown()) {
            pTooltipComponents.accept(Component.translatable("tooltip.nyfsquiver",Component.translatable("tooltip.nyfsquiver.shift").withStyle(ChatFormatting.YELLOW)));
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        boolean hasContainer = pStack.has(DataComponents.CONTAINER);
        if(hasContainer && ClientUtil.isShiftDown()){
            NonNullList<ItemStack> container = NonNullList.withSize((int)pStack.get(DataComponents.CONTAINER).allItemsCopyStream().count(),ItemStack.EMPTY);
            pStack.get(DataComponents.CONTAINER).copyInto(container);
            return Optional.of(new QuiverTooltip(container,1,pStack.get(DataComponentInit.QUIVER_TYPE.get())));
        }
        return Optional.empty();
    }
}
