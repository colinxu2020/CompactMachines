package dev.compactmods.machines.api.room.spawn;

import java.util.Optional;
import java.util.UUID;

public interface IRoomSpawns {
    IRoomSpawn defaultSpawn();

    Optional<IRoomSpawn> forPlayer(UUID player);
}
