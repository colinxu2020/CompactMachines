package dev.compactmods.machines.network.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.CompactMachinesApi;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record MachineColorSyncPacket(GlobalPos position, int color) implements CustomPacketPayload {
    public static final Type<MachineColorSyncPacket> TYPE = new Type<>(CompactMachinesApi.modRL("update_machine_color"));

    public static final Codec<MachineColorSyncPacket> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            GlobalPos.CODEC.fieldOf("position").forGetter(MachineColorSyncPacket::position),
            Codec.INT.fieldOf("color").forGetter(MachineColorSyncPacket::color)
    ).apply(inst, MachineColorSyncPacket::new));

    public static final StreamCodec<FriendlyByteBuf, MachineColorSyncPacket> STREAM_CODEC = StreamCodec.composite(
            GlobalPos.STREAM_CODEC, MachineColorSyncPacket::position,
            ByteBufCodecs.INT, MachineColorSyncPacket::color,
            MachineColorSyncPacket::new
    );

    public static final IPayloadHandler<MachineColorSyncPacket> HANDLER = (pkt, ctx) -> {
        ClientMachinePacketHandler.setMachineColor(pkt.position, pkt.color);
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
