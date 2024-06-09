package dev.compactmods.machines.test;

import com.google.common.base.Predicates;
import dev.compactmods.machines.api.dimension.MissingDimensionException;
import dev.compactmods.machines.api.room.IRoomApi;
import dev.compactmods.machines.room.RoomApiInstance;
import dev.compactmods.machines.room.RoomRegistrar;
import dev.compactmods.machines.room.spatial.GraphChunkManager;
import dev.compactmods.machines.room.spawn.RoomSpawnManagers;
import dev.compactmods.machines.room.spawn.SpawnManager;
import net.minecraft.gametest.framework.GameTestHelper;

public class TestRoomApi {

    public static IRoomApi forTest(GameTestHelper test) {
	   final var registrar = new RoomRegistrar();
	   final var spawnManagers = new RoomSpawnManagers(registrar);
	   final var chunkManager = new GraphChunkManager();

	   return new RoomApiInstance(Predicates.alwaysTrue(), registrar, spawnManagers, chunkManager);
	}
}
