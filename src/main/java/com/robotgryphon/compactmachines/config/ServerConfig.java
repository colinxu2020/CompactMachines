package com.robotgryphon.compactmachines.config;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.robotgryphon.compactmachines.core.EnumMachinePlayersBreakHandling;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServerConfig {
    public static ForgeConfigSpec CONFIG;

    public static ForgeConfigSpec.EnumValue<EnumMachinePlayersBreakHandling> MACHINE_PLAYER_BREAK_HANDLING;

    static {
        generateConfig();
    }

    private static void generateConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

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

        builder.pop();

        CONFIG = builder.build();
    }
}
