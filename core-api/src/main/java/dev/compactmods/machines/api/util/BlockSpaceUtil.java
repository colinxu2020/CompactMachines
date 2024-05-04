package dev.compactmods.machines.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.stream.Stream;

public class BlockSpaceUtil {

    public static Stream<BlockPos> blocksInside(AABB bounds) {
        return BlockPos.betweenClosedStream(bounds.contract(1, 1, 1));
    }

    public static Stream<BlockPos> forAllCorners(AABB bounds) {
        Stream.Builder<BlockPos> stream = Stream.builder();
        stream.add(BlockPos.containing(bounds.maxX - 1, bounds.maxY - 1, bounds.maxZ - 1));
        stream.add(BlockPos.containing(bounds.minX, bounds.maxY - 1, bounds.maxZ - 1));
        stream.add(BlockPos.containing(bounds.maxX - 1, bounds.minY, bounds.maxZ - 1));
        stream.add(BlockPos.containing(bounds.minX, bounds.minY, bounds.maxZ - 1));
        stream.add(BlockPos.containing(bounds.maxX - 1, bounds.maxY - 1, bounds.minZ));
        stream.add(BlockPos.containing(bounds.minX, bounds.maxY - 1, bounds.minZ));
        stream.add(BlockPos.containing(bounds.maxX - 1, bounds.minY, bounds.minZ));
        stream.add(BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ));
        return stream.build();
    }

    public static AABB getWallBounds(AABB area, Direction wall) {
        return getWallBounds(area, wall, 1d);
    }

    public static BlockPos centerWallBlockPos(AABB area, Direction direction) {
        var center = area.getCenter();
        var offset = direction.getAxis().choose(area.getXsize(), area.getYsize(), area.getZsize())
                / 2d;

        if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE)
            offset -= 1;

        var centerWallPos = center.relative(direction, offset);
        return BlockPos.containing(centerWallPos);
    }

    public static AABB getPlaneAABB(Direction direction) {
        if (direction.getAxis().isHorizontal())
            return getPlaneAABB(Direction.UP, direction.getCounterClockWise());
        else
            return getPlaneAABB(direction == Direction.UP ? Direction.SOUTH : Direction.NORTH,
                    direction.getClockWise(Direction.Axis.Z));
    }

    public static AABB getPlaneAABB(Direction up, Direction right) {
        Vec3i rightNorm = right.getNormal();
        Vec3i upNorm = up.getNormal();

        return AABB.ofSize(Vec3.ZERO, rightNorm.getX() + upNorm.getX(),
                rightNorm.getY() + upNorm.getY(),
                rightNorm.getZ() + upNorm.getZ());
    }

    public static AABB getWallBounds(AABB area, Direction wallDirection, double thickness) {
        var wallCenterDistance = centerWallBlockPos(area, wallDirection);
        Vec3 wallCenter = Vec3.atCenterOf(wallCenterDistance);

        var normal = getPlaneAABB(wallDirection);
        return AABB.ofSize(wallCenter,
                area.getXsize() * normal.getXsize() + (wallDirection.getNormal().getX() * thickness),
                area.getYsize() * normal.getYsize() + (wallDirection.getNormal().getY() * thickness),
                area.getZsize() * normal.getZsize() + (wallDirection.getNormal().getZ() * thickness));
    }
}
