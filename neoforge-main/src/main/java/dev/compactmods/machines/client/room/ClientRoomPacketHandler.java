package dev.compactmods.machines.client.room;

import dev.compactmods.machines.room.Rooms;
import dev.compactmods.machines.room.ui.preview.MachineRoomScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.UUID;

public class ClientRoomPacketHandler {
    public static void handleBlockData(StructureTemplate blocks) {
        final var mc = Minecraft.getInstance();
        if(mc.screen instanceof MachineRoomScreen mrs) {
            mrs.getMenu().setBlocks(blocks);
            mrs.updateBlockRender();
        }
    }

    public static void handleRoomSync(String roomCode, UUID owner) {
        final var mc = Minecraft.getInstance();

        // FIXME - Current Room Owner
        mc.player.setData(Rooms.DataAttachments.CURRENT_ROOM_CODE, roomCode);
        // mc.player.setData(Rooms.DataAttachments)
    }
}
