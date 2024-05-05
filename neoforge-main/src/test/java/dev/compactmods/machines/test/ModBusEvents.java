package dev.compactmods.machines.test;

import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.neoforge.CompactMachines;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEvents {

    @SubscribeEvent
    public static void onGameTests(final RegisterGameTestsEvent gameTests) {

    }
}
