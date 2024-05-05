package dev.compactmods.machines.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec2;

import java.util.Optional;
import java.util.UUID;

public abstract class PlayerUtil {

    public static Optional<GameProfile> getProfileByUUID(MinecraftServer server, UUID uuid) {
        final var player = server.getPlayerList().getPlayer(uuid);
        if (player == null) {
            var p2 = server.getSessionService().fetchProfile(uuid, false);
            return p2 == null ? Optional.empty() : Optional.ofNullable(p2.profile());
        }

        GameProfile profile = player.getGameProfile();
        return Optional.of(profile);
    }

    public static Optional<GameProfile> getProfileByUUID(LevelAccessor world, UUID uuid) {
        final var player = world.getPlayerByUUID(uuid);
        if (player == null)
            return Optional.empty();

        GameProfile profile = player.getGameProfile();
        return Optional.of(profile);
    }

    public static Vec2 getLookDirection(Player player) {
        return new Vec2(player.xRotO, player.yRotO);
    }
}
