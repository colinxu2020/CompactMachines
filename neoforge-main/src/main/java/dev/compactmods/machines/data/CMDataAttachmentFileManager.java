package dev.compactmods.machines.data;

import dev.compactmods.machines.LoggingUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.attachment.AttachmentHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.BiFunction;

public class CMDataAttachmentFileManager<Key, T extends AttachmentHolder & CMDataFile> {

   protected final MinecraftServer server;
   private final BiFunction<MinecraftServer, Key, T> creator;
   private final HashMap<Key, T> cache;

   protected CMDataAttachmentFileManager(MinecraftServer server, BiFunction<MinecraftServer, Key, T> creator) {
	  this.server = server;
	  this.creator = creator;
	  this.cache = new HashMap<>();
   }

   public T data(Key key) {
	  return cache.computeIfAbsent(key, k -> creator.apply(server, key));
   }

   public void save(HolderLookup.Provider provider) {
	  cache.forEach((key, data) -> {
		 CompoundTag fullTag = new CompoundTag();
		 fullTag.putString("version", "1.0");

		 if(data.hasAttachments()) {
			var ad = data.serializeAttachments(provider);
			if(ad != null)
			   fullTag.put("attachments", ad);
		 }

		 try {
			var path = data.getDataLocation(server).resolve(key.toString() + ".dat");
			NbtIo.writeCompressed(fullTag, path);
		 } catch (IOException e) {
			LoggingUtil.modLog().error("Failed to write data: " + e.getMessage(), e);
		 }
	  });
   }

   public boolean hasData(String code) {
	  return cache.containsKey(code);
   }

}
