package dev.compactmods.machines.api.room.upgrade.events;

import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.api.room.upgrade.RoomUpgrade;
import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface LevelUnloadedUpgradeEventListener {

    /**
     * Called when a level is unloaded.
     */
    void onLevelUnloaded(ServerLevel level, RoomInstance room);
}
