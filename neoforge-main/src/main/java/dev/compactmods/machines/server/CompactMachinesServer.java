package dev.compactmods.machines.server;

import dev.compactmods.machines.LoggingUtil;
import dev.compactmods.machines.api.CompactMachinesApi;
import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.api.room.IRoomRegistrar;
import dev.compactmods.machines.api.room.RoomApi;
import dev.compactmods.machines.api.room.history.IPlayerEntryPointHistoryManager;
import dev.compactmods.machines.api.room.history.IPlayerHistoryApi;
import dev.compactmods.machines.api.room.history.PlayerHistoryApi;
import dev.compactmods.machines.api.room.spawn.IRoomSpawnManagers;
import dev.compactmods.machines.data.manager.CMSingletonDataFileManager;
import dev.compactmods.machines.data.room.RoomAttachmentDataManager;
import dev.compactmods.machines.player.PlayerEntryPointHistoryManager;
import dev.compactmods.machines.room.RoomApiInstance;
import dev.compactmods.machines.room.RoomRegistrar;
import dev.compactmods.machines.room.spatial.GraphChunkManager;
import dev.compactmods.machines.room.spawn.RoomSpawnManagers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@Mod(value = CompactMachinesApi.MOD_ID, dist = Dist.DEDICATED_SERVER)
public class CompactMachinesServer {

   private static @Nullable MinecraftServer CURRENT_SERVER;

   @ApiStatus.Internal // NO TOUCHY  #INeedServerCapabilities
   public static CMSingletonDataFileManager<PlayerEntryPointHistoryManager> PLAYER_HISTORY;

   @ApiStatus.Internal // NO TOUCHY  #INeedServerCapabilities
   public static RoomAttachmentDataManager ROOM_DATA_ATTACHMENTS;

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

	  PLAYER_HISTORY = new CMSingletonDataFileManager<>(server, "player_entrypoint_history", serv -> new PlayerEntryPointHistoryManager(5));

	  // Set up room data attachments for Neo
	  ROOM_DATA_ATTACHMENTS = new RoomAttachmentDataManager(server);

	  // Set up room API
	  final var registrarData = new CMSingletonDataFileManager<>(server, "room_registrations", serv -> new RoomRegistrar());
	  final IRoomRegistrar registrar = registrarData.data();

	  final IRoomSpawnManagers spawnManager = new RoomSpawnManagers(registrar);

	  final var gcm = new GraphChunkManager();
	  registrar.allRooms().forEach(inst -> gcm.calculateChunks(inst.code(), inst.boundaries()));

	  RoomApi.INSTANCE = new RoomApiInstance(registrar::isRegistered, registrar, spawnManager, gcm);

	  PlayerHistoryApi.INSTANCE = new IPlayerHistoryApi() {
		  @Override
		  public IPlayerEntryPointHistoryManager entryPoints() {
			 return PLAYER_HISTORY.data();
		  }
	 };

	  modLog.debug("Completed setting up room API and data.");
   }

   public static void save() {
	  if(CURRENT_SERVER != null) {
		 PLAYER_HISTORY.save();
		 ROOM_DATA_ATTACHMENTS.save(CURRENT_SERVER.registryAccess());
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
