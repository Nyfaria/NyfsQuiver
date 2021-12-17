package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import com.nyfaria.nyfsquiver.items.QuiverType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NyfsQuiver.MOD_ID);

    public static final RegistryObject<QuiverItem> BASIC_QUIVER = ITEMS.register("basic_quiver",()->new QuiverItem(QuiverType.BASIC));
    public static final RegistryObject<QuiverItem> IRON_QUIVER = ITEMS.register("iron_quiver",()->new QuiverItem(QuiverType.IRON));
    public static final RegistryObject<QuiverItem> COPPER_QUIVER = ITEMS.register("copper_quiver",()->new QuiverItem(QuiverType.COPPER));
    public static final RegistryObject<QuiverItem> GOLD_QUIVER = ITEMS.register("gold_quiver",()->new QuiverItem(QuiverType.GOLD));
    public static final RegistryObject<QuiverItem> DIAMOND_QUIVER = ITEMS.register("diamond_quiver",()->new QuiverItem(QuiverType.DIAMOND));
    public static final RegistryObject<QuiverItem> NETHERITE_QUIVER = ITEMS.register("netherite_quiver",()->new QuiverItem(QuiverType.NETHERITE));
}
