package dev.compactmods.machines.neoforge.compat.jei;

import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.neoforge.machine.MachineItemCreator;
import dev.compactmods.machines.api.room.RoomApi;
import dev.compactmods.machines.neoforge.CompactMachines;
import dev.compactmods.machines.neoforge.machine.Machines;
import dev.compactmods.machines.neoforge.shrinking.Shrinking;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@JeiPlugin
public class CompactMachinesJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Constants.MOD_ID, "main");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        final var ingManager = registration.getIngredientManager();

        registration.addIngredientInfo(
                MachineItemCreator.unboundColored(CompactMachines.BRAND_MACHINE_COLOR),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.compactmachines.machines"));

        // Add all known template JEI infos
        RoomApi.getTemplates(ServerLifecycleHooks.getCurrentServer())
                .entrySet()
                .stream()
                .map(t -> MachineItemCreator.forNewRoom(t.getKey().location(), t.getValue()))
                .forEach(t -> registration.addIngredientInfo(t, VanillaTypes.ITEM_STACK, Component.translatable("jei.compactmachines.machines")));

        registration.addIngredientInfo(
                new ItemStack(Shrinking.PERSONAL_SHRINKING_DEVICE.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.compactmachines.shrinking_device"));
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(Machines.Items.UNBOUND_MACHINE.get(),
                (ingredient, context) -> {
                    // return (ingredient.has(ROOM_TEMPLATE) ? ub.getTemplateId(ingredient).toString() : "");
                    return "";
                });
    }
}
