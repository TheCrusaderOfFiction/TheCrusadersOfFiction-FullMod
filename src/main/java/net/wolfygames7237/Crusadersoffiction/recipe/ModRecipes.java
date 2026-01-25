package net.wolfygames7237.Crusadersoffiction.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CrusadersOfFiction.MOD_ID);

    public static final RegistryObject<RecipeSerializer<ForgeRecipe>> FORGE_SERIALIZER =
            SERIALIZERS.register("forge", () -> ForgeRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<StructureBuilderRecipe>> STRUCTURE_BUILDER_SERIALIZER =
            SERIALIZERS.register("structure", () -> StructureBuilderRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<BlockCompressorRecipe>> BLOCK_COMPRESSOR_SERIALIZER =
            SERIALIZERS.register("compressor", () -> BlockCompressorRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
