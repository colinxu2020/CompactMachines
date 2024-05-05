package dev.compactmods.machines.room.spawn;

import dev.compactmods.machines.api.room.IRoomRegistrar;
import dev.compactmods.machines.api.room.spatial.IRoomBoundaries;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManagers;
import dev.compactmods.machines.api.dimension.MissingDimensionException;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;

public class RoomSpawnManagers implements IRoomSpawnManagers {

    private final HashMap<String, IRoomSpawnManager> spawnManagers;
    private final IRoomRegistrar roomReg;
    private final MinecraftServer server;

    public RoomSpawnManagers(MinecraftServer server, IRoomRegistrar roomReg) {
        this.roomReg = roomReg;
        this.spawnManagers = new HashMap<>();
        this.server = server;

        roomReg.allRooms().forEach(roomInstance -> {
            final var manager = createManager(roomInstance.code(), roomInstance.boundaries());
            spawnManagers.put(roomInstance.code(), manager);
        });
    }

    private IRoomSpawnManager createManager(String roomCode, IRoomBoundaries bounds) {
        try {
            return SpawnManager.forRoom(server, roomCode, bounds);
        } catch (MissingDimensionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IRoomSpawnManager get(String roomCode) {
        return spawnManagers.computeIfAbsent(roomCode, (code) -> roomReg.get(roomCode)
                .map(inst -> createManager(roomCode, inst.boundaries()))
                .orElseThrow());
    }
}
