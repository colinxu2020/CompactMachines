package dev.compactmods.machines.api.advancement;

import dev.compactmods.machines.api.CompactMachines;
import net.minecraft.resources.ResourceLocation;

/**
 * Holds references to advancements and grantables.
 */
public interface Advancements {
    /**
     * Granted when the player is teleported out by leaving room boundaries, or
     * when in an invalid state (such as trying to leave a machine room with no entry history)
     */
    ResourceLocation HOW_DID_YOU_GET_HERE = CompactMachines.modRL("how_did_you_get_here");

    /**
     * Root advancement. Required for vanilla's tree design.
     */
    ResourceLocation ROOT = CompactMachines.modRL("root");

    /**
     * Granted when a player first crafts machine items.
     */
    ResourceLocation FOUNDATIONS = CompactMachines.modRL("foundations");

    /**
     * Granted on first pickup of a PSD item.
     */
    ResourceLocation GOT_SHRINKING_DEVICE = CompactMachines.modRL("got_shrinking_device");

    /**
     * Granted if a player tries to enter a machine room they're currently in.
     */
    ResourceLocation RECURSIVE_ROOMS = CompactMachines.modRL("recursion");

    /**
     * Was granted when players gained a tiny machine item. TBR in 1.20 once tiny machines are no longer
     * part of the mod.
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    ResourceLocation CLAIMED_TINY_MACHINE = CompactMachines.modRL("claimed_machine_tiny");

    /**
     * Was granted when players gained a small machine item. TBR in 1.20 once small machines are no longer
     * part of the mod.
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    ResourceLocation CLAIMED_SMALL_MACHINE = CompactMachines.modRL("claimed_machine_small");

    /**
     * Was granted when players gained a normal machine item. TBR in 1.20 once normal machines are no longer
     * part of the mod.
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    ResourceLocation CLAIMED_NORMAL_MACHINE = CompactMachines.modRL("claimed_machine_normal");

    /**
     * Was granted when players gained a large machine item. TBR in 1.20 once large machines are no longer
     * part of the mod.
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    ResourceLocation CLAIMED_LARGE_MACHINE = CompactMachines.modRL("claimed_machine_large");

    /**
     * Was granted when players gained a giant machine item. TBR in 1.20 once giant machines are no longer
     * part of the mod.
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    ResourceLocation CLAIMED_GIANT_MACHINE = CompactMachines.modRL("claimed_machine_giant");

    /**
     * Was granted when players gained a maximum-size machine item. TBR in 1.20 once maximum-size machines are no longer
     * part of the mod.
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    ResourceLocation CLAIMED_MAX_MACHINE = CompactMachines.modRL("claimed_machine_max");
}
