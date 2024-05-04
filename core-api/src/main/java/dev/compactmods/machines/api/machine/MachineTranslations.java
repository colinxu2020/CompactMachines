package dev.compactmods.machines.api.machine;

import dev.compactmods.machines.api.Constants;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.Function;
import java.util.function.Supplier;

public interface MachineTranslations {

    Function<BlockPos, Component> NOT_A_MACHINE_BLOCK = (pos) -> Component.empty();

    interface IDs {
        String OWNER = Util.makeDescriptionId("machine", Constants.modRL("machine.owner"));
        String SIZE = Util.makeDescriptionId("machine", Constants.modRL("machine.size"));
        String BOUND_TO = Util.makeDescriptionId("machine", Constants.modRL("machine.bound_to"));
        String NEW_MACHINE = Util.makeDescriptionId("machine", Constants.modRL("new_machine"));
    }
}
