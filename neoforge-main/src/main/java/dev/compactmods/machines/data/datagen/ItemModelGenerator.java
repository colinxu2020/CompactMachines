package dev.compactmods.machines.data.datagen;

import dev.compactmods.machines.api.CompactMachines;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.shrinking.Shrinking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ItemModelGenerator extends ItemModelProvider {

    public ItemModelGenerator(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, CompactMachines.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(Machines.Items.BOUND_MACHINE.getId().getPath(), modLoc("block/machine/machine"));
        withExistingParent(Machines.Items.UNBOUND_MACHINE.getId().getPath(), modLoc("block/machine/machine"));
        
        withExistingParent("solid_wall", modLoc("block/wall"));
        withExistingParent("wall", modLoc("block/wall"));

        basic(modLoc("personal_shrinking_device"))
                .texture("layer0", modLoc("item/personal_shrinking_device"));

        basic(Shrinking.SHRINKING_MODULE)
                .texture("layer0", modLoc("item/atom_shrinker"));

        basic(Shrinking.ENLARGING_MODULE)
                .texture("layer0", modLoc("item/atom_enlarger"));

//        basic(Shrinking.RESIZING_MODULE)
//                .texture("layer0", modLoc("item/atom_resizer"));
    }

    private ItemModelBuilder basic(ResourceLocation name) {
        return withExistingParent(name.getPath(), mcLoc("item/generated"));
    }

    private ItemModelBuilder basic(Supplier<Item> supplier) {
        Item i = supplier.get();
        return basic(BuiltInRegistries.ITEM.getKey(i));
    }
}
