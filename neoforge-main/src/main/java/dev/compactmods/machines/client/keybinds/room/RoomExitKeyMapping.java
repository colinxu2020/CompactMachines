package dev.compactmods.machines.client.keybinds.room;

import com.mojang.blaze3d.platform.InputConstants;
import dev.compactmods.machines.api.CompactMachinesApi;
import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.network.PlayerRequestedLeavePacket;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.network.PacketDistributor;

public class RoomExitKeyMapping {

   public interface I18n {
	  String CATEGORY = Util.makeDescriptionId("key.category", CompactMachinesApi.modRL("general"));
	  String NAME = Util.makeDescriptionId("key.mapping", CompactMachinesApi.modRL("exit_room"));
   }

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

   public static final KeyMapping MAPPING = new KeyMapping(I18n.NAME, CONFLICT_CONTEXT, InputConstants.UNKNOWN, I18n.CATEGORY);

   public static void handle() {
	  final var level = Minecraft.getInstance().level;
	  if (level != null && level.dimension().equals(CompactDimension.LEVEL_KEY))
		 PacketDistributor.sendToServer(new PlayerRequestedLeavePacket());
   }
}
