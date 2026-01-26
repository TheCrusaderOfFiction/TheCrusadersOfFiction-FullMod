package net.wolfygames7237.Crusadersoffiction.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;
import net.wolfygames7237.Crusadersoffiction.recipe.StructureBuilderRecipe;

import java.util.Arrays;

public class StructureBuilderRecipeCategory implements IRecipeCategory<StructureBuilderRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CrusadersOfFiction.MOD_ID, "structure_builder");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CrusadersOfFiction.MOD_ID,
            "textures/gui/structure_builder_gui.png");

    public static final RecipeType<StructureBuilderRecipe> STRUCTURE_TYPE =
            new RecipeType<>(UID, StructureBuilderRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public StructureBuilderRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 80);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.STRUCTURE_BUILDER.get()));
    }


    @Override
    public RecipeType<StructureBuilderRecipe> getRecipeType() {
        return STRUCTURE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Structure Builder");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, StructureBuilderRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int[] amounts = recipe.getRequiredAmounts();

        if (ingredients.isEmpty()) return;

        int xStart = 7;
        int yStart = 6;

        // 4 rows × 5 columns = 20 input slots
        for (int i = 0; i < StructureBuilderRecipe.INPUT_COUNT; i++) {
            Ingredient ingredient = ingredients.get(i);
            int count = amounts[i];

            int row = i / 5;
            int col = i % 5;
            int x = xStart + col * 18;
            int y = yStart + row * 18;

            builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                    .addItemStacks(
                            Arrays.stream(ingredient.getItems())
                                    .map(stack -> {
                                        ItemStack copy = stack.copy();
                                        copy.setCount(count);
                                        return copy;
                                    })
                                    .toList()
                    );
        }

        // Output slot
        builder.addSlot(RecipeIngredientRole.OUTPUT, 138, 32)
                .addItemStack(recipe.getResultItem(null));
    }
}
