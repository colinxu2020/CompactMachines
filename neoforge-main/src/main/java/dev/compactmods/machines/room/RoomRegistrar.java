package dev.compactmods.machines.room;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.api.room.IRoomRegistrar;
import dev.compactmods.machines.api.room.RoomApi;
import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.api.room.RoomTemplate;
import dev.compactmods.machines.api.room.registration.IRoomBuilder;
import dev.compactmods.feather.MemoryGraph;
import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.api.dimension.MissingDimensionException;
import dev.compactmods.machines.api.util.AABBAligner;
import dev.compactmods.machines.data.CodecBackedSavedData;
import dev.compactmods.machines.room.graph.node.RoomRegistrationNode;
import dev.compactmods.machines.util.MathUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class RoomRegistrar extends CodecBackedSavedData<RoomRegistrar> implements IRoomRegistrar {

    public static final Logger LOGS = LogManager.getLogger();

    public static final String DATA_NAME = Constants.MOD_ID + "_rooms";

    public static final Codec<RoomRegistrar> CODEC = RoomRegistrationNode.CODEC.listOf()
            .fieldOf("rooms")
            .xmap(RoomRegistrar::new, (RoomRegistrar x) -> List.copyOf(x.registrationNodes.values()))
            .codec();
    private final MemoryGraph graph;
    private final Map<String, RoomRegistrationNode> registrationNodes;

    private RoomRegistrar() {
        super(CODEC, RoomRegistrar::new);
        this.graph = new MemoryGraph();
        this.registrationNodes = new HashMap<>();
    }

    private RoomRegistrar(List<RoomRegistrationNode> regNodes) {
        super(CODEC, RoomRegistrar::new);
        this.graph = new MemoryGraph();
        this.registrationNodes = new HashMap<>();
        regNodes.forEach(this::registerDirty);
    }

    public static RoomRegistrar forServer(MinecraftServer server) throws MissingDimensionException {
        return CompactDimension.forServer(server)
                .getDataStorage()
                .computeIfAbsent(new CodecWrappedSavedData<>(CODEC, RoomRegistrar::new).sd(), DATA_NAME);
    }

    @Override
    public IRoomBuilder builder() {
        return new NewRoomBuilder();
    }

    @Override
    public AABB getNextBoundaries(RoomTemplate template) {
        final var region = MathUtil.getRegionPositionByIndex(registrationNodes.size());
        final var floor = MathUtil.getCenterWithY(region, 0);

        return AABBAligner.floor(template.getZeroBoundaries().move(floor), 0);
    }

    @Override
    public RoomInstance createNew(RoomTemplate template, UUID owner, Consumer<IRoomBuilder> override) {
        final var inst = IRoomRegistrar.super.createNew(template, owner, override);

        var node = new RoomRegistrationNode(UUID.randomUUID(), new RoomRegistrationNode.Data(inst));
        this.registrationNodes.put(inst.code(), node);
        this.graph.addNode(node);

        RoomApi.chunkManager().calculateChunks(inst.code(), node);
        setDirty();

        return inst;
    }

    @Override
    public boolean isRegistered(String room) {
        return registrationNodes.containsKey(room);
    }

    @Override
    public Optional<RoomInstance> get(String room) {
        final var regNode = registrationNodes.get(room);
        if (regNode == null)
            return Optional.empty();

        RoomInstance inst = makeRoomInstance(regNode);
        return Optional.of(inst);
    }

    @NotNull
    private static RoomInstance makeRoomInstance(RoomRegistrationNode regNode) {
        return new RoomInstance(regNode.code(), regNode.defaultMachineColor(), regNode);
    }

    @Override
    public long count() {
        return registrationNodes.size();
    }

    @Override
    public Stream<String> allRoomCodes() {
        return registrationNodes.keySet().stream();
    }

    @Override
    public Stream<RoomInstance> allRooms() {
        return registrationNodes.values()
                .stream()
                .map(RoomRegistrar::makeRoomInstance);
    }

    private void registerDirty(RoomRegistrationNode node) {
        registrationNodes.putIfAbsent(node.code(), node);
        graph.addNode(node);
    }
}
