package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagInit {
    public static TagKey<Item> QUIVER = TagKey.create(Registries.ITEM, Constants.modLoc("quiver"));
    public static TagKey<Item> QUIVER_ITEMS = TagKey.create(Registries.ITEM, Constants.modLoc("quiver_items"));


    public static void loadClass() {
    }
}
