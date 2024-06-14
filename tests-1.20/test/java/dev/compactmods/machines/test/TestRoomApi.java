package dev.compactmods.machines.test;

import com.google.common.base.Predicates;
import dev.compactmods.machines.api.dimension.MissingDimensionException;
import dev.compactmods.machines.api.room.ICompactMachines.roomApi();
import dev.compactmods.machines.room.CompactMachines.roomApi()Instance;
import dev.compactmods.machines.room.RoomRegistrar;
import dev.compactmods.machines.room.spatial.GraphChunkManager;
import dev.compactmods.machines.room.spawn.RoomSpawnManagers;
import dev.compactmods.machines.room.spawn.SpawnManager;
import net.minecraft.gametest.framework.GameTestHelper;

public class TestCompactMachines.roomApi() {

    public static ICompactMachines.roomApi() forTest(GameTestHelper test) {
	   final var registrar = new RoomRegistrar();
	   final var spawnManagers = new RoomSpawnManagers(registrar);
	   final var chunkManager = new GraphChunkManager();

	   return new CompactMachines.roomApi()Instance(Predicates.alwaysTrue(), registrar, spawnManagers, chunkManager);
	}
}
