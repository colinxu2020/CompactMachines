package dev.compactmods.machines.datagen;

import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.api.room.RoomTemplate;
import dev.compactmods.machines.machine.MachineItemCreator;
import dev.compactmods.machines.room.Rooms;
import dev.compactmods.machines.shrinking.Shrinking;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends RecipeProvider {

   public RecipeGenerator(PackOutput packOut, CompletableFuture<HolderLookup.Provider> holders) {
	  super(packOut, holders);
   }

   @Override
   protected void buildRecipes(RecipeOutput recipeOutput) {
	  ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Rooms.Items.BREAKABLE_WALL.get(), 8)
		  .pattern("DDD")
		  .pattern("D D")
		  .pattern("DDD")
		  .define('D', Items.POLISHED_DEEPSLATE)
		  .unlockedBy("picked_up_deepslate", has(Tags.Items.COBBLESTONES_DEEPSLATE))
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
		  .unlockedBy("picked_up_ender_eye", has(Items.ENDER_EYE))
		  .save(recipeOutput);

	  ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Shrinking.ENLARGING_MODULE)
		  .pattern("BPB")
		  .pattern("BEB")
		  .pattern("BLB")
		  .define('B', Items.STONE_BUTTON)
		  .define('P', Items.PISTON)
		  .define('E', Items.ENDER_EYE)
		  .define('L', Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
		  .unlockedBy("picked_up_ender_eye", has(Items.ENDER_EYE))
		  .save(recipeOutput);

	  ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Shrinking.SHRINKING_MODULE)
		  .pattern("BPB")
		  .pattern("BEB")
		  .pattern("BLB")
		  .define('B', Items.STONE_BUTTON)
		  .define('P', Items.STICKY_PISTON)
		  .define('E', Items.ENDER_EYE)
		  .define('L', Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
		  .unlockedBy("picked_up_ender_eye", has(Items.ENDER_EYE))
		  .save(recipeOutput);

	  addMachineRecipes(recipeOutput);
   }

   private void addMachineRecipes(RecipeOutput consumer) {
	  registerMachineRecipe(consumer, Constants.modRL("tiny"),
		  new RoomTemplate(3, FastColor.ARGB32.color(255, 201, 91, 19)), Tags.Items.INGOTS_COPPER);

	  registerMachineRecipe(consumer, Constants.modRL("small"),
		  new RoomTemplate(5, FastColor.ARGB32.color(255, 212, 210, 210)), Tags.Items.INGOTS_IRON);

	  registerMachineRecipe(consumer, Constants.modRL("normal"),
		  new RoomTemplate(7, FastColor.ARGB32.color(255, 251, 242, 54)), Tags.Items.INGOTS_GOLD);

	  registerMachineRecipe(consumer, Constants.modRL("large"),
		  new RoomTemplate(9, FastColor.ARGB32.color(255, 33, 27, 46)), Tags.Items.GEMS_DIAMOND);

	  registerMachineRecipe(consumer, Constants.modRL("giant"),
		  new RoomTemplate(11, FastColor.ARGB32.color(255, 67, 214, 205)), Tags.Items.OBSIDIANS);

	  registerMachineRecipe(consumer, Constants.modRL("colossal"),
		  new RoomTemplate(13, FastColor.ARGB32.color(255, 66, 63, 66)), Tags.Items.INGOTS_NETHERITE);
   }

   protected void registerMachineRecipe(RecipeOutput consumer, ResourceLocation temId, RoomTemplate template, TagKey<Item> catalyst) {
	  final var recipe = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MachineItemCreator.forNewRoom(temId, template))
		  .pattern("WWW")
		  .pattern("EPS")
		  .pattern("WWW")
		  .define('W', Rooms.Items.BREAKABLE_WALL)
		  .define('E', Shrinking.ENLARGING_MODULE)
		  .define('S', Shrinking.SHRINKING_MODULE)
		  .define('P', catalyst);

	  recipe.unlockedBy("has_recipe", has(Rooms.Items.BREAKABLE_WALL));

	  final var recipeId = Constants.modRL("new_machine_" + temId.getPath());
	  recipe.save(consumer, recipeId);
   }
}
