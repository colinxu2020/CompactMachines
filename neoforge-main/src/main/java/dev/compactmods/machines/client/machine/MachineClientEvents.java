package dev.compactmods.machines.client.machine;

import dev.compactmods.machines.client.keybinds.room.RoomExitKeyMapping;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.room.Rooms;
import dev.compactmods.machines.room.ui.preview.MachineRoomScreen;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class MachineClientEvents {

    public static void registerMenuScreens(final RegisterMenuScreensEvent evt) {
        evt.register(Rooms.Menus.MACHINE_MENU.get(), MachineRoomScreen::new);
    }

    public static void handleKeybinds(final ClientTickEvent.Post clientTick) {
        if (RoomExitKeyMapping.MAPPING.consumeClick())
            RoomExitKeyMapping.handle();
    }

    public static void onItemColors(final RegisterColorHandlersEvent.Item colors) {
        colors.register(MachineColors.ITEM, Machines.Items.BOUND_MACHINE.get(), Machines.Items.UNBOUND_MACHINE.get());
    }

    public static void onBlockColors(final RegisterColorHandlersEvent.Block colors) {
        colors.register(MachineColors.BLOCK, Machines.Blocks.BOUND_MACHINE.get(), Machines.Blocks.UNBOUND_MACHINE.get());
    }
}
