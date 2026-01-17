package net.wolfygames7237.Crusadersoffiction.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.wolfygames7237.Crusadersoffiction.Item.ModItem;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.datagen.custom.ForgeRecipeBuilder;


import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> WYSTERIUM_SMELTABLES = List.of(ModItem.RAWWYSTERIUM.get(),
            ModBlocks.WYSTERIUM_ORE.get());

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        oreSmelting(pWriter, WYSTERIUM_SMELTABLES, RecipeCategory.MISC, ModItem.WYSTERIUMNUGGET.get(), 70f, 32000, "wysterium");
        oreBlasting(pWriter, WYSTERIUM_SMELTABLES, RecipeCategory.MISC, ModItem.WYSTERIUMNUGGET.get(), 70f, 6400, "wysterium");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.WYSTERIUM.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItem.WYSTERIUMNUGGET.get())
                .unlockedBy(getHasName(ModItem.WYSTERIUMNUGGET.get()), has(ModItem.WYSTERIUMNUGGET.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COPPER_INGOT)
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItem.COPPERNUGGET.get())
                .unlockedBy(getHasName(ModItem.COPPERNUGGET.get()), has(ModItem.COPPERNUGGET.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.COPPERWIRE.get())
                .pattern("NCN")
                .define('N', ModItem.COPPERNUGGET.get())
                .define('C', Items.COPPER_INGOT)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.COMPUTERCHIP.get())
                .pattern("GLG")
                .pattern("WCW")
                .pattern("III")
                .define('W', ModItem.COPPERWIRE.get())
                .define('C', Items.COPPER_INGOT)
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GREEN_DYE)
                .define('L', Items.LIME_DYE)
                .unlockedBy(getHasName(ModItem.COPPERWIRE.get()), has(ModItem.COPPERWIRE.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.BLUESHARD.get())
                .pattern("LWL")
                .pattern("WCW")
                .pattern("LWL")
                .define('L', Items.LAPIS_LAZULI)
                .define('W', ModItem.WYSTERIUM.get())
                .define('C', ModItem.COMPUTERCHIP.get())
                .unlockedBy(getHasName(ModItem.COPPERNUGGET.get()), has(ModItem.COPPERNUGGET.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.BLUEARMORSHARD.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItem.BLUESHARD.get())
                .unlockedBy(getHasName(ModItem.BLUESHARD.get()), has(ModItem.BLUESHARD.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.BLUEARMORCASING.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItem.BLUEARMORSHARD.get())
                .unlockedBy(getHasName(ModItem.BLUEARMORSHARD.get()), has(ModItem.BLUEARMORSHARD.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WYSTERIUM_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItem.WYSTERIUM.get())
                .unlockedBy(getHasName(ModItem.WYSTERIUM.get()), has(ModItem.WYSTERIUM.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.OBI_ROD.get(), 2)
                .pattern("   ")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(Blocks.OBSIDIAN), has(Blocks.OBSIDIAN))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.WYSTERIUM_SWORD.get())
                .pattern(" W ")
                .pattern(" W ")
                .pattern(" S ")
                .define('S', ModItem.OBI_ROD.get())
                .define('W', ModItem.WYSTERIUM.get())
                .unlockedBy(getHasName(ModItem.OBI_ROD.get()), has(ModItem.OBI_ROD.get()))
                .unlockedBy(getHasName(ModItem.WYSTERIUM.get()), has(ModItem.WYSTERIUM.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.WYSTERIUM_PICKAXE.get())
                .pattern("WWW")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', ModItem.OBI_ROD.get())
                .define('W', ModItem.WYSTERIUM.get())
                .unlockedBy(getHasName(ModItem.OBI_ROD.get()), has(ModItem.OBI_ROD.get()))
                .unlockedBy(getHasName(ModItem.WYSTERIUM.get()), has(ModItem.WYSTERIUM.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.WYSTERIUM_AXE.get())
                .pattern("WW ")
                .pattern("WS ")
                .pattern(" S ")
                .define('S', ModItem.OBI_ROD.get())
                .define('W', ModItem.WYSTERIUM.get())
                .unlockedBy(getHasName(ModItem.OBI_ROD.get()), has(ModItem.OBI_ROD.get()))
                .unlockedBy(getHasName(ModItem.WYSTERIUM.get()), has(ModItem.WYSTERIUM.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.WYSTERIUM_SHOVEL.get())
                .pattern(" W ")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', ModItem.OBI_ROD.get())
                .define('W', ModItem.WYSTERIUM.get())
                .unlockedBy(getHasName(ModItem.OBI_ROD.get()), has(ModItem.OBI_ROD.get()))
                .unlockedBy(getHasName(ModItem.WYSTERIUM.get()), has(ModItem.WYSTERIUM.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.WYSTERIUM_HOE.get())
                .pattern("WW ")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', ModItem.OBI_ROD.get())
                .define('W', ModItem.WYSTERIUM.get())
                .unlockedBy(getHasName(ModItem.OBI_ROD.get()), has(ModItem.OBI_ROD.get()))
                .unlockedBy(getHasName(ModItem.WYSTERIUM.get()), has(ModItem.WYSTERIUM.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.WYSTERIUM_CHESTPLATE.get())
                .pattern("W W")
                .pattern("WWW")
                .pattern("WWW")
                .define('W', ModItem.WYSTERIUM.get())
                .unlockedBy(getHasName(ModItem.WYSTERIUM.get()), has(ModItem.WYSTERIUM.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.WYSTERIUM_LEGGINGS.get())
                .pattern("WWW")
                .pattern("W W")
                .pattern("W W")
                .define('W', ModItem.WYSTERIUM.get())
                .unlockedBy(getHasName(ModItem.WYSTERIUM.get()), has(ModItem.WYSTERIUM.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.WYSTERIUM_BOOTS.get())
                .pattern("W W")
                .pattern("W W")
                .define('W', ModItem.WYSTERIUM.get())
                .unlockedBy(getHasName(ModItem.WYSTERIUM.get()), has(ModItem.WYSTERIUM.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FORGE.get())
                .pattern("DCD")
                .pattern("SGS")
                .pattern("IWI")
                .define('D', Blocks.COBBLED_DEEPSLATE)
                .define('C', Blocks.COPPER_BLOCK)
                .define('S', Blocks.STONE)
                .define('G', Blocks.GLASS)
                .define('I', Items.IRON_INGOT)
                .define('W', ItemTags.LOGS)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(ItemTags.STONE_TOOL_MATERIALS))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItem.ROCK_HATCHET.get())
                .pattern("FR")
                .pattern("S ")
                .define('F', ModItem.FIBER.get())
                .define('R', ModItem.ROCK.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItem.ROCK.get()), has(ModItem.ROCK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.IRON_AXE)
                .pattern("H")
                .pattern("T")
                .pattern("S")
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .define('H', ModItem.IRON_AXE_HEAD.get())
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter, "iron_axe_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.IRON_PICKAXE)
                .pattern("H")
                .pattern("T")
                .pattern("S")
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .define('H', ModItem.IRON_PICKAXE_HEAD.get())
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter, "iron_pickaxe_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.IRON_SWORD)
                .pattern("H")
                .pattern("T")
                .pattern("S")
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .define('H', ModItem.IRON_SWORD_BLADE.get())
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter, "iron_sword_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.IRON_SHOVEL)
                .pattern("H")
                .pattern("T")
                .pattern("S")
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .define('H', ModItem.IRON_SHOVEL_BLADE.get())
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter, "iron_shovel_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.IRON_HOE)
                .pattern("H")
                .pattern("T")
                .pattern("S")
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .define('H', ModItem.IRON_HOE_BLADE.get())
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter, "iron_hoe_new");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.DIAMOND_AXE)
                .pattern("H")
                .pattern("T")
                .pattern("S")
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .define('H', ModItem.DIAMOND_AXE_HEAD.get())
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(pWriter, "diamond_axe_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.DIAMOND_PICKAXE)
                .pattern("H")
                .pattern("T")
                .pattern("S")
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .define('H', ModItem.DIAMOND_PICKAXE_HEAD.get())
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(pWriter, "diamond_pickaxe_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.DIAMOND_SWORD)
                .pattern("H")
                .pattern("T")
                .pattern("S")
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .define('H', ModItem.DIAMOND_SWORD_BLADE.get())
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(pWriter, "diamond_sword_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.DIAMOND_SHOVEL)
                .pattern("H")
                .pattern("T")
                .pattern("S")
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .define('H', ModItem.DIAMOND_SHOVEL_BLADE.get())
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(pWriter, "diamond_shovel_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.DIAMOND_HOE)
                .pattern("H")
                .pattern("T")
                .pattern("S")
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .define('H', ModItem.DIAMOND_HOE_BLADE.get())
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(pWriter, "diamond_hoe_new");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.STONE_HOE)
                .pattern("WW")
                .pattern("FS")
                .pattern(" S")
                .define('S', Items.STICK)
                .define('F', Ingredient.of(ModItem.FIBER.get(), Items.STRING))
                .define('W', ItemTags.STONE_TOOL_MATERIALS)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(ItemTags.STONE_TOOL_MATERIALS))
                .save(pWriter, "stone_hoe_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.STONE_PICKAXE)
                .pattern("WWW")
                .pattern("FS ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('F', Ingredient.of(ModItem.FIBER.get(), Items.STRING))
                .define('W', ItemTags.STONE_TOOL_MATERIALS)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(ItemTags.STONE_TOOL_MATERIALS))
                .save(pWriter, "stone_pickaxe_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.STONE_SHOVEL)
                .pattern(" W")
                .pattern("FS")
                .pattern(" S")
                .define('S', Items.STICK)
                .define('F', Ingredient.of(ModItem.FIBER.get(), Items.STRING))
                .define('W', ItemTags.STONE_TOOL_MATERIALS)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(ItemTags.STONE_TOOL_MATERIALS))
                .save(pWriter, "stone_shovel_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.STONE_AXE)
                .pattern("WW ")
                .pattern("WSF")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('F', Ingredient.of(ModItem.FIBER.get(), Items.STRING))
                .define('W', ItemTags.STONE_TOOL_MATERIALS)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(ItemTags.STONE_TOOL_MATERIALS))
                .save(pWriter, "stone_axe_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.STONE_SWORD)
                .pattern(" W")
                .pattern("FW")
                .pattern(" S")
                .define('S', Items.STICK)
                .define('F', Ingredient.of(ModItem.FIBER.get(), Items.STRING))
                .define('W', ItemTags.STONE_TOOL_MATERIALS)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(ItemTags.STONE_TOOL_MATERIALS))
                .save(pWriter, "stone_sword_new");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.WOODEN_HOE)
                .pattern("WW")
                .pattern("FS")
                .pattern(" S")
                .define('S', Items.STICK)
                .define('F', ModItem.FIBER.get())
                .define('W', ItemTags.PLANKS)
                .unlockedBy(getHasName(Items.OAK_PLANKS), has(ItemTags.PLANKS))
                .save(pWriter, "wooden_hoe_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.WOODEN_PICKAXE)
                .pattern("WWW")
                .pattern("FS ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('F', ModItem.FIBER.get())
                .define('W', ItemTags.PLANKS)
                .unlockedBy(getHasName(Items.OAK_PLANKS), has(ItemTags.PLANKS))
                .save(pWriter, "wooden_pickaxe_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.WOODEN_SHOVEL)
                .pattern(" W")
                .pattern("FS")
                .pattern(" S")
                .define('S', Items.STICK)
                .define('F', ModItem.FIBER.get())
                .define('W', ItemTags.PLANKS)
                .unlockedBy(getHasName(Items.OAK_PLANKS), has(ItemTags.PLANKS))
                .save(pWriter, "wooden_shovel_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.WOODEN_AXE)
                .pattern("WW ")
                .pattern("WSF")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('F', ModItem.FIBER.get())
                .define('W', ItemTags.PLANKS)
                .unlockedBy(getHasName(Items.OAK_PLANKS), has(ItemTags.PLANKS))
                .save(pWriter, "wooden_axe_new");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.WOODEN_SWORD)
                .pattern(" W")
                .pattern("FW")
                .pattern(" S")
                .define('S', Items.STICK)
                .define('F', ModItem.FIBER.get())
                .define('W', ItemTags.PLANKS)
                .unlockedBy(getHasName(Items.OAK_PLANKS), has(ItemTags.PLANKS))
                .save(pWriter, "wooden_sword_new");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItem.COPPER_HAMMER.get(), 5)
                .pattern("CCC")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('C', Items.COPPER_INGOT)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItem.IRON_HAMMER.get(), 5)
                .pattern(" CC")
                .pattern(" SC")
                .pattern("S  ")
                .define('S', Items.STICK)
                .define('C', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CHARGED_COAL_BLOCK.get(), 1)
                .pattern("CCC")
                .pattern("CLC")
                .pattern("CCC")
                .define('L', Items.LAVA_BUCKET)
                .define('C', Items.COAL)
                .unlockedBy(getHasName(Items.COAL), has(Items.COAL))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.COPPER_SWORD.get())
                .pattern(" W ")
                .pattern(" W ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('W', Items.COPPER_INGOT)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.COPPER_PICKAXE.get())
                .pattern("WWW")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('W', Items.COPPER_INGOT)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.COPPER_AXE.get())
                .pattern("WW ")
                .pattern("WS ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('W', Items.COPPER_INGOT)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.COPPER_SHOVEL.get())
                .pattern(" W ")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('W', Items.COPPER_INGOT)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItem.COPPER_HOE.get())
                .pattern("WW ")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('W', Items.COPPER_INGOT)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItem.COPPERNUGGET.get(), 9)
                .requires(Items.COPPER_INGOT)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItem.WYSTERIUM.get(), 9)
                .requires(ModBlocks.WYSTERIUM_BLOCK.get())
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(pWriter, "wysterium_from_block");

        new ForgeRecipeBuilder(ModItem.IRON_PICKAXE_HEAD.get(), 1)
                .setIngredient(0, Ingredient.of(Items.IRON_INGOT))
                .setIngredient(1, Ingredient.of(Items.IRON_INGOT))
                .setIngredient(2, Ingredient.of(Items.IRON_INGOT))
                // ... other slots ...
                .setFuel(Ingredient.of(Items.COAL))
                .setTool(Ingredient.of(ModItem.COPPER_HAMMER.get()))
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(pWriter, new ResourceLocation(CrusadersOfFiction.MOD_ID, "iron_pickaxe_top_forge"));

        new ForgeRecipeBuilder(ModItem.IRON_SHOVEL_BLADE.get(), 1)
                .setIngredient(1, Ingredient.of(Items.IRON_INGOT))
                // ... other slots ...
                .setFuel(Ingredient.of(Items.COAL))
                .setTool(Ingredient.of(ModItem.COPPER_HAMMER.get()))
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(pWriter, new ResourceLocation(CrusadersOfFiction.MOD_ID, "iron_shovel_2_forge"));
        new ForgeRecipeBuilder(ModItem.IRON_SHOVEL_BLADE.get(), 1)
                .setIngredient(0, Ingredient.of(Items.IRON_INGOT))
                // ... other slots ...
                .setFuel(Ingredient.of(Items.COAL))
                .setTool(Ingredient.of(ModItem.COPPER_HAMMER.get()))
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(pWriter, new ResourceLocation(CrusadersOfFiction.MOD_ID, "iron_shovel_1_forge"));

        new ForgeRecipeBuilder(ModItem.IRON_HOE_BLADE.get(), 1)
                .setIngredient(0, Ingredient.of(Items.IRON_INGOT))
                .setIngredient(1, Ingredient.of(Items.IRON_INGOT))
                // ... other slots ...
                .setFuel(Ingredient.of(Items.COAL))
                .setTool(Ingredient.of(ModItem.COPPER_HAMMER.get()))
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(pWriter, new ResourceLocation(CrusadersOfFiction.MOD_ID, "iron_hoe_top_left_forge"));

        new ForgeRecipeBuilder(ModItem.IRON_AXE_HEAD.get(), 1)
                .setIngredient(0, Ingredient.of(Items.IRON_INGOT))
                .setIngredient(1, Ingredient.of(Items.IRON_INGOT))
                .setIngredient(3, Ingredient.of(Items.IRON_INGOT))
                // ... other slots ...
                .setFuel(Ingredient.of(Items.COAL))
                .setTool(Ingredient.of(ModItem.COPPER_HAMMER.get()))
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(pWriter, new ResourceLocation(CrusadersOfFiction.MOD_ID, "iron_axe_top_left_forge"));

        new ForgeRecipeBuilder(ModItem.IRON_SWORD_BLADE.get(), 1)
                .setIngredient(0, Ingredient.of(Items.IRON_INGOT))
                .setIngredient(3, Ingredient.of(Items.IRON_INGOT))
                // ... other slots ...
                .setFuel(Ingredient.of(Items.COAL))
                .setTool(Ingredient.of(ModItem.COPPER_HAMMER.get()))
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(pWriter, new ResourceLocation(CrusadersOfFiction.MOD_ID, "iron_sword_top_left_forge"));

        new ForgeRecipeBuilder(ModItem.DIAMOND_PICKAXE_HEAD.get(), 1)
                .setIngredient(0, Ingredient.of(Items.DIAMOND))
                .setIngredient(1, Ingredient.of(Items.DIAMOND))
                .setIngredient(2, Ingredient.of(Items.DIAMOND))
                // ... other slots ...
                .setFuel(Ingredient.of(ModBlocks.CHARGED_COAL_BLOCK.get()))
                .setTool(Ingredient.of(ModItem.IRON_HAMMER.get()))
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(pWriter, new ResourceLocation(CrusadersOfFiction.MOD_ID, "diamond_pickaxe_top_forge"));

        new ForgeRecipeBuilder(ModItem.DIAMOND_SHOVEL_BLADE.get(), 1)
                .setIngredient(1, Ingredient.of(Items.DIAMOND))
                // ... other slots ...
                .setFuel(Ingredient.of(ModBlocks.CHARGED_COAL_BLOCK.get()))
                .setTool(Ingredient.of(ModItem.IRON_HAMMER.get()))
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(pWriter, new ResourceLocation(CrusadersOfFiction.MOD_ID, "diamond_shovel_2_forge"));
        new ForgeRecipeBuilder(ModItem.DIAMOND_SHOVEL_BLADE.get(), 1)
                .setIngredient(0, Ingredient.of(Items.DIAMOND))
                // ... other slots ...
                .setFuel(Ingredient.of(ModBlocks.CHARGED_COAL_BLOCK.get()))
                .setTool(Ingredient.of(ModItem.IRON_HAMMER.get()))
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(pWriter, new ResourceLocation(CrusadersOfFiction.MOD_ID, "diamond_shovel_1_forge"));

        new ForgeRecipeBuilder(ModItem.DIAMOND_HOE_BLADE.get(), 1)
                .setIngredient(0, Ingredient.of(Items.DIAMOND))
                .setIngredient(1, Ingredient.of(Items.DIAMOND))
                // ... other slots ...
                .setFuel(Ingredient.of(ModBlocks.CHARGED_COAL_BLOCK.get()))
                .setTool(Ingredient.of(ModItem.IRON_HAMMER.get()))
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(pWriter, new ResourceLocation(CrusadersOfFiction.MOD_ID, "diamond_hoe_top_left_forge"));

        new ForgeRecipeBuilder(ModItem.DIAMOND_AXE_HEAD.get(), 1)
                .setIngredient(0, Ingredient.of(Items.DIAMOND))
                .setIngredient(1, Ingredient.of(Items.DIAMOND))
                .setIngredient(3, Ingredient.of(Items.DIAMOND))
                // ... other slots ...
                .setFuel(Ingredient.of(ModBlocks.CHARGED_COAL_BLOCK.get()))
                .setTool(Ingredient.of(ModItem.IRON_HAMMER.get()))
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(pWriter, new ResourceLocation(CrusadersOfFiction.MOD_ID, "diamond_axe_top_left_forge"));

        new ForgeRecipeBuilder(ModItem.DIAMOND_SWORD_BLADE.get(), 1)
                .setIngredient(0, Ingredient.of(Items.DIAMOND))
                .setIngredient(3, Ingredient.of(Items.DIAMOND))
                // ... other slots ...
                .setFuel(Ingredient.of(ModBlocks.CHARGED_COAL_BLOCK.get()))
                .setTool(Ingredient.of(ModItem.IRON_HAMMER.get()))
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(pWriter, new ResourceLocation(CrusadersOfFiction.MOD_ID, "diamond_sword_top_left_forge"));




    }
    private static ResourceLocation recipes(String name) {
        return new ResourceLocation("crusadersoffiction", name);
    }


    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                            pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer,  CrusadersOfFiction.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    private void removeRecipe(Consumer<FinishedRecipe> consumer, ResourceLocation recipeId) {
        consumer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(com.google.gson.JsonObject json) {
                // No data — this marks it for removal
            }

            @Override
            public ResourceLocation getId() {
                return recipeId;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return null; // Placeholder
            }

            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}
