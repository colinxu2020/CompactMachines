package dev.compactmods.machines.room;

import dev.compactmods.machines.api.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class RoomClientEventHandler {

    @SubscribeEvent
    public static void onOverlayRegistration(final RegisterGuiLayersEvent layers) {
        // FIXME overlays.registerAbove(VanillaGuiOverlay.DEBUG_SCREEN.id(), new ResourceLocation(Constants.MOD_ID, "room_meta_debug"), new RoomMetadataDebugOverlay());
    }
}
