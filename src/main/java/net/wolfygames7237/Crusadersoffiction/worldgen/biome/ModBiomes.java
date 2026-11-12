package net.wolfygames7237.Crusadersoffiction.worldgen.biome;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/** Central biome catalog and dynamic-registry bootstrap. Add each biome class here. */
public final class ModBiomes {
    private static final Map<ResourceKey<Biome>, Supplier<BiomeDefinition>> BIOME_TEMPLATES = Map.of(
            TerrariaJungle.KEY, TerrariaJungle::definition,
            TerrariaUndergroundJungle.KEY, TerrariaUndergroundJungle::definition
    );

    private static final List<BiomeDefinition> BIOMES = List.of(
            new BiomeDefinition(TerrariaJungle.KEY),
            new BiomeDefinition(TerrariaUndergroundJungle.KEY)
    );
    private static final List<BlockCountBiomeUpdate> POST_GENERATION_UPDATES = List.of(
            TerrariaJungle.POST_GENERATION_UPDATES,
            TerrariaUndergroundJungle.POST_GENERATION_UPDATES
    ).stream().flatMap(List::stream).toList();

    private ModBiomes() {}

    public static List<BiomeDefinition> definitions() {
        return BIOMES;
    }

    public static List<BlockCountBiomeUpdate> postGenerationUpdates() {
        return POST_GENERATION_UPDATES;
    }

    public static BiomeDefinition definition(ResourceKey<Biome> key) {
        return BIOMES.stream().filter(biome -> biome.key().equals(key)).findFirst().orElse(null);
    }

    public static BiomeDefinition replacementFor(ResourceKey<Biome> vanillaBiome) {
        if (vanillaBiome == null) return null;
        return BIOMES.stream()
                .filter(biome -> !biome.transitionOnly())
                .filter(biome -> biome.replacesBiomes().contains(vanillaBiome))
                .max(java.util.Comparator.comparingInt(BiomeDefinition::priority))
                .orElse(null);
    }

    public static void bootstrap(BootstapContext<Biome> context) {
        for (BiomeDefinition definition : BIOMES) {
            context.register(definition.key(), definition.factory().apply(context));
        }
    }

    public record SurfaceLayer(BlockState blockState, int minimumDepth, int maximumDepth,
                               int priority, boolean toBiomeBottom) {
        public SurfaceLayer(BlockState blockState, int minimumDepth, int maximumDepth, int priority) {
            this(blockState, minimumDepth, maximumDepth, priority, false);
        }

        public static SurfaceLayer toBiomeBottom(BlockState blockState, int minimumDepth, int priority) {
            return new SurfaceLayer(blockState, minimumDepth, Integer.MAX_VALUE, priority, true);
        }

        public SurfaceLayer {
            Objects.requireNonNull(blockState, "blockState");
            if (minimumDepth < 0 || (!toBiomeBottom && maximumDepth < minimumDepth)) {
                throw new IllegalArgumentException("Surface layer depths must be nonnegative and ordered");
            }
        }
    }

    public record BiomeTransition(int belowWorldY, ResourceKey<Biome> targetBiome) {
        public BiomeTransition {
            Objects.requireNonNull(targetBiome, "targetBiome");
        }
    }

    /** Reassigns biome columns after chunk generation when nearby blocks meet a count threshold. */
    public record BlockCountBiomeUpdate(ResourceKey<Biome> targetBiome, List<Block> countedBlocks,
                                        int minimumBlockCount, int horizontalRadius, int verticalRadius,
                                        int countMinimumWorldY, int countMaximumWorldY,
                                        int updateMinimumWorldY, int updateMaximumWorldY) {
        public BlockCountBiomeUpdate {
            Objects.requireNonNull(targetBiome, "targetBiome");
            countedBlocks = List.copyOf(countedBlocks);
            if (countedBlocks.isEmpty() || minimumBlockCount < 1 || horizontalRadius < 0
                    || horizontalRadius > 32 || verticalRadius < 0 || verticalRadius > 128
                    || countMinimumWorldY > countMaximumWorldY
                    || updateMinimumWorldY > updateMaximumWorldY) {
                throw new IllegalArgumentException("Block-count biome update settings are invalid");
            }
        }
    }

    /** Vanilla 1.20.1 Overworld build limits, inclusive at the upper end. */
    public static final int WORLD_BOTTOM_Y = -64;
    public static final int WORLD_TOP_Y = 319;

