package net.wolfygames7237.Crusadersoffiction.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;
import net.wolfygames7237.Crusadersoffiction.recipe.BlockCompressorRecipe;

import java.util.Arrays;

public class BlockCompressorRecipeCategory implements IRecipeCategory<BlockCompressorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CrusadersOfFiction.MOD_ID, "block_compressor");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CrusadersOfFiction.MOD_ID,
            "textures/gui/block_compressor_gui.png");

    public static final RecipeType<BlockCompressorRecipe> COMPRESSOR_TYPE =
            new RecipeType<>(UID, BlockCompressorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public BlockCompressorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 80);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BLOCK_COMPRESSOR.get()));
    }


    @Override
    public RecipeType<BlockCompressorRecipe> getRecipeType() {
        return COMPRESSOR_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Block Compressor");
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
    public void setRecipe(IRecipeLayoutBuilder builder, BlockCompressorRecipe recipe, IFocusGroup focuses) {
        if (recipe.getIngredients().isEmpty()) return;

        int required = recipe.getRequiredAmounts()[0];
        Ingredient ingredient = recipe.getIngredients().get(0);

        // INPUT
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 33)
                .addItemStacks(
                        Arrays.stream(ingredient.getItems())
                                .map(stack -> {
                                    ItemStack copy = stack.copy();
                                    copy.setCount(required);
                                    return copy;
                                })
                                .toList()
                );

        // OUTPUT
        builder.addSlot(RecipeIngredientRole.OUTPUT, 139, 33)
                .addItemStack(recipe.getResultItem(null));
    }

}
