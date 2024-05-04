package dev.compactmods.machines.api.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3d;

public class DirectionalMath {

    public static Vector3d directionalEdge(Direction direction, Vector3d origin, Vector3d size) {
        final var normal = direction.getNormal();

        var x = origin.x + ((size.x / 2) * normal.getX());
        var y = origin.y + ((size.y / 2) * normal.getY());
        var z = origin.z + ((size.z / 2) * normal.getZ());

        return new Vector3d(x, y, z);
    }

    public static Vector3d directionalEdge(Direction direction, AABB aabb) {
        var center = VectorUtils.convert3d(aabb.getCenter());
        return directionalEdge(direction, center, AABBHelper.sizeOf(aabb));
    }

}
