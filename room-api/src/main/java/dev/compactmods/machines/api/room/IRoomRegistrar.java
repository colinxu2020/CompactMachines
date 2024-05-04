package dev.compactmods.machines.api.room;

import dev.compactmods.machines.api.room.registration.IRoomBuilder;
import net.minecraft.world.phys.AABB;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface IRoomRegistrar {

    AABB getNextBoundaries(RoomTemplate template);

    IRoomBuilder builder();

    default RoomInstance createNew(RoomTemplate template, UUID owner) {
        return createNew(template, owner, override -> {});
    }

    default RoomInstance createNew(RoomTemplate template, UUID owner, Consumer<IRoomBuilder> override) {
        final Consumer<IRoomBuilder> preOverride = builder -> builder.defaultMachineColor(template.color())
                .owner(owner)
                .boundaries(getNextBoundaries(template));

        // Make builder, set template defaults, then allow overrides
        final var b = builder();
        preOverride.andThen(override).accept(b);
        return b.build();
    }

    boolean isRegistered(String room);

    Optional<RoomInstance> get(String room);

    long count();

    Stream<String> allRoomCodes();

    Stream<RoomInstance> allRooms();
}
