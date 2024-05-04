package dev.compactmods.machines.api.room.upgrade.events;

import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.api.room.upgrade.RoomUpgrade;
import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface LevelLoadedUpgradeEventListener {

    /**
     * Called when a level is loaded, typically when the server first boots up.
     */
    void onLevelLoaded(ServerLevel level, RoomInstance room);

}
