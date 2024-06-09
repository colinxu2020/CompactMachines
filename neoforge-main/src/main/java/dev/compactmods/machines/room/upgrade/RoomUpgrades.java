package dev.compactmods.machines.room.upgrade;

import dev.compactmods.machines.CMRegistries;
import dev.compactmods.machines.api.CompactMachinesApi;
import dev.compactmods.machines.api.room.RoomApi;
import dev.compactmods.machines.api.room.upgrade.RoomUpgradeDefinition;
import dev.compactmods.machines.api.room.upgrade.components.RoomUpgradeList;
import dev.compactmods.machines.room.upgrade.example.TreeCutterUpgrade;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface RoomUpgrades {

  DeferredRegister<RoomUpgradeDefinition<?>> ROOM_UPGRADE_DEFINITIONS = RoomApi.roomUpgradeDR(CompactMachinesApi.MOD_ID);

  DeferredHolder<DataComponentType<?>, DataComponentType<RoomUpgradeList>> UPGRADE_LIST_COMPONENT = CMRegistries.DATA_COMPONENTS
      .registerComponentType("room_upgrades", (builder) -> builder
          .persistent(RoomUpgradeList.CODEC)
          .networkSynchronized(RoomUpgradeList.STREAM_CODEC));

  DeferredHolder<RoomUpgradeDefinition<?>, RoomUpgradeDefinition<TreeCutterUpgrade>> TREECUTTER = ROOM_UPGRADE_DEFINITIONS
      .register("tree_cutter", () -> new RoomUpgradeDefinition<>(TreeCutterUpgrade.CODEC));

  static void prepare() {
    ROOM_UPGRADE_DEFINITIONS.makeRegistry(builder -> {
      builder.sync(true);
    });
  }

  static void registerEvents(IEventBus modBus) {
    ROOM_UPGRADE_DEFINITIONS.register(modBus);

    NeoForge.EVENT_BUS.addListener(RoomUpgradeEventHandlers::onLevelTick);
    NeoForge.EVENT_BUS.addListener(RoomUpgradeEventHandlers::onTooltips);
  }
}
