package dev.compactmods.machines.neoforge.network;

import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.neoforge.network.machine.MachineColorSyncPacket;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CMNetworks {

    @SubscribeEvent
    public static void onPacketRegistration(final RegisterPayloadHandlersEvent payloads) {
        final PayloadRegistrar main = payloads.registrar("6.0.0");

        main.playToServer(PlayerRequestedTeleportPacket.TYPE, PlayerRequestedTeleportPacket.STREAM_CODEC, PlayerRequestedTeleportPacket.HANDLER);

        main.playToClient(SyncRoomMetadataPacket.TYPE, SyncRoomMetadataPacket.STREAM_CODEC, SyncRoomMetadataPacket.HANDLER);

        main.playToServer(PlayerRequestedLeavePacket.TYPE, StreamCodec.unit(new PlayerRequestedLeavePacket()), PlayerRequestedLeavePacket.HANDLER);

        main.playToServer(PlayerRequestedRoomUIPacket.TYPE, PlayerRequestedRoomUIPacket.STREAM_CODEC, PlayerRequestedRoomUIPacket.HANDLER);

        main.playToServer(PlayerRequestedUpgradeMenuPacket.TYPE, PlayerRequestedUpgradeMenuPacket.STREAM_CODEC, PlayerRequestedUpgradeMenuPacket.HANDLER);

        main.playToClient(MachineColorSyncPacket.TYPE, MachineColorSyncPacket.STREAM_CODEC, MachineColorSyncPacket.HANDLER);
    }
}
