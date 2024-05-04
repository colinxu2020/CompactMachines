package dev.compactmods.machines.api.util;

import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class VectorUtils {

    public static Vector3d convert3d(Vec3i vector) {
        return new Vector3d(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector3d convert3d(Vec3 vector) {
        return new Vector3d(vector.x, vector.y, vector.z);
    }

    public static Vec3 convert3d(Vector3d vector) {
        return new Vec3(vector.x, vector.y, vector.z);
    }
}
