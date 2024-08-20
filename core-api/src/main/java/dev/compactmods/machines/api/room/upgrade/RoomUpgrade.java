package dev.compactmods.machines.api.room.upgrade;

import dev.compactmods.machines.api.room.upgrade.events.RoomUpgradeEvent;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.stream.Stream;

public interface RoomUpgrade extends TooltipProvider {

	RoomUpgradeDefinition<?> getType();

   default Stream<RoomUpgradeEvent> gatherEvents() {
	  return Stream.empty();
   }
}
