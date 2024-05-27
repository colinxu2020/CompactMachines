package dev.compactmods.machines.client;

import dev.compactmods.machines.client.machine.MachinesClient;
import dev.compactmods.machines.client.room.RoomsClient;
import net.neoforged.bus.api.IEventBus;

public class CompactMachinesClient {

   public static void registerEvents(IEventBus modBus) {
	  MachinesClient.registerEvents(modBus);
	  RoomsClient.registerEvents(modBus);
   }

}
