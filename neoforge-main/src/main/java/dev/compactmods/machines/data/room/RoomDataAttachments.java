package dev.compactmods.machines.data.room;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.room.data.CMRoomDataLocations;
import dev.compactmods.machines.data.CMDataFile;
import dev.compactmods.machines.data.CodecHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.attachment.AttachmentHolder;

import java.nio.file.Path;

public class RoomDataAttachments extends AttachmentHolder implements CMDataFile, CodecHolder<RoomDataAttachments> {

    private final String roomCode;
    private final Codec<RoomDataAttachments> codec;

    public RoomDataAttachments(MinecraftServer server, String roomCode) {
        this.roomCode = roomCode;
        this.codec = makeCodec(server);
    }

    public static Codec<RoomDataAttachments> makeCodec(MinecraftServer server) {
        return RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("room_code").forGetter(RoomDataAttachments::roomCode),
            CompoundTag.CODEC.fieldOf("attachments").forGetter(inst -> inst.serializeAttachments(server.registryAccess()))
        ).apply(i, (roomCode, ct) -> {
            var inst = new RoomDataAttachments(server, roomCode);
            inst.deserializeAttachments(server.registryAccess(), ct);
            return inst;
        }));
    }

    @Override
    public Path getDataLocation(MinecraftServer server) {
        return CMRoomDataLocations.ROOM_DATA_ATTACHMENTS.apply(server);
    }

    public String roomCode() {
        return roomCode;
    }

    @Override
    public Codec<RoomDataAttachments> codec() {
        return codec;
    }
}
