package dev.compactmods.machines.neoforge.machine.block;

import dev.compactmods.machines.api.machine.block.ICompactMachineBlockEntity;
import dev.compactmods.machines.neoforge.machine.Machines;
import dev.compactmods.machines.neoforge.network.machine.MachineColorSyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompactMachineBlock extends Block {
    public CompactMachineBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(level, pPos, pState, pPlacer, pStack);

        final var color = pStack.getOrDefault(Machines.DataComponents.MACHINE_COLOR, DyeColor.WHITE.getFireworkColor());
        final var be = level.getBlockEntity(pPos);
        if(be != null)
            be.setData(Machines.Attachments.MACHINE_COLOR, color);
    }

    @NotNull
    protected static ItemInteractionResult tryDyingMachine(ServerLevel level, @NotNull BlockPos pos, Player player, DyeItem dye, ItemStack mainItem) {
        var color = dye.getDyeColor();
        final var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ICompactMachineBlockEntity) {
            blockEntity.setData(Machines.Attachments.MACHINE_COLOR, color.getFireworkColor());

            PacketDistributor.sendToPlayersTrackingChunk(
                    level, new ChunkPos(pos), new MachineColorSyncPacket(GlobalPos.of(level.dimension(), pos), color.getFireworkColor()));

            if (!player.isCreative())
                mainItem.shrink(1);

            return ItemInteractionResult.CONSUME;
        }

        return ItemInteractionResult.FAIL;
    }
}
