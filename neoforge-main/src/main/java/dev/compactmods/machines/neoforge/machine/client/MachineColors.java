package dev.compactmods.machines.neoforge.machine.client;

import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.neoforge.machine.Machines;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;

public class MachineColors {

    private static final int DEFAULT = 0xFFFFFFFF;

    public static final ItemColor ITEM = (stack, pTintIndex) -> {
        if (!stack.is(MachineConstants.MACHINE_ITEM)) return DEFAULT;
        return pTintIndex == 0 ? stack.getData(Machines.MACHINE_COLOR) : DEFAULT;
    };

    public static final BlockColor BLOCK = (state, level, pos, tintIndex) -> {
        if (!state.is(MachineConstants.MACHINE_BLOCK) || level == null || pos == null)
            return DEFAULT;

        var be = level.getBlockEntity(pos);
        if (be != null)
            return tintIndex == 0 ? be.getData(Machines.MACHINE_COLOR) : DEFAULT;

        return DEFAULT;
    };
}
