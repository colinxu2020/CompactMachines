package dev.compactmods.machines.api.room.upgrade;

import com.mojang.serialization.MapCodec;
import dev.compactmods.machines.api.CompactMachinesApi;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record RoomUpgradeDefinition<T extends RoomUpgrade>(MapCodec<T> codec) {

   public static final ResourceKey<Registry<RoomUpgradeDefinition<?>>> REG_KEY = ResourceKey
       .createRegistryKey(CompactMachinesApi.modRL("room_upgrades"));

}