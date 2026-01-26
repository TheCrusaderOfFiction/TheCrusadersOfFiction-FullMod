package net.wolfygames7237.Crusadersoffiction.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class BlockCompressorRecipe implements Recipe<SimpleContainer> {

    public static final int INPUT_COUNT = 1;
    public static final RecipeType<BlockCompressorRecipe> Type = ModRecipeTypes.COMPRESSOR.get();

    private final NonNullList<Ingredient> ingredients;
    private final int[] requiredAmounts;
    private final ItemStack output;
    private final ResourceLocation id;

    public BlockCompressorRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> ingredients, int[] amounts) {
        if (amounts.length != INPUT_COUNT) {
            throw new IllegalArgumentException("Amounts array must have length " + INPUT_COUNT);
        }
        this.id = id;
        this.output = output;
        this.ingredients = ingredients;
        this.requiredAmounts = amounts;
    }

    public int[] getRequiredAmounts() {
        return requiredAmounts;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {


        if (level.isClientSide) return false;

        for (int i = 0; i < INPUT_COUNT; i++) {
            Ingredient ingredient = ingredients.get(i);
            int required = requiredAmounts[i];

            if (ingredient.isEmpty() || required <= 0) continue;

            ItemStack stack = container.getItem(i);

            if (!ingredient.test(stack)) {
                return false;
            }

            if (stack.getCount() < required) {
                return false;
            }
        }
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
        return ingredients;
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
        return ModRecipeTypes.COMPRESSOR.get();
    }

    // -------------------- Serializer --------------------

    public static class Serializer implements RecipeSerializer<BlockCompressorRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public BlockCompressorRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("output"));
            JsonArray array = json.getAsJsonArray("ingredients");

            NonNullList<Ingredient> ingredients = NonNullList.withSize(INPUT_COUNT, Ingredient.EMPTY);
            int[] amounts = new int[INPUT_COUNT];

            for (int i = 0; i < INPUT_COUNT; i++) {
                if (i >= array.size()) {
                    ingredients.set(i, Ingredient.EMPTY);
                    amounts[i] = 0;
                    continue;
                }

                JsonObject entry = array.get(i).getAsJsonObject();
                if (entry.has("ingredient") && !entry.get("ingredient").isJsonNull()) {
                    ingredients.set(i, Ingredient.fromJson(entry.get("ingredient")));
                    amounts[i] = entry.has("count") ? entry.get("count").getAsInt() : 1;
                } else {
                    ingredients.set(i, Ingredient.EMPTY);
                    amounts[i] = 0;
                }

            }

            return new BlockCompressorRecipe(id, output, ingredients, amounts);
        }

        @Override
        public BlockCompressorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(INPUT_COUNT, Ingredient.EMPTY);
            int[] amounts = new int[INPUT_COUNT];

            for (int i = 0; i < INPUT_COUNT; i++) {
                ingredients.set(i, Ingredient.fromNetwork(buf));
                amounts[i] = buf.readInt();
            }

            ItemStack output = buf.readItem();
            return new BlockCompressorRecipe(id, output, ingredients, amounts);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BlockCompressorRecipe recipe) {
            for (int i = 0; i < INPUT_COUNT; i++) {
                recipe.ingredients.get(i).toNetwork(buf);
                buf.writeInt(recipe.requiredAmounts[i]);
            }
            buf.writeItem(recipe.output);
        }
    }
}

