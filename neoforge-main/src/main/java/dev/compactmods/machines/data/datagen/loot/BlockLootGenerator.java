package dev.compactmods.machines.data.datagen.loot;

import dev.compactmods.machines.data.functions.CopyRoomBindingFunction;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.room.Rooms;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Collections;
import java.util.Set;

public class BlockLootGenerator extends BlockLootSubProvider {

    public BlockLootGenerator(HolderLookup.Provider holderLookup) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), holderLookup);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Set.of(Rooms.Blocks.BREAKABLE_WALL.get(), Machines.Blocks.BOUND_MACHINE.get());
    }

    @Override
    protected void generate() {
        this.add(Rooms.Blocks.BREAKABLE_WALL.get(), LootTable.lootTable().withPool(LootPool
                .lootPool()
                .name(Rooms.Blocks.BREAKABLE_WALL.getId().toString())
                .setRolls(ConstantValue.exactly(1))
                .when(ExplosionCondition.survivesExplosion())
                .add(LootItem.lootTableItem(Rooms.Items.BREAKABLE_WALL.get()))));

        var drop = LootItem.lootTableItem(Machines.Items.BOUND_MACHINE.get());

        final var lootPoolCM = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(ExplosionCondition.survivesExplosion())
                .apply(CopyRoomBindingFunction::new)
                .add(drop);

        final var cmLootTable = LootTable.lootTable().withPool(lootPoolCM);

        this.add(Machines.Blocks.BOUND_MACHINE.get(), cmLootTable);
    }
}
