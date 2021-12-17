package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class TagInit {

    public static void init() {}

    public static final Tag.Named<Item> QUIVER_ITEMS = itemTag("quiver_items");
    private static Tag.Named<Item> itemTag(String path) {
        return ItemTags.createOptional(new ResourceLocation(NyfsQuiver.MOD_ID, path));
    }
}
