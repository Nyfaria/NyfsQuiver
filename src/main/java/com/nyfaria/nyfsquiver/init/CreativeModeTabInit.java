package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CreativeModeTabInit {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "nyfsquiver");

    public static final RegistryObject<CreativeModeTab> QUIVERS =  TABS.register("quivers", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + NyfsQuiver.MODID + ".quivers"))
                .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                .icon(() -> new ItemStack(ItemInit.BASIC_QUIVER.get()))
                .displayItems((displayParams, output) -> output.acceptAll(ItemInit.ITEMS.getEntries().stream().map(item->item.get().getDefaultInstance()).toList()))
                .build());

}
