package dev.compactmods.machines.api.room;

import dev.compactmods.machines.api.room.spatial.IRoomBoundaries;
import dev.compactmods.machines.api.room.spatial.IRoomChunks;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManager;

import java.util.function.Supplier;

public record RoomInstance(String code, int defaultMachineColor, IRoomBoundaries boundaries) {

}
