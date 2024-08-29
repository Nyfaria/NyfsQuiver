package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.api.RegistryAccessAccess;
import com.nyfaria.nyfsquiver.item.QuiverItem;
import com.nyfaria.nyfsquiver.item.component.QuiverContainerContents;
import com.nyfaria.nyfsquiver.registration.RegistrationProvider;
import com.nyfaria.nyfsquiver.registration.RegistryObject;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Blocks;

public class ItemInit {
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, Constants.MODID);

    public static final RegistryObject<Item,Item> QUIVER = ITEMS.register("quiver", () -> new QuiverItem(getItemProperties().stacksTo(1)
            .component(DataComponentInit.CURRENT_SLOT.get(),0)
            .component(DataComponents.CONTAINER, ItemContainerContents.fromItems(NonNullList.withSize(27,ItemStack.EMPTY)))
            .component(DataComponentInit.QUIVER_TYPE.get(), new QuiverType(Constants.modLoc("leather"),3,9,false,1, null))
    ));

    public static final RegistrationProvider<CreativeModeTab> CREATIVE_MODE_TABS = RegistrationProvider.get(Registries.CREATIVE_MODE_TAB, Constants.MODID);
    public static final RegistryObject<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(Constants.MODID, () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, -1)
            .icon(() -> new ItemStack(ItemInit.QUIVER.get()))
            .displayItems(
                    (itemDisplayParameters, output) -> {
                        QuiverInit.getRegistry(RegistryAccessAccess.ACCESS).entrySet().forEach(
                                entry -> {
                                    ItemStack quiverStack = new ItemStack(QUIVER.get());
                                    quiverStack.set(DataComponentInit.QUIVER_TYPE.get(), entry.getValue());
                                    quiverStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(NonNullList.withSize(entry.getValue().columns()*entry.getValue().rows(), new ItemStack(Items.ARROW, 64))));
//                                    quiverStack.set(DataComponentInit.QUIVER_CONTENTS.get(), QuiverContainerContents.fromItems(NonNullList.withSize(entry.getValue().columns()*entry.getValue().rows(), new ItemStack(Items.ARROW, 64))));
                                    output.accept(quiverStack);
                                    quiverStack = new ItemStack(QUIVER.get());
                                    quiverStack.set(DataComponentInit.QUIVER_TYPE.get(), entry.getValue());
                                    quiverStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(NonNullList.withSize(entry.getValue().columns()*entry.getValue().rows(), ItemStack.EMPTY)));
                                    output.accept(quiverStack);
                                }
                        );
                    }).title(Component.translatable("itemGroup." + Constants.MODID + ".tab"))
            .build());


    public static Item.Properties getItemProperties() {
        return new Item.Properties();
    }

    public static void loadClass() {
    }

}
