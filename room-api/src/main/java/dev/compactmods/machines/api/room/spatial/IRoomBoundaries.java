package dev.compactmods.machines.api.room.spatial;

import dev.compactmods.machines.api.util.AABBHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.stream.Stream;

public interface IRoomBoundaries {

    default AABB innerBounds() {
        return outerBounds().deflate(1);
    }

    default Stream<ChunkPos> innerChunkPositions() {
        final var ib =  innerBounds();
        return AABBHelper.chunkPositions(ib);
    }

    AABB outerBounds();

    default Vec3 defaultSpawn() {
        var newFloorCenter = BlockPos.containing(innerBounds().getCenter()).mutable();
        newFloorCenter.setY((int) (innerBounds().minY + 1));
        return Vec3.atBottomCenterOf(newFloorCenter);
    }
}
