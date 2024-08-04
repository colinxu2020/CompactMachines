package dev.compactmods.machines.api;

import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.api.dimension.MissingDimensionException;
import dev.compactmods.machines.api.room.CompactRoomGenerator;
import dev.compactmods.machines.api.room.IRoomApi;
import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.api.room.template.RoomTemplate;
import dev.compactmods.machines.api.room.data.IRoomDataAttachmentAccessor;
import dev.compactmods.machines.api.room.history.IPlayerHistoryApi;
import dev.compactmods.machines.api.room.upgrade.RoomUpgradeDefinition;
import dev.compactmods.machines.api.util.BlockSpaceUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.UUID;

public class CompactMachines {
	public final static String MOD_ID = "compactmachines";

	public static String id(String path) {
		return ResourceLocation.isValidPath(path) ? (MOD_ID + ":" + path) : MOD_ID + ":invalid";
	}

	public static ResourceLocation modRL(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static DeferredRegister<RoomUpgradeDefinition<?>> roomUpgradeDR(String namespace) {
		return DeferredRegister.create(RoomUpgradeDefinition.REG_KEY, namespace);
	}

	public static IRoomApi roomApi() {
		return Internal.ROOM_API;
	}

	public static IPlayerHistoryApi playerHistoryApi() {
		return Internal.PLAYER_HISTORY_API;
	}

	public static Optional<RoomInstance> room(String roomCode) {
		return Internal.ROOM_API.registrar().get(roomCode);
	}

	/**
	 * Registers a new room instance and generates the structure in the compact world.
	 *
	 * @param server   Server to generate room on.
	 * @param template
	 * @param owner
	 * @return
	 */
	public static RoomInstance newRoom(MinecraftServer server, RoomTemplate template, UUID owner) throws MissingDimensionException {
		final var instance = CompactMachines.roomApi().registrar().createNew(template, owner);
		final var compactDim = CompactDimension.forServer(server);
		CompactRoomGenerator.generateRoom(compactDim, instance.boundaries().outerBounds());

		if (!template.structures().isEmpty()) {
			for (var struct : template.structures()) {
				CompactRoomGenerator.populateStructure(compactDim, struct.template(), instance.boundaries().innerBounds(), struct.placement());
			}
		}

		final var spawnManager = CompactMachines.roomApi().spawnManager(instance.code());
		template.optionalFloor().ifPresent(floorState -> {
			var fixedSpawn = instance.boundaries()
				.defaultSpawn()
				.add(0, 1, 0);

			spawnManager.setDefaultSpawn(fixedSpawn, Vec2.ZERO);

			AABB floorBounds = BlockSpaceUtil.getWallBounds(instance.boundaries().innerBounds(), Direction.DOWN);
			BlockSpaceUtil.blocksInside(floorBounds).forEach(floorBlockPos -> {
				compactDim.setBlock(floorBlockPos, floorState, Block.UPDATE_ALL);
			});
		});

		return instance;
	}

	public static boolean isValidRoomCode(String roomCode) {
		return Internal.ROOM_API.roomCodeValidator().test(roomCode);
	}

	public static Optional<? extends IAttachmentHolder> existingRoomData(String code) {
		return Internal.ROOM_DATA_ACCESSOR.get(code);
	}

	public static IAttachmentHolder roomData(String code) {
		return Internal.ROOM_DATA_ACCESSOR.getOrCreate(code);
	}


	/**
	 * Set up when the server or single-player instance changes.
	 * NOT for API consumers to use! Use the methods provided here for safety.
	 *
	 * @since 6.0.0
	 */
	@ApiStatus.Internal
	@Deprecated
	public final class Internal {
		public static IRoomApi ROOM_API;
		public static IRoomDataAttachmentAccessor ROOM_DATA_ACCESSOR;
		public static IPlayerHistoryApi PLAYER_HISTORY_API;
	}
}
