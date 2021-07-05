package com.nyfaria.nyfsquiver;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ObjectHolder;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.type.capability.ICurio;

import top.theillusivec4.curios.client.gui.CuriosScreen;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.nyfaria.nyfsquiver.client.ClientProxy;
import com.nyfaria.nyfsquiver.client.curios.ArrowsCurio;
import com.nyfaria.nyfsquiver.common.CommonProxy;
import com.nyfaria.nyfsquiver.common.containers.QuiverContainer;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import com.nyfaria.nyfsquiver.common.items.QuiverItem;
import com.nyfaria.nyfsquiver.common.items.QuiverStorageManager;
import com.nyfaria.nyfsquiver.common.items.QuiverType;
import com.nyfaria.nyfsquiver.compat.Compatibility;
import com.nyfaria.nyfsquiver.packets.PacketMaxLayers;
import com.nyfaria.nyfsquiver.packets.PacketNextSlot;
import com.nyfaria.nyfsquiver.packets.PacketPreviousSlot;
import com.nyfaria.nyfsquiver.packets.PacketRename;
import net.minecraft.item.FireworkRocketItem;


import static net.minecraft.client.gui.screen.inventory.ContainerScreen.INVENTORY_LOCATION;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NyfsQuiver.MOD_ID)
public class NyfsQuiver
{
		public static final String MOD_ID = "nyfsquiver";

		public static boolean drawn = false;
		public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("nyfsquiver", "main"), () -> "1", "1"::equals, "1"::equals);

	    @SuppressWarnings("deprecation")
		public static CommonProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new CommonProxy()); 

	    @ObjectHolder("nyfsquiver:container")
	    public static ContainerType<QuiverContainer> container;

	    @ObjectHolder("nyfsquiver:basicquiver")
	    public static QuiverItem basicQuiver;
	    @ObjectHolder("nyfsquiver:ironquiver")
	    public static QuiverItem ironQuiver;
	    @ObjectHolder("nyfsquiver:copperquiver")
	    public static QuiverItem copperQuiver;
	    @ObjectHolder("nyfsquiver:silverquiver")
	    public static QuiverItem silverQuiver;
	    @ObjectHolder("nyfsquiver:goldquiver")
	    public static QuiverItem goldQuiver;
	    @ObjectHolder("nyfsquiver:diamondquiver")
	    public static QuiverItem diamondQuiver;
	    @ObjectHolder("nyfsquiver:netheritequiver")
	    public static QuiverItem netheriteQuiver;
	    

	    public static final IRecipeSerializer<QuiverRecipe> QUIVER_RECIPE_SERIALIZER = new QuiverRecipe.Serializer();

		public static ITag<Item> ARROWS_CURIO = ItemTags.bind(new ResourceLocation("curios", "arrows").toString());
		public static ITag<Item> QUIVER_CURIO = ItemTags.bind(new ResourceLocation("curios", "quiver").toString());

		public static final Predicate<ItemStack> arrow_predicate = stack -> stack.getItem().is(ARROWS_CURIO);
		public static final Predicate<ItemStack> quiver_predicate = stack -> stack.getItem().is(QUIVER_CURIO);
		
	    public static float interpolation;
	    static @Nullable
		public ItemStack lastHeld;
	    static @Nullable
		public List<ItemStack> lastReadyArrows;
	    public static final ResourceLocation WIDGETS;
	    public static final ResourceLocation SLOT;

	    static {
	        interpolation = 0;
	        lastHeld = null;
	        lastReadyArrows = null;
	        WIDGETS = new ResourceLocation(MOD_ID,"textures/gui/widgets.png");
	        SLOT = new ResourceLocation(MOD_ID,"textures/gui/equipmentslot.png");
	    }

		
	    public NyfsQuiver(){
			IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

	        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NQConfig.CONFIG_SPEC);
	        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, NQConfig_Client.CLIENT_SPEC);
	        bus.addListener(this::init);
	        bus.addListener(this::interModEnqueue);
	        EVENT_BUS.register(QuiverStorageManager.class);
			EVENT_BUS.addGenericListener(ItemStack.class, this::attachCaps);
	        CHANNEL.registerMessage(0, PacketRename.class, PacketRename::encode, PacketRename::decode, PacketRename::handle);
	        CHANNEL.registerMessage(1, PacketMaxLayers.class, PacketMaxLayers::encode, PacketMaxLayers::decode, PacketMaxLayers::handle);
	        CHANNEL.registerMessage(2, PacketNextSlot.class, (msg, buffer) -> {},buffer -> new PacketNextSlot(0), PacketNextSlot::handle);
	        CHANNEL.registerMessage(3, PacketPreviousSlot.class, (msg, buffer) -> {},buffer -> new PacketPreviousSlot(0), PacketPreviousSlot::handle);

			if (FMLEnvironment.dist == Dist.CLIENT) {
				//EVENT_BUS.addListener(this::drawSlotBackground);
				bus.addListener(this::stitchTextures);
			}
	    }

	    public void init(FMLCommonSetupEvent e){
	        Compatibility.init();
	        proxy.init();
	    }

	    public void interModEnqueue(InterModEnqueueEvent e){
		      InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("quiver").size(1).icon(new ResourceLocation("nyfsquiver","gui/basicquiver")).build());
		      InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("arrows").size(1).hide().build());
	    }

