package dev.compactmods.machines.neoforge.network;

import dev.compactmods.machines.api.room.RoomApi;
import dev.compactmods.machines.neoforge.CompactMachines;
import dev.compactmods.machines.neoforge.room.ui.upgrades.RoomUpgradeMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record PlayerRequestedUpgradeMenuPacket(String roomCode) implements CustomPacketPayload {

    public static final Type<PlayerRequestedUpgradeMenuPacket> TYPE = new Type<>(CompactMachines.rl("player_wants_to_open_room_upgrade_menu"));

    public static final IPayloadHandler<PlayerRequestedUpgradeMenuPacket> HANDLER = (pkt, ctx) -> {
        final var player = ctx.player();
        RoomApi.room(pkt.roomCode()).ifPresent(inst -> {
            player.openMenu(RoomUpgradeMenu.provider(inst), buf -> {
                buf.writeUtf(pkt.roomCode());
            });
        });
    };

    public static final StreamCodec<FriendlyByteBuf, PlayerRequestedUpgradeMenuPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, PlayerRequestedUpgradeMenuPacket::roomCode,
            PlayerRequestedUpgradeMenuPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
