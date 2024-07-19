package dev.compactmods.machines.test.junit;


import com.google.common.base.Predicates;
import com.google.common.math.DoubleMath;
import dev.compactmods.machines.api.util.AABBAligner;
import dev.compactmods.machines.api.util.AABBHelper;
import dev.compactmods.machines.util.RandomSourceUtil;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

@ExtendWith(EphemeralTestServerProvider.class)
public class AABBHelperTests {

	private static class MyThing extends ItemStackHandler {
		public MyThing(final int size) {
			super(size);
		}

		boolean isEmpty() {
			return stacks.stream().allMatch(ItemStack::isEmpty);
		}
	}

   @Test
   public void canFloorToY0() {
		Predicate<MyThing> empty = Predicates.not(MyThing::isEmpty);

	  // Source minY = 5
	  AABB before = AABB.ofSize(new Vec3(0, 7.5, 0), 5, 5, 5);

	  // Align to Y-0
	  final var after = AABBAligner.floor(before, 0);

	  Assertions.assertEquals(5, before.minY, "Before was modified in-place rather than immutably moved.");
	  Assertions.assertEquals(0, after.minY, "After y level should be zero. (was: %s)".formatted(after.minY));
	  Assertions.assertEquals(5, after.getYsize(), "AABB size was modified; should have remained the same.");
   }

   @Test
   public void canFloorToAnotherAABB() {
	  // Source minY = 5
	  AABB before = AABB.ofSize(Vec3.ZERO.relative(Direction.UP, 7.5), 5, 5, 5);

	  // Target minY = 1 (bounds are Y 1-11)
	  AABB bounds = AABB.ofSize(Vec3.ZERO.relative(Direction.UP, 6), 10, 10, 10);

	  // Align to Y-0
	  final var after = AABBAligner.floor(before, bounds);

	  Assertions.assertEquals(5, before.minY, "Before was modified in-place rather than immutably moved.");
	  Assertions.assertEquals(1, after.minY, "After y level should be 1. (was: %s)".formatted(after.minY));

	  Assertions.assertEquals(5, after.getYsize(), "AABB size was modified; should have remained the same.");
   }

   @Test
   public void normalizeToZero() {
	  AABB before = AABB.ofSize(Vec3.ZERO.relative(Direction.UP, 7.5), 5, 5, 5);

	  // Align to Y-0
	  final var after = AABBHelper.normalize(before);

	  Assertions.assertEquals(5, before.minY, "Before was modified in-place rather than immutably moved.");

	  Assertions.assertEquals(0, after.minX, "After x level was not zero (was: %s)".formatted(after.minX));
	  Assertions.assertEquals(0, after.minY, "After y level was not zero (was: %s)".formatted(after.minY));
	  Assertions.assertEquals(0, after.minZ, "After z level was not zero (was: %s)".formatted(after.minZ));

	  Assertions.assertEquals(5, after.getYsize(), "AABB size was modified; should have remained the same.");
   }

   @TestFactory
   public static Collection<DynamicTest> normalizeBoundaryTests() {
	  final var random = RandomSource.create();
	  return Stream.concat(
			  RandomSourceUtil.randomVec3Stream(random).limit(10),

			  // Ensure at least one negative and one positive bound are part of the test
			  Stream.of(
				  Vec3.ZERO.subtract(-3, -2, 5),
				  Vec3.ZERO.add(2, 5, 1)
			  )
		  ).map(randomOffset -> DynamicTest.dynamicTest(
			  "normalize_boundaries_%s".formatted(randomOffset.hashCode()),
			  () -> normalizeIntoBoundaries(randomOffset)
		  ))
		  .toList();
   }

   private static void assertVec3Equals(Vec3 actual, Vec3 expected) {
	  Assertions.assertTrue(DoubleMath.fuzzyEquals(actual.x, expected.x, 0.001),
		  "X did not match expected value (was: %s; expected: %s)".formatted(actual.x, expected.x));

	  Assertions.assertTrue(DoubleMath.fuzzyEquals(actual.y, expected.y, 0.001),
		  "Y did not match expected value (was: %s; expected: %s)".formatted(actual.y, expected.y));

	  Assertions.assertTrue(DoubleMath.fuzzyEquals(actual.z, expected.z, 0.001),
		  "Z did not match expected value (was: %s; expected: %s)".formatted(actual.z, expected.z));
   }

   public static void normalizeIntoBoundaries(Vec3 randomOffset) {
	  AABB before = AABB.ofSize(Vec3.ZERO.relative(Direction.UP, 7.5), 5, 5, 5);
	  AABB bounds = AABB.ofSize(randomOffset, 5, 5, 5);

	  final var after = AABBHelper.normalizeWithin(before, bounds);

	  Assertions.assertEquals(5, before.minY, "Before was modified in-place rather than immutably moved.");

	  assertVec3Equals(AABBHelper.minCorner(after), AABBHelper.minCorner(bounds));

	  Assertions.assertEquals(5, after.getYsize(), "AABB size was modified; should have remained the same.");
   }
}
