package net.wolfygames7237.Crusadersoffiction.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.recipe.ForgeRecipe;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

public class ForgeRecipeBuilder implements RecipeBuilder {
    // A single list of 11 ingredients to match the BlockEntity/Recipe logic
    private final NonNullList<Ingredient> ingredients = NonNullList.withSize(11, Ingredient.EMPTY);
    private final Item result;
    private final int count;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    public ForgeRecipeBuilder(ItemLike result, int count) {
        this.result = result.asItem();
        this.count = count;
    }

    /** Sets an ingredient in a specific input slot (0–8 for grid, 9 for fuel, 10 for tool) */
    public ForgeRecipeBuilder setIngredient(int slot, Ingredient ingredient) {
        if (slot < 0 || slot >= 11) {
            throw new IllegalArgumentException("Slot must be between 0 and 10");
        }
        this.ingredients.set(slot, ingredient);
        return this;
    }

    // Helper methods to make it easier to read
    public ForgeRecipeBuilder setFuel(Ingredient ingredient) {
        return setIngredient(9, ingredient);
    }

    public ForgeRecipeBuilder setTool(Ingredient ingredient) {
        return setIngredient(10, ingredient);
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        this.advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                .rewards(AdvancementRewards.Builder.recipe(pRecipeId))
                .requirements(RequirementsStrategy.OR);

        pFinishedRecipeConsumer.accept(new Result(
                pRecipeId,
                this.result,
                this.count,
                this.ingredients,
                this.advancement,
                new ResourceLocation(pRecipeId.getNamespace(), "recipes/" + pRecipeId.getPath())));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final NonNullList<Ingredient> ingredients;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Item result, int count,
                      NonNullList<Ingredient> ingredients,
                      Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.result = result;
            this.count = count;
            this.ingredients = ingredients;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray ingredientsArray = new JsonArray();

            for (Ingredient ingredient : ingredients) {
                // If the ingredient is empty, manually add an empty object {}
                if (ingredient.isEmpty()) {
                    ingredientsArray.add(new JsonObject());
                } else {
                    // Otherwise, use the standard Forge/Minecraft JSON conversion
                    ingredientsArray.add(ingredient.toJson());
                }
            }

            json.add("ingredients", ingredientsArray);

            // Standard Output Serialization
            JsonObject outputObject = new JsonObject();
            outputObject.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
            if (this.count > 1) {
                outputObject.addProperty("count", this.count);
            }
            json.add("output", outputObject);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ForgeRecipe.Serializer.INSTANCE;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}