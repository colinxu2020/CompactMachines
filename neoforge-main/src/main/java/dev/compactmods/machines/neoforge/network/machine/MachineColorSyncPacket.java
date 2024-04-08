package dev.compactmods.machines.neoforge.network.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.Constants;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPlayPayloadHandler;

public record MachineColorSyncPacket(GlobalPos position, int color) implements CustomPacketPayload {
    public static final Codec<MachineColorSyncPacket> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            GlobalPos.CODEC.fieldOf("position").forGetter(MachineColorSyncPacket::position),
            Codec.INT.fieldOf("color").forGetter(MachineColorSyncPacket::color)
    ).apply(inst, MachineColorSyncPacket::new));

    public static final ResourceLocation ID = Constants.modRL("update_machine_color");
    public static final IPlayPayloadHandler<MachineColorSyncPacket> HANDLER = (pkt, ctx) -> {
        ClientMachinePacketHandler.setMachineColor(pkt.position, pkt.color);
    };

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeJsonWithCodec(CODEC, this);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
