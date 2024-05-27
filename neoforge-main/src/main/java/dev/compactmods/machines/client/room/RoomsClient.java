package dev.compactmods.machines.client.room;

import net.neoforged.bus.api.IEventBus;

public interface RoomsClient {

   static void registerEvents(IEventBus modBus) {
	  modBus.addListener(RoomClientEvents::registerMenuScreens);
	  modBus.addListener(RoomClientEvents::onKeybindRegistration);
	  modBus.addListener(RoomClientEvents::onOverlayRegistration);
   }
}
