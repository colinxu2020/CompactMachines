package dev.compactmods.machines.api.room.upgrade.events;

import dev.compactmods.machines.api.room.RoomInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface UpgradeRemovedEventListener extends RoomUpgradeEvent {

    /**
     * Called when an update is removed from a room.
     */
    @Override
    void handle(ServerLevel level, RoomInstance room, ItemStack upgrade);
}
