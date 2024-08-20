package dev.compactmods.machines.room.upgrade.example;

import com.google.common.base.Objects;
import com.mojang.serialization.MapCodec;
import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.api.room.upgrade.RoomUpgrade;
import dev.compactmods.machines.api.room.upgrade.RoomUpgradeDefinition;
import dev.compactmods.machines.api.room.upgrade.events.RoomUpgradeEvent;
import dev.compactmods.machines.api.room.upgrade.events.lifecycle.UpgradeTickedEventListener;
import dev.compactmods.machines.api.util.AABBHelper;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.room.upgrade.RoomUpgrades;
import dev.compactmods.machines.util.item.ItemHandlerScan;
import dev.compactmods.machines.util.item.ItemHandlerUtil;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
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

        var energyHandler = upgrade.getCapability(Capabilities.EnergyStorage.ITEM);

        boolean doItemDamage = false;
        boolean preferEnergy = false;
        int maxAllowed = 0;
        if (upgrade.isDamageableItem()) {
            doItemDamage = true;
            var durabilityLeft = upgrade.getMaxDamage() - upgrade.getDamageValue();
            maxAllowed = Math.clamp(durabilityLeft, 0, 5);
        }

        if (energyHandler != null && energyHandler.canExtract()) {
            doItemDamage = true;
            preferEnergy = true;
            maxAllowed = Math.clamp(energyHandler.getEnergyStored() / 10, 0, 5);
        }

        final var logs = BlockPos.betweenClosedStream(innerBounds)
                .map(pos -> {
                    final var state = level.getBlockState(pos);
                    return Pair.of(pos.immutable(), state);
                })
                .filter(pair -> pair.right().is(BlockTags.LOGS) || pair.right().is(BlockTags.LEAVES))
                .limit(maxAllowed)
                .collect(Collectors.toUnmodifiableSet());

        final var numLogs = logs.size();

        if (!logs.isEmpty()) {

            final var bounds = room.boundaries().innerBounds();
            final var maybeChest = BlockPos.containing(AABBHelper.minCorner(bounds));

            var itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, maybeChest, null);
            if (itemHandler == null)
                itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, maybeChest, Direction.UP);

            for (Pair<BlockPos, BlockState> pos : logs) {
                final var blockEntity = level.getBlockEntity(pos.left());
                final var drops = Block.getDrops(pos.right(), level, pos.left(), blockEntity, null, upgrade);
                level.destroyBlock(pos.left(), false);

                if (!drops.isEmpty()) {
                    if (itemHandler != null) {
                        var failed = ItemHandlerUtil.insertMultipleStacks(itemHandler, drops);

                        for (var failedStack : failed) {
                            Block.popResource(level, maybeChest.above(), failedStack);
                        }
                    } else {
                        for (var failedStack : drops) {
                            Block.popResource(level, maybeChest.above(), failedStack);
                        }
                    }
                }
            }


            if (preferEnergy) {
                energyHandler.extractEnergy(numLogs * 10, false);
            } else {
                upgrade.hurtAndBreak(numLogs, level, null, (item) -> {
                    upgrade.shrink(1);
                });
            }
        }
    }
}
