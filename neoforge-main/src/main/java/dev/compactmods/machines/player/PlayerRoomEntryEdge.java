package dev.compactmods.machines.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.feather.edge.GraphEdge;
import dev.compactmods.feather.edge.GraphValueEdge;
import dev.compactmods.machines.room.graph.node.RoomReferenceNode;

import java.lang.ref.WeakReference;
import java.time.Instant;

public record PlayerRoomEntryEdge(WeakReference<PlayerEntryPointNode> source, WeakReference<RoomReferenceNode> target, Instant entryTime)
        implements GraphValueEdge<PlayerEntryPointNode, RoomReferenceNode, Instant> {

    public PlayerRoomEntryEdge(PlayerEntryPointNode entryNode, RoomReferenceNode roomNode) {
        this(entryNode, roomNode, Instant.now());
    }

    public PlayerRoomEntryEdge(PlayerEntryPointNode entryNode, RoomReferenceNode roomNode, Instant instant) {
        this(new WeakReference<>(entryNode), new WeakReference<>(roomNode), instant);
    }

    @Override
    public Instant value() {
        return entryTime;
    }
}
