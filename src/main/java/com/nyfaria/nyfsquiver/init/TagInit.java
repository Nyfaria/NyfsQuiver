package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class TagInit {

    public static void init() {}

    public static final ITag.INamedTag<Item> QUIVER_ITEMS = itemTag("quiver_items");
    private static ITag.INamedTag<Item> itemTag(String path) {
        return ItemTags.createOptional(new ResourceLocation(NyfsQuiver.MOD_ID, path));
    }
}
