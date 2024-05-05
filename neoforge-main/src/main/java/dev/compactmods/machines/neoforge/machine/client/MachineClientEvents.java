package dev.compactmods.machines.neoforge.machine.client;

import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.neoforge.machine.Machines;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MachineClientEvents {

    @SubscribeEvent
    public static void onItemColors(final RegisterColorHandlersEvent.Item colors) {
        colors.register(MachineColors.ITEM, Machines.Items.BOUND_MACHINE.get(), Machines.Items.UNBOUND_MACHINE.get());
    }

    @SubscribeEvent
    public static void onBlockColors(final RegisterColorHandlersEvent.Block colors) {
        colors.register(MachineColors.BLOCK, Machines.Blocks.BOUND_MACHINE.get(), Machines.Blocks.UNBOUND_MACHINE.get());
    }
}
