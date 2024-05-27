package dev.compactmods.machines.datagen.tags;

import dev.compactmods.machines.api.CompactMachinesApi;
import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.room.Rooms;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends BlockTagsProvider {

  public BlockTagGenerator(PackOutput packOut, ExistingFileHelper files, CompletableFuture<HolderLookup.Provider> lookup) {
	 super(packOut, lookup, CompactMachinesApi.MOD_ID, files);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
	 var breakableWall = Rooms.Blocks.BREAKABLE_WALL.get();
	 var boundMachine = Machines.Blocks.BOUND_MACHINE.get();
	 var unboundMachine = Machines.Blocks.UNBOUND_MACHINE.get();

	 tag(MachineConstants.MACHINE_BLOCK)
		  .add(boundMachine, unboundMachine);

	 tag(MachineConstants.UNBOUND_MACHINE_BLOCK)
		  .add(unboundMachine);

	 tag(BlockTags.MINEABLE_WITH_PICKAXE)
		  .add(breakableWall)
		  .add(boundMachine, unboundMachine);

	 tag(BlockTags.NEEDS_IRON_TOOL)
		  .add(breakableWall)
		  .add(boundMachine, unboundMachine);
  }
}
