package dev.compactmods.machines.data.datagen.tags;

import dev.compactmods.machines.api.machine.MachineConstants;
import dev.compactmods.machines.api.shrinking.PSDTags;
import dev.compactmods.machines.CMRegistries;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.shrinking.PersonalShrinkingDevice;
import dev.compactmods.machines.shrinking.Shrinking;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends ItemTagsProvider {
    public ItemTagGenerator(PackOutput packOut, BlockTagGenerator blocks, CompletableFuture<HolderLookup.Provider> lookups) {
        super(packOut, lookups, blocks.contentsGetter());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        final var psd = Shrinking.PERSONAL_SHRINKING_DEVICE.get();

        machines();
        shrinkingDevices(psd);
        curiosTags(psd);
    }

    private void shrinkingDevices(PersonalShrinkingDevice psd) {
        final var cmShrinkTag = tag(PSDTags.ITEM);
        cmShrinkTag.add(psd);
        cmShrinkTag.addOptional(ResourceLocation.fromNamespaceAndPath("shrink", "shrinking_device"));
    }

    private void curiosTags(PersonalShrinkingDevice psd) {
        final var curiosPsdTag = tag(TagKey.create(CMRegistries.ITEMS.getRegistryKey(), ResourceLocation.fromNamespaceAndPath("curios", "psd")));
        curiosPsdTag.add(psd);
    }

    private void machines() {
        var machinesTag = tag(MachineConstants.MACHINE_ITEM);
        var boundMachines = tag(MachineConstants.BOUND_MACHINE_ITEM);
        var unboundMachines = tag(MachineConstants.NEW_MACHINE_ITEM);

        var boundMachineItem = Machines.Items.BOUND_MACHINE.get();
        var unboundMachineItem = Machines.Items.UNBOUND_MACHINE.get();

        machinesTag.add(boundMachineItem);
        machinesTag.add(unboundMachineItem);
        boundMachines.add(boundMachineItem);
        unboundMachines.add(unboundMachineItem);
    }
}
