package com.nyfaria.nyfsquiver;

import com.google.common.collect.Lists;
import com.nyfaria.nyfsquiver.config.NQConfig;
import com.nyfaria.nyfsquiver.config.NQConfig_Client;
import com.nyfaria.nyfsquiver.init.ContainerInit;
import com.nyfaria.nyfsquiver.init.CreativeModeTabInit;
import com.nyfaria.nyfsquiver.init.EnchantmentInit;
import com.nyfaria.nyfsquiver.init.ItemInit;
import com.nyfaria.nyfsquiver.init.RecipeInit;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import com.nyfaria.nyfsquiver.items.QuiverModels;
import com.nyfaria.nyfsquiver.items.QuiverStorageManager;
import com.nyfaria.nyfsquiver.packets.PacketMaxLayers;
import com.nyfaria.nyfsquiver.packets.PacketNextSlot;
import com.nyfaria.nyfsquiver.packets.PacketOpenQuiver;
import com.nyfaria.nyfsquiver.packets.PacketPreviousSlot;
import com.nyfaria.nyfsquiver.packets.PacketRename;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;


@Mod(NyfsQuiver.MODID)
public class NyfsQuiver {
    public static final String MODID = "nyfsquiver";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), () -> "1", "1"::equals, "1"::equals);
    public static final ResourceLocation WIDGETS = new ResourceLocation(MODID, "textures/gui/widgets.png");
    public static boolean drawn = false;
    public static TagKey<Item> QUIVER_CURIO = ItemTags.create(new ResourceLocation("curios", "quiver"));
    public static final Predicate<ItemStack> QUIVER_PREDICATE = stack -> stack.is(QUIVER_CURIO);
    public static float interpolation = 0;
    static @Nullable
    public ItemStack lastHeld = null;
    static @Nullable
    public List<ItemStack> lastReadyArrows = null;

    public NyfsQuiver() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NQConfig.CONFIG_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, NQConfig_Client.CLIENT_SPEC);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.ITEMS.register(bus);
        ContainerInit.CONTAINERS.register(bus);
        RecipeInit.RECIPES.register(bus);
        EnchantmentInit.ENCHANTMENTS.register(bus);
        CreativeModeTabInit.TABS.register(bus);
        QuiverModels.init();
        EVENT_BUS.register(QuiverStorageManager.class);

        CHANNEL.registerMessage(0, PacketRename.class, PacketRename::encode, PacketRename::decode, PacketRename::handle);
        CHANNEL.registerMessage(1, PacketMaxLayers.class, PacketMaxLayers::encode, PacketMaxLayers::decode, PacketMaxLayers::handle);
        CHANNEL.registerMessage(2, PacketNextSlot.class, (msg, buffer) -> {
        }, buffer -> new PacketNextSlot(0), PacketNextSlot::handle);
        CHANNEL.registerMessage(3, PacketPreviousSlot.class, (msg, buffer) -> {
        }, buffer -> new PacketPreviousSlot(0), PacketPreviousSlot::handle);
        CHANNEL.registerMessage(4, PacketOpenQuiver.class, (msg, buffer) -> {
        }, buffer -> new PacketOpenQuiver(), PacketOpenQuiver::handle);

        TagInit.init();

    }


    public static List<ItemStack> findAmmos(Player player, ItemStack shootable) {

        ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem, player)
                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
        ItemStack itemstack = QuiverStorageManager.getCurrentSlotStack(quiverStack);

        if ((quiverStack.isEmpty())) {
            return Lists.newLinkedList();
        } else {
            List<ItemStack> list = Lists.newLinkedList();

            Predicate<ItemStack> predicate = ((ProjectileWeaponItem) shootable.getItem()).getSupportedHeldProjectiles();
            if (!itemstack.isEmpty()) {
                list.add(itemstack);
            }


            if (list.isEmpty() && player.getAbilities().instabuild) {
                list.add(new ItemStack(Items.ARROW));
            }

            return list;
        }
    }


    public static float bezier(float x, float min, float max) {
        return Mth.clamp(((x * x) * (3 - 2 * x)) / (1 / (max - min)) + min, min, max);
    }


}

	