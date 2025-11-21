package net.wolfygames7237.Crusadersoffiction.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
import net.minecraftforge.common.data.ExistingFileHelper;


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


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItem.COPPERNUGGET.get(), 9)
                .requires(Items.COPPER_INGOT)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItem.WYSTERIUM.get(), 9)
                .requires(ModBlocks.WYSTERIUM_BLOCK.get())
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(pWriter, "wysterium_from_block");


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
