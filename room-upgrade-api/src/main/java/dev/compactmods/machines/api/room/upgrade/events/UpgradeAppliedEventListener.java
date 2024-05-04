package dev.compactmods.machines.api.room.upgrade.events;

import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.api.room.upgrade.RoomUpgrade;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface UpgradeAppliedEventListener {

    /**
     * Called when an upgrade is first applied to a room.
     */
    void onAdded(ServerLevel level, RoomInstance room, ItemStack upgradeItem);
}
