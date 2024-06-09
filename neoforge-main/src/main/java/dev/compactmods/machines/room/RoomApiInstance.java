package dev.compactmods.machines.room;

import dev.compactmods.machines.api.room.IRoomApi;
import dev.compactmods.machines.api.room.IRoomRegistrar;
import dev.compactmods.machines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.machines.api.room.spatial.IRoomChunks;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManagers;

import java.util.function.Predicate;

public record RoomApiInstance(
        Predicate<String> roomCodeValidator,
        IRoomRegistrar registrar,
        IRoomSpawnManagers spawnManagers,
        IRoomChunkManager chunkManager
) implements IRoomApi {

    @Override
    public IRoomSpawnManager spawnManager(String roomCode) {
        return spawnManagers.get(roomCode);
    }

    @Override
    public IRoomChunks chunks(String roomCode) {
        return chunkManager.get(roomCode);
    }
}
