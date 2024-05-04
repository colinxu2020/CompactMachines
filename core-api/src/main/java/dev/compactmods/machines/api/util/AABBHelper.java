package dev.compactmods.machines.api.util;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public abstract class AABBHelper {

    public static Vector3d sizeOf(AABB aabb) {
        return new Vector3d(aabb.getXsize(), aabb.getYsize(), aabb.getZsize());
    }

    public static boolean fitsInside(AABB aabb, Vec3 vec3) {
        return fitsInside(aabb, VectorUtils.convert3d(vec3));
    }

    public static boolean fitsInside(AABB aabb, Vector3d dimensions) {
        return dimensions.x <= aabb.getXsize() &&
                dimensions.y <= aabb.getYsize() &&
                dimensions.z <= aabb.getZsize();
    }

    public static boolean fitsInside(AABB outer, AABB inner) {
        return fitsInside(outer, sizeOf(inner));
    }

    public static Vec3 minCorner(AABB aabb) {
        return new Vec3(aabb.minX, aabb.minY, aabb.minZ);
    }

    public static Vec3 maxCorner(AABB aabb) {
        return new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public static AABB normalize(AABB source) {
        Vec3 offset = minCorner(source).reverse();
        return source.move(offset);
    }

    public static AABB normalizeWithin(AABB source, AABB within) {
        Vec3 offset = minCorner(source).subtract(minCorner(within)).reverse();
        return source.move(offset);
    }


    public static String toString(AABB aabb) {
        return "%s,%s,%s".formatted(aabb.getXsize(), aabb.getYsize(), aabb.getZsize());
    }
}
