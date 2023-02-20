package com.nyfaria.nyfsquiver.events;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.init.ItemInit;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvent {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        QuiverHolderAttacher.register();
    }

    @SubscribeEvent
    public static void interModEnqueue(InterModEnqueueEvent e) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("quiver").size(1).icon(new ResourceLocation("nyfsquiver", "gui/basic_quiver")).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("arrows").size(1).hide().build());
    }

    @SubscribeEvent
    public static void registerTab(CreativeModeTabEvent.Register event){
        event.registerCreativeModeTab(new ResourceLocation(NyfsQuiver.MODID), tab-> tab.title(Component.translatable("itemGroup.nyfsquiver")).icon(()-> new ItemStack(ItemInit.BASIC_QUIVER.get())).displayItems((featureFlagSet, output, flag)->{
            output.acceptAll(ItemInit.ITEMS.getEntries().stream().map(item->item.get().getDefaultInstance()).toList());
        }));
    }
}
