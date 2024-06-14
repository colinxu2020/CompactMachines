package dev.compactmods.machines.api.room.registration;

import dev.compactmods.machines.api.machine.MachineColor;
import dev.compactmods.machines.api.room.RoomInstance;
import net.minecraft.world.phys.AABB;

import java.util.UUID;

public interface IRoomBuilder {

    IRoomBuilder boundaries(AABB boundaries);

    IRoomBuilder owner(UUID owner);

    default IRoomBuilder defaultMachineColor(MachineColor color) {
        return defaultMachineColor(color.rgb());
    }

    IRoomBuilder defaultMachineColor(int color);

    RoomInstance build();
}
