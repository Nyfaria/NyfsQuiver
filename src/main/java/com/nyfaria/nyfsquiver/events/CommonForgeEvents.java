package com.nyfaria.nyfsquiver.events;

import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.cap.QuiverHolderAttacher;
import com.nyfaria.nyfsquiver.config.NQConfig;
import com.nyfaria.nyfsquiver.init.EnchantmentInit;
import com.nyfaria.nyfsquiver.init.TagInit;
import com.nyfaria.nyfsquiver.items.QuiverInventory;
import com.nyfaria.nyfsquiver.items.QuiverItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.core.jmx.Server;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Predicate;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void arrowPickup(final EntityItemPickupEvent e) {
        ItemStack toPickup = e.getItem().getItem();
        Player player = e.getPlayer();
        ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.getItem() instanceof QuiverItem, player)
                .map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);

        if (toPickup.is(TagInit.QUIVER_ITEMS) && !quiverStack.isEmpty()) {
            QuiverInventory qi = QuiverItem.getInventory(quiverStack);
            int slots = qi.getSlots();
            for (int s = 0; s < slots; s++) {
                ItemStack currentStack = qi.getStackInSlot(s);
                ItemStack rem2 = toPickup.copy();
                if (currentStack.getItem() == toPickup.getItem() || currentStack.isEmpty()) {
                    rem2 = qi.insertItem(s, rem2, false);
                }
                toPickup.setCount(rem2.getCount());
            }
        }
    }


    @SubscribeEvent
    public static void startUsingBow(LivingGetProjectileEvent e) {

        if (!(e.getProjectileWeaponItemStack().getItem() instanceof ProjectileWeaponItem)) {
            return;
        }
        Predicate<ItemStack> predicate = ((ProjectileWeaponItem) e.getProjectileWeaponItemStack().getItem()).getSupportedHeldProjectiles();
        ItemStack itemStack;
        if(!CuriosApi.getCuriosHelper().findFirstCurio(e.getEntityLiving(), NyfsQuiver.QUIVER_PREDICATE).isPresent()) {
            if(NQConfig.INSTANCE.requireQuiver.get() && e.getEntityLiving() instanceof Player) {
                e.setProjectileItemStack(ItemStack.EMPTY);
            }
        }
        if (!CuriosApi.getCuriosHelper().findFirstCurio(e.getEntityLiving(),NyfsQuiver.QUIVER_PREDICATE).isPresent()){
            return;
        }

        ItemStack quiverStack = CuriosApi.getCuriosHelper().findEquippedCurio(NyfsQuiver.QUIVER_PREDICATE, e.getEntityLiving()).get().right;
        if (quiverStack.isEmpty() || !(quiverStack.getItem() instanceof QuiverItem)) {
            return;
        }

        QuiverInventory quiverInventory = QuiverItem.getInventory(quiverStack);
        itemStack = quiverInventory.getStackInSlot(QuiverHolderAttacher.getQuiverHolderUnwrap(quiverStack).getCurrentSlot());
//		ItemStack stack = CuriosApi.getCuriosHelper().findEquippedCurio(item -> item.is(TagInit.QUIVER_ITEMS),(Player)(Object)this)
//				.map(stringIntegerItemStackImmutableTriple -> stringIntegerItemStackImmutableTriple.right).orElse(ItemStack.EMPTY);


        if (predicate.test(itemStack)) {
            if (e.getEntityLiving().level.isClientSide()) {
                e.setProjectileItemStack(itemStack);
            } else {

                QuiverItem.useQuiver(quiverStack, (ServerPlayer) e.getEntityLiving(), itemStack);
                e.setProjectileItemStack(itemStack);

            }
        }




    }

//    @SubscribeEvent
//    public static void onClone(PlayerEvent.Clone event){
//        if(event.isWasDeath()){
//            event.getOriginal().revive();
//            Inventory oldInventory = event.getOriginal().getInventory();
//            oldInventory.items.removeAll(event.getOriginal().getInventory().items.stream().filter(
//                    itemStack -> EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOULBOUND.get(),itemStack) == 0).toList());
//            oldInventory.armor.removeAll(event.getOriginal().getInventory().items.stream().filter(
//                    itemStack -> EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOULBOUND.get(),itemStack) == 0).toList());
//            event.getPlayer().getInventory().replaceWith(oldInventory);
//        }
//    }

    @SubscribeEvent
    public static void attachCaps(AttachCapabilitiesEvent<ItemStack> e) {
//        ItemStack stack = e.getObject();
//        if (ItemTags.getAllTags().getTag(new ResourceLocation("curios","arrows")) != null
//                && NyfsQuiver.ARROWS_CURIO.contains(stack.getItem())) {
//            ArrowsCurio arrowCurio = new ArrowsCurio(stack);
//            e.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
//                final LazyOptional<ICurio> arrowsCurioCap = LazyOptional.of(() -> arrowCurio);
//
//                @Nonnull
//                @Override
//                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
//                                                         @Nullable Direction side) {
//                    return CuriosCapability.ITEM.orEmpty(cap, arrowsCurioCap);
//                }
//
//            });
//        }
    }
}
