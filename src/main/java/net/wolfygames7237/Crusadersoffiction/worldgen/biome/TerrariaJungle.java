package net.wolfygames7237.Crusadersoffiction.worldgen.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;

import java.util.List;
import java.util.Optional;


/** Copy this class as a starting point when creating another biome. */
public final class TerrariaJungle {
    public static final ResourceKey<Biome> KEY = ResourceKey.create(
            Registries.BIOME, new ResourceLocation(CrusadersOfFiction.MOD_ID, "terraria_jungle"));

    // Narrower climate spans make this biome's selection area smaller/rarer;
    // wider spans make it cover more of the Overworld climate map.
    public static final Climate.ParameterPoint CLIMATE = Climate.parameters(
            Climate.Parameter.span(0.2F, 1.0F), // warm
            Climate.Parameter.span(0.3F, 1.0F), // humid
            Climate.Parameter.span(0.03F, 1.0F), // inland
            Climate.Parameter.span(-0.3F, 0.5F), // rolling terrain
            Climate.Parameter.span(-0.2F, 0.2F), // retained for vanilla climate-map registration; custom Y bounds control biome thickness
            Climate.Parameter.span(-1.0F, 1.0F),
            0.0F);

    /** One entry per contiguous terrain layer; depths are measured down from the terrain surface. */
    public static final List<ModBiomes.SurfaceLayer> SURFACE_LAYERS = List.of(
            new ModBiomes.SurfaceLayer(ModBlocks.JUNGLE_GRASS_BLOCK.get().defaultBlockState(), 0, 0, 100),
            ModBiomes.SurfaceLayer.toBiomeBottom(Blocks.MUD.defaultBlockState(), 1, 10)
    );
    /** Post-generation jungle spread: nearby jungle grass blocks can extend the biome into this chunk. */
    public static final List<ModBiomes.BlockCountBiomeUpdate> POST_GENERATION_UPDATES = List.of(
            new ModBiomes.BlockCountBiomeUpdate(KEY,
                    List.of(ModBlocks.JUNGLE_GRASS_BLOCK.get()),
                    64, 16, 16,
                    ModBiomes.WORLD_BOTTOM_Y, ModBiomes.WORLD_TOP_Y,
                    ModBiomes.WORLD_BOTTOM_Y, ModBiomes.WORLD_TOP_Y)
    );

    /** Inclusive world Y band available for the biome's vertical placement. */
    public static final int MINIMUM_WORLD_Y = -64;
    public static final int MAXIMUM_WORLD_Y = 200;
    /** Horizontal island diameters and vertical thickness, all in blocks. */
    public static final int MINIMUM_SIZE = 256;
    public static final int MAXIMUM_SIZE = 1024;
    public static final int MINIMUM_HEIGHT = 250;
    public static final int MAXIMUM_HEIGHT = 250;
    /** Every generated vertical span intersects this inclusive, quart-grid-aligned Y band. */
    public static final int GUARANTEED_MINIMUM_WORLD_Y = 0;
    public static final int GUARANTEED_MAXIMUM_WORLD_Y = 0;
    /** Higher values win when custom biome climate ranges overlap. */
    public static final int BIOME_PRIORITY = 10;
    /** Optional underground biome replacement; use Optional.empty() to disable. */
    public static final Optional<ModBiomes.BiomeTransition> TRANSITION_BELOW_Y = Optional.of(
            new ModBiomes.BiomeTransition(30, TerrariaUndergroundJungle.KEY));
    public static final boolean MINIMUM_AT_WORLD_BOTTOM = false;
    public static final boolean MAXIMUM_AT_WORLD_TOP = false;
    public static final boolean TRANSITION_ONLY = false;
    public static final boolean SNAP_TO_SURFACE = true;
    public static final boolean SURFACE_TO_WORLD_BOTTOM = true;
    public static final List<ResourceKey<Biome>> REPLACES_BIOMES = List.of(
            Biomes.JUNGLE,
            Biomes.SPARSE_JUNGLE,
            Biomes.BAMBOO_JUNGLE
    );

    private TerrariaJungle() {}

    public static ModBiomes.BiomeDefinition definition() {
        return new ModBiomes.BiomeDefinition(KEY, CLIMATE, TerrariaJungle::create,
                SURFACE_LAYERS, MINIMUM_WORLD_Y, MAXIMUM_WORLD_Y,
                MINIMUM_SIZE, MAXIMUM_SIZE, MINIMUM_HEIGHT, MAXIMUM_HEIGHT, BIOME_PRIORITY,
                GUARANTEED_MINIMUM_WORLD_Y, GUARANTEED_MAXIMUM_WORLD_Y, TRANSITION_BELOW_Y,
                MINIMUM_AT_WORLD_BOTTOM, MAXIMUM_AT_WORLD_TOP, TRANSITION_ONLY, SNAP_TO_SURFACE,
                SURFACE_TO_WORLD_BOTTOM, REPLACES_BIOMES);
    }

    public static Biome create(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(
                placedFeatures, context.lookup(Registries.CONFIGURED_CARVER));
        generation.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        generation.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        generation.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        generation.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND);
        generation.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND);
        generation.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
        generation.addFeature(GenerationStep.Decoration.LAKES,
                net.wolfygames7237.Crusadersoffiction.worldgen.ModPlacedFeatures.JUNGLE_SURFACE_WATER_POOL_KEY);
        generation.addFeature(GenerationStep.Decoration.LAKES,
                net.wolfygames7237.Crusadersoffiction.worldgen.ModPlacedFeatures.JUNGLE_UNDERGROUND_WATER_POOL_KEY);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                net.wolfygames7237.Crusadersoffiction.worldgen.ModPlacedFeatures.JUNGLE_GRASS_MUD_PATCH_PLACED_KEY);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                net.wolfygames7237.Crusadersoffiction.worldgen.ModPlacedFeatures.MAHOGANY_PLACED_KEY);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                net.wolfygames7237.Crusadersoffiction.worldgen.ModPlacedFeatures.MAHOGANY_LARGE_PLACED_KEY);
        net.minecraft.data.worldgen.BiomeDefaultFeatures.addJungleTrees(generation);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PIG, 10, 2, 4));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.COW, 8, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 80, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 80, 2, 4));

        BiomeSpecialEffects effects = new BiomeSpecialEffects.Builder()
                .fogColor(0xC0D8FF)
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .skyColor(0x78A7FF)
                .foliageColorOverride(0x55AA55)
                .grassColorOverride(0x7FB238)
                .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0D))
                .build();

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(0.8F)
                .downfall(0.9F)
                .specialEffects(effects)
                .mobSpawnSettings(spawns.build())
                .generationSettings(generation.build())
                .build();
    }
}
