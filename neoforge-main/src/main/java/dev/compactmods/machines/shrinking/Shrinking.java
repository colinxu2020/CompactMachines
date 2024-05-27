package dev.compactmods.machines.shrinking;

import dev.compactmods.machines.CMRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public class Shrinking {

    public static final DeferredItem<PersonalShrinkingDevice> PERSONAL_SHRINKING_DEVICE = CMRegistries.ITEMS.register("personal_shrinking_device",
            () -> new PersonalShrinkingDevice(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SHRINKING_MODULE = CMRegistries.ITEMS.register("shrinking_module", CMRegistries::basicItem);
    public static final DeferredItem<Item> ENLARGING_MODULE = CMRegistries.ITEMS.register("enlarging_module", CMRegistries::basicItem);
    // public static final DeferredItem<Item> RESIZING_MODULE = Registries.ITEMS.register("resizing_module", Registries::basicItem);

    public static void prepare() {

    }
}

