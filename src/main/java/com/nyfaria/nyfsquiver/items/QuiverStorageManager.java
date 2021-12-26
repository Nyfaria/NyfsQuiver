package com.nyfaria.nyfsquiver.items;

import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.events.ClientModEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.packets.PacketMaxLayers;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

public class QuiverStorageManager {

	private static File directory;
	private static final HashMap<Integer, QuiverInventory> inventories = new HashMap<>();
	private static int inventoryIndex = 0;

	public static int maxLayers;

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onJoin(PlayerEvent.PlayerLoggedInEvent e) {
		if (!e.getEntity().getCommandSenderWorld().isClientSide)
			NyfsQuiver.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) e.getEntityLiving()), new PacketMaxLayers(maxLayers));
	}

	public static ItemStack getCurrentSlotStack(ItemStack itemStack) {
		QuiverInventory quiverInventory = QuiverItem.getInventory(itemStack);
		return quiverInventory.getStackInSlot(QuiverHolderAttacher.getQuiverHolderUnwrap(itemStack).getCurrentSlot());
	}

	public static boolean increaseQuiverSlot(PlayerEntity player, int Direction) {
		Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem()
				instanceof QuiverItem, player);
		optional.ifPresent(triple ->
				QuiverHolderAttacher.getQuiverHolderUnwrap(triple.getRight()).changeCurrentSlot(1)
		);
		return optional.isPresent();
	}

	public static boolean decreaseQuiverSlot(PlayerEntity player, int Direction) {
		Optional<ImmutableTriple<String, Integer, ItemStack>> optional = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem()
				instanceof QuiverItem, player);
		optional.ifPresent(triple ->
				QuiverHolderAttacher.getQuiverHolderUnwrap(triple.getRight()).changeCurrentSlot(-1)
		);
		return optional.isPresent();
	}

	public static void nextSlot(ItemStack stack, PlayerEntity player, int bagSlot) {
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
	public static boolean openQuiver(PlayerEntity playerIn){
		World worldIn = playerIn.level;

		ItemStack stack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem,playerIn)
				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);


		if (!playerIn.isShiftKeyDown()) {
			if (!worldIn.isClientSide() && stack.getItem() instanceof QuiverItem) {
				int bagSlot = 0;
				QuiverInventory inventory = QuiverItem.getInventory(stack);
				NetworkHooks.openGui((ServerPlayerEntity) playerIn, new QuiverItem.ContainerProvider(stack.getDisplayName(), bagSlot, inventory), a -> {
					a.writeInt(bagSlot);
					a.writeInt(inventory.rows);
					a.writeInt(inventory.columns);
					a.writeNbt(inventory.serializeNBT());
				});
				return true;
			}
		} else if (worldIn.isClientSide) {
			ClientModEvents.openScreen(stack.getItem().getName(stack).getContents(), stack.getDisplayName().getContents());
			return true;
		}
		return false;
	}

}

