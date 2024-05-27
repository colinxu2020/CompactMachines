package dev.compactmods.machines.api.room.upgrade.events;

import dev.compactmods.machines.api.room.RoomInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface LevelUnloadedUpgradeEventListener extends RoomUpgradeEvent {

    /**
     * Called when a level is unloaded.
     */
    @Override
    void handle(ServerLevel level, RoomInstance room, ItemStack sourceItem);
}
