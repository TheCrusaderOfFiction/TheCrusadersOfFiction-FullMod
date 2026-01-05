package net.wolfygames7237.Crusadersoffiction.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import org.jetbrains.annotations.NotNull;

public class ForgeRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;

    public ForgeRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> inputItems) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) return false;

        // Check all 11 slots (0-10)
        for (int i = 0; i < 11; i++) {
            ItemStack stackInSlot = pContainer.getItem(i);
            Ingredient recipeIngredient = inputItems.get(i);

            // ingredient.isEmpty() returns true for Ingredient.EMPTY (Air)
            if (recipeIngredient.isEmpty()) {
                if (!stackInSlot.isEmpty()) {
                   // System.out.println("[Forge Debug] Slot " + i + " FAILED: Recipe wants AIR, but found " + stackInSlot.getItem());
                    return false;
                }
            } else {
                if (!recipeIngredient.test(stackInSlot)) {
                    String expected = recipeIngredient.getItems().length > 0 ?
                            recipeIngredient.getItems()[0].getItem().toString() : "Unknown";
                   // System.out.println("[Forge Debug] Slot " + i + " FAILED: Recipe wants " + expected + ", but found " + (stackInSlot.isEmpty() ? "Empty" : stackInSlot.getItem()));
                    return false;
                }
            }
        }

        //System.out.println("[Forge Debug] SUCCESS: Recipe matches perfectly!");
        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputItems;
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
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ForgeRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "forge";
    }

    public static class Serializer implements RecipeSerializer<ForgeRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(CrusadersOfFiction.MOD_ID,"forge");

        @Override
        public ForgeRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(11, Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size() && i < 11; i++) {
                JsonElement element = ingredients.get(i);

                // Check if the element is an empty object {}
                if (element.isJsonObject() && element.getAsJsonObject().size() == 0) {
                    inputs.set(i, Ingredient.EMPTY);
                } else {
                    inputs.set(i, Ingredient.fromJson(element));
                }
            }

            return new ForgeRecipe(id, output, inputs);
        }
        @Override
        public ForgeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(11, Ingredient.EMPTY);

            // Read the size we sent from toNetwork
            int size = buf.readInt();

            for (int i = 0; i < size; i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new ForgeRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ForgeRecipe recipe) {
            // Write the size so the client knows how many to read
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItem(recipe.output); // Use writeItem for cleaner code
        }
    }
    }

