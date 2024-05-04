package dev.compactmods.machines.api.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class AABBAligner {

    private final AABB outerBounds;
    private final Vector3d size;
    private Vector3d center;

    private AABBAligner(AABB outerBounds, Vector3d center, Vector3d size) {
        this.outerBounds = outerBounds;
        this.size = size;
        this.center = center;
    }

    public static AABBAligner create(AABB boundaries, AABB pendingAlign) {
        return new AABBAligner(boundaries, VectorUtils.convert3d(pendingAlign.getCenter()), AABBHelper.sizeOf(pendingAlign));
    }

    public static AABBAligner create(AABB boundaries, Vector3d size) {
        return new AABBAligner(boundaries, new Vector3d(), size);
    }

    public AABBAligner center(Vec3 center) {
        this.center = VectorUtils.convert3d(center);
        return this;
    }

    public AABBAligner center(Vector3d center) {
        this.center = center;
        return this;
    }

    public AABBAligner center() {
        this.center = VectorUtils.convert3d(outerBounds.getCenter());
        return this;
    }

    public AABBAligner boundedDirection(Direction direction) {
        var outerEdge = DirectionalMath.directionalEdge(direction, outerBounds);
        var targetPoint = DirectionalMath.directionalEdge(direction, center, size);

        var offset = outerEdge.sub(targetPoint);

        center.add(offset);

        return this;
    }

    public AABB align() {
        return AABB.ofSize(VectorUtils.convert3d(center), size.x, size.y, size.z);
    }

    public static AABB center(AABB source, Vec3 center) {
        final var size = AABBHelper.sizeOf(source);
        return AABB.ofSize(center, size.x, size.y, size.z);
    }

    public static AABB center(AABB source, Vector3d center) {
        return center(source, VectorUtils.convert3d(center));
    }

    public static AABB floor(AABB source, AABB within) {
        double targetY = within.minY;
        return floor(source, targetY);
    }

    public static AABB floor(AABB source, double targetY) {
        double offset = source.minY - targetY;
        return source.move(0, offset * -1, 0);
    }
}
