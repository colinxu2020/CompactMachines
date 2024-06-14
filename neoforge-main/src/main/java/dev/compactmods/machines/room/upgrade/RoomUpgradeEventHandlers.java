package dev.compactmods.machines.room.upgrade;

import dev.compactmods.machines.api.CompactMachines;
import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.api.room.upgrade.events.lifecycle.UpgradeTickedEventListener;
import dev.compactmods.machines.room.Rooms;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RoomUpgradeEventHandlers {

   public static void onLevelTick(LevelTickEvent.Post postTick) {
	  if (postTick.getLevel() instanceof ServerLevel serverLevel && CompactDimension.isLevelCompact(serverLevel)) {
		 final var rooms = CompactMachines.roomApi()
			 .registrar()
			 .allRooms()
			 .collect(Collectors.toUnmodifiableSet());

		 for (var room : rooms) {
			CompactMachines.existingRoomData(room.code())
				.flatMap(rd -> rd.getExistingData(Rooms.DataAttachments.UPGRADE_INV))
				.ifPresent(appliedUpgrades -> {
				   final var upgradeStacks = appliedUpgrades.items()
					   .collect(Collectors.toUnmodifiableSet());

				   for (final var upgradeStack : upgradeStacks) {
					  final var upgrades = upgradeStack.get(RoomUpgrades.UPGRADE_LIST_COMPONENT);
					  upgrades.upgrades().stream()
						  .flatMap(ru -> ru.gatherEvents().filter(UpgradeTickedEventListener.class::isInstance))
						  .map(UpgradeTickedEventListener.class::cast)
						  .forEach(ticker -> {
							 ticker.handle(serverLevel, room, upgradeStack);
						  });
				   }
				});
		 }
	  }
   }

   public static void onTooltips(ItemTooltipEvent evt) {
	  Item.TooltipContext ctx = evt.getContext();
	  Consumer<Component> tooltips = evt.getToolTip()::add;
	  TooltipFlag flags = evt.getFlags();

	  ItemStack stack = evt.getItemStack();

	  stack.addToTooltip(RoomUpgrades.UPGRADE_LIST_COMPONENT, ctx, tooltips, flags);
   }
}
