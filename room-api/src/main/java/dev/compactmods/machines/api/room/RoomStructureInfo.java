package dev.compactmods.machines.api.room;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public record RoomStructureInfo(ResourceLocation template, RoomStructurePlacement placement) {
    public static final Codec<RoomStructureInfo> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("template").forGetter(RoomStructureInfo::template),
            RoomStructurePlacement.CODEC.fieldOf("placement").forGetter(RoomStructureInfo::placement)
    ).apply(inst, RoomStructureInfo::new));

    public enum RoomStructurePlacement implements StringRepresentable {
        CENTERED_CEILING("centered_ceiling"),
        CENTERED("centered"),
        CENTERED_FLOOR("centered_floor");

        public static final StringRepresentable.StringRepresentableCodec<RoomStructurePlacement> CODEC = StringRepresentable.fromEnum(RoomStructurePlacement::values);

        private final String name;

        RoomStructurePlacement(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
