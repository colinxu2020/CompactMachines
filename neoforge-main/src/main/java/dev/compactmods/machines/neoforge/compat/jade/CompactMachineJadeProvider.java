package dev.compactmods.machines.neoforge.compat.jade;

import com.mojang.authlib.GameProfile;
import dev.compactmods.machines.api.machine.MachineTranslations;
import dev.compactmods.machines.api.room.RoomApi;
import dev.compactmods.machines.neoforge.CompactMachines;
import dev.compactmods.machines.neoforge.machine.block.BoundCompactMachineBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class CompactMachineJadeProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final CompactMachineJadeProvider INSTANCE = new CompactMachineJadeProvider();

    @Override
    public ResourceLocation getUid() {
        return CompactMachines.rl("bound_machine");
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor) {
        final var player = blockAccessor.getPlayer();
        if (blockAccessor.getBlockEntity() instanceof BoundCompactMachineBlockEntity machine) {
            var owner = machine.getOwnerUUID().orElse(Util.NIL_UUID);
            tag.putUUID("owner", owner);

            RoomApi.room(machine.connectedRoom()).ifPresent(inst -> {
                tag.putString("room_code", inst.code());
            });
        }
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig config) {
        final var serverData = blockAccessor.getServerData();
        if (serverData.contains("owner")) {
            final var owner = blockAccessor.getLevel().getPlayerByUUID(serverData.getUUID("owner"));
            if (owner != null) {
                GameProfile ownerProfile = owner.getGameProfile();
                tooltip.add(Component
                        .translatable(MachineTranslations.IDs.OWNER, ownerProfile.getName())
                        .withStyle(ChatFormatting.GRAY));
            }
        }

        if (serverData.contains("room_code")) {
            tooltip.add(Component
                    .translatable(MachineTranslations.IDs.BOUND_TO, serverData.getString("room_code"))
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}