package dev.compactmods.machines.neoforge.machine;

import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.api.machine.item.IBoundCompactMachineItem;
import dev.compactmods.machines.api.machine.item.IUnboundCompactMachineItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MachineCreator {

    static final Holder<Item> UNBOUND_HOLDER = BuiltInRegistries.ITEM.getHolderOrThrow(MachineConstants.UNBOUND_MACHINE_ITEM_RESKEY);
    static final Holder<Item> BOUND_HOLDER = BuiltInRegistries.ITEM.getHolderOrThrow(MachineConstants.BOUND_MACHINE_ITEM_RESKEY);

    public static ItemStack unbound() {
        final var stack = new ItemStack(UNBOUND_HOLDER, 1);
        if(stack.getItem() instanceof IUnboundCompactMachineItem unbound) {
            unbound.setTemplate(stack, IUnboundCompactMachineItem.NO_ROOM_TEMPLATE);
            stack.setData(Machines.MACHINE_COLOR, 0xFFFFFFFF);
        }

        return stack;
    }

    public static ItemStack unboundColored(int color) {
        final var stack = new ItemStack(UNBOUND_HOLDER, 1);
        if(stack.getItem() instanceof IUnboundCompactMachineItem unbound) {
            unbound.setTemplate(stack, IUnboundCompactMachineItem.NO_ROOM_TEMPLATE);
            stack.setData(Machines.MACHINE_COLOR, color);
        }

        return stack;
    }

    public static ItemStack boundToRoom(String roomCode) {
        ItemStack item = new ItemStack(BOUND_HOLDER);
        if(item.getItem() instanceof IBoundCompactMachineItem bound) {
            bound.setRoom(item, roomCode);
            item.setData(Machines.MACHINE_COLOR, DyeColor.WHITE.getTextColor());
        }

        return item;
    }

    public static ItemStack boundToRoom(String roomCode, int color) {
        ItemStack item = new ItemStack(BOUND_HOLDER);
        if(item.getItem() instanceof IBoundCompactMachineItem bound) {
            bound.setRoom(item, roomCode);
            item.setData(Machines.MACHINE_COLOR, color);
        }

        return item;
    }

}
