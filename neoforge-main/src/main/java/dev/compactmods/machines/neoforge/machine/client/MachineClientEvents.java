package dev.compactmods.machines.neoforge.machine.client;

import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.neoforge.machine.Machines;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MachineClientEvents {

    @SubscribeEvent
    public static void onItemColors(final RegisterColorHandlersEvent.Item colors) {
        colors.register(MachineColors.ITEM, Machines.BOUND_MACHINE_BLOCK_ITEM.get(), Machines.UNBOUND_MACHINE_BLOCK_ITEM.get());
    }

    @SubscribeEvent
    public static void onBlockColors(final RegisterColorHandlersEvent.Block colors) {
        colors.register(MachineColors.BLOCK, Machines.MACHINE_BLOCK.get(), Machines.UNBOUND_MACHINE_BLOCK.get());
    }
}
