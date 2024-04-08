package dev.compactmods.machines.neoforge.machine.client;

import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.neoforge.machine.Machines;
import dev.compactmods.machines.neoforge.machine.block.UnboundCompactMachineEntity;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class MachineColors {

    private static final int DEFAULT = 0xFFFFFFFF;

    public static final ItemColor ITEM = (stack, pTintIndex) -> {
        if (!stack.is(MachineConstants.MACHINE_ITEM)) return DEFAULT;
        return pTintIndex == 0 ? stack.getData(Machines.MACHINE_COLOR) : DEFAULT;
    };

    public static final BlockColor BOUND_BLOCK = (state, level, pos, tintIndex) -> {
        if (!state.is(MachineConstants.MACHINE_BLOCK) || level == null || pos == null)
            return DEFAULT;

        var be = level.getBlockEntity(pos);
        if (be != null)
            return tintIndex == 0 ? be.getData(Machines.MACHINE_COLOR) : DEFAULT;

        return DEFAULT;
    };

    public static final BlockColor UNBOUND_BLOCK = (BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) -> switch (tintIndex) {
        case 0 -> level.getBlockEntity(pos) instanceof UnboundCompactMachineEntity unbound ? unbound.getColor() : 0xFFFFFFFF;
        default -> 0xFFFFFFFF;
    };
}
