package com.nyfaria.nyfsquiver;

import com.google.common.collect.Lists;
import com.nyfaria.nyfsquiver.init.*;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import com.nyfaria.nyfsquiver.items.QuiverModels;
import com.nyfaria.nyfsquiver.items.QuiverStorageManager;
import com.nyfaria.nyfsquiver.curios.QuiverCurios;
import com.nyfaria.nyfsquiver.config.NQConfig;
import com.nyfaria.nyfsquiver.config.NQConfig_Client;
import com.nyfaria.nyfsquiver.packets.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NyfsQuiver.MOD_ID)
public class NyfsQuiver
{
		public static final String MOD_ID = "nyfsquiver";

		public static boolean drawn = false;
		public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "main"), () -> "1", "1"::equals, "1"::equals);

		public static Tag<Item> QUIVER_CURIO = ItemTags.bind(new ResourceLocation("curios", "quiver").toString());
		public static final Predicate<ItemStack> QUIVER_PREDICATE = stack -> stack.is(QUIVER_CURIO);

	    public static float interpolation = 0;
	    static @Nullable
		public ItemStack lastHeld = null;
	    static @Nullable
		public List<ItemStack> lastReadyArrows = null;
	    public static final ResourceLocation WIDGETS = new ResourceLocation(MOD_ID,"textures/gui/widgets.png");

		public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(MOD_ID) {
		@Override
		@NotNull
		public ItemStack makeIcon() {
			return new ItemStack(ItemInit.BASIC_QUIVER.get());
		}
		};

	    public NyfsQuiver(){
			ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NQConfig.CONFIG_SPEC);
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, NQConfig_Client.CLIENT_SPEC);
			IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
			ItemInit.ITEMS.register(bus);
			ContainerInit.CONTAINERS.register(bus);
			RecipeInit.RECIPES.register(bus);
			EnchantmentInit.ENCHANTMENTS.register(bus);
			QuiverModels.init();
	        EVENT_BUS.register(QuiverStorageManager.class);

	        CHANNEL.registerMessage(0, PacketRename.class, PacketRename::encode, PacketRename::decode, PacketRename::handle);
	        CHANNEL.registerMessage(1, PacketMaxLayers.class, PacketMaxLayers::encode, PacketMaxLayers::decode, PacketMaxLayers::handle);
	        CHANNEL.registerMessage(2, PacketNextSlot.class, (msg, buffer) -> {},buffer -> new PacketNextSlot(0), PacketNextSlot::handle);
	        CHANNEL.registerMessage(3, PacketPreviousSlot.class, (msg, buffer) -> {},buffer -> new PacketPreviousSlot(0), PacketPreviousSlot::handle);
	        CHANNEL.registerMessage(4, PacketOpenQuiver.class, (msg, buffer) -> {}, buffer -> new PacketOpenQuiver(), PacketOpenQuiver::handle);

			TagInit.init();

	    }


		public static List<ItemStack> findAmmos(Player player, ItemStack shootable) {

			ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem,player)
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


	             if(list.isEmpty() && player.getAbilities().instabuild) {
	                 list.add(new ItemStack(Items.ARROW));
	             }

	            return list;
	        }
	    }

		
		public static float bezier(float x, float min, float max) {
	        return Mth.clamp(((x * x) * (3 - 2 * x)) / (1 / (max - min)) + min, min, max);
	    }




}

	