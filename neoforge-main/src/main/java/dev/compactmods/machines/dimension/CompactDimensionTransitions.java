package dev.compactmods.machines.dimension;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class CompactDimensionTransitions {

   public static DimensionTransition to(ServerLevel targetLevel, Vec3 pos) {
	  return new DimensionTransition(targetLevel, pos, Vec3.ZERO, 0, 0, DimensionTransition.DO_NOTHING);
   }

   public static DimensionTransition to(ServerLevel targetLevel, Vec3 pos, Vec2 rotation) {
	  return new DimensionTransition(targetLevel, pos, Vec3.ZERO, rotation.y, rotation.x, DimensionTransition.DO_NOTHING);
   }
}
