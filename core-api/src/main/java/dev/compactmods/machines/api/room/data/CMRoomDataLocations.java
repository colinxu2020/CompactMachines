package dev.compactmods.machines.api.room.data;

import dev.compactmods.machines.api.CompactMachines;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;
import java.util.function.Function;

public interface CMRoomDataLocations {

   Function<MinecraftServer, Path> REGISTRATION_DATA = (server) -> server.getWorldPath(LevelResource.ROOT)
	   .resolve(CompactMachines.MOD_ID)
	   .resolve("data");

   Function<MinecraftServer, Path> SPAWN_DATA = (server) -> server.getWorldPath(LevelResource.ROOT)
	   .resolve(CompactMachines.MOD_ID)
	   .resolve("data")
	   .resolve("spawn_data");

   Function<MinecraftServer, Path> ROOM_DATA_ATTACHMENTS = (server) -> server.getWorldPath(LevelResource.ROOT)
	   .resolve(CompactMachines.MOD_ID)
	   .resolve("data")
	   .resolve("room_data");
}
