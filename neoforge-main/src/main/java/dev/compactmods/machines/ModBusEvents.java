package dev.compactmods.machines;

import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.network.RoomNetworkHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event) {
        var logger = LoggingUtil.modLog();

        logger.trace("Initializing network handler.");
        RoomNetworkHandler.setupMessages();
    }
}
