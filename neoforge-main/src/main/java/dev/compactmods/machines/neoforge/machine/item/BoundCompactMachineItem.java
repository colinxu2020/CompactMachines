package dev.compactmods.machines.neoforge.machine.item;

import dev.compactmods.machines.api.machine.item.IBoundCompactMachineItem;
import dev.compactmods.machines.neoforge.CompactMachines;
import dev.compactmods.machines.neoforge.machine.Machines;
import net.minecraft.Util;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BoundCompactMachineItem extends BlockItem implements IBoundCompactMachineItem {

    public static final String NBT_ROOM_DIMENSIONS = "room_dimensions";

    private static final String FALLBACK_ID = Util.makeDescriptionId("block", CompactMachines.rl("bound_machine_fallback"));

    public BoundCompactMachineItem(Properties builder) {
        super(Machines.MACHINE_BLOCK.get(), builder);
    }

    @Override
    public Component getName(ItemStack stack) {
        return getMachineName(stack)
                .map(Component::literal)
                .orElse(Component.translatableWithFallback(FALLBACK_ID, "Compact Machine"));
    }

    @NotNull
    @Override
    public String getDescriptionId(ItemStack stack) {
        return FALLBACK_ID;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
//        getRoom(stack).ifPresent(room -> {
//            // TODO - Server-synced room name list
//            tooltip.add(TranslationUtil.tooltip(Tooltips.ROOM_NAME, room));
//        });
    }
}
