package dev.compactmods.machines.room.upgrade.example;

import com.mojang.serialization.MapCodec;
import dev.compactmods.machines.api.room.upgrade.RoomUpgrade;
import dev.compactmods.machines.api.room.upgrade.RoomUpgradeType;
import dev.compactmods.machines.room.upgrade.RoomUpgrades;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TreeCutterUpgrade implements RoomUpgrade {

   public static final MapCodec<TreeCutterUpgrade> CODEC = MapCodec.unit(TreeCutterUpgrade::new);

   @Override
   public void addToTooltip(@NotNull Item.TooltipContext ctx, Consumer<Component> tooltips, @NotNull TooltipFlag flags) {
	  final var c = Component.literal("Tree Cutter")
			  .withColor(CommonColors.LIGHT_GRAY);

	  tooltips.accept(c);
   }

   @Override
   public RoomUpgradeType<TreeCutterUpgrade> getType() {
	  return RoomUpgrades.TREECUTTER.get();
   }
}
