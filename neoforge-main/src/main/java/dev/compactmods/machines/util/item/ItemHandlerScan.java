package dev.compactmods.machines.util.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.ImmutableIntArray;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;

public record ItemHandlerScan(Set<Integer> stacking, Set<Integer> empty) {

    public static ItemHandlerScan scanInventory(IItemHandler inventory, ItemStack stack) {
        var stackingBuilder = ImmutableSet.<Integer>builder();
        var emptyBuilder = ImmutableSet.<Integer>builder();
        for(int i = 0; i < inventory.getSlots(); i++) {
            var inSlot = inventory.getStackInSlot(i);
            if(inSlot.isEmpty()) {
                emptyBuilder.add(i);
                continue;
            }

            if(!inSlot.isStackable())
                continue;

            if(!ItemStack.isSameItemSameComponents(inSlot, stack))
                continue;

            var canActuallyInsert = inventory.insertItem(i, stack.copyWithCount(1), true).isEmpty();
            if(canActuallyInsert)
                stackingBuilder.add(i);
        }

        return new ItemHandlerScan(stackingBuilder.build(), emptyBuilder.build());
    }

    public boolean hasSpaceAvailable() {
        return !stacking.isEmpty() || !empty.isEmpty();
    }
}
