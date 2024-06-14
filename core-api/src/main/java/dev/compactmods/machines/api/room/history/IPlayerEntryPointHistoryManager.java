package dev.compactmods.machines.api.room.history;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface IPlayerEntryPointHistoryManager {

   Optional<PlayerRoomHistoryEntry> lastHistory(Player player);

   void popHistory(Player player, int steps);

   Stream<PlayerRoomHistoryEntry> history(Player player);

   Stream<PlayerRoomHistoryEntry> history(UUID player);

   RoomEntryResult enterRoom(UUID player, PlayerRoomHistoryEntry history);

   RoomEntryResult enterRoom(Player player, String roomCode, RoomEntryPoint entryPoint);

   void clearHistory(ServerPlayer player);
}
