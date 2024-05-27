package dev.compactmods.machines.client.machine;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public interface MachinesClient {
   static void registerEvents(IEventBus modBus) {
	  modBus.addListener(MachineClientEvents::registerMenuScreens);
	  modBus.addListener(MachineClientEvents::onBlockColors);
	  modBus.addListener(MachineClientEvents::onItemColors);

	  NeoForge.EVENT_BUS.addListener(MachineClientEvents::handleKeybinds);
   }
}
