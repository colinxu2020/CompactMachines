package dev.compactmods.machines.api.room.upgrade;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.api.room.RoomInstance;

public interface DataProvidingUpgrade<T> {

   Codec<T> dataCodec();

   T initialize(RoomInstance roomInstance);

   boolean canRemove(RoomInstance roomInstance, T data);
}
