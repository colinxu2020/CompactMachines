package dev.compactmods.machines.shrinking;

import dev.compactmods.machines.api.room.RoomApi;
import dev.compactmods.machines.api.Translations;
import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.api.room.RoomTranslations;
import dev.compactmods.machines.room.RoomHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class PersonalShrinkingDevice extends Item {

    public PersonalShrinkingDevice(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag flags) {
        super.appendHoverText(stack, context, tooltips, flags);
        tooltips.add(Screen.hasShiftDown() ? Translations.UNBREAKABLE_BLOCK.get() : Translations.HINT_HOLD_SHIFT.get());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND) {
            return InteractionResultHolder.fail(stack);
        }

        // If we aren't in the compact dimension, allow PSD guide usage
        // Prevents misfiring if a player is trying to leave a machine or set their spawn
        if (world.isClientSide && !world.dimension().equals(CompactDimension.LEVEL_KEY)) {
            // fixme PersonalShrinkingDeviceScreen.show();
            return InteractionResultHolder.success(stack);
        }

        if (world instanceof ServerLevel playerDim && player instanceof ServerPlayer serverPlayer) {
            if (playerDim.dimension().equals(CompactDimension.LEVEL_KEY)) {
                if (player.isShiftKeyDown()) {
                    final var roomCode = RoomApi.chunkManager()
                            .findRoomByChunk(serverPlayer.chunkPosition())
                            .orElseThrow();

                    final var spawnManager = RoomApi.spawnManager(roomCode);
                    spawnManager.setPlayerSpawn(serverPlayer);

                    player.displayClientMessage(RoomTranslations.ROOM_SPAWNPOINT_SET.apply(serverPlayer, roomCode), true);
                } else {
                    RoomHelper.teleportPlayerOutOfRoom(serverPlayer);
                }
            }
        }

        return InteractionResultHolder.success(stack);
    }
}
