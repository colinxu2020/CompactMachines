package dev.compactmods.machines.api.room;

import net.minecraft.core.component.DataComponentType;

import java.util.function.UnaryOperator;

public interface RoomComponents {

    UnaryOperator<DataComponentType.Builder<RoomTemplate>> ROOM_TEMPLATE = (builder) -> builder
            .persistent(RoomTemplate.CODEC)
            .networkSynchronized(RoomTemplate.STREAM_CODEC);

}