    public record BiomeDefinition(ResourceKey<Biome> key, Climate.ParameterPoint climate,
                                  Function<BootstapContext<Biome>, Biome> factory,
                                  List<SurfaceLayer> surfaceLayers,
                                  int minimumWorldY, int maximumWorldY,
                                  int minimumSize, int maximumSize,
                                  int minimumHeight, int maximumHeight, int priority,
                                  int guaranteedMinimumWorldY, int guaranteedMaximumWorldY,
                                  Optional<BiomeTransition> transitionBelowY,
                                  boolean minimumAtWorldBottom, boolean maximumAtWorldTop,
                                  boolean transitionOnly,
                                  boolean snapToSurface,
                                  boolean surfaceToWorldBottom,
                                  List<ResourceKey<Biome>> replacesBiomes) {
        public BiomeDefinition(ResourceKey<Biome> key) {
            this(resolveTemplate(key));
        }

        private BiomeDefinition(BiomeDefinition definition) {
            this(definition.key(), definition.climate(), definition.factory(), definition.surfaceLayers(),
                    definition.minimumWorldY(), definition.maximumWorldY(),
                    definition.minimumSize(), definition.maximumSize(),
                    definition.minimumHeight(), definition.maximumHeight(), definition.priority(),
                    definition.guaranteedMinimumWorldY(), definition.guaranteedMaximumWorldY(),
                    definition.transitionBelowY(), definition.minimumAtWorldBottom(),
                    definition.maximumAtWorldTop(), definition.transitionOnly(), definition.snapToSurface(),
                    definition.surfaceToWorldBottom(), definition.replacesBiomes());
        }

        private static BiomeDefinition resolveTemplate(ResourceKey<Biome> key) {
            Supplier<BiomeDefinition> template = BIOME_TEMPLATES.get(key);
            if (template == null) {
                throw new IllegalArgumentException("No biome template is registered for " + key.location());
            }
            return template.get();
        }

        public BiomeDefinition {
            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(climate, "climate");
            Objects.requireNonNull(factory, "factory");
            Objects.requireNonNull(transitionBelowY, "transitionBelowY");
            surfaceLayers = List.copyOf(surfaceLayers);
            replacesBiomes = List.copyOf(replacesBiomes);
            if (minimumSize < 1 || maximumSize < minimumSize) {
                throw new IllegalArgumentException("Biome size limits must be positive and ordered");
            }
            if (!surfaceToWorldBottom && (minimumHeight < 1 || maximumHeight < minimumHeight)) {
                throw new IllegalArgumentException("Biome height limits must be positive and ordered");
            }
            int effectiveMinimumY = minimumAtWorldBottom ? WORLD_BOTTOM_Y : minimumWorldY;
            int effectiveMaximumY = maximumAtWorldTop ? WORLD_TOP_Y : maximumWorldY;
            if (!surfaceToWorldBottom && (effectiveMinimumY > effectiveMaximumY
                    || (long) effectiveMaximumY - effectiveMinimumY + 1 < maximumHeight)) {
                throw new IllegalArgumentException("Biome world Y limits must fit the configured height");
            }
            int firstGuaranteedSampleY = Math.floorDiv(guaranteedMinimumWorldY + 3, 4) * 4;
            int lastGuaranteedSampleY = Math.floorDiv(guaranteedMaximumWorldY, 4) * 4;
            if (!surfaceToWorldBottom && (guaranteedMinimumWorldY < effectiveMinimumY
                    || guaranteedMaximumWorldY > effectiveMaximumY
                    || guaranteedMinimumWorldY > guaranteedMaximumWorldY
                    || firstGuaranteedSampleY > lastGuaranteedSampleY
                    || (long) effectiveMaximumY - effectiveMinimumY + 1 < minimumHeight)) {
                throw new IllegalArgumentException("Guaranteed biome Y range must fit inside the world Y limits");
            }
            int lowestAllowedBottom = Math.max(effectiveMinimumY, firstGuaranteedSampleY - minimumHeight + 1);
            int highestAllowedBottom = Math.min(effectiveMaximumY - minimumHeight + 1, lastGuaranteedSampleY);
            if (!surfaceToWorldBottom && lowestAllowedBottom > highestAllowedBottom) {
                throw new IllegalArgumentException("Biome height cannot reach its guaranteed Y range");
            }
        }

        public int effectiveMinimumWorldY() {
            return surfaceToWorldBottom || minimumAtWorldBottom ? WORLD_BOTTOM_Y : minimumWorldY;
        }

        public int effectiveMaximumWorldY() {
            return surfaceToWorldBottom || maximumAtWorldTop ? WORLD_TOP_Y : maximumWorldY;
        }

        public boolean spansEntireWorldY() {
            return minimumAtWorldBottom && maximumAtWorldTop;
        }
    }
}
