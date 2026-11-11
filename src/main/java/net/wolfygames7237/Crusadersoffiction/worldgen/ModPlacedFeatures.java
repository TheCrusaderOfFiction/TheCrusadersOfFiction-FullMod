package net.wolfygames7237.Crusadersoffiction.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> NETHER_WYSTERIUM_ORE_PLACED_KEY = registerKey("nether_wysterium_ore_placed");
    public static final ResourceKey<PlacedFeature> END_WYSTERIUM_ORE_PLACED_KEY = registerKey("end_wysterium_ore_placed");
    public static final ResourceKey<PlacedFeature> MAHOGANY_PLACED_KEY = registerKey("mahogany_placed");
    public static final ResourceKey<PlacedFeature> MAHOGANY_LARGE_PLACED_KEY = registerKey("mahogany_large_placed");
    public static final ResourceKey<PlacedFeature> JUNGLE_SURFACE_WATER_POOL_KEY = registerKey("jungle_surface_water_pool");
    public static final ResourceKey<PlacedFeature> JUNGLE_UNDERGROUND_WATER_POOL_KEY = registerKey("jungle_underground_water_pool");
    public static final ResourceKey<PlacedFeature> JUNGLE_GRASS_MUD_PATCH_PLACED_KEY = registerKey("jungle_grass_mud_patch_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, NETHER_WYSTERIUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.NETHER_WYSTERIUM_ORE_KEY),
                ModOrePlacement.commonOrePlacement(1,
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(30))));
        register(context, END_WYSTERIUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.END_WYSTERIUM_ORE_KEY),
                ModOrePlacement.rareOrePlacement(3,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(50))));
        register(context, MAHOGANY_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.MAHOGANY_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2),
                        ModBlocks.MAHOGANY_SAPLING.get()));
        register(context, MAHOGANY_LARGE_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.MAHOGANY_LARGE_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2),
                        ModBlocks.MAHOGANY_SAPLING.get()));
        register(context, JUNGLE_SURFACE_WATER_POOL_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.JUNGLE_WATER_POOL_KEY),
                List.of(CountPlacement.of(2), InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome()));
        register(context, JUNGLE_UNDERGROUND_WATER_POOL_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.JUNGLE_WATER_POOL_KEY),
                List.of(CountPlacement.of(3), InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(48)),
                        BiomeFilter.biome()));
        register(context, JUNGLE_GRASS_MUD_PATCH_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.JUNGLE_GRASS_MUD_PATCH_KEY),
                List.of(CountPlacement.of(2), InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(12), VerticalAnchor.absolute(60)),
                        BiomeFilter.biome()));

    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(CrusadersOfFiction.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
