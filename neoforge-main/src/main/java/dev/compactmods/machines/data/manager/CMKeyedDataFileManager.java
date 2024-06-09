package dev.compactmods.machines.data.manager;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.LoggingUtil;
import dev.compactmods.machines.data.CMDataFile;
import dev.compactmods.machines.data.CodecHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.BiFunction;

public class CMKeyedDataFileManager<Key, T extends CMDataFile & CodecHolder<T>> implements IKeyedDataFileManager<Key, T> {

   protected final MinecraftServer server;
   private final BiFunction<MinecraftServer, Key, T> creator;
   private final HashMap<Key, T> cache;

   public CMKeyedDataFileManager(MinecraftServer server, BiFunction<MinecraftServer, Key, T> creator) {
	  this.server = server;
	  this.creator = creator;
	  this.cache = new HashMap<>();
   }

   @Override
   public T data(Key key) {
	  return cache.computeIfAbsent(key, k -> {
		 var inst = creator.apply(server, k);
		 var dir = inst.getDataLocation(server);
		 CMDataFile.ensureDirExists(dir);
		 return loadItem(dir.resolve(k.toString() + ".dat").toFile(), inst.codec());
	  });
   }

   public void save() {
	  cache.forEach((key, data) -> {
		 var fullData = new CompoundTag();
		 fullData.putString("version", data.getDataVersion());

		 var fileData = data.codec()
			 .encodeStart(NbtOps.INSTANCE, data)
			 .getOrThrow();

		 fullData.put("data", fileData);

		 try {
			NbtIo.writeCompressed(fullData, data.getDataLocation(server).resolve(key.toString() + ".dat"));
		 } catch (IOException e) {
			LoggingUtil.modLog().error("Failed to write data: " + e.getMessage(), e);
		 }
	  });
   }

   protected T loadItem(File file, Codec<T> codec) {
	  try (var is = new FileInputStream(file)) {
		 final var tag = NbtIo.readCompressed(is, NbtAccounter.unlimitedHeap());
		 return codec.parse(NbtOps.INSTANCE, tag.contains("data") ? tag.getCompound("data") : new CompoundTag())
			 .getOrThrow();

	  } catch (IOException e) {
		 throw new RuntimeException(e);
	  }
   }

   public boolean hasData(String code) {
	  return cache.containsKey(code);
   }
}
