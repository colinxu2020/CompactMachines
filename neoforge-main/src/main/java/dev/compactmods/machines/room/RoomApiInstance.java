package dev.compactmods.machines.room;

import dev.compactmods.machines.api.dimension.MissingDimensionException;
import dev.compactmods.machines.api.room.IRoomApi;
import dev.compactmods.machines.api.room.IRoomRegistrar;
import dev.compactmods.machines.api.room.owner.IRoomOwners;
import dev.compactmods.machines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.machines.api.room.spatial.IRoomChunks;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManagers;
import dev.compactmods.machines.room.spatial.GraphChunkManager;
import dev.compactmods.machines.room.spawn.RoomSpawnManagers;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Predicate;

public record RoomApiInstance(
        Predicate<String> roomCodeValidator,
        IRoomRegistrar registrar,
        IRoomOwners owners,
        IRoomSpawnManagers spawnManagers,
        IRoomChunkManager chunkManager
) implements IRoomApi {

    @ApiStatus.Internal
    public static RoomApiInstance forServer(MinecraftServer server) throws MissingDimensionException {
        final IRoomRegistrar registrar = RoomRegistrar.forServer(server);
        final IRoomOwners owners = null;
        final IRoomSpawnManagers spawnManager = new RoomSpawnManagers(server, registrar);

        final var gcm = new GraphChunkManager();
        registrar.allRooms().forEach(inst -> gcm.calculateChunks(inst.code(), inst.boundaries()));

        return new RoomApiInstance(registrar::isRegistered, registrar, owners, spawnManager, gcm);
    }

    @Override
    public IRoomSpawnManager spawnManager(String roomCode) {
        return spawnManagers.get(roomCode);
    }

    @Override
    public IRoomChunks chunks(String roomCode) {
        return chunkManager.get(roomCode);
    }
}
