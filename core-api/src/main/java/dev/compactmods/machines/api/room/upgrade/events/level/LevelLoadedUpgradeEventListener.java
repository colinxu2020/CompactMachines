package dev.compactmods.machines.api.room.upgrade.events.level;

import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.api.room.upgrade.events.RoomUpgradeEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface LevelLoadedUpgradeEventListener extends RoomUpgradeEvent {

    /**
     * Called when a level is loaded, typically when the server first boots up.
     */
    @Override
    void handle(ServerLevel level, RoomInstance room, ItemStack upgrade);
}