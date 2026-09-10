package com.nyfaria.nyfsquiver.network.c2s;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.init.DataComponentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.menu.QuiverContainer;
import com.nyfaria.nyfsquiver.menu.QuiverMenu;
import com.nyfaria.nyfsquiver.platform.Services;
import com.nyfaria.nyfsquiver.util.QuiverEquipment;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record NextSlotPacket(boolean decrease) {

    public static final ResourceLocation LOCATION = Constants.modLoc("next_slot");

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(decrease);
    }

    public static NextSlotPacket decode(FriendlyByteBuf buf) {
        return new NextSlotPacket(buf.readBoolean());
    }

    public static void handle(PacketContext<NextSlotPacket> context) {
        Player player = context.sender();
        ItemStack itemStack = QuiverEquipment.getEquippedQuiver(player);
        if (!itemStack.isEmpty()) {
            int currentSlot = itemStack.getOrDefault(DataComponentInit.CURRENT_SLOT.get(),0);
            int inventorySize = (int) new QuiverContainer(itemStack).getContainerSize();
            currentSlot += context.message().decrease() ? -1 : 1;
            if (currentSlot < 0) {
                currentSlot = inventorySize - 1;
            } else if (currentSlot >= inventorySize) {
                currentSlot = 0;
            }
            itemStack.set(DataComponentInit.CURRENT_SLOT.get(), currentSlot);

        }
    }
}
