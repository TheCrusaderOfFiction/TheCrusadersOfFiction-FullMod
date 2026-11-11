package net.wolfygames7237.Crusadersoffiction.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaJungleFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.registries.RegistryObject;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;

import java.util.List;


public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_WYSTERIUM_ORE_KEY = registerKey("nether_wysterium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> END_WYSTERIUM_ORE_KEY = registerKey("end_wysterium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MAHOGANY_KEY = registerKey("mahogany");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MAHOGANY_LARGE_KEY = registerKey("mahogany_large");
    public static final ResourceKey<ConfiguredFeature<?, ?>> JUNGLE_WATER_POOL_KEY = registerKey("jungle_water_pool");
    public static final ResourceKey<ConfiguredFeature<?, ?>> JUNGLE_GRASS_MUD_PATCH_KEY = registerKey("jungle_grass_mud_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BOREAL_KEY = registerKey("boreal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BOREAL_LARGE_KEY = registerKey("boreal_large");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrackReplacables = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endReplaceables = new BlockMatchTest(Blocks.END_STONE);

        register(context, NETHER_WYSTERIUM_ORE_KEY, Feature.ORE, new OreConfiguration(netherrackReplacables,
                ModBlocks.WYSTERIUM_ORE.get().defaultBlockState(), 2));
        register(context, END_WYSTERIUM_ORE_KEY, Feature.ORE, new OreConfiguration(endReplaceables,
                ModBlocks.WYSTERIUM_ORE.get().defaultBlockState(), 2));
        register(context, MAHOGANY_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.MAHOGANY_LOG.get()),
                new StraightTrunkPlacer(5, 4, 3),
                BlockStateProvider.simple(ModBlocks.MAHOGANY_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(2), 3),
                new TwoLayersFeatureSize(1, 0, 2)).dirt(BlockStateProvider.simple(Blocks.MUD)).build());
        register(context, MAHOGANY_LARGE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.MAHOGANY_LOG.get()),
                new MegaJungleTrunkPlacer(10, 2, 19),
                BlockStateProvider.simple(ModBlocks.MAHOGANY_LEAVES.get()),
                new MegaJungleFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 2),
                new TwoLayersFeatureSize(1, 1, 2))
                .decorators(List.of(new CocoaDecorator(0.2F), new LeaveVineDecorator(0.25F),
                        new TrunkVineDecorator()))
                .dirt(BlockStateProvider.simple(Blocks.MUD))
                .build());
        register(context, JUNGLE_WATER_POOL_KEY, Feature.LAKE,
                new LakeFeature.Configuration(BlockStateProvider.simple(Blocks.WATER),
                        BlockStateProvider.simple(Blocks.STONE)));
        register(context, JUNGLE_GRASS_MUD_PATCH_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(8, 4, 2,
                        PlacementUtils.filtered(Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(
                                        ModBlocks.JUNGLE_GRASS_BLOCK.get())),
                                BlockPredicate.matchesBlocks(Blocks.MUD))));

        register(context, BOREAL_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.BOREAL_LOG.get()),
                new StraightTrunkPlacer(5, 4, 3),
                BlockStateProvider.simple(ModBlocks.BOREAL_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(2), 3),
                new TwoLayersFeatureSize(1, 0, 2)).dirt(BlockStateProvider.simple(Blocks.SNOW_BLOCK)).build());
        register(context, BOREAL_LARGE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.BOREAL_LOG.get()),
                new MegaJungleTrunkPlacer(10, 2, 19),
                BlockStateProvider.simple(ModBlocks.BOREAL_LEAVES.get()),
                new MegaJungleFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 2),
                new TwoLayersFeatureSize(1, 1, 2))
                .decorators(List.of(new CocoaDecorator(0.2F), new LeaveVineDecorator(0.25F),
                        new TrunkVineDecorator()))
                .dirt(BlockStateProvider.simple(Blocks.SNOW_BLOCK))
                .build());
    }



    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(CrusadersOfFiction.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
