package com.nyfaria.nyfsquiver.network.c2s;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.menu.QuiverContainer;
import com.nyfaria.nyfsquiver.menu.QuiverMenu;
import com.nyfaria.nyfsquiver.platform.Services;
import commonnetwork.networking.data.PacketContext;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record OpenEquippedQuiverPacket() {

    public static final ResourceLocation LOCATION = Constants.modLoc("open_equipped_quiver");

    public void encode(FriendlyByteBuf buf) {

    }

    public static OpenEquippedQuiverPacket decode(FriendlyByteBuf buf) {
        return new OpenEquippedQuiverPacket();
    }

    public static void handle(PacketContext<OpenEquippedQuiverPacket> context) {
        Player player = context.sender();
        if (player == null) return;
        SlotEntryReference slotReference = AccessoriesCapability.get(player).getFirstEquipped(ItemInit.QUIVER.get());
        if (slotReference == null) {
            return;
        }
        ItemStack itemStack = slotReference.stack();
        if (itemStack.isEmpty()) {
            return;
        }
        Services.PLATFORM.openQuiverMenu(new SimpleMenuProvider((pContainerId, pInventory, pPlayer1) -> new QuiverMenu(pContainerId, pInventory, new QuiverContainer(itemStack)), Component.literal("Quiver")), player, itemStack);

    }
}
