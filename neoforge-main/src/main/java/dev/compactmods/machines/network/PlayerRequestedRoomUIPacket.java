package dev.compactmods.machines.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.api.room.RoomApi;
import dev.compactmods.machines.CompactMachines;
import dev.compactmods.machines.room.Rooms;
import dev.compactmods.machines.room.ui.preview.MachineRoomMenu;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.Optional;

public record PlayerRequestedRoomUIPacket(String roomCode) implements CustomPacketPayload {
    public static final Type<PlayerRequestedRoomUIPacket> TYPE = new Type<>(Constants.modRL("player_wants_to_open_room_ui"));

    public static final Codec<PlayerRequestedRoomUIPacket> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("roomCode").forGetter(PlayerRequestedRoomUIPacket::roomCode)
    ).apply(inst, PlayerRequestedRoomUIPacket::new));

    public static final StreamCodec<FriendlyByteBuf, PlayerRequestedRoomUIPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, PlayerRequestedRoomUIPacket::roomCode,
            PlayerRequestedRoomUIPacket::new
    );

    public static final IPayloadHandler<PlayerRequestedRoomUIPacket> HANDLER = (pkt, ctx) -> {
        final var player = ctx.player();
        RoomApi.room(pkt.roomCode).ifPresent(inst -> {
            final var server = player.getServer();
            final var pos = player.getData(Rooms.DataAttachments.OPEN_MACHINE_POS);
            player.openMenu(MachineRoomMenu.provider(server, inst), buf -> {
                buf.writeJsonWithCodec(GlobalPos.CODEC, pos);
                buf.writeUtf(pkt.roomCode);
                buf.writeOptional(Optional.<String>empty(), FriendlyByteBuf::writeUtf);
            });
        });
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
