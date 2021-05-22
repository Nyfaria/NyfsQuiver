package com.nyfaria.nyfsquiver.core.init;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, 
			NyfsQuiver.MOD_ID);	
	
	//public static final RegistryObject<QuiverItem> QUIVER_ITEM = ITEMS.register("quiver_item", 
	//		() -> new QuiverItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
}
