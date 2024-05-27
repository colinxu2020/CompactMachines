package dev.compactmods.machines.client.creative;

import dev.compactmods.machines.api.room.RoomTemplate;
import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.machine.MachineItemCreator;
import dev.compactmods.machines.CompactMachines;
import dev.compactmods.machines.room.Rooms;
import dev.compactmods.machines.shrinking.Shrinking;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;

import static dev.compactmods.machines.CMRegistries.TABS;

public interface CreativeTabs {

    ResourceLocation MAIN_RL = Constants.modRL("main");
    ResourceLocation LINKED_MACHINES_RL = Constants.modRL("linked_machines");

    DeferredHolder<CreativeModeTab, CreativeModeTab> NEW_MACHINES = TABS.register(MAIN_RL.getPath(), () -> CreativeModeTab.builder()
            .icon(MachineItemCreator::unbound)
            .title(Component.translatableWithFallback("itemGroup.compactmachines.main", "Compact Machines"))
            .displayItems(CreativeTabs::fillItems)
            .build());

    DeferredHolder<CreativeModeTab, CreativeModeTab> EXISTING_MACHINES = TABS.register(LINKED_MACHINES_RL.getPath(), () -> CreativeModeTab.builder()
            .icon(() -> {
                final var ub = MachineItemCreator.unboundColored(CompactMachines.BRAND_MACHINE_COLOR);
                return ub;
            })
            .title(Component.translatableWithFallback("itemGroup.compactmachines.linked_machines", "Linked Machines"))
            .withTabsBefore(MAIN_RL)
            .withSearchBar()
            .build());

    static void fillItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
        output.accept(Shrinking.PERSONAL_SHRINKING_DEVICE.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        output.accept(Rooms.Items.BREAKABLE_WALL.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        output.accept(Shrinking.SHRINKING_MODULE.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        output.accept(Shrinking.ENLARGING_MODULE.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        // output.accept(Shrinking.RESIZING_MODULE.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

        final var lookup = params.holders().lookupOrThrow(RoomTemplate.REGISTRY_KEY);
        final var machines = lookup.listElements()
                .map(k -> MachineItemCreator.forNewRoom(k.key().location(), k.value()))
                .toList();

        output.acceptAll(machines, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    static void prepare() {}
}
