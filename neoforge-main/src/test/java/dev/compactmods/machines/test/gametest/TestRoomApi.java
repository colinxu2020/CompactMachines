package dev.compactmods.machines.test.gametest;

import com.google.common.base.Predicates;
import dev.compactmods.machines.api.room.IRoomApi;
import dev.compactmods.machines.api.room.registration.IRoomRegistrar;
import dev.compactmods.machines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.machines.api.room.spatial.IRoomChunks;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.machines.room.RoomRegistrar;
import dev.compactmods.machines.room.spatial.GraphChunkManager;
import dev.compactmods.machines.room.spawn.RoomSpawnManagers;

import java.util.function.Predicate;

public class TestRoomApi {

    public static IRoomApi forTest() {
	   final var registrar = new RoomRegistrar();
	   final var spawnManagers = new RoomSpawnManagers(registrar);
	   final var chunkManager = new GraphChunkManager();

	   return new IRoomApi() {
		  @Override
		  public Predicate<String> roomCodeValidator() {
			 return Predicates.alwaysTrue();
		  }

		  @Override
		  public IRoomRegistrar registrar() {
			 return registrar;
		  }

		  @Override
		  public IRoomSpawnManager spawnManager(String roomCode) {
			 return spawnManagers.get(roomCode);
		  }

		  @Override
		  public IRoomChunkManager chunkManager() {
			 return chunkManager;
		  }

		  @Override
		  public IRoomChunks chunks(String roomCode) {
			 return chunkManager.get(roomCode);
		  }
	   };
	}
}
