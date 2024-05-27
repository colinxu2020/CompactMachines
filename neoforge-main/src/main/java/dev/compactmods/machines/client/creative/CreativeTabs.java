package dev.compactmods.machines.client.creative;

import dev.compactmods.machines.api.room.RoomTemplate;
import dev.compactmods.machines.api.CompactMachinesApi;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.room.Rooms;
import dev.compactmods.machines.shrinking.Shrinking;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import static dev.compactmods.machines.CMRegistries.TABS;

public interface CreativeTabs {

    ResourceLocation MAIN_RL = CompactMachinesApi.modRL("main");

    static void prepare() {
        TABS.register(MAIN_RL.getPath(), () -> CreativeModeTab.builder()
            .icon(Machines.Items::unbound)
            .title(Component.translatableWithFallback("itemGroup.compactmachines.main", "Compact Machines"))
            .displayItems(CreativeTabs::fillItems)
            .build());
    }

    static void fillItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
        output.accept(Shrinking.PERSONAL_SHRINKING_DEVICE.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        output.accept(Rooms.Items.BREAKABLE_WALL.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        output.accept(Shrinking.SHRINKING_MODULE.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        output.accept(Shrinking.ENLARGING_MODULE.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        // output.accept(Shrinking.RESIZING_MODULE.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

        final var lookup = params.holders().lookupOrThrow(RoomTemplate.REGISTRY_KEY);
        final var machines = lookup.listElements()
            .map(k -> Machines.Items.forNewRoom(k.key().location(), k.value()))
            .toList();

        output.acceptAll(machines, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}
