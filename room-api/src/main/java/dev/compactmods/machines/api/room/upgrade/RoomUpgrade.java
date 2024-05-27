package dev.compactmods.machines.api.room.upgrade;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.api.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.TooltipProvider;

public interface RoomUpgrade extends TooltipProvider {

   Codec<RoomUpgrade> DISPATCH_CODEC = Codec.lazyInitialized(() -> {
	  @SuppressWarnings("unchecked") final var reg = (Registry<RoomUpgradeType<?>>) BuiltInRegistries.REGISTRY.get(Constants.modRL("room_upgrades"));

	  if (reg != null) {
		 var upgradeRegistry = reg.byNameCodec();
		 return upgradeRegistry.dispatchStable(RoomUpgrade::getType, RoomUpgradeType::codec);
	  }

	  throw new RuntimeException("Room upgrade registry not registered yet; calling too early?");
   });

   RoomUpgradeType<?> getType();

   StreamCodec<RegistryFriendlyByteBuf, RoomUpgrade> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(DISPATCH_CODEC);

}
