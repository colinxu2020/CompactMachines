package dev.compactmods.machines.neoforge.network;

import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.neoforge.room.RoomHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record PlayerRequestedLeavePacket() implements CustomPacketPayload {

    public static final Type<PlayerRequestedLeavePacket> TYPE = new Type<>(new ResourceLocation(Constants.MOD_ID, "player_requested_to_leave_room"));

    public static final IPayloadHandler<PlayerRequestedLeavePacket> HANDLER = (pkt, ctx) -> {
        final var player = ctx.player();
        if(player instanceof ServerPlayer sp) {
            RoomHelper.teleportPlayerOutOfRoom(sp);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
