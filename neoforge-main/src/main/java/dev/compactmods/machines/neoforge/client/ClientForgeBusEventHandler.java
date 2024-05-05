package dev.compactmods.machines.neoforge.client;

import dev.compactmods.machines.api.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientForgeBusEventHandler {

    @SubscribeEvent
    public static void onClientTick(final ClientTickEvent.Post clientTick) {
        if(RoomExitKeyMapping.MAPPING.consumeClick())
            RoomExitKeyMapping.handle();
    }
}
