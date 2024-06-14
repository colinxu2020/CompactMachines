package dev.compactmods.machines.api.room.spatial;

import net.minecraft.world.level.ChunkPos;

import java.util.Optional;

public interface IRoomChunkManager {

    void calculateChunks(String roomCode, IRoomBoundaries boundaries);

    Optional<String> findRoomByChunk(ChunkPos chunk);

    IRoomChunks get(String room);
}
