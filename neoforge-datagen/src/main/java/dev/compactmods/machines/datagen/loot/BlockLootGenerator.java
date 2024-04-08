package dev.compactmods.machines.datagen.loot;

import dev.compactmods.machines.neoforge.data.functions.CopyRoomBindingFunction;
import dev.compactmods.machines.neoforge.machine.Machines;
import dev.compactmods.machines.neoforge.room.Rooms;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;

public class BlockLootGenerator extends BlockLootSubProvider {

    public BlockLootGenerator() {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Set.of(Rooms.BLOCK_BREAKABLE_WALL.get(), Machines.MACHINE_BLOCK.get());
    }

    @Override
    protected void generate() {
        this.add(Rooms.BLOCK_BREAKABLE_WALL.get(), LootTable.lootTable().withPool(LootPool
                .lootPool()
                .name(Rooms.BLOCK_BREAKABLE_WALL.getId().toString())
                .setRolls(ConstantValue.exactly(1))
                .when(ExplosionCondition.survivesExplosion())
                .add(LootItem.lootTableItem(Rooms.ITEM_BREAKABLE_WALL.get()))));

        var drop = LootItem.lootTableItem(Machines.BOUND_MACHINE_BLOCK_ITEM.get());

        final var lootPoolCM = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(ExplosionCondition.survivesExplosion())
                .apply(CopyRoomBindingFunction::new)
                .add(drop);

        final var cmLootTable = LootTable.lootTable().withPool(lootPoolCM);

        this.add(Machines.MACHINE_BLOCK.get(), cmLootTable);
    }
}
