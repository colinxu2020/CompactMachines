package dev.compactmods.machines.api.room.upgrade;

public record RoomUpgradeInstance<T extends RoomUpgradeType>(T upgrade, String room) {}