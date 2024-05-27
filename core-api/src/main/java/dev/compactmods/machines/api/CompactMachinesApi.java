package dev.compactmods.machines.api;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface CompactMachinesApi {
    String MOD_ID = "compactmachines";

    static ResourceLocation modRL(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
