package dev.compactmods.machines.test;

import dev.compactmods.machines.api.CompactMachinesApi;
import dev.compactmods.machines.api.room.CompactMachines.roomApi();
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.testframework.conf.Feature;
import net.neoforged.testframework.conf.FrameworkConfiguration;

@Mod(CompactMachinesApi.MOD_ID)
public class CompactMachinesTest {

   public CompactMachinesTest(ModContainer container, IEventBus modBus) {
	  final var config = FrameworkConfiguration.builder(CompactMachinesApi.modRL("tests"))
		  .enable(Feature.GAMETEST)
		  .build();

	  var fw = config.create();
	  fw.init(modBus, container);
   }
}
