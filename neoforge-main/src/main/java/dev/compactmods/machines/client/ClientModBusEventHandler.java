package dev.compactmods.machines.client;

import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.room.ui.preview.MachineRoomScreen;
import dev.compactmods.machines.room.Rooms;
import dev.compactmods.machines.room.ui.upgrades.RoomUpgradeScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEventHandler {

    @SubscribeEvent
    public static void onKeybindRegistration(final RegisterKeyMappingsEvent evt) {
        evt.register(RoomExitKeyMapping.MAPPING);
    }

    @SubscribeEvent
    public static void onClientSetup(final RegisterMenuScreensEvent evt) {
        evt.register(Rooms.Menus.MACHINE_MENU.get(), MachineRoomScreen::new);
        evt.register(Rooms.Menus.ROOM_UPGRADES.get(), RoomUpgradeScreen::new);
    }
}
