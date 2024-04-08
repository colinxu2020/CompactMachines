package dev.compactmods.machines.neoforge.machine;

import com.google.common.base.Predicates;
import com.mojang.serialization.Codec;
import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.neoforge.Registries;
import dev.compactmods.machines.neoforge.machine.block.BoundCompactMachineBlock;
import dev.compactmods.machines.neoforge.machine.block.UnboundCompactMachineBlock;
import dev.compactmods.machines.neoforge.machine.block.BoundCompactMachineBlockEntity;
import dev.compactmods.machines.neoforge.machine.block.UnboundCompactMachineEntity;
import dev.compactmods.machines.neoforge.machine.item.BoundCompactMachineItem;
import dev.compactmods.machines.neoforge.machine.item.UnboundCompactMachineItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;
import net.minecraft.nbt.TagVisitor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.util.function.Supplier;

@SuppressWarnings("removal")
public interface Machines {
    // TODO: Metal material replacement
    BlockBehaviour.Properties MACHINE_BLOCK_PROPS = BlockBehaviour.Properties
            .of()
            .strength(8.0F, 20.0F)
            .requiresCorrectToolForDrops();

    Supplier<Item.Properties> MACHINE_ITEM_PROPS = Item.Properties::new;

    DeferredBlock<UnboundCompactMachineBlock> UNBOUND_MACHINE_BLOCK = Registries.BLOCKS.register("new_machine", () ->
            new UnboundCompactMachineBlock(MACHINE_BLOCK_PROPS));

    DeferredBlock<BoundCompactMachineBlock> MACHINE_BLOCK = Registries.BLOCKS.register("machine", () ->
            new BoundCompactMachineBlock(MACHINE_BLOCK_PROPS));

    DeferredItem<BoundCompactMachineItem> BOUND_MACHINE_BLOCK_ITEM = Registries.ITEMS.register("machine",
            () -> new BoundCompactMachineItem(MACHINE_ITEM_PROPS.get()));

    DeferredItem<UnboundCompactMachineItem> UNBOUND_MACHINE_BLOCK_ITEM = Registries.ITEMS.register("new_machine",
            () -> new UnboundCompactMachineItem(MACHINE_ITEM_PROPS.get()));

    DeferredHolder<BlockEntityType<?>, BlockEntityType<UnboundCompactMachineEntity>> UNBOUND_MACHINE_ENTITY = Registries.BLOCK_ENTITIES.register(MachineConstants.UNBOUND_MACHINE_ENTITY.getPath(), () ->
            BlockEntityType.Builder.of(UnboundCompactMachineEntity::new, UNBOUND_MACHINE_BLOCK.get())
                    .build(null));

    DeferredHolder<BlockEntityType<?>,BlockEntityType<BoundCompactMachineBlockEntity>> MACHINE_ENTITY = Registries.BLOCK_ENTITIES.register(MachineConstants.BOUND_MACHINE_ENTITY.getPath(), () ->
            BlockEntityType.Builder.of(BoundCompactMachineBlockEntity::new, MACHINE_BLOCK.get())
                    .build(null));

    Supplier<AttachmentType<Integer>> MACHINE_COLOR = Registries.ATTACHMENT_TYPES.register("machine_color", () -> AttachmentType
            .builder(() -> 0xFFFFFFFF)
            .serialize(new IAttachmentSerializer<IntTag, Integer>() {
                @Override
                public Integer read(IAttachmentHolder holder, IntTag tag) {
                    return tag.getAsInt();
                }

                @Override
                public @Nullable IntTag write(Integer attachment) {
                    return IntTag.valueOf(attachment);
                }
            })
            .build());

    static void prepare() {

    }
}
