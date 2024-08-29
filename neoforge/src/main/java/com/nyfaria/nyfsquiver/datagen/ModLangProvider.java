package com.nyfaria.nyfsquiver.datagen;

import com.google.common.collect.ImmutableMap;
import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.registration.RegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ModLangProvider extends LanguageProvider {
    protected static final Map<String, String> REPLACE_LIST = ImmutableMap.of(
            "tnt", "TNT",
            "sus", ""
    );

    public ModLangProvider(PackOutput gen) {
        super(gen, Constants.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        ItemInit.ITEMS.getEntries().forEach(this::itemLang);
//        EntityInit.ENTITIES.getEntries().forEach(this::entityLang);
//        BlockInit.BLOCKS.getEntries().forEach(this::blockLang);
        add("itemGroup." + Constants.MODID + ".tab", Constants.MOD_NAME);
        add("accessories.slot.quiver_back", "Quiver Back");
        add("accessories.slot.quiver_hip", "Quiver Hip");
        add("enchantment." + Constants.MODID + ".cycling", "Cycling");
        add("quiver." + Constants.MODID + ".leather", "Leather Quiver");
        add("quiver." + Constants.MODID + ".iron", "Iron Quiver");
        add("quiver." + Constants.MODID + ".gold", "Gold Quiver");
        add("quiver." + Constants.MODID + ".diamond", "Diamond Quiver");
        add("quiver." + Constants.MODID + ".netherite", "Netherite Quiver");
        add("key.categories"+ Constants.MODID, Constants.MOD_NAME);
        add("key."+ Constants.MODID+".next_slot", "Next Slot");
        add("key."+ Constants.MODID+".previous_slot", "Previous Slot");
        add("key."+ Constants.MODID+".open_quiver", "Open Equipped Quiver");
        add("tooltip.nyfsquiver", "Hold '%s' to view contents");
        add("tooltip.nyfsquiver.shift", "SHIFT");
        add("enchantment.nyfsquiver.cycling_enchant.desc", "Quivers Cycle to the next Slot if Current Slot is empty");
    }

    protected void itemLang(RegistryObject<Item, ?> entry) {
        if (!(entry.get() instanceof BlockItem) || entry.get() instanceof ItemNameBlockItem) {
            addItem(entry, checkReplace(entry));
        }
    }

    protected void blockLang(RegistryObject<Block, ?> entry) {
        addBlock(entry, checkReplace(entry));
    }

    protected void entityLang(RegistryObject<EntityType<?>, ? extends EntityType<?>> entry) {
        addEntityType(entry, checkReplace(entry));
    }

    protected String checkReplace(RegistryObject<?, ?> registryObject) {
        return Arrays.stream(registryObject.getId().getPath().split("_"))
                .map(this::checkReplace)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(" "))
                .trim();
    }

    protected String checkReplace(String string) {
        return REPLACE_LIST.containsKey(string) ? REPLACE_LIST.get(string) : StringUtils.capitalize(string);
    }
}
