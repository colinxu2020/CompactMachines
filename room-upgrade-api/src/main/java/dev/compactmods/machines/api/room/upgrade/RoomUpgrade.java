package dev.compactmods.machines.api.room.upgrade;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static dev.compactmods.machines.api.Constants.MOD_ID;

public interface RoomUpgrade {

    TagKey<Item> ITEM_TAG = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "room_upgrade"));

}
