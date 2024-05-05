package dev.compactmods.machines.room.graph;

import dev.compactmods.feather.edge.OutboundGraphEdgeLookupFunction;
import dev.compactmods.feather.node.GraphAdjacentNodeStream;
import dev.compactmods.feather.node.GraphNodeStream;
import dev.compactmods.feather.traversal.GraphTraversalHelper;
import dev.compactmods.machines.room.graph.node.RoomChunkNode;
import dev.compactmods.machines.room.graph.node.RoomReferenceNode;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;

public class GraphNodes {

    public static final GraphNodeStream<RoomRegistrationNode> ALL_REGISTRATIONS = (graph) -> GraphTraversalHelper.nodes(graph, RoomRegistrationNode.class);
    public static final OutboundGraphEdgeLookupFunction<RoomReferenceNode, RoomChunkNode> ROOM_CHUNKS =
            (edgeAccessor, regNode) -> edgeAccessor.outboundEdges(regNode, RoomChunkNode.class);

    public static final GraphAdjacentNodeStream<RoomChunkNode, RoomRegistrationNode> LOOKUP_ROOM_REGISTRATION =
            (g, on) -> GraphTraversalHelper.predecessors(g, on, RoomRegistrationNode.class);

}
