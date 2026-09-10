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
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

/**
 * The quiver item.
 * <p>
 * Both accessory libraries are soft dependencies, so this item cannot extend the Accessories
 * {@code AccessoryItem} base class anymore. Its accessory behavior is registered from
 * {@link com.nyfaria.nyfsquiver.compat.AccessoriesCompat} instead, while Curios picks the item up
 * through the {@code curios:quiver} item tag.
 */
public class QuiverItem extends Item {

    public QuiverItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        Services.PLATFORM.openQuiverMenu(new SimpleMenuProvider((pContainerId, pInventory, pPlayer1) -> {
            return new QuiverMenu(pContainerId, pInventory, new QuiverContainer(pPlayer.getItemInHand(pUsedHand)));
        }, Component.literal("Quiver")),pPlayer,pPlayer.getItemInHand(pUsedHand));
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public DataComponentMap components() {
        return super.components();
    }

    @Override
    public Component getName(ItemStack pStack) {
        QuiverType type = pStack.get(DataComponentInit.QUIVER_TYPE.get());
        return Component.translatable(type.getTranslationKey());
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        if (ClientUtil.isShiftDown()) {

        } else {
            pTooltipComponents.add(Component.translatable("tooltip.nyfsquiver",Component.translatable("tooltip.nyfsquiver.shift").withStyle(ChatFormatting.YELLOW)));
        }
    }
    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        boolean hasContainer = pStack.has(DataComponents.CONTAINER);
//        boolean hasContainer = pStack.has(DataComponentInit.QUIVER_CONTENTS.get());
        if(hasContainer && ClientUtil.isShiftDown()){
//            NonNullList<ItemStack> container = pStack.get(DataComponentInit.QUIVER_CONTENTS.get()).items();
            NonNullList<ItemStack> container = NonNullList.withSize((int)pStack.get(DataComponents.CONTAINER).stream().count(),ItemStack.EMPTY);
            pStack.get(DataComponents.CONTAINER).copyInto(container);

            return Optional.of(new QuiverTooltip(container,1,pStack.get(DataComponentInit.QUIVER_TYPE.get())));
        }
        return Optional.empty();
    }
}
