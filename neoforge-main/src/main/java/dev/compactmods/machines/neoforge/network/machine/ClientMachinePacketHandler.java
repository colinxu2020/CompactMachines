package dev.compactmods.machines.neoforge.network.machine;

import dev.compactmods.machines.api.room.RoomTemplate;
import dev.compactmods.machines.neoforge.machine.Machines;
import net.minecraft.client.Minecraft;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceLocation;

public class ClientMachinePacketHandler {
    public static void setMachineTemplate(GlobalPos position, ResourceLocation template) {
        var mc = Minecraft.getInstance();
        assert mc.level != null;
        if(mc.level.dimension() == position.dimension()) {
            mc.level.getBlockEntity(position.pos(), Machines.UNBOUND_MACHINE_ENTITY.get())
                    .ifPresent(ent -> ent.setTemplate(template));
        }
    }
}
