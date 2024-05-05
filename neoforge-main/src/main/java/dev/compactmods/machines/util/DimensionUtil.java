package dev.compactmods.machines.util;

import com.mojang.serialization.JsonOps;
import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.LoggingUtil;
import net.minecraft.SharedConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DimensionUtil {

    private static final Logger LOG = LoggingUtil.modLog();

    public static boolean doLevelFileBackup(MinecraftServer server) {
        var levelRoot = server.getWorldPath(LevelResource.ROOT);
        var levelFile = server.getWorldPath(LevelResource.LEVEL_DATA_FILE);

        var formatter = DateTimeFormatter.ofPattern("'cm4-dimension-'yyyyMMdd-HHmmss'.dat'");
        var timestamp = formatter.format(ZonedDateTime.now());
        try {
            Files.copy(levelFile, levelRoot.resolve(timestamp));
        } catch (IOException e) {
            LOG.error("Failed to backup dimension.dat file before modification; canceling register dim attempt.");
            return false;
        }

        return true;
    }

    @NotNull
    public static Path getDataFolder(@NotNull Path rootDir, ResourceKey<Level> key) {
        final var dimPath = DimensionType.getStorageFolder(key, rootDir);
        return dimPath.resolve("data");
    }

    @NotNull
    public static Path getDataFolder(@NotNull LevelStorageSource.LevelDirectory levelDir, ResourceKey<Level> key) {
        final var dimPath = DimensionType.getStorageFolder(key, levelDir.path());
        return dimPath.resolve("data");
    }

    @NotNull
    public static DimensionDataStorage getDataStorage(@NotNull LevelStorageSource.LevelDirectory levelDir, ResourceKey<Level> key, HolderLookup.Provider provider) {
        final var folder = getDataFolder(levelDir, key).toFile();
        final var fixer = DataFixers.getDataFixer();
        return new DimensionDataStorage(folder, fixer, provider);
    }
}
