package dev.compactmods.machines.client.room;

import dev.compactmods.machines.client.keybinds.room.RoomExitKeyMapping;
import dev.compactmods.machines.room.Rooms;
import dev.compactmods.machines.room.ui.upgrades.RoomUpgradeScreen;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class RoomClientEvents {

    public static void registerMenuScreens(final RegisterMenuScreensEvent evt) {
        evt.register(Rooms.Menus.ROOM_UPGRADES.get(), RoomUpgradeScreen::new);
    }

    public static void onKeybindRegistration(final RegisterKeyMappingsEvent evt) {
        evt.register(RoomExitKeyMapping.MAPPING);
    }

    public static void onOverlayRegistration(final RegisterGuiLayersEvent layers) {
        // FIXME overlays.registerAbove(VanillaGuiOverlay.DEBUG_SCREEN.id(), new ResourceLocation(Constants.MOD_ID, "room_meta_debug"), new RoomMetadataDebugOverlay());
    }
}
