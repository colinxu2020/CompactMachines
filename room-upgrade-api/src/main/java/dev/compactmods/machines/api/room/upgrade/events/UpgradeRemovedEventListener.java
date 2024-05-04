package dev.compactmods.machines.api.room.upgrade.events;

import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.api.room.upgrade.RoomUpgrade;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface UpgradeRemovedEventListener {

    /**
     * Called when an update is removed from a room.
     */
    void onRemoved(ServerLevel level, RoomInstance room, ItemStack upgradeItem);
}
