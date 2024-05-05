package dev.compactmods.machines.room;

import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.api.room.registration.IRoomBuilder;
import dev.compactmods.machines.api.room.spatial.IRoomBoundaries;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class NewRoomBuilder implements IRoomBuilder {
    private final String code;
    private int color = 0;

    private AABB boundaries = AABB.ofSize(Vec3.ZERO, 1, 1, 1);
    UUID owner;

    NewRoomBuilder() {
        this.code = RoomCodeGenerator.generateRoomId();
    }

    public NewRoomBuilder boundaries(AABB boundaries) {
        this.boundaries = boundaries;
        return this;
    }

    public NewRoomBuilder offsetCenter(Vec3 offset) {
        this.boundaries = this.boundaries.move(offset);
        return this;
    }

    public NewRoomBuilder owner(UUID owner) {
        this.owner = owner;
        return this;
    }

    public NewRoomBuilder defaultMachineColor(int color) {
        this.color = color;
        return this;
    }

    public RoomInstance build() {
        return new RoomInstance(code, color, () -> boundaries);
    }
}
