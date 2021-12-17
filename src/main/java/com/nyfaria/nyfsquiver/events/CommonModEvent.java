package com.nyfaria.nyfsquiver.events;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.curios.QuiverCurios;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.event.TextureStitchEvent;
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
    public static void stitchTextures(TextureStitchEvent.Pre evt) {
        if (evt.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {

            evt.addSprite(new ResourceLocation(NyfsQuiver.MOD_ID, "gui/basic_quiver"));
            evt.addSprite(new ResourceLocation(NyfsQuiver.MOD_ID, "gui/equipmentslot"));

        }
    }
    @SubscribeEvent
    public static void interModEnqueue(InterModEnqueueEvent e){
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("quiver").size(1).icon(new ResourceLocation("nyfsquiver","gui/basic_quiver")).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("arrows").size(1).hide().build());
    }

}
