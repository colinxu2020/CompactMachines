package dev.compactmods.machines.machine;

import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.api.room.RoomTemplate;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MachineItemCreator {

    static final Holder<Item> UNBOUND_HOLDER = BuiltInRegistries.ITEM.getHolderOrThrow(MachineConstants.UNBOUND_MACHINE_ITEM_RESKEY);
    static final Holder<Item> BOUND_HOLDER = BuiltInRegistries.ITEM.getHolderOrThrow(MachineConstants.BOUND_MACHINE_ITEM_RESKEY);

    public static ItemStack unbound() {
        return unboundColored(0xFFFFFFFF);
    }

    public static ItemStack unboundColored(int color) {
        final var stack = new ItemStack(UNBOUND_HOLDER, 1);
        stack.set(Machines.DataComponents.ROOM_TEMPLATE_ID, RoomTemplate.NO_TEMPLATE);
        stack.set(Machines.DataComponents.MACHINE_COLOR, color);
        return stack;
    }

    public static ItemStack boundToRoom(String roomCode) {
        return boundToRoom(roomCode, 0xFFFFFFFF);
    }

    public static ItemStack boundToRoom(String roomCode, int color) {
        ItemStack stack = new ItemStack(BOUND_HOLDER, 1);
        stack.set(Machines.DataComponents.BOUND_ROOM_CODE, roomCode);
        stack.set(Machines.DataComponents.MACHINE_COLOR, color);
        return stack;
    }

    public static ItemStack forNewRoom(ResourceLocation templateID, RoomTemplate template) {
        final var stack = new ItemStack(UNBOUND_HOLDER, 1);
        stack.set(Machines.DataComponents.ROOM_TEMPLATE_ID, templateID);
        stack.set(Machines.DataComponents.ROOM_TEMPLATE, template);
        stack.set(Machines.DataComponents.MACHINE_COLOR, template.defaultMachineColor().rgb());
        return stack;
    }
}
