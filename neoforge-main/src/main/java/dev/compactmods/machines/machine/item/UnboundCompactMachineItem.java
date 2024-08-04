package dev.compactmods.machines.machine.item;

import dev.compactmods.machines.api.Translations;
import dev.compactmods.machines.api.machine.MachineTranslations;
import dev.compactmods.machines.api.room.template.RoomTemplate;
import dev.compactmods.machines.machine.Machines;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a machine item that has not been bound to a room yet,
 * but has an assigned template to use.
 */
public class UnboundCompactMachineItem extends BlockItem {

    public UnboundCompactMachineItem(Properties builder) {
        super(Machines.Blocks.UNBOUND_MACHINE.get(), builder);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return Component.translatableWithFallback(getDescriptionId(pStack), "Compact Machine");
    }

    @NotNull
    @Override
    public String getDescriptionId(ItemStack stack) {
        return Util.makeDescriptionId("machine", getTemplateId(stack));
    }

    @Override
    public ItemStack getDefaultInstance() {
        var stack = new ItemStack(this);
        stack.set(Machines.DataComponents.ROOM_TEMPLATE_ID, RoomTemplate.NO_TEMPLATE);
        stack.set(Machines.DataComponents.MACHINE_COLOR, CommonColors.WHITE);
        return stack;
    }

    private ResourceLocation getTemplateId(ItemStack stack) {
        return stack.get(Machines.DataComponents.ROOM_TEMPLATE_ID);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
        super.appendHoverText(stack, context, tooltip, flags);

        tooltip.add(Component.translatableWithFallback(MachineTranslations.IDs.NEW_MACHINE, "New Machine"));
    }
}
