package dev.compactmods.machines.api.room.upgrade;

import com.mojang.serialization.MapCodec;
import dev.compactmods.machines.api.CompactMachinesApi;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface RoomUpgradeType<RU extends RoomUpgrade> {

  ResourceKey<Registry<RoomUpgradeType<?>>> REG_KEY = ResourceKey.createRegistryKey(CompactMachinesApi.modRL("room_upgrades"));

  MapCodec<RU> codec();
}
