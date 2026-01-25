package net.wolfygames7237.Crusadersoffiction;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.wolfygames7237.Crusadersoffiction.Item.ModCreativeModeTabs;
import net.wolfygames7237.Crusadersoffiction.Item.ModItem;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.wolfygames7237.Crusadersoffiction.enchantments.ModEnchantments;
import net.wolfygames7237.Crusadersoffiction.blocks.entity.ModBlockEntities;
import net.wolfygames7237.Crusadersoffiction.entity.ModEntities;
import net.wolfygames7237.Crusadersoffiction.entity.client.EnchantedSwordProjectile2Renderer;
import net.wolfygames7237.Crusadersoffiction.entity.client.EnchantedSwordProjectileRenderer;
import net.wolfygames7237.Crusadersoffiction.loot.ModLootModifiers;
import net.wolfygames7237.Crusadersoffiction.recipe.ModRecipeTypes;
import net.wolfygames7237.Crusadersoffiction.recipe.ModRecipes;
import net.wolfygames7237.Crusadersoffiction.screen.BlockCompressorScreen;
import net.wolfygames7237.Crusadersoffiction.screen.ForgeScreen;
import net.wolfygames7237.Crusadersoffiction.screen.ModMenuTypes;
import net.wolfygames7237.Crusadersoffiction.screen.StructureBuilderScreen;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CrusadersOfFiction.MOD_ID)
public class CrusadersOfFiction
{
    public static final String MOD_ID = "crusadersoffiction";
    private static final Logger LOGGER = LogUtils.getLogger();
       private static int messageID = 0;

    private static final Set<ResourceLocation> BLOCKED_RECIPES = Set.of(
            new ResourceLocation("minecraft", "diamond_sword"),
            new ResourceLocation("minecraft", "diamond_axe"),
            new ResourceLocation("minecraft", "diamond_pickaxe"),
            new ResourceLocation("minecraft", "diamond_hoe"),
            new ResourceLocation("minecraft", "diamond_shovel"),

            new ResourceLocation("minecraft", "iron_sword"),
            new ResourceLocation("minecraft", "iron_axe"),
            new ResourceLocation("minecraft", "iron_pickaxe"),
            new ResourceLocation("minecraft", "iron_hoe"),
            new ResourceLocation("minecraft", "iron_shovel"),

            new ResourceLocation("minecraft", "wooden_sword"),
            new ResourceLocation("minecraft", "wooden_axe"),
            new ResourceLocation("minecraft", "wooden_pickaxe"),
            new ResourceLocation("minecraft", "wooden_hoe"),
            new ResourceLocation("minecraft", "wooden_shovel"),

            new ResourceLocation("minecraft", "stone_sword"),
            new ResourceLocation("minecraft", "stone_axe"),
            new ResourceLocation("minecraft", "stone_pickaxe"),
            new ResourceLocation("minecraft", "stone_hoe"),
            new ResourceLocation("minecraft", "stone_shovel")
    );

    public CrusadersOfFiction(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModCreativeModeTabs.register(modEventBus);
        ModItem.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModEnchantments.register(modEventBus);
        ModEntities.register(modEventBus);
        ModRecipeTypes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            for (Block block : ForgeRegistries.BLOCKS) {
                if (block.defaultBlockState().is(BlockTags.LOGS)) {
                    // Since AT made 'properties' public:
                    block.properties.requiresCorrectToolForDrops();

                    // FORCE the internal field 'requiresCorrectToolForDrops' to true
                    // In some versions, the method above only sets a builder flag.
                    // You may need to use your AT for the field 'hasCollision' etc. if needed,
                    // but for drops, you usually need a Global Loot Modifier (GLM) for vanilla blocks.
                }
            }
        });
    }
    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        ++messageID;
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {

    }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new SimplePreparableReloadListener<Void>() {
            @Override
            protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
                return null;
            }

            @Override
            protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profiler) {
                RecipeManager recipeManager = event.getServerResources().getRecipeManager();

                try {
                    // Access the private "recipes" field: Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>>
                    Field recipesField = RecipeManager.class.getDeclaredField("recipes");
                    recipesField.setAccessible(true);

                    Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes =
                            (Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>>) recipesField.get(recipeManager);

                    // Create a new filtered copy
                    Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> filtered = new HashMap<>();
                    int removedCount = 0;

                    for (Map.Entry<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> typeEntry : recipes.entrySet()) {
                        Map<ResourceLocation, Recipe<?>> innerMap = new HashMap<>(typeEntry.getValue());
                        for (ResourceLocation blocked : BLOCKED_RECIPES) {
                            if (innerMap.remove(blocked) != null) {
                                removedCount++;
                                LOGGER.info("Removed recipe: {}", blocked);
                            }
                        }
                        filtered.put(typeEntry.getKey(), innerMap);
                    }

                    LOGGER.info("Total recipes removed: {}", removedCount);

                    // Replace the original map
                    recipesField.set(recipeManager, filtered);

                } catch (Exception e) {
                    LOGGER.error("Failed to modify RecipeManager recipes", e);
                }
            }
        });
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(() -> {

                MenuScreens.register(ModMenuTypes.FORGE_MENU.get(), ForgeScreen::new);
                MenuScreens.register(ModMenuTypes.STRUCTURE_BUILDER_MENU.get(), StructureBuilderScreen::new);
                MenuScreens.register(ModMenuTypes.BLOCK_COMPRESSOR_MENU.get(), BlockCompressorScreen::new);
                EntityRenderers.register(ModEntities.ENCHANTED_SWORD_PROJECTILE.get(), EnchantedSwordProjectileRenderer::new);
                EntityRenderers.register(ModEntities.ENCHANTED_SWORD_PROJECTILE2.get(), EnchantedSwordProjectile2Renderer::new);
            });
        }
    }

}
