package dev.compactmods.machines.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.compactmods.machines.api.CompactMachines;
import dev.compactmods.machines.api.room.upgrade.components.RoomUpgradeList;
import dev.compactmods.machines.command.subcommand.CMEjectSubcommand;
import dev.compactmods.machines.command.subcommand.CMGiveMachineSubcommand;
import dev.compactmods.machines.command.subcommand.CMRebindSubcommand;
import dev.compactmods.machines.command.subcommand.CMRoomsSubcommand;
import dev.compactmods.machines.command.subcommand.CMTeleportSubcommand;
import dev.compactmods.machines.command.subcommand.CMUnbindSubcommand;
import dev.compactmods.machines.command.subcommand.SpawnSubcommand;
import dev.compactmods.machines.room.upgrade.RoomUpgrades;
import dev.compactmods.machines.room.upgrade.example.TreeCutterUpgrade;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.List;

public class Commands {

    // TODO: /cm create <size:RoomSize> <owner:Player> <giveMachine:true|false>
    // TODO: /cm spawn set <room> <pos>

    static final LiteralArgumentBuilder<CommandSourceStack> CM_COMMAND_ROOT
            = LiteralArgumentBuilder.literal(CompactMachines.MOD_ID);

    public static void prepare() {

    }

    public static LiteralArgumentBuilder<CommandSourceStack> getRoot() {
        return CM_COMMAND_ROOT;
    }

    public static void onCommandsRegister(final RegisterCommandsEvent event) {
        Commands.CM_COMMAND_ROOT.then(CMTeleportSubcommand.make());
        Commands.CM_COMMAND_ROOT.then(CMEjectSubcommand.make());
        Commands.CM_COMMAND_ROOT.then(CMRebindSubcommand.make());
        Commands.CM_COMMAND_ROOT.then(CMUnbindSubcommand.make());
        Commands.CM_COMMAND_ROOT.then(CMRoomsSubcommand.make());
        Commands.CM_COMMAND_ROOT.then(CMGiveMachineSubcommand.make());
        Commands.CM_COMMAND_ROOT.then(SpawnSubcommand.make());

        CM_COMMAND_ROOT.then(net.minecraft.commands.Commands.literal("test").executes(ctx -> {
            final var player = ctx.getSource().getPlayerOrException();

            final var diamondAxe = new ItemStack(Items.DIAMOND_AXE);

            final var treecutter = new TreeCutterUpgrade();

            final var upgrades = new RoomUpgradeList(List.of(treecutter));

            diamondAxe.set(RoomUpgrades.UPGRADE_LIST_COMPONENT, upgrades);

            player.addItem(diamondAxe);

            return 0;
        }));

        event.getDispatcher().register(Commands.CM_COMMAND_ROOT);
    }
}
