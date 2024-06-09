package dev.compactmods.machines.data.room;

import dev.compactmods.machines.api.room.data.IRoomDataAttachmentAccessor;
import dev.compactmods.machines.data.CMDataAttachmentFileManager;
import net.minecraft.server.MinecraftServer;

public class RoomAttachmentDataManager extends CMDataAttachmentFileManager<String, RoomDataAttachments> implements IRoomDataAttachmentAccessor {

    public RoomAttachmentDataManager(MinecraftServer server) {
        super(server, (serv, key) -> new RoomDataAttachments(key));
    }

    public RoomDataAttachments roomData(String roomCode) {
        return data(roomCode);
    }
}
