package dev.compactmods.machines.api.room;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record RoomDimensions(int width, int depth, int height) {
    private static final Codec<RoomDimensions> FULL_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.intRange(3, 45).fieldOf("width").forGetter(RoomDimensions::width),
            Codec.intRange(3, 45).fieldOf("depth").forGetter(RoomDimensions::depth),
            Codec.intRange(3, 45).fieldOf("height").forGetter(RoomDimensions::height)
    ).apply(inst, RoomDimensions::new));

    public static final Codec<RoomDimensions> CODEC = Codec.of(FULL_CODEC, Decoder.INSTANCE);

    public RoomDimensions(int cubicSize) {
        this(Math.min(cubicSize, 45), Math.min(cubicSize, 45), Math.min(cubicSize, 45));
    }

    public static RoomDimensions cubic(int cubic) {
        return new RoomDimensions(cubic);
    }

    @Override
    public String toString() {
        return "%s x %s x %s".formatted(width, depth, height);
    }

    private static class Decoder implements com.mojang.serialization.Decoder<RoomDimensions> {

        public static final Decoder INSTANCE = new Decoder();

        @Override
        public <T> DataResult<Pair<RoomDimensions, T>> decode(DynamicOps<T> dynamicOps, T t) {
            final var asNum = dynamicOps.withParser(Codec.intRange(3, 45))
                    .apply(t)
                    .get();

            if (asNum.left().isPresent())
                return DataResult.success(Pair.of(RoomDimensions.cubic(asNum.left().get()), t));

            final var asObj = dynamicOps.withParser(FULL_CODEC)
                    .apply(t)
                    .get();

            if (asObj.left().isPresent())
                return DataResult.success(Pair.of(asObj.left().get(), t));

            return DataResult.error(() -> "Dimensions must either be a single integer between 3-45, or specify width/depth/height.");
        }
    }
}
