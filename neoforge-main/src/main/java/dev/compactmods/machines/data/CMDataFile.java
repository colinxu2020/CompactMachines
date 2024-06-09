package dev.compactmods.machines.data;

import dev.compactmods.machines.api.CompactMachinesApi;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface CMDataFile {

   default String getDataVersion() {
      return "1.0.0";
   }

   default Path getDataLocation(MinecraftServer server) {
      return server.getWorldPath(LevelResource.ROOT)
          .resolve(CompactMachinesApi.MOD_ID)
          .resolve("data");
   }

   static void ensureDirExists(Path dir) {
      if(!Files.exists(dir)) {
         try {
            Files.createDirectories(dir);
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
      }
   }
}
