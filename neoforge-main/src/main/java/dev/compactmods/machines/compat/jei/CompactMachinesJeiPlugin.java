package dev.compactmods.machines.compat.jei;

import dev.compactmods.machines.api.CompactMachinesApi;
import dev.compactmods.machines.api.room.RoomApi;
import dev.compactmods.machines.CompactMachines;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.shrinking.Shrinking;
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
        return CompactMachinesApi.modRL("main");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        final var ingManager = registration.getIngredientManager();

        registration.addIngredientInfo(
                Machines.Items.unboundColored(CompactMachines.BRAND_MACHINE_COLOR),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.compactmachines.machines"));

        // Add all known template JEI infos
        RoomApi.getTemplates(ServerLifecycleHooks.getCurrentServer())
                .entrySet()
                .stream()
                .map(t -> Machines.Items.forNewRoom(t.getKey().location(), t.getValue()))
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
