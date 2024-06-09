package dev.compactmods.machines.room.upgrade;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.UUID;
import java.util.stream.Stream;

public class RoomUpgradeInventory extends ItemStackHandler {

   public static final RoomUpgradeInventory EMPTY = new RoomUpgradeInventory();

   private final Int2ObjectMap<UUID> UPGRADE_IDS = new Int2ObjectOpenHashMap<>();

   public RoomUpgradeInventory() {
	  super(9);
   }

   @Override
   public boolean isItemValid(int slot, ItemStack stack) {
	  return !stack.isEmpty() && stack.has(RoomUpgrades.UPGRADE_LIST_COMPONENT);
   }

   @Override
   public int getSlotLimit(int slot) {
	  return 1;
   }

   @Override
   protected void onContentsChanged(int slot) {
	  super.onContentsChanged(slot);

	  var acceptedStack = getStackInSlot(slot);

	  if(acceptedStack.isEmpty()) {
		 UPGRADE_IDS.remove(slot);
	  } else {
		 var upgrades = acceptedStack.get(RoomUpgrades.UPGRADE_LIST_COMPONENT);

		 var newID = UUID.randomUUID();
		 UPGRADE_IDS.put(slot, newID);

		 // TODO Apply upgrade list to room
	  }
   }

   public Stream<ItemStack> items() {
	  final Stream.Builder<ItemStack> b = Stream.builder();
	  for (int i = 0; i < 9; i++) {
		 final var stack = getStackInSlot(i);
		 if (!stack.isEmpty()) {
			b.add(stack);
		 }
	  }

	  return b.build();
   }
}
