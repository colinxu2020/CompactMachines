package dev.compactmods.machines.datagen;

import dev.compactmods.machines.api.CompactMachines;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.room.Rooms;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class StateGenerator extends BlockStateProvider {
    public StateGenerator(PackOutput packOutput, ExistingFileHelper exFileHelper) {
        super(packOutput, CompactMachines.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Wall block model
        BlockModelProvider models = models();

        var wall = models.cubeAll("block/wall", modLoc("block/wall"));
        simpleBlock(Rooms.Blocks.SOLID_WALL.get(), wall);
        simpleBlock(Rooms.Blocks.BREAKABLE_WALL.get(), wall);

        // New machine block
        final var m = models
                .withExistingParent("block/machine/machine", mcLoc("block/block"))
                .texture("border", modLoc("block/machine/border"))
                .texture("tint", modLoc("block/machine/tint"))
                .texture("overlay", modLoc("block/machine/overlay"))
                .renderType(mcLoc("cutout"))
                .element()
                .allFaces((dir, face) -> face.texture("#border")
                        .uvs(0, 0, 16, 16)
                        .cullface(dir)
                        .end())
                .end()
                .element()
                .allFaces((dir, face) -> face.texture("#tint")
                        .emissivity(2, 0)
                        .uvs(0, 0, 16, 16)
                        .cullface(dir)
                        .tintindex(0)
                        .end())
                .end()
                .element()
                .allFaces((dir, face) -> face.texture("#overlay")
                        .uvs(0, 0, 16, 16)
                        .cullface(dir)
                        .tintindex(1)
                        .end())
                .end();

        simpleBlock(Machines.Blocks.UNBOUND_MACHINE.get(), ConfiguredModel.builder()
                .modelFile(m)
                .build());

        simpleBlock(Machines.Blocks.BOUND_MACHINE.get(), ConfiguredModel.builder()
                .modelFile(m)
                .build());

//        this.simpleBlock(MachineRoomUpgrades.WORKBENCH_BLOCK.get(), models()
//                .cubeTop("block/workbench", modLoc("block/workbench/top"), modLoc("block/workbench/sides")));
    }
}
