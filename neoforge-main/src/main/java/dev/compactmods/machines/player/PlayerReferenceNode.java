package dev.compactmods.machines.player;

import dev.compactmods.feather.node.Node;

import java.util.UUID;

public record PlayerReferenceNode(UUID id, UUID data) implements Node<UUID> {
    public UUID playerID() {
        return data;
    }
}
