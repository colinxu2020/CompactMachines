package dev.compactmods.machines.api.dimension;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

import static dev.compactmods.machines.api.Constants.MOD_ID;

public abstract class CompactDimension {
    public static final ResourceKey<Level> LEVEL_KEY = ResourceKey
            .create(Registries.DIMENSION, new ResourceLocation(MOD_ID, "compact_world"));

    public static final ResourceKey<DimensionType> DIM_TYPE_KEY = ResourceKey
            .create(Registries.DIMENSION_TYPE, new ResourceLocation(MOD_ID, "compact_world"));

    private CompactDimension() {}

    @NotNull
    public static ServerLevel forServer(MinecraftServer server) throws MissingDimensionException {
        final var level = server.getLevel(LEVEL_KEY);
        if(level == null)
            throw new MissingDimensionException();

        return level;
    }

    public static Path getDataDirectory(@NotNull MinecraftServer server) {
        return DimensionType.getStorageFolder(CompactDimension.LEVEL_KEY, server.getWorldPath(LevelResource.ROOT));
    }

    public static Path getDataDirectory(@NotNull LevelStorageSource.LevelDirectory levelDir) {
        return DimensionType.getStorageFolder(CompactDimension.LEVEL_KEY, levelDir.path());
    }

    @NotNull
    public static DimensionDataStorage getDataStorage(@NotNull LevelStorageSource.LevelDirectory levelDir, HolderLookup.Provider holderLookup) {
        final var dimPath = DimensionType.getStorageFolder(CompactDimension.LEVEL_KEY, levelDir.path());
        final var fixer = DataFixers.getDataFixer();
        return new DimensionDataStorage(dimPath.resolve("data").toFile(), fixer, holderLookup);
    }

    public static boolean isLevelCompact(Level level) {
        return level.dimension().equals(LEVEL_KEY);
    }
}
