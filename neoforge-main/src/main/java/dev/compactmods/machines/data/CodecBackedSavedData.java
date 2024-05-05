package dev.compactmods.machines.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class CodecBackedSavedData<D extends SavedData> extends SavedData {

    private static final Logger LOGS = LogManager.getLogger();

    protected final CodecWrappedSavedData<CodecBackedSavedData<D>, D> factory;

    public CodecBackedSavedData(Codec<D> codec, Supplier<D> factory) {
        this.factory = new CodecWrappedSavedData<>(codec, factory);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider pRegistries) {
        final var data = factory.codec
                .encodeStart(NbtOps.INSTANCE, (D) this)
                .getOrThrow();

        if (data instanceof CompoundTag dataTag) {
            compoundTag.merge(dataTag);
        }

        return compoundTag;
    }

    public record CodecWrappedSavedData<T extends CodecBackedSavedData<D>, D extends SavedData>(Codec<D> codec, Supplier<D> factory) {

        public SavedData.Factory<D> sd() {
            return new SavedData.Factory<>(factory, this::load, null);
        }

        public D load(CompoundTag tag, HolderLookup.Provider registries) {
            return codec.parse(NbtOps.INSTANCE, tag).getOrThrow();
        }
    }
}
