package dev.compactmods.machines.api.room.upgrade;

import com.mojang.serialization.MapCodec;

public record SimpleRoomUpgradeType<T extends RoomUpgrade>(MapCodec<T> codec) implements RoomUpgradeType<T> {
}