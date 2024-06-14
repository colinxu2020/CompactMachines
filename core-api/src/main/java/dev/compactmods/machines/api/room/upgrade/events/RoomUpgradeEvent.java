package dev.compactmods.machines.api.room.upgrade.events;

import dev.compactmods.machines.api.room.RoomInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

/**
 * Marker interface for all room upgrade events.
 */
@FunctionalInterface
public interface RoomUpgradeEvent {

   void handle(ServerLevel level, RoomInstance room, ItemStack upgrade);
}
