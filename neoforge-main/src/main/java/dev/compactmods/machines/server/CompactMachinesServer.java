package dev.compactmods.machines.server;

import dev.compactmods.machines.LoggingUtil;
import dev.compactmods.machines.api.CompactMachines;
import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.api.room.IRoomApi;
import dev.compactmods.machines.api.room.data.CMRoomDataLocations;
import dev.compactmods.machines.api.room.data.IRoomDataAttachmentAccessor;
import dev.compactmods.machines.api.room.registration.IRoomRegistrar;
import dev.compactmods.machines.api.room.history.IPlayerEntryPointHistoryManager;
import dev.compactmods.machines.api.room.history.IPlayerHistoryApi;
import dev.compactmods.machines.api.room.spatial.IRoomChunkManager;
import dev.compactmods.machines.api.room.spatial.IRoomChunks;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManager;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManagers;
import dev.compactmods.machines.data.DataFileUtil;
import dev.compactmods.machines.data.manager.CMKeyedDataFileManager;
import dev.compactmods.machines.data.manager.CMSingletonDataFileManager;
import dev.compactmods.machines.data.room.RoomDataAttachments;
import dev.compactmods.machines.player.PlayerEntryPointHistoryManager;
import dev.compactmods.machines.room.RoomRegistrar;
import dev.compactmods.machines.room.spatial.GraphChunkManager;
import dev.compactmods.machines.room.spawn.RoomSpawnManagers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

@Mod(value = CompactMachines.MOD_ID)
public class CompactMachinesServer {

   private static @Nullable MinecraftServer CURRENT_SERVER;

   private static RoomRegistrar ROOM_REGISTRAR;
   private static PlayerEntryPointHistoryManager PLAYER_HISTORY;

   private static CMSingletonDataFileManager<RoomRegistrar> ROOM_REGISTRAR_DATA;
   private static CMSingletonDataFileManager<PlayerEntryPointHistoryManager> PLAYER_HISTORY_DATA;
   private static CMKeyedDataFileManager<String, RoomDataAttachments> ROOM_DATA_ATTACHMENTS;

   public CompactMachinesServer() {
	  NeoForge.EVENT_BUS.addListener(CompactMachinesServer::serverStarting);
	  NeoForge.EVENT_BUS.addListener(CompactMachinesServer::serverStopping);
	  NeoForge.EVENT_BUS.addListener(CompactMachinesServer::levelSaved);
   }

   public static void serverStarting(final ServerStartingEvent evt) {
	  final var modLog = LoggingUtil.modLog();

	  modLog.debug("Setting up room API and data...");
	  MinecraftServer server = evt.getServer();


	  if (CompactMachinesServer.CURRENT_SERVER != null) {
		 save();
	  }

	  // Set up room data attachments for Neo
	  ROOM_DATA_ATTACHMENTS = new CMKeyedDataFileManager<>(server, RoomDataAttachments::new);

	  PLAYER_HISTORY = new PlayerEntryPointHistoryManager(5);
	  PLAYER_HISTORY_DATA = new CMSingletonDataFileManager<>(server, "player_entrypoint_history", PLAYER_HISTORY);

	  // Set up room API
	  var file = CMRoomDataLocations.REGISTRATION_DATA.apply(server)
		  .resolve("room_registrations.dat")
		  .toFile();

	  ROOM_REGISTRAR = file.exists() ? DataFileUtil.loadFileWithCodec(file, RoomRegistrar.CODEC) : new RoomRegistrar();
	  ROOM_REGISTRAR_DATA = new CMSingletonDataFileManager<>(server, "room_registrations", ROOM_REGISTRAR);

	  final IRoomSpawnManagers spawnManager = new RoomSpawnManagers(ROOM_REGISTRAR);

	  final var gcm = new GraphChunkManager();
	  ROOM_REGISTRAR.allRooms().forEach(inst -> gcm.calculateChunks(inst.code(), inst.boundaries()));

	  setupInternalApiStuff(spawnManager, gcm);

	  CURRENT_SERVER = server;

	  modLog.debug("Completed setting up room API and data.");
   }

   @SuppressWarnings("deprecation")
   private static void setupInternalApiStuff(IRoomSpawnManagers spawnManager, GraphChunkManager gcm) {
	  CompactMachines.Internal.ROOM_API = new IRoomApi() {
		 @Override
		 public Predicate<String> roomCodeValidator() {
			return ROOM_REGISTRAR::isRegistered;
		 }

		 @Override
		 public IRoomRegistrar registrar() {
			return ROOM_REGISTRAR;
		 }

		 @Override
		 public IRoomSpawnManager spawnManager(String roomCode) {
			return spawnManager.get(roomCode);
		 }

		 @Override
		 public IRoomChunkManager chunkManager() {
			return gcm;
		 }

		 @Override
		 public IRoomChunks chunks(String roomCode) {
			return gcm.get(roomCode);
		 }
	  };

	  CompactMachines.Internal.PLAYER_HISTORY_API = new IPlayerHistoryApi() {
		 @Override
		 public IPlayerEntryPointHistoryManager entryPoints() {
			return PLAYER_HISTORY;
		 }
	  };

	  CompactMachines.Internal.ROOM_DATA_ACCESSOR = new IRoomDataAttachmentAccessor() {
		 @Override
		 public Optional<? extends IAttachmentHolder> get(String roomCode) {
			return ROOM_DATA_ATTACHMENTS.optionalData(roomCode);
		 }

		 @Override
		 public IAttachmentHolder getOrCreate(String roomCode) {
			return ROOM_DATA_ATTACHMENTS.data(roomCode);
		 }
	  };
   }

   public static void save() {
	  if (CURRENT_SERVER != null) {
		 ROOM_REGISTRAR_DATA.save();
		 ROOM_DATA_ATTACHMENTS.save();
		 PLAYER_HISTORY_DATA.save();
	  }
   }

   public static void serverStopping(final ServerStoppingEvent evt) {
	  save();
   }

   public static void levelSaved(final LevelEvent.Save level) {
	  if (level.getLevel() instanceof Level l && CompactDimension.isLevelCompact(l)) {
		 save();
	  }
   }
}
