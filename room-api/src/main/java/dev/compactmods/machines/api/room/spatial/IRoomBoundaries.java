package dev.compactmods.machines.api.room.spatial;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public interface IRoomBoundaries {

    default AABB innerBounds() {
        return outerBounds().deflate(1);
    }

    AABB outerBounds();

    default Vec3 defaultSpawn() {
        var newFloorCenter = BlockPos.containing(innerBounds().getCenter()).mutable();
        newFloorCenter.setY((int) (innerBounds().minY + 1));
        return Vec3.atBottomCenterOf(newFloorCenter);
    }
}
