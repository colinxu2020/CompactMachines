package dev.compactmods.machines.data;

import dev.compactmods.machines.data.manager.CMKeyedDataFileManager;
import dev.compactmods.machines.data.manager.CMSingletonDataFileManager;
import dev.compactmods.machines.data.manager.IDataFileManager;
import dev.compactmods.machines.data.manager.IKeyedDataFileManager;
import dev.compactmods.machines.data.room.RoomDataAttachments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.util.function.BiFunction;
import java.util.function.Function;

public class DataManagerBuilder {

   public static <Key, T extends CMDataFile & CodecHolder<T>> DataManagerBuilder.Keyed<Key, T> keyed(MinecraftServer server, ResourceLocation id) {
	  return new Keyed<>(server, id);
   }

   public static <T extends CMDataFile & CodecHolder<T>> DataManagerBuilder.Singleton<T> singleton(MinecraftServer server, ResourceLocation id) {
	  return new Singleton<>(server, id);
   }

   public static class Keyed<Key, T extends CMDataFile & CodecHolder<T>> {

	  private final MinecraftServer server;
	  private final ResourceLocation id;
	  private BiFunction<MinecraftServer, Key, T> factory;

	  public Keyed(MinecraftServer server, ResourceLocation id) {
		 this.server = server;
		 this.id = id;
	  }

	  public IKeyedDataFileManager<Key, T> build() {
		 return new CMKeyedDataFileManager<>(server, factory);
	  }
   }

   public static class Singleton<T extends CMDataFile & CodecHolder<T>> {

	  private final MinecraftServer server;
	  private final ResourceLocation id;
	  private Function<MinecraftServer, T> factory;

	  public Singleton(MinecraftServer server, ResourceLocation id) {
		 this.server = server;
		 this.id = id;
	  }

	  public IDataFileManager<T> build() {
		 return new CMSingletonDataFileManager<>(server, "", factory);
	  }
   }
}
