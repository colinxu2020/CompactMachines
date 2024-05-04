package dev.compactmods.machines.api.room;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static dev.compactmods.machines.api.Constants.MOD_ID;

/**
 * Template structure for creating a new Compact Machine room. These can be added and removed from the registry
 * at any point, so persistent data must be stored outside these instances.
 *
 * @param internalDimensions      The internal dimensions of the room when it is created.
 * @param color           The color of the machine blocks created for this template.
 * @param prefillTemplate A template (structure) file reference, if specified this will fill the new room post-generation
 */
public record RoomTemplate(RoomDimensions internalDimensions, int color, List<RoomStructureInfo> structures) {

    public static final ResourceKey<Registry<RoomTemplate>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "room_templates"));

    public static final RoomTemplate INVALID_TEMPLATE = new RoomTemplate(0, 0);

    public static Codec<RoomTemplate> CODEC = RecordCodecBuilder.create(i -> i.group(
            RoomDimensions.CODEC.fieldOf("dimensions").forGetter(RoomTemplate::internalDimensions),
            Codec.INT.fieldOf("color").forGetter(RoomTemplate::color),
            RoomStructureInfo.CODEC.listOf().optionalFieldOf("structures", Collections.emptyList())
                    .forGetter(RoomTemplate::structures)
    ).apply(i, RoomTemplate::new));

    public RoomTemplate(int cubicSizeInternal, int color) {
        this(RoomDimensions.cubic(cubicSizeInternal), color, Collections.emptyList());
    }

    public AABB getZeroBoundaries() {
        return AABB.ofSize(Vec3.ZERO, internalDimensions.width(), internalDimensions.height(), internalDimensions.depth())
                .inflate(1)
                .move(0, internalDimensions.height() / 2f, 0);
    }

    public AABB getBoundariesCenteredAt(Vec3 center) {
        return AABB.ofSize(center, internalDimensions.width(), internalDimensions.height(), internalDimensions.depth())
                .inflate(1);
    }
}
