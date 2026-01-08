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
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;
import net.wolfygames7237.Crusadersoffiction.recipe.ForgeRecipe;

public class ForgeRecipeCategory implements IRecipeCategory<ForgeRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CrusadersOfFiction.MOD_ID, "forge");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CrusadersOfFiction.MOD_ID,
            "textures/gui/forge_gui.png");

    public static final RecipeType<ForgeRecipe> FORGE_TYPE =
            new RecipeType<>(UID, ForgeRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ForgeRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 80);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FORGE.get()));
    }


    @Override
    public RecipeType<ForgeRecipe> getRecipeType() {
        return FORGE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Forge");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ForgeRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 34, 14).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 14).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 70, 14).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 34, 32).addIngredients(recipe.getIngredients().get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 32).addIngredients(recipe.getIngredients().get(4));
        builder.addSlot(RecipeIngredientRole.INPUT, 70, 32).addIngredients(recipe.getIngredients().get(5));
        builder.addSlot(RecipeIngredientRole.INPUT, 34, 50).addIngredients(recipe.getIngredients().get(6));
        builder.addSlot(RecipeIngredientRole.INPUT, 52, 50).addIngredients(recipe.getIngredients().get(7));
        builder.addSlot(RecipeIngredientRole.INPUT, 70, 50).addIngredients(recipe.getIngredients().get(8));
        builder.addSlot(RecipeIngredientRole.INPUT, 105, 59).addIngredients(recipe.getIngredients().get(9));
        builder.addSlot(RecipeIngredientRole.INPUT, 105, 10).addIngredients(recipe.getIngredients().get(10));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 137, 33).addItemStack(recipe.getResultItem(null));
    }

}
