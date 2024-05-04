package dev.compactmods.machines.api.room;

import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.api.room.function.PlayerAndRoomCodeFunction;
import dev.compactmods.machines.api.util.AABBHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface RoomTranslations {

    /**
     * Used to show information about a player inside a Compact room.
     */
    PlayerAndRoomCodeFunction<Component> PLAYER_ROOM_INFO = (player, roomCode) ->
            Component.translatableWithFallback(IDs.PLAYER_ROOM_INFO, "%1$s is inside room '%2$s'.",
                    player.getDisplayName(), roomCode);

    Function<Player, Component> PLAYER_NOT_IN_COMPACT_DIM = (player) -> Component.translatableWithFallback(IDs.Errors.PLAYER_NOT_IN_COMPACT_DIM, "", player.getDisplayName());

    Function<Player, Component> UNKNOWN_ROOM_BY_PLAYER_CHUNK = (player) -> Component
            .translatableWithFallback(IDs.Errors.UNKNOWN_ROOM_BY_PLAYER_CHUNK, "Room not found at chunk: %s", player.chunkPosition())
            .withStyle(ChatFormatting.DARK_RED);

    Function<String, Component> UNKNOWN_ROOM_BY_CODE = (roomCode) -> Component
            .translatableWithFallback(IDs.Errors.UNKNOWN_ROOM_BY_CODE, "Room not found: %s", roomCode);

    BiFunction<BlockPos, RoomInstance, Component> MACHINE_ROOM_INFO = (machinePos, info) -> Component.translatableWithFallback(IDs.MACHINE_ROOM_INFO,
            "Machine at %1$s is bound to a %2$s size room at %3$s",
            machinePos.toShortString(), AABBHelper.toString(info.boundaries().innerBounds()), info.boundaries().innerBounds().getCenter());

    PlayerAndRoomCodeFunction<Component> ROOM_SPAWNPOINT_SET = (player, roomCode) -> Component
            .translatableWithFallback(IDs.ROOM_SPAWNPOINT_SET, "Room spawn for %s updated.", player.getDisplayName())
            .withStyle(ChatFormatting.GREEN);

    interface IDs {
        String ROOM_SPAWNPOINT_SET = Util.makeDescriptionId("rooms", Constants.modRL("spawnpoint_set"));

        String PLAYER_ROOM_INFO = Util.makeDescriptionId("rooms", Constants.modRL("player_room_info"));

        String MACHINE_ROOM_INFO = Util.makeDescriptionId("machine", Constants.modRL("machine_room_info"));

        interface Errors {
            String UNKNOWN_ROOM_BY_CODE = Util.makeDescriptionId("rooms.errors", Constants.modRL("room_not_found"));

            String CANNOT_ENTER_ROOM = Util.makeDescriptionId("rooms.errors", Constants.modRL("cannot_enter"));

            String UNKNOWN_ROOM_BY_PLAYER_CHUNK = Util.makeDescriptionId("rooms.errors", Constants.modRL("unknown_room_chunk"));

            String PLAYER_NOT_IN_COMPACT_DIM = Util.makeDescriptionId("rooms.errors", Constants.modRL("player_not_in_compact_dimension"));
        }
    }
}
