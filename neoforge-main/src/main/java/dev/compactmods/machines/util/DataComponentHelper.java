package dev.compactmods.machines.util;

import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Consumer;

public class DataComponentHelper {

    public static <T extends TooltipProvider> void addComponentInfo(DataComponentHolder holder, DeferredHolder<DataComponentType<?>, DataComponentType<T>> componentType,
                                                                    Item.TooltipContext ctx, Consumer<Component> consumer, TooltipFlag flags) {
        T t = holder.get(componentType);
        if (t != null) {
            t.addToTooltip(ctx, consumer, flags);
        }
    }
}
