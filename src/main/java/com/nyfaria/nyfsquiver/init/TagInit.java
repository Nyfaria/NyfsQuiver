package com.nyfaria.nyfsquiver.init;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagInit {

    public static final TagKey<Item> QUIVER_ITEMS = itemTag("quiver_items");

    public static void init() {
    }

    private static TagKey<Item> itemTag(String path) {
        return ItemTags.create(new ResourceLocation(NyfsQuiver.MODID, path));
    }
}
