package net.wolfygames7237.Crusadersoffiction.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.recipe.ForgeRecipe;
import net.wolfygames7237.Crusadersoffiction.screen.ForgeScreen;

import java.util.List;

@JeiPlugin
public class JEICrusadersOfFictionPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CrusadersOfFiction.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ForgeRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<ForgeRecipe> empoweringRecipes = recipeManager.getAllRecipesFor(ForgeRecipe.Type.INSTANCE);
        registration.addRecipes(ForgeRecipeCategory.FORGE_TYPE, empoweringRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ForgeScreen.class, 100, 33, 30, 10,
                ForgeRecipeCategory.FORGE_TYPE);
    }
}
