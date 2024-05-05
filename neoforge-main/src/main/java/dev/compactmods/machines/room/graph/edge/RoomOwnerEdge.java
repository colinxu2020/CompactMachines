package dev.compactmods.machines.room.graph.edge;

import dev.compactmods.feather.edge.GraphEdge;
import dev.compactmods.machines.room.graph.node.RoomOwnerNode;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;

import java.lang.ref.WeakReference;

public record RoomOwnerEdge(WeakReference<RoomRegistrationNode> source, WeakReference<RoomOwnerNode> target)
        implements GraphEdge<RoomRegistrationNode, RoomOwnerNode> {

}
