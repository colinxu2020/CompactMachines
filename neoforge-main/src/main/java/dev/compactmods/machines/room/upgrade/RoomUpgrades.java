package dev.compactmods.machines.room.upgrade;

import dev.compactmods.machines.CMRegistries;
import dev.compactmods.machines.api.room.upgrade.RoomUpgradeType;
import dev.compactmods.machines.api.room.upgrade.SimpleRoomUpgradeType;
import dev.compactmods.machines.api.room.upgrade.components.RoomUpgradeList;
import dev.compactmods.machines.room.upgrade.example.TreeCutterUpgrade;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;

public interface RoomUpgrades {

  DeferredHolder<DataComponentType<?>, DataComponentType<RoomUpgradeList>> UPGRADE_LIST_COMPONENT = CMRegistries.DATA_COMPONENTS
      .registerComponentType("room_upgrades", (builder) -> builder
          .persistent(RoomUpgradeList.CODEC)
          .networkSynchronized(RoomUpgradeList.STREAM_CODEC));

  DeferredHolder<RoomUpgradeType<?>, SimpleRoomUpgradeType<TreeCutterUpgrade>> TREECUTTER = CMRegistries.ROOM_UPGRADE_TYPES
      .register("tree_cutter", () -> new SimpleRoomUpgradeType<>(TreeCutterUpgrade.CODEC));

  static void prepare() {}
}
