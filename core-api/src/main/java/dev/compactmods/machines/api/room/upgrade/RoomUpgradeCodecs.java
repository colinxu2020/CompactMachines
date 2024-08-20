package dev.compactmods.machines.api.room.upgrade;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.api.CompactMachines;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public interface RoomUpgradeCodecs {
    Codec<RoomUpgrade> DISPATCH_CODEC = Codec.lazyInitialized(() -> {
       @SuppressWarnings("unchecked") final var reg = (Registry<RoomUpgradeDefinition<?>>) BuiltInRegistries.REGISTRY.get(CompactMachines.modRL("room_upgrades"));

       if (reg != null) {
          var upgradeRegistry = reg.byNameCodec();
          return upgradeRegistry.dispatchStable(RoomUpgrade::getType, RoomUpgradeDefinition::codec);
       }

       throw new RuntimeException("Room upgrade registry not registered yet; calling too early?");
    });
    StreamCodec<RegistryFriendlyByteBuf, RoomUpgrade> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(DISPATCH_CODEC);
}
