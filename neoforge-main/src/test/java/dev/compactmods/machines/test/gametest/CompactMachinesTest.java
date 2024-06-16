package dev.compactmods.machines.test.gametest;

import dev.compactmods.machines.api.CompactMachines;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.testframework.conf.Feature;
import net.neoforged.testframework.conf.FrameworkConfiguration;

@Mod(CompactMachines.MOD_ID)
public class CompactMachinesTest {

   public CompactMachinesTest(ModContainer container, IEventBus modBus) {
	  final var config = FrameworkConfiguration.builder(CompactMachines.modRL("tests"))
		  .enable(Feature.GAMETEST)
		  .build();

	  var fw = config.create();
	  fw.init(modBus, container);
   }
}
