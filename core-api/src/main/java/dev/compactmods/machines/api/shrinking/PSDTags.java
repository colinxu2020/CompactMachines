package dev.compactmods.machines.api.shrinking;

import dev.compactmods.machines.api.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface PSDTags {
    /**
     * Marks an item as a personal shrinking device.
     */
    TagKey<Item> ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Constants.MOD_ID, "shrinking_device"));
}
