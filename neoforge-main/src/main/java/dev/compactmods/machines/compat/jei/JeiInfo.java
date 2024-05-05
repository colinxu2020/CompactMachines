package dev.compactmods.machines.compat.jei;

import dev.compactmods.machines.api.Constants;
import net.minecraft.resources.ResourceLocation;

public interface JeiInfo {
    ResourceLocation MACHINE = new ResourceLocation(Constants.MOD_ID, "machines");

    ResourceLocation SHRINKING_DEVICE = new ResourceLocation(Constants.MOD_ID, "shrinking_device");
}
