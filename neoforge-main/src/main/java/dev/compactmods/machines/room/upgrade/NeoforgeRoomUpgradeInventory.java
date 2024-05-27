package dev.compactmods.machines.room.upgrade;

import dev.compactmods.machines.room.Rooms;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class NeoforgeRoomUpgradeInventory extends ItemStackHandler {

    public static final NeoforgeRoomUpgradeInventory EMPTY = new NeoforgeRoomUpgradeInventory();

    public NeoforgeRoomUpgradeInventory() {
        super(9);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return !stack.isEmpty() && stack.has(RoomUpgrades.UPGRADE_LIST_COMPONENT);
    }
}
