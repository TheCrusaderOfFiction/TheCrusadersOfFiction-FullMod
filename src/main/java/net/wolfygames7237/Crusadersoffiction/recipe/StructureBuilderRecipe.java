package net.wolfygames7237.Crusadersoffiction.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StructureBuilderRecipe implements Recipe<SimpleContainer> {

    public static final int INPUT_COUNT = 20;

    private final NonNullList<Ingredient> inputItems;
    private final int[] amounts;
    private final ItemStack output;
    private final ResourceLocation id;

    public StructureBuilderRecipe(
            ResourceLocation id,
            ItemStack output,
            NonNullList<Ingredient> inputItems,
            int[] amounts
    ) {
        if (amounts.length != INPUT_COUNT) {
            throw new IllegalArgumentException("Amounts array must have length " + INPUT_COUNT);
        }
        this.id = id;
        this.output = output;
        this.inputItems = inputItems;
        this.amounts = amounts;
    }

    public int[] getRequiredAmounts() {
        return amounts;
    }

    // ---------------- Recipe logic ----------------

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        System.out.println("matches() called for recipe " + id);

        if (level.isClientSide) return false;

        for (int i = 0; i < INPUT_COUNT; i++) {
            Ingredient ingredient = inputItems.get(i);
            int required = amounts[i];

            if (ingredient.isEmpty() || required <= 0) continue;

            ItemStack stack = container.getItem(i);

            if (!ingredient.test(stack)) {
                System.out.println("Ingredient mismatch at slot " + i + " required: " + ingredient + " found: " + stack);
                return false;
            }

            if (stack.getCount() < required) {
                System.out.println("Not enough items at slot " + i + " required: " + required + " found: " + stack.getCount());
                return false;
            }
        }

        System.out.println("Recipe matches: " + id);
        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess access) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.STRUCTURE.get();
    }

    // ---------------- Serializer ----------------

    public static class Serializer implements RecipeSerializer<StructureBuilderRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public StructureBuilderRecipe fromJson(ResourceLocation id, JsonObject json) {
            System.out.println("STRUCTURE JSON HIT: " + id);
            ItemStack output =
                    ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            System.out.println("Loaded Structure recipe: " + id);
            JsonArray array = GsonHelper.getAsJsonArray(json, "ingredients");

            NonNullList<Ingredient> ingredients =
                    NonNullList.withSize(INPUT_COUNT, Ingredient.EMPTY);
            int[] amounts = new int[INPUT_COUNT];

            for (int i = 0; i < INPUT_COUNT; i++) {
                if (i >= array.size()) {
                    ingredients.set(i, Ingredient.EMPTY);
                    amounts[i] = 0;
                    continue;
                }

                JsonObject entry = array.get(i).getAsJsonObject();

                if (!entry.has("ingredient")
                        || entry.get("ingredient").isJsonNull()
                        || entry.getAsJsonObject("ingredient").size() == 0) {

                    ingredients.set(i, Ingredient.EMPTY);
                    amounts[i] = 0;

                } else {
                    JsonObject ingredientJson = entry.getAsJsonObject("ingredient");
                    ingredients.set(i, Ingredient.fromJson(ingredientJson));
                    amounts[i] = GsonHelper.getAsInt(entry, "count", 1);
                }
            }

            return new StructureBuilderRecipe(id, output, ingredients, amounts);
        }

        @Override
        public StructureBuilderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> ingredients =
                    NonNullList.withSize(INPUT_COUNT, Ingredient.EMPTY);
            int[] amounts = new int[INPUT_COUNT];

            for (int i = 0; i < INPUT_COUNT; i++) {
                ingredients.set(i, Ingredient.fromNetwork(buf));
                amounts[i] = buf.readInt();
            }

            ItemStack output = buf.readItem();
            return new StructureBuilderRecipe(id, output, ingredients, amounts);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, StructureBuilderRecipe recipe) {
            for (int i = 0; i < INPUT_COUNT; i++) {
                recipe.inputItems.get(i).toNetwork(buf);
                buf.writeInt(recipe.amounts[i]);
            }
            buf.writeItem(recipe.output);
        }
    }
}

