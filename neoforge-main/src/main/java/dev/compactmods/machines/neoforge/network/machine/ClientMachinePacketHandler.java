package dev.compactmods.machines.neoforge.network.machine;

import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.neoforge.machine.Machines;
import net.minecraft.client.Minecraft;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class ClientMachinePacketHandler {
    public static void setMachineColor(GlobalPos position, int newColor) {
        var mc = Minecraft.getInstance();
        assert mc.level != null;
        if (mc.level.dimension() == position.dimension()) {
            var state = mc.level.getBlockState(position.pos());
            var blockEntity = mc.level.getBlockEntity(position.pos());
            if(state.is(MachineConstants.MACHINE_BLOCK)) {
                blockEntity.setData(Machines.Attachments.MACHINE_COLOR, newColor);
                mc.level.sendBlockUpdated(position.pos(), state, state, Block.UPDATE_ALL_IMMEDIATE);
            }
        }
    }
}
