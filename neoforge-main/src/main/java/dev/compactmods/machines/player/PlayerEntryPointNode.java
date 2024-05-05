package dev.compactmods.machines.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.feather.node.Node;
import dev.compactmods.machines.api.room.history.RoomEntryPoint;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public record PlayerEntryPointNode(UUID id, RoomEntryPoint data) implements Node<RoomEntryPoint> {
    public static final Codec<PlayerEntryPointNode> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(PlayerEntryPointNode::id),
            RoomEntryPoint.CODEC.fieldOf("data").forGetter(PlayerEntryPointNode::data)
    ).apply(inst, PlayerEntryPointNode::new));
}
