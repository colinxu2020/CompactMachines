package dev.compactmods.machines.room.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Optional;

public class RoomAttachmentDataManager {

    private static RoomAttachmentDataManager INSTANCE;
    private final MinecraftServer server;
    private final HashMap<String, RoomAttachmentData> cache;

    private RoomAttachmentDataManager(MinecraftServer server) {
        this.server = server;
        this.cache = new HashMap<>();
    }

    public static Optional<RoomAttachmentDataManager> instance() {
        return Optional.ofNullable(INSTANCE);
    }

    public static RoomAttachmentDataManager instance(MinecraftServer server) {
        if(INSTANCE == null) {
            INSTANCE = new RoomAttachmentDataManager(server);
            return INSTANCE;
        }

        // Compare instance servers. If they aren't a match, save the old instance and set up fresh
        if(INSTANCE.server != server) {
            INSTANCE.save(server.registryAccess());
            INSTANCE = new RoomAttachmentDataManager(server);
            return INSTANCE;
        } else {
            return INSTANCE;
        }
    }

    public RoomAttachmentData data(String roomCode) {
        return cache.computeIfAbsent(roomCode, k -> RoomAttachmentData.createForRoom(server, roomCode));
    }

    public void save(HolderLookup.Provider provider) {
        cache.forEach((key, data) -> data.save(provider));
    }

    public boolean hasData(String code) {
        return cache.containsKey(code);
    }
}
