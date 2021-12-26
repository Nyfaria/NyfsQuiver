package com.nyfaria.nyfsquiver.events;

import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {
    @SubscribeEvent
    public static void arrowPickup(final EntityItemPickupEvent e) {
        ItemStack toPickup = e.getItem().getItem();
        PlayerEntity player = e.getPlayer();
        ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem,player)
                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);

        if(toPickup.getItem().is(TagInit.QUIVER_ITEMS) && !quiverStack.isEmpty()) {
            QuiverInventory qi = QuiverItem.getInventory(quiverStack);
            int slots = qi.getSlots();
            for(int s = 0; s < slots; s++) {
                ItemStack currentStack = qi.getStackInSlot(s);
                ItemStack rem2 = toPickup.copy();
                if(currentStack.getItem() == toPickup.getItem() || currentStack.isEmpty())
                {
                    rem2 = qi.insertItem(s, rem2, false);
                }
                toPickup.setCount(rem2.getCount());
            }
        }
    }
}
