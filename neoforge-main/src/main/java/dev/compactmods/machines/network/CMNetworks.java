package dev.compactmods.machines.network;

import dev.compactmods.machines.network.machine.MachineColorSyncPacket;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class CMNetworks {

    public static void onPacketRegistration(final RegisterPayloadHandlersEvent payloads) {
        final PayloadRegistrar main = payloads.registrar("6.0.0");

        main.playToServer(PlayerRequestedTeleportPacket.TYPE, PlayerRequestedTeleportPacket.STREAM_CODEC, PlayerRequestedTeleportPacket.HANDLER);

        main.playToClient(SyncRoomMetadataPacket.TYPE, SyncRoomMetadataPacket.STREAM_CODEC, SyncRoomMetadataPacket.HANDLER);

        main.playToServer(PlayerRequestedLeavePacket.TYPE, StreamCodec.unit(new PlayerRequestedLeavePacket()), PlayerRequestedLeavePacket.HANDLER);

        main.playToServer(PlayerRequestedRoomUIPacket.TYPE, PlayerRequestedRoomUIPacket.STREAM_CODEC, PlayerRequestedRoomUIPacket.HANDLER);

        main.playToServer(PlayerRequestedUpgradeUIPacket.TYPE, PlayerRequestedUpgradeUIPacket.STREAM_CODEC, PlayerRequestedUpgradeUIPacket.HANDLER);

        main.playToClient(MachineColorSyncPacket.TYPE, MachineColorSyncPacket.STREAM_CODEC, MachineColorSyncPacket.HANDLER);
    }
}
