package dev.compactmods.machines.api.room;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record RoomStructureInfo(ResourceLocation template, RoomStructurePlacement placement) {
    public static final Codec<RoomStructureInfo> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("template").forGetter(RoomStructureInfo::template),
            RoomStructurePlacement.CODEC.fieldOf("placement").forGetter(RoomStructureInfo::placement)
    ).apply(inst, RoomStructureInfo::new));

    public static final StreamCodec<FriendlyByteBuf, RoomStructureInfo> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, RoomStructureInfo::template,
            NeoForgeStreamCodecs.enumCodec(RoomStructurePlacement.class), RoomStructureInfo::placement,
            RoomStructureInfo::new
    );

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
