package dev.compactmods.machines;

import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.command.Commands;
import dev.compactmods.machines.client.ClientConfig;
import dev.compactmods.machines.client.creative.CreativeTabs;
import dev.compactmods.machines.config.CommonConfig;
import dev.compactmods.machines.config.ServerConfig;
import dev.compactmods.machines.functions.LootFunctions;
import dev.compactmods.machines.dimension.Dimension;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.room.Rooms;
import dev.compactmods.machines.room.upgrade.RoomUpgradeEventHandlers;
import dev.compactmods.machines.room.upgrade.RoomUpgrades;
import dev.compactmods.machines.shrinking.Shrinking;
import dev.compactmods.machines.villager.Villagers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Constants.MOD_ID)
public class CompactMachines {

    public static final int BRAND_MACHINE_COLOR = FastColor.ARGB32.color(255, 248, 246, 76);

    @SuppressWarnings("unused")
    public CompactMachines(IEventBus modBus) {
        // Package initialization here, this kick-starts the rest of the DR code (classloading)
        Machines.prepare();
        Shrinking.prepare();
        Rooms.prepare();

        RoomUpgrades.prepare();
        RoomUpgrades.registerGameEvents(modBus);

        Dimension.prepare();
//  todo upgrade system      MachineRoomUpgrades.prepare();
        Commands.prepare();
        LootFunctions.prepare();

        Villagers.prepare();
        CreativeTabs.prepare();

        CMRegistries.setup(modBus);

        // Configuration
        ModLoadingContext mlCtx = ModLoadingContext.get();

        final var modContainer = mlCtx.getActiveContainer();
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG);
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG);
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG);


    }

    public static ResourceLocation rl(String id) {
        return ResourceLocation.isValidPath(id) ?
            new ResourceLocation(Constants.MOD_ID, id) :
                new ResourceLocation(Constants.MOD_ID, "invalid");
    }
}
