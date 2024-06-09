package dev.compactmods.machines.data.room;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.api.CompactMachinesApi;
import dev.compactmods.machines.data.CMDataFile;
import dev.compactmods.machines.data.CodecHolder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.neoforge.attachment.AttachmentHolder;

import java.nio.file.Path;

public class RoomDataAttachments extends AttachmentHolder implements CMDataFile, CodecHolder<RoomDataAttachments> {

    private final String roomCode;

    RoomDataAttachments(String roomCode) {
        this.roomCode = roomCode;
    }

    @Override
    public Path getDataLocation(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT)
            .resolve(CompactMachinesApi.MOD_ID)
            .resolve("data")
            .resolve("room_data");
    }

    public String roomCode() {
        return roomCode;
    }

    @Override
    public Codec<RoomDataAttachments> codec() {
        return null;
    }
}
