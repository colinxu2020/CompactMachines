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
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Function;

public class CMSingletonDataFileManager<T extends CMDataFile & CodecHolder<T>> implements IDataFileManager<T> {

   protected final MinecraftServer server;
   private final Function<MinecraftServer, T> creator;
   private final String dataKey;
   private @Nullable T instance;

   public CMSingletonDataFileManager(MinecraftServer server, String dataKey, Function<MinecraftServer, T> creator) {
	  this.server = server;
	  this.dataKey = dataKey;
	  this.creator = creator;
   }

   public T data() {
	  if (instance == null) {
		 var inst = creator.apply(server);
		 var dir = inst.getDataLocation(server);
		 CMDataFile.ensureDirExists(dir);
		 final var file = dir.resolve(dataKey + ".dat").toFile();
		 instance = file.exists() ? loadItem(file, inst.codec()) : inst;
	  }

	  return this.instance;
   }

   public void save() {
	  if (instance != null) {
		 var fullData = new CompoundTag();
		 fullData.putString("version", instance.getDataVersion());

		 var fileData = instance.codec()
			 .encodeStart(NbtOps.INSTANCE, instance)
			 .getOrThrow();

		 fullData.put("data", fileData);

		 try {
			NbtIo.writeCompressed(fullData, instance.getDataLocation(server).resolve(dataKey + ".dat"));
		 } catch (IOException e) {
			LoggingUtil.modLog().error("Failed to write data: " + e.getMessage(), e);
		 }
	  }
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
}
