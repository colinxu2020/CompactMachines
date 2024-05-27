package dev.compactmods.machines.api.room.upgrade.events;

import dev.compactmods.machines.api.room.RoomInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface UpgradeAppliedEventListener extends RoomUpgradeEvent {

    /**
     * Called when an upgrade is first applied to a room.
     */
    @Override
    void handle(ServerLevel level, RoomInstance room, ItemStack upgrade);
}
