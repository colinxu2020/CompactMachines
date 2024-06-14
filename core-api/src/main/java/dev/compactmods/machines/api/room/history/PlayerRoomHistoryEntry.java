package dev.compactmods.machines.api.room.history;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

/**
 * A historical record of a player entering a compact machine room.
 *
 * @param roomCode   The room being entered.
 * @param instant
 * @param entryPoint The last position the player was in, and how they entered.
 */
public record PlayerRoomHistoryEntry(String roomCode, java.time.Instant instant, RoomEntryPoint entryPoint) {
    public static final Codec<PlayerRoomHistoryEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("room").forGetter(PlayerRoomHistoryEntry::roomCode),
            ExtraCodecs.INSTANT_ISO8601.fieldOf("timestamp").forGetter(PlayerRoomHistoryEntry::instant),
            RoomEntryPoint.CODEC.fieldOf("entry_point").forGetter(PlayerRoomHistoryEntry::entryPoint)
    ).apply(inst, PlayerRoomHistoryEntry::new));
}
