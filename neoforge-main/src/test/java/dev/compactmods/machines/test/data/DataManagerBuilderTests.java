package dev.compactmods.machines.test.data;

import dev.compactmods.machines.api.CompactMachinesApi;
import dev.compactmods.machines.data.DataManagerBuilder;
import dev.compactmods.machines.data.manager.IKeyedDataFileManager;
import dev.compactmods.machines.data.room.RoomDataAttachments;
import dev.compactmods.machines.test.util.CompactGameTestHelper;
import net.minecraft.gametest.framework.GameTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.gametest.EmptyTemplate;

@ForEachTest(groups = "data_manager_builder")
public class DataManagerBuilderTests {

   @GameTest
   @EmptyTemplate
   public static void testCanCreate(CompactGameTestHelper helper) {

	  // TODO - TEST
	  
	  var dataManager = DataManagerBuilder
		  .<String, RoomDataAttachments>keyed(helper.getLevel().getServer(), CompactMachinesApi.modRL("test"))
		  .build();

	  var data = dataManager.data("room-code-here");

	  dataManager.save();

	  helper.succeed();
   }
}
