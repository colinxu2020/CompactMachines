package dev.compactmods.machines.server;

import com.electronwill.nightconfig.core.EnumGetMethod;
import dev.compactmods.machines.machine.config.EnumMachinePlayersBreakHandling;
import net.minecraft.commands.Commands;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServerConfig {
    public static ModConfigSpec CONFIG;

    public static ModConfigSpec.EnumValue<EnumMachinePlayersBreakHandling> MACHINE_PLAYER_BREAK_HANDLING;

    private static ModConfigSpec.IntValue REBIND_LEVEL;
    private static ModConfigSpec.IntValue GIVE_MACHINE;
    private static ModConfigSpec.IntValue CHANGE_SPAWN_LEVEL;

    private static ModConfigSpec.IntValue CHANGE_ROOM_UPGRADES;

    private static ModConfigSpec.BooleanValue ALLOWED_OUTSIDE_MACHINE;
    private static ModConfigSpec.BooleanValue DAMAGE_PLAYERS_OUTSIDE_MACHINE;

    static {
        generateConfig();
    }

    private static void generateConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder
                .comment("Machines")
                .push("machines");

        List<String> handling = Arrays
                .stream(EnumMachinePlayersBreakHandling.values())
                .map(v -> String.format(" '%s' = %s", v.configName(), v.configDesc()))
                .collect(Collectors.toList());

        handling.add(0, "Specifies machine breakability while players are inside.");
        String[] handlingFinal = handling.toArray(new String[0]);

        MACHINE_PLAYER_BREAK_HANDLING = builder
                .comment(handlingFinal)
                .defineEnum("breakHandling",
                        EnumMachinePlayersBreakHandling.UNBREAKABLE,
                        EnumGetMethod.NAME_IGNORECASE);

        ALLOWED_OUTSIDE_MACHINE = builder
                .comment("Specify if we want to allow players outside the room boundaries")
                .comment("default: false")
                .define("allowOutside", false);

        DAMAGE_PLAYERS_OUTSIDE_MACHINE = builder
                .comment("Specify if we want to damage players that are outside the room boundaries")
                .comment("default: true")
                .define("damagePlayersOutOfBounds", true);

        builder.pop();

        builder
                .push("commands")
                .push("permLevels")
                .comment("Specifies requirements for running administrative commands. Requires a server restart to take effect.")
                .comment("0 = ALL, 1 = ADMIN, 2 = OP, 4 = OWNER");


        REBIND_LEVEL = builder
                .comment("Command level required for using the rebind and unbind commands.")
                .defineInRange("rebind", Commands.LEVEL_GAMEMASTERS, Commands.LEVEL_ALL, Commands.LEVEL_OWNERS);

        GIVE_MACHINE = builder
                .comment("Command level required for giving new machines to players.")
                .defineInRange("give", Commands.LEVEL_GAMEMASTERS, Commands.LEVEL_ALL, Commands.LEVEL_OWNERS);

        CHANGE_SPAWN_LEVEL = builder
                .comment("Command level required for changing room spawn information.")
                .defineInRange("spawn", Commands.LEVEL_GAMEMASTERS, Commands.LEVEL_ALL, Commands.LEVEL_OWNERS);

        CHANGE_ROOM_UPGRADES = builder
                .comment("Command level required for changing room upgrades.")
                .defineInRange("upgrades", Commands.LEVEL_GAMEMASTERS, Commands.LEVEL_ALL, Commands.LEVEL_OWNERS);


        builder.pop(2);

        CONFIG = builder.build();
    }

    public static int rebindLevel() {
        return REBIND_LEVEL.get();
    }

    public static int giveMachineLevel() {
        return GIVE_MACHINE.get();
    }

    public static int changeRoomSpawn() {
        return CHANGE_SPAWN_LEVEL.get();
    }

    public static int changeUpgrades() { return CHANGE_ROOM_UPGRADES.get(); }

    public static Boolean isAllowedOutsideOfMachine() {
        return ALLOWED_OUTSIDE_MACHINE.get();
    }

    public static Boolean damagePlayersOutOfBounds() {
        return DAMAGE_PLAYERS_OUTSIDE_MACHINE.get();
    }
}
