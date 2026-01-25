package net.wolfygames7237.Crusadersoffiction.recipe;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;

public class ModRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, CrusadersOfFiction.MOD_ID);

    public static final RegistryObject<RecipeType<StructureBuilderRecipe>> STRUCTURE =
            RECIPE_TYPES.register("structure",
                    () -> new RecipeType<>() {
                        @Override
                        public String toString() {
                            return CrusadersOfFiction.MOD_ID + ":structure";
                        }
                    });
    public static final RegistryObject<RecipeType<BlockCompressorRecipe>> COMPRESSOR =
            RECIPE_TYPES.register("compressor",
                    () -> new RecipeType<>() {
                        @Override
                        public String toString() {
                            return CrusadersOfFiction.MOD_ID + ":compressor";
                        }
                    });

    public static void register(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }
}
