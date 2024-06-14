package dev.compactmods.machines.room.spawn;

import dev.compactmods.machines.api.room.registration.IRoomRegistrar;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManagers;

import java.util.HashMap;

public class RoomSpawnManagers implements IRoomSpawnManagers {

    private final HashMap<String, IRoomSpawnManager> spawnManagers;
    private final IRoomRegistrar roomReg;

    public RoomSpawnManagers(IRoomRegistrar roomReg) {
        this.roomReg = roomReg;
        this.spawnManagers = new HashMap<>();

        roomReg.allRooms().forEach(roomInstance -> {
            final var manager = new SpawnManager(roomInstance.code(), roomInstance.boundaries());
            spawnManagers.put(roomInstance.code(), manager);
        });
    }

    @Override
    public IRoomSpawnManager get(String roomCode) {
        return spawnManagers.computeIfAbsent(roomCode, (code) -> roomReg.get(roomCode)
                .map(inst -> new SpawnManager(roomCode, inst.boundaries()))
                .orElseThrow());
    }
}
