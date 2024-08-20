package dev.compactmods.machines.util.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ItemStackHandlerCodec implements Codec<ItemStackHandler> {

    public static final ItemStackHandlerCodec INSTANCE = new ItemStackHandlerCodec();

    @Override
    public <T> DataResult<Pair<ItemStackHandler, T>> decode(DynamicOps<T> ops, T input) {
        if (!(ops instanceof RegistryOps<T> regOps) || !(regOps.lookupProvider instanceof RegistryOps.HolderLookupAdapter adapter))
            return DataResult.error(() -> "Was not passed registry ops for serialization; cannot continue.");

        final var map = ops.getMap(input).getOrThrow();

        int maxSize = Codec.INT.fieldOf("Size")
                .decode(ops, map)
                .getOrThrow();

        ItemStackHandler handler = new ItemStackHandler(maxSize);
        CompoundTag.CODEC.listOf()
                .fieldOf("Items")
                .decode(ops, map)
                .ifSuccess(itemTags -> {
                    itemTags.forEach(itemTag -> {
                        int slot = itemTag.getInt("Slot");
                        var stack = ItemStack.parseOptional(adapter.lookupProvider, itemTag);
                        handler.setStackInSlot(slot, stack);
                    });
                });

        return DataResult.success(Pair.of(handler, input));
    }

    @Override
    public <T> DataResult<T> encode(ItemStackHandler input, DynamicOps<T> ops, T prefix) {
        if (!(ops instanceof RegistryOps<T> regOps) || !(regOps.lookupProvider instanceof RegistryOps.HolderLookupAdapter adapter))
            return DataResult.error(() -> "Was not passed registry ops for serialization; cannot continue.");

        var list = ops.listBuilder();
        for (int i = 0; i < input.getSlots(); i++) {
            final var inSlot = input.getStackInSlot(i);
            if (!inSlot.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                var encodedStack = inSlot.save(adapter.lookupProvider, itemTag);
                if (encodedStack instanceof CompoundTag ct)
                    list.add(CompoundTag.CODEC.encode(ct, ops, ops.empty()));
            }
        }

        return ops.mapBuilder()
                .add("Items", list.build(ops.empty()))
                .add("Size", ops.createInt(input.getSlots()))
                .build(prefix);
    }
}
