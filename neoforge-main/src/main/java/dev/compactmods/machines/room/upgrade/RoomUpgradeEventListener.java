package dev.compactmods.machines.room.upgrade;

import dev.compactmods.machines.room.Rooms;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.function.Consumer;

public class RoomUpgradeEventListener {

   public static void onTooltips(ItemTooltipEvent evt) {
	  Item.TooltipContext ctx = evt.getContext();
	  Consumer<Component> tooltips = evt.getToolTip()::add;
	  TooltipFlag flags = evt.getFlags();

	  ItemStack stack = evt.getItemStack();

	  stack.addToTooltip(RoomUpgrades.UPGRADE_LIST_COMPONENT, ctx, tooltips, flags);
   }

   public static void registerGameEvents(IEventBus modBus) {
	  NeoForge.EVENT_BUS.addListener(RoomUpgradeEventListener::onTooltips);
   }
}
