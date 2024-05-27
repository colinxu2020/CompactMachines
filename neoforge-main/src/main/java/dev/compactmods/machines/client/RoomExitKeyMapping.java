package dev.compactmods.machines.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.network.PlayerRequestedLeavePacket;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.network.PacketDistributor;

public class RoomExitKeyMapping {

  public static final String CATEGORY = Util.makeDescriptionId("key.category", Constants.modRL("general"));
  public static final String NAME = Util.makeDescriptionId("key.mapping", Constants.modRL("exit_room"));

  public static final IKeyConflictContext CONFLICT_CONTEXT = new IKeyConflictContext() {
	 @Override
	 public boolean isActive() {
		final var level = Minecraft.getInstance().level;
		return level != null && level.dimension().equals(CompactDimension.LEVEL_KEY);
	 }

	 @Override
	 public boolean conflicts(IKeyConflictContext other) {
		return this == other;
	 }
  };

  public static final KeyMapping MAPPING = new KeyMapping(NAME, CONFLICT_CONTEXT, InputConstants.UNKNOWN, CATEGORY);

  public static void handle() {
	 final var level = Minecraft.getInstance().level;
	 if (level != null && level.dimension().equals(CompactDimension.LEVEL_KEY))
		PacketDistributor.sendToServer(new PlayerRequestedLeavePacket());
  }
}
