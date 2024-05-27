package dev.compactmods.machines.room.upgrade;

import dev.compactmods.machines.api.room.spatial.IRoomChunks;
import dev.compactmods.machines.room.Rooms;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.stream.Stream;

public class NeoforgeRoomUpgradeInventory extends ItemStackHandler {

   public static final NeoforgeRoomUpgradeInventory EMPTY = new NeoforgeRoomUpgradeInventory();

   public NeoforgeRoomUpgradeInventory() {
	  super(9);
   }

   @Override
   public boolean isItemValid(int slot, ItemStack stack) {
	  return !stack.isEmpty() && stack.has(RoomUpgrades.UPGRADE_LIST_COMPONENT);
   }

   public Stream<ItemStack> items() {
	  final var b = Stream.<ItemStack>builder();
	  for (int i = 0; i < 9; i++) {
		 final var stack = getStackInSlot(i);
		 if (!stack.isEmpty()) {
			b.add(stack);
		 }
	  }

	  return b.build();
   }
}
