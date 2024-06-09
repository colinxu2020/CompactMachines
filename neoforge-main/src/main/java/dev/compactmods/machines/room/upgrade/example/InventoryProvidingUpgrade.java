package dev.compactmods.machines.room.upgrade.example;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.api.room.upgrade.DataProvidingUpgrade;
import dev.compactmods.machines.api.room.upgrade.RoomUpgrade;
import dev.compactmods.machines.api.room.upgrade.RoomUpgradeDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.function.Consumer;

public class InventoryProvidingUpgrade implements RoomUpgrade {
	//, DataProvidingUpgrade<ItemStackHandler> {
//   @Override
//   public Codec<ItemStackHandler> dataCodec() {
//	  // return Codec.of(ItemStackHandler.)
//   }

//   @Override
//   public ItemStackHandler initialize(RoomInstance roomInstance) {
//	  return new ItemStackHandler(5);
//   }
//
//   @Override
//   public boolean canRemove(RoomInstance roomInstance, ItemStackHandler data) {
//	  return false;
//   }

   @Override
   public RoomUpgradeDefinition<?> getType() {
	  return null;
   }

   @Override
   public void addToTooltip(Item.TooltipContext pContext, Consumer<Component> pTooltipAdder, TooltipFlag pTooltipFlag) {

   }
}
