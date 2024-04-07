package dev.compactmods.machines.neoforge.network.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.Constants;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPlayPayloadHandler;

public record UnboundMachineTemplateSyncPacket(GlobalPos position, ResourceLocation template) implements CustomPacketPayload {
    public static final Codec<UnboundMachineTemplateSyncPacket> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            GlobalPos.CODEC.fieldOf("position").forGetter(UnboundMachineTemplateSyncPacket::position),
            ResourceLocation.CODEC.fieldOf("template").forGetter(UnboundMachineTemplateSyncPacket::template)
    ).apply(inst, UnboundMachineTemplateSyncPacket::new));

    public static final ResourceLocation ID = Constants.modRL("unbound_machine_template");
    public static final IPlayPayloadHandler<UnboundMachineTemplateSyncPacket> HANDLER = (pkt, ctx) -> {
        ClientMachinePacketHandler.setMachineTemplate(pkt.position, pkt.template);
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
