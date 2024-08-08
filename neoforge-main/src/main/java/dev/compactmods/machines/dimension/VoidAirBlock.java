package dev.compactmods.machines.dimension;

import dev.compactmods.machines.api.dimension.CompactDimension;
import dev.compactmods.machines.room.Rooms;
import dev.compactmods.machines.server.ServerConfig;
import dev.compactmods.machines.util.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;


public class VoidAirBlock extends AirBlock {
    // FIXME final public static DamageSource DAMAGE_SOURCE = new DamageSource(MOD_ID + "_voidair");

    public VoidAirBlock() {
        super(BlockBehaviour.Properties.of()
                .isValidSpawn((state, level, pos, entity) -> false)
                .strength(-1.0F, 3600000.0F)
                .noTerrainParticles()
                .noLootTable()
                .forceSolidOn());
    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return false;
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        return false;
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pLevel.isClientSide) return;
        if (!CompactDimension.isLevelCompact(pLevel)) return;

        if (pEntity instanceof ServerPlayer player) {
            // If players are allowed outside of machine bounds, early exit -- but damage them if configured
            if (ServerConfig.isAllowedOutsideOfMachine()) {
                tryDamagingAdventurousPlayer(pLevel, player);
                return;
            }

            tryDamagingAdventurousPlayer(pLevel, player);

            // FIXME - Achievement
            // PlayerUtil.howDidYouGetThere(player);
            PlayerUtil.teleportPlayerToRespawnOrOverworld(player.server, player);
        }
    }

    /**
     * Attempts to inflict the bad effects on players that are touching a void air block.
     * Does nothing to players that are in creative mode.
     *
     * @param pLevel
     * @param player
     */
    private static void tryDamagingAdventurousPlayer(Level pLevel, ServerPlayer player) {
        if (player.isCreative()) return;

        if (ServerConfig.damagePlayersOutOfBounds()) {
            if(!player.hasEffect(MobEffects.CONFUSION)) {
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 20));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20));
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 5 * 20));
            }

            if (player.getHealth() > 1) {
                player.hurt(pLevel.damageSources().fellOutOfWorld(), player.getHealth() - 1);
            }
        }
    }
}
