package net.wolfygames7237.Crusadersoffiction.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.wolfygames7237.Crusadersoffiction.recipe.StructureBuilderRecipe;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Consumer;

public class StructureBuilderRecipeBuilder implements RecipeBuilder {

    public static final int INPUT_COUNT = 20;

    private final NonNullList<Ingredient> ingredients =
            NonNullList.withSize(INPUT_COUNT, Ingredient.EMPTY);
    private final int[] amounts = new int[INPUT_COUNT];

    private final Item resultItem;
    private final int resultCount;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    public StructureBuilderRecipeBuilder(ItemLike result, int count) {
        this.resultItem = result.asItem();
        this.resultCount = count;
        Arrays.fill(this.amounts, 1);
    }

    /* ---------------- Slot helpers ---------------- */

    private static void checkSlot(int slot) {
        if (slot < 0 || slot >= INPUT_COUNT) {
            throw new IllegalArgumentException("Slot must be between 0 and 19");
        }
    }

    /* ---------------- Ingredient setters ---------------- */

    /** Ingredient only (count = 1) */
    public StructureBuilderRecipeBuilder setIngredient(int slot, Ingredient ingredient) {
        return setIngredient(slot, ingredient, 1);
    }

    /** Ingredient + count */
    public StructureBuilderRecipeBuilder setIngredient(int slot, Ingredient ingredient, int count) {
        checkSlot(slot);
        ingredients.set(slot, ingredient);
        amounts[slot] = Math.max(0, count);
        return this;
    }

    /** ItemLike only (count = 1) — THIS FIXES YOUR ERROR */
    public StructureBuilderRecipeBuilder setIngredient(int slot, ItemLike item) {
        return setIngredient(slot, Ingredient.of(item), 1);
    }

    /** ItemLike + count — Forge-style usage */
    public StructureBuilderRecipeBuilder setIngredient(int slot, ItemLike item, int count) {
        return setIngredient(slot, Ingredient.of(item), count);
    }

    /** Only change count */
    public StructureBuilderRecipeBuilder setAmount(int slot, int count) {
        checkSlot(slot);
        amounts[slot] = Math.max(0, count);
        return this;
    }

    /* ---------------- RecipeBuilder ---------------- */

    @Override
    public RecipeBuilder unlockedBy(String name, CriterionTriggerInstance trigger) {
        advancement.addCriterion(name, trigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return resultItem;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);

        consumer.accept(new Result(
                id,
                resultItem,
                resultCount,
                ingredients,
                amounts,
                advancement,
                new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath())
        ));
    }

    /* ---------------- FinishedRecipe ---------------- */

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final Item resultItem;
        private final int resultCount;
        private final NonNullList<Ingredient> ingredients;
        private final int[] amounts;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Item resultItem, int resultCount,
                      NonNullList<Ingredient> ingredients, int[] amounts,
                      Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.resultItem = resultItem;
            this.resultCount = resultCount;
            this.ingredients = ingredients;
            this.amounts = amounts;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray array = new JsonArray();

            for (int i = 0; i < ingredients.size(); i++) {
                JsonObject entry = new JsonObject();

                if (!ingredients.get(i).isEmpty()) {
                    entry.add("ingredient", ingredients.get(i).toJson());
                } else {
                    entry.add("ingredient", new JsonObject());
                }

                entry.addProperty("count", amounts[i]);
                array.add(entry);
            }

            json.add("ingredients", array);

            JsonObject output = new JsonObject();
            output.addProperty("item", ForgeRegistries.ITEMS.getKey(resultItem).toString());
            if (resultCount > 1) {
                output.addProperty("count", resultCount);
            }
            json.add("output", output);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return StructureBuilderRecipe.Serializer.INSTANCE;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }

        /** Used by the recipe / block entity */
        public int[] getRequiredAmounts() {
            return amounts.clone();
        }
    }
}