package dev.compactmods.machines.datagen;

import dev.compactmods.machines.api.CompactMachines;
import dev.compactmods.machines.api.room.template.RoomTemplate;
import dev.compactmods.machines.machine.Machines;
import dev.compactmods.machines.room.Rooms;
import dev.compactmods.machines.shrinking.Shrinking;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class RecipeGenerator extends RecipeProvider {

	public RecipeGenerator(PackOutput packOut, CompletableFuture<HolderLookup.Provider> holders) {
		super(packOut, holders);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider provider) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Rooms.Items.BREAKABLE_WALL.get(), 8)
			.pattern("DDD")
			.pattern("D D")
			.pattern("DDD")
			.define('D', Items.POLISHED_DEEPSLATE)
			.unlockedBy("picked_up_deepslate", RecipeProvider.has(Tags.Items.COBBLESTONES_DEEPSLATE))
			.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Shrinking.PERSONAL_SHRINKING_DEVICE.get())
			.pattern("121")
			.pattern("345")
			.pattern("676")
			.define('1', Tags.Items.NUGGETS_IRON)
			.define('2', Tags.Items.GLASS_PANES)
			.define('3', Shrinking.ENLARGING_MODULE)
			.define('4', Items.ENDER_EYE)
			.define('5', Shrinking.SHRINKING_MODULE)
			.define('6', Tags.Items.INGOTS_IRON)
			.define('7', Tags.Items.INGOTS_COPPER)
			.unlockedBy("picked_up_ender_eye", RecipeProvider.has(Items.ENDER_EYE))
			.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Shrinking.ENLARGING_MODULE)
			.pattern("BPB")
			.pattern("BEB")
			.pattern("BLB")
			.define('B', Items.STONE_BUTTON)
			.define('P', Items.PISTON)
			.define('E', Items.ENDER_EYE)
			.define('L', Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
			.unlockedBy("picked_up_ender_eye", RecipeProvider.has(Items.ENDER_EYE))
			.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Shrinking.SHRINKING_MODULE)
			.pattern("BPB")
			.pattern("BEB")
			.pattern("BLB")
			.define('B', Items.STONE_BUTTON)
			.define('P', Items.STICKY_PISTON)
			.define('E', Items.ENDER_EYE)
			.define('L', Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
			.unlockedBy("picked_up_ender_eye", RecipeProvider.has(Items.ENDER_EYE))
			.save(recipeOutput);

		addMachineRecipes(recipeOutput, provider);
	}

	private void addMachineRecipes(RecipeOutput consumer, HolderLookup.Provider provider) {
		var allPossible = provider.lookupOrThrow(RoomTemplate.REGISTRY_KEY)
			.listElements()
			.toList();

		addMachineRecipe(consumer, provider, CompactMachines.modRL("tiny"), Tags.Items.INGOTS_COPPER);
		addMachineRecipe(consumer, provider, CompactMachines.modRL("small"), Tags.Items.INGOTS_IRON);
		addMachineRecipe(consumer, provider, CompactMachines.modRL("normal"), Tags.Items.INGOTS_GOLD);
		addMachineRecipe(consumer, provider, CompactMachines.modRL("large"), Tags.Items.GEMS_DIAMOND);
		addMachineRecipe(consumer, provider, CompactMachines.modRL("giant"), Tags.Items.OBSIDIANS);
		addMachineRecipe(consumer, provider, CompactMachines.modRL("colossal"), Tags.Items.INGOTS_NETHERITE);

		addMachineRecipe(consumer, provider, CompactMachines.modRL("soaryn"), Tags.Items.NETHER_STARS);
		addMachineRecipe(consumer, provider, CompactMachines.modRL("farming"), Items.DIAMOND_HOE);
	}

	private void addMachineRecipe(RecipeOutput consumer, HolderLookup.Provider provider, ResourceLocation id, TagKey<Item> catalyst) {
		final var templateRef = provider.lookupOrThrow(RoomTemplate.REGISTRY_KEY)
			.getOrThrow(ResourceKey.create(RoomTemplate.REGISTRY_KEY, id));

		machineRecipeBuilder(consumer, templateRef, builder -> builder.define('P', catalyst));
	}

	private void addMachineRecipe(RecipeOutput consumer, HolderLookup.Provider provider, ResourceLocation id, ItemLike catalyst) {
		final var templateRef = provider.lookupOrThrow(RoomTemplate.REGISTRY_KEY)
			.getOrThrow(ResourceKey.create(RoomTemplate.REGISTRY_KEY, id));

		machineRecipeBuilder(consumer, templateRef, builder -> builder.define('P', catalyst));
	}

	protected void machineRecipeBuilder(RecipeOutput consumer, Holder.Reference<RoomTemplate> templateRef, UnaryOperator<ShapedRecipeBuilder> configure) {
		final var builder = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Machines.Items.forNewRoom(templateRef))
			.pattern("WWW")
			.pattern("EPS")
			.pattern("WWW")
			.define('W', Rooms.Items.BREAKABLE_WALL)
			.define('E', Shrinking.ENLARGING_MODULE)
			.define('S', Shrinking.SHRINKING_MODULE);

		configure.apply(builder);

		builder.unlockedBy("has_recipe", RecipeProvider.has(Rooms.Items.BREAKABLE_WALL));

		final var recipeId = CompactMachines.modRL("new_machine_" + templateRef.key().location().getPath());
		builder.save(consumer, recipeId);
	}
}