/*		private void drawSlotBackground(GuiContainerEvent.DrawBackground e) {
			if (e.getGuiContainer() instanceof CuriosScreen) {
				Minecraft.getInstance().getTextureManager().getTexture(SLOT);
				CuriosScreen curiosScreen = (CuriosScreen) e.getGuiContainer();
				int i = curiosScreen.getGuiLeft();
				int j = curiosScreen.getGuiTop();
				curiosScreen.blit(e.getMatrixStack(),i + NQConfig.x.get(), j + NQConfig.y.get(), 7, 7, 18, 18);
			}
		} 
	*/
		
	    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	    public static class RegistryEvents {
	        @SubscribeEvent
	        public static void onItemRegistry(final RegistryEvent.Register<Item> e){
	            for(QuiverType type : QuiverType.values())
	                e.getRegistry().register(new QuiverItem(type));
	        }

	        @SubscribeEvent
	        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> e){
	            e.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
	                int bagSlot = data.readInt();
	                int inventoryIndex = data.readInt();
	                int rows = data.readInt();
	                int size = data.readInt();
	                Set<Integer> bagsInThisBag = new HashSet<>(size);
	                for(int i = 0; i < size; i++)
	                    bagsInThisBag.add(data.readInt());
	                size = data.readInt();
	                Set<Integer> bagsThisBagIsIn = new HashSet<>(size);
	                for(int i = 0; i < size; i++)
	                    bagsThisBagIsIn.add(data.readInt());
	                int layer = data.readInt();
	                return new QuiverContainer(windowId, inv, bagSlot, inventoryIndex, rows, bagsInThisBag, bagsThisBagIsIn, layer);
	            }).setRegistryName("container"));
	        }

	        @SubscribeEvent
	        public static void onRecipeRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> e){
	            e.getRegistry().register(QUIVER_RECIPE_SERIALIZER.setRegistryName(new ResourceLocation("nyfsquiver", "quiverrecipe")));
	        }
	    }
		private void attachCaps(AttachCapabilitiesEvent<ItemStack> e) {
			ItemStack stack = e.getObject();
			if (ItemTags.getAllTags().getTag(new ResourceLocation("curios","arrows")) != null
					&& ARROWS_CURIO.contains(stack.getItem())) {
				ArrowsCurio arrowCurio = new ArrowsCurio(stack);
            	e.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
                final LazyOptional<ICurio> arrowsCurioCap = LazyOptional.of(() -> arrowCurio);

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
                                                         @Nullable Direction side) {
                    return CuriosCapability.ITEM.orEmpty(cap, arrowsCurioCap);
                }

            });
			}
		}
		public static ICapabilityProvider createProvider(ICurio curio) {
			return new Provider(curio);
		}
		public static class Provider implements ICapabilityProvider {
			final LazyOptional<ICurio> capability;

			Provider(ICurio curio) {
				this.capability = LazyOptional.of(() -> curio);
			}

			@Nonnull
			public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
				return CuriosCapability.ITEM.orEmpty(cap, this.capability);
			}
		}
	    
		@SuppressWarnings("unused")
		public static List<ItemStack> findAmmos(PlayerEntity player, ItemStack shootable) {
			ItemStack itemstack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof ArrowItem ||item.getItem() instanceof FireworkRocketItem,player)
					.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
			ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem,player)
					.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);
			
			
			
			
			//if (!(shootable.getItem() instanceof ShootableItem)) {
			if ((quiverStack.isEmpty())) {
			            return Lists.newLinkedList();
	        } else {
	        	 List<ItemStack> list = Lists.newLinkedList();

	             Predicate<ItemStack> predicate = ((ShootableItem) shootable.getItem()).getSupportedHeldProjectiles();
	             //ItemStack itemstack = ShootableItem.getHeldProjectile(player, predicate);
	             if (!itemstack.isEmpty()) {
	                 list.add(itemstack);
	             }
	             //predicate = ((ShootableItem) shootable.getItem()).getAllSupportedProjectiles();

	             //for(int i = 0; i < player.inventory.getContainerSize(); ++i) {
	             //    ItemStack itemstack1 = player.inventory.getItem(i);
	             //    if (predicate.test(itemstack1) && itemstack1 != itemstack) {
	             //        list.add(itemstack1);
	             //    }
	             //}

	             if(list.isEmpty() && player.abilities.instabuild) {
	                 list.add(new ItemStack(Items.ARROW));
	             }


	            return list;
	        }
	    }

		
		public static float bezier(float x, float min, float max) {
	        return MathHelper.clamp(((x * x) * (3 - 2 * x)) / (1 / (max - min)) + min, min, max);
	    }
		 @Mod.EventBusSubscriber
		    public static class Events {
		        
		 }

			
		 public void stitchTextures(TextureStitchEvent.Pre evt) {
			 if (evt.getMap().location().equals(PlayerContainer.BLOCK_ATLAS)) {

				 evt.addSprite(new ResourceLocation(MOD_ID, "gui/basicquiver"));
				 evt.addSprite(new ResourceLocation(MOD_ID, "gui/equipmentslot"));

			 }
		 }
}

	