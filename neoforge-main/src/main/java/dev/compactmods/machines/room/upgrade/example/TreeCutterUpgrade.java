package dev.compactmods.machines.room.upgrade.example;

import com.mojang.serialization.MapCodec;
import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.api.room.upgrade.RoomUpgrade;
import dev.compactmods.machines.api.room.upgrade.RoomUpgradeDefinition;
import dev.compactmods.machines.api.room.upgrade.events.RoomUpgradeEvent;
import dev.compactmods.machines.api.room.upgrade.events.lifecycle.UpgradeTickedEventListener;
import dev.compactmods.machines.room.upgrade.RoomUpgrades;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeCutterUpgrade implements RoomUpgrade {

   public static final MapCodec<TreeCutterUpgrade> CODEC = MapCodec.unit(TreeCutterUpgrade::new);

   @Override
   public void addToTooltip(@NotNull Item.TooltipContext ctx, Consumer<Component> tooltips, @NotNull TooltipFlag flags) {
	  final var c = Component.literal("Tree Cutter")
		  .withColor(CommonColors.LIGHT_GRAY);

	  tooltips.accept(c);
   }

   @Override
   public Stream<RoomUpgradeEvent> gatherEvents() {
	  final UpgradeTickedEventListener ticker = TreeCutterUpgrade::onTick;
	  return Stream.of(ticker);
   }

   @Override
   public RoomUpgradeDefinition<TreeCutterUpgrade> getType() {
	  return RoomUpgrades.TREECUTTER.get();
   }

   public static void onTick(ServerLevel level, RoomInstance room, ItemStack upgrade) {
	  final var innerBounds = room.boundaries().innerBounds();

	  final var everythingLoaded = room.boundaries()
		  .innerChunkPositions()
		  .allMatch(cp -> level.shouldTickBlocksAt(cp.toLong()));

	  // TODO - Implement upgrade cooldowns (i.e. retry in 100 ticks if room isn't loaded)
	  if (!everythingLoaded) return;

	  final var logs = BlockPos.betweenClosedStream(innerBounds)
		  .filter(pos -> {
			 final var state = level.getBlockState(pos);
			 return state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES);
		  })
		  .map(BlockPos::immutable)
		  .limit(5)
		  .collect(Collectors.toUnmodifiableSet());

	  if (!logs.isEmpty()) {
		 logs.forEach(pos -> level.destroyBlock(pos, true));
		 upgrade.hurtAndBreak(logs.size(), level, null, (item) -> {
			upgrade.shrink(1);
		 });
	  }
   }
}
