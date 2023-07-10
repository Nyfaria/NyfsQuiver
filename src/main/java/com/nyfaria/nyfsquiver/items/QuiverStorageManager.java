package com.nyfaria.nyfsquiver.items;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.events.ClientModEvents;
import com.nyfaria.nyfsquiver.packets.PacketMaxLayers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;

public class QuiverStorageManager {

    private static final HashMap<Integer, QuiverInventory> inventories = new HashMap<>();
    public static int maxLayers;
    private static File directory;
    private static final int inventoryIndex = 0;

//	@SubscribeEvent
//	public static void onWorldSave(WorldEvent.Save event) {
//		if (event.getWorld().isClientSide() || !(event.getWorld() instanceof Level) || ((Level) event.getWorld()).dimension() != Level.OVERWORLD)
//			return;
//		save();
//	}
//
//	@SubscribeEvent
//	public static void onWorldLoad(WorldEvent.Load event) {
//		if (event.getWorld().isClientSide() || !(event.getWorld() instanceof Level) || ((Level) event.getWorld()).dimension() != Level.OVERWORLD)
//			return;
//		maxLayers = 0;
//		ServerLevel world = (ServerLevel) event.getWorld();
//		directory = new File(world.getServer().getWorldPath(LevelResource.ROOT).toFile(), "nyfsquiver/quivers");
//		load();
//	}

//	public static QuiverInventory getInventory(int index) {
//		QuiverInventory inventory = inventories.get(index);
//
////		if (inventory == null) {
////			File file = new File(directory, "inventory" + index + ".nbt");
////			if (file.exists()) {
////				inventory = new QuiverInventory(false, index);
////				inventory.load(file);
////				inventories.put(index, inventory);
////			}
////		}
//		return inventory;
//	}

//	public static int createInventoryIndex(QuiverType type) {
//		int index = inventoryIndex++;
//		inventories.put(index, new QuiverInventory(false, index, type.getRows(), type.getColumns()));
//		return index;
//	}

    public static void save() {
//		directory.mkdirs();
//		for (int i : inventories.keySet())
//			inventories.get(i).save(new File(directory, "inventory" + i + ".nbt"));
    }

    public static void load() {
//		File[] files = directory.listFiles();
//		inventories.clear();
//		if (files == null)
//			files = new File[0];
//		int highest = -1;
//		for (File file : files) {
//			String name = file.getName();
//			if (!name.startsWith("inventory") || !name.endsWith(".nbt"))
//				continue;
//			int index;
//			try {
//				index = Integer.parseInt(name.substring("inventory".length(), name.length() - ".nbt".length()));
//			} catch (NumberFormatException e) {
//				continue;
//			}
//			if (index > highest)
//				highest = index;
//
//			// for validation
//			QuiverInventory inventory = new QuiverInventory(false, index);
//			inventory.load(file);
//			inventories.put(index, inventory);
//		}
//
//		inventoryIndex = highest + 1;
//
//		// validation
//		for (Map.Entry<Integer, QuiverInventory> entry : inventories.entrySet()) {
//			QuiverInventory inventory = entry.getValue();
//			for (ItemStack stack : inventory.getStacks()) {
//				if (stack.getItem() instanceof QuiverItem && stack.getOrCreateTag().contains("invIndex")) {
//					int index = stack.getTag().getInt("invIndex");
//					if (!inventories.containsKey(index)) {
//						stack.getTag().remove("invIndex");
//						continue;
//					}
//				}
//			}
//		}
//
//		for (Map.Entry<Integer, QuiverInventory> entry : inventories.entrySet()) {
//			QuiverInventory inventory = entry.getValue();
//
//		}
//
//		save();
//		inventories.clear();
    }


    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent e) {
        if (!e.getEntity().getCommandSenderWorld().isClientSide)
            NyfsQuiver.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) e.getEntity()), new PacketMaxLayers(maxLayers));
    }

    public static ItemStack getCurrentSlotStack(ItemStack itemStack) {
        QuiverInventory quiverInventory = QuiverItem.getInventory(itemStack);
        return quiverInventory.getStackInSlot(QuiverHolderAttacher.getQuiverHolderUnwrap(itemStack).getCurrentSlot());
    }

    public static boolean increaseQuiverSlot(Player player, int Direction) {
        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem()
                instanceof QuiverItem, player);
        optional.ifPresent(triple ->
                QuiverHolderAttacher.getQuiverHolderUnwrap(triple.getRight()).changeCurrentSlot(1)
        );
        return optional.isPresent();
    }

    public static boolean decreaseQuiverSlot(Player player, int Direction) {
        Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem()
                instanceof QuiverItem, player);
        optional.ifPresent(triple ->
                QuiverHolderAttacher.getQuiverHolderUnwrap(triple.getRight()).changeCurrentSlot(-1)
        );
        return optional.isPresent();
    }

    public static void nextSlot(ItemStack stack, Player player, int bagSlot) {
        int quiverSize = QuiverItem.getInventory(stack).getSlots();
        int currentSlot = stack.getOrCreateTag().getInt("slotIndex");
        int newSlot = currentSlot + bagSlot;
        if (newSlot >= quiverSize) {
            newSlot = 0;
        }
        if (newSlot < 0) {
            newSlot = quiverSize - 1;
        }
        System.out.println("OldSlot: " + currentSlot + " NewSlot: " + newSlot);
        stack.getOrCreateTag().putInt("slotIndex", newSlot);
    }

    public static boolean openQuiver(Player playerIn) {
        Level worldIn = playerIn.level();

        ItemStack stack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem, playerIn)
                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);


        if (!playerIn.isShiftKeyDown()) {
            if (!worldIn.isClientSide() && stack.getItem() instanceof QuiverItem) {
                int bagSlot = 0;
                QuiverInventory inventory = QuiverItem.getInventory(stack);
                NetworkHooks.openScreen((ServerPlayer) playerIn, new QuiverItem.ContainerProvider(stack.getDisplayName(), bagSlot, inventory), a -> {
                    a.writeInt(bagSlot);
                    a.writeInt(inventory.rows);
                    a.writeInt(inventory.columns);
                    a.writeNbt(inventory.serializeNBT());
                });
                return true;
            }
        } else if (worldIn.isClientSide) {
            ClientModEvents.openScreen(stack.getItem().getName(stack).getContents().toString(), stack.getDisplayName().getContents().toString());
            return true;
        }
        return false;
    }

}

