package com.nyfaria.nyfsquiver.events;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.curios.ArrowsCurio;
import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import com.nyfaria.nyfsquiver.items.QuiverStorageManager;
import com.nyfaria.nyfsquiver.init.TagInit;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void arrowPickup(final EntityItemPickupEvent e) {
        ItemStack toPickup = e.getItem().getItem();
        Player player = e.getPlayer();
        ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem,player)
                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);

        if(toPickup.is(TagInit.QUIVER_ITEMS) && !quiverStack.isEmpty()) {
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

    @SubscribeEvent
    public static void attachCaps(AttachCapabilitiesEvent<ItemStack> e) {
//        ItemStack stack = e.getObject();
//        if (ItemTags.getAllTags().getTag(new ResourceLocation("curios","arrows")) != null
//                && NyfsQuiver.ARROWS_CURIO.contains(stack.getItem())) {
//            ArrowsCurio arrowCurio = new ArrowsCurio(stack);
//            e.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
//                final LazyOptional<ICurio> arrowsCurioCap = LazyOptional.of(() -> arrowCurio);
//
//                @Nonnull
//                @Override
//                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
//                                                         @Nullable Direction side) {
//                    return CuriosCapability.ITEM.orEmpty(cap, arrowsCurioCap);
//                }
//
//            });
//        }
    }
}
