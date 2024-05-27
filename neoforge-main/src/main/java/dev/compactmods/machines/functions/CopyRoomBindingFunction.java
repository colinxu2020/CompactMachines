package dev.compactmods.machines.functions;

import com.mojang.serialization.MapCodec;
import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.machine.block.BoundCompactMachineBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;

public class CopyRoomBindingFunction implements LootItemFunction {

    public static final MapCodec<CopyRoomBindingFunction> CODEC = MapCodec.unit(new CopyRoomBindingFunction());

    @Override
    public ItemStack apply(ItemStack stack, LootContext ctx) {
        var state = ctx.getParam(LootContextParams.BLOCK_STATE);
        if(state.is(MachineConstants.MACHINE_BLOCK)) {
            var blockEntity = ctx.getParam(LootContextParams.BLOCK_ENTITY);
            if (blockEntity instanceof BoundCompactMachineBlockEntity machine && stack.is(MachineConstants.BOUND_MACHINE_ITEM)) {
                stack.set(Machines.DataComponents.MACHINE_COLOR, machine.getData(Machines.Attachments.MACHINE_COLOR));
                stack.set(Machines.DataComponents.BOUND_ROOM_CODE, machine.connectedRoom());
            }
        }

        return stack;
    }

    @Override
    public @NotNull LootItemFunctionType getType() {
        return LootFunctions.COPY_ROOM_BINDING.value();
    }
}
