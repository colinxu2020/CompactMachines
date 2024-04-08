package dev.compactmods.machines.neoforge.machine.block;

import dev.compactmods.machines.LoggingUtil;
import dev.compactmods.machines.neoforge.machine.MachineCreator;
import dev.compactmods.machines.api.room.RoomApi;
import dev.compactmods.machines.api.shrinking.PSDTags;
import dev.compactmods.machines.machine.EnumMachinePlayersBreakHandling;
import dev.compactmods.machines.neoforge.config.ServerConfig;
import dev.compactmods.machines.neoforge.machine.Machines;
import dev.compactmods.machines.neoforge.room.RoomHelper;
import dev.compactmods.machines.neoforge.room.Rooms;
import dev.compactmods.machines.neoforge.room.ui.preview.MachineRoomMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class BoundCompactMachineBlock extends CompactMachineBlock implements EntityBlock {
    public BoundCompactMachineBlock(Properties props) {
        super(props);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        try {
            if (level.getBlockEntity(pos) instanceof BoundCompactMachineBlockEntity be) {
                return MachineCreator.boundToRoom(be.connectedRoom(), be.getColor());
            }

            return MachineCreator.unbound();
        }

        catch(Exception ex) {
            LoggingUtil.modLog().warn("Warning: tried to pick block on a bound machine that does not have a room bound.", ex);
            return MachineCreator.unbound();
        }
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        int baseSpeedForge = CommonHooks.isCorrectToolForDrops(state, player) ? 30 : 100;
        float normalHardness = player.getDigSpeed(state, pos) / baseSpeedForge;

        if (level.getBlockEntity(pos) instanceof BoundCompactMachineBlockEntity bound) {
            boolean hasPlayers = bound.hasPlayersInside();

            // If there are players inside, check config for break handling
            if (hasPlayers) {
                EnumMachinePlayersBreakHandling hand = ServerConfig.MACHINE_PLAYER_BREAK_HANDLING.get();
                switch (hand) {
                    case UNBREAKABLE:
                        return 0;

                    case OWNER:
                        Optional<UUID> ownerUUID = bound.getOwnerUUID();
                        return ownerUUID
                                .map(uuid -> player.getUUID() == uuid ? normalHardness : 0)
                                .orElse(normalHardness);

                    case ANYONE:
                        return normalHardness;
                }
            }

            // No players inside - let anyone break it
            return normalHardness;
        } else {
            return normalHardness;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BoundCompactMachineBlockEntity(pos, state);
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        ItemStack mainItem = player.getMainHandItem();

        if (mainItem.getItem() instanceof DyeItem dye && !level.isClientSide) {
            return tryDyingMachine(level, pos, player, dye, mainItem);
        }

        if (mainItem.is(PSDTags.ITEM)
                && player instanceof ServerPlayer sp
                && level.getBlockEntity(pos) instanceof BoundCompactMachineBlockEntity tile) {

            // Try to teleport player into room
            RoomHelper.teleportPlayerIntoMachine(level, sp, tile.getLevelPosition(), tile.connectedRoom());
            return InteractionResult.SUCCESS;
        }

        // All other items, open preview screen
        if (!level.isClientSide && !(player instanceof FakePlayer)) {
            level.getBlockEntity(pos, Machines.MACHINE_ENTITY.get()).ifPresent(machine -> {
                final var roomCode = machine.connectedRoom();
                RoomApi.room(roomCode).ifPresent(inst -> {
                    if (player instanceof ServerPlayer sp) {
                        sp.setData(Rooms.OPEN_MACHINE_POS, machine.getLevelPosition());
                        sp.openMenu(MachineRoomMenu.provider(sp.server, inst), (buf) -> {
                            buf.writeJsonWithCodec(GlobalPos.CODEC, machine.getLevelPosition());
                            buf.writeUtf(roomCode);
                            buf.writeOptional(Optional.<String>empty(), FriendlyByteBuf::writeUtf);
                        });
                    }
                });
            });
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        // TODO: Remove machine from room graph
    }
}
