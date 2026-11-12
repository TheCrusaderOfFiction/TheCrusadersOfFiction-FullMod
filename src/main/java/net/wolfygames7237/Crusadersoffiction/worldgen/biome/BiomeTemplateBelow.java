package net.wolfygames7237.Crusadersoffiction.worldgen.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;

import java.util.List;
import java.util.Optional;

/** A cave biome template that is selected only by a parent biome's below-Y transition. */
public final class BiomeTemplateBelow {
    public static final ResourceKey<Biome> KEY = ResourceKey.create(
            Registries.BIOME, new ResourceLocation(CrusadersOfFiction.MOD_ID, "biome_template_below"));

    // Kept aligned with the parent climate point so it is available in the multi-noise source.
    // MultiNoiseBiomeSourceMixin prevents this biome from spawning independently.
    public static final Climate.ParameterPoint CLIMATE = Climate.parameters(
            Climate.Parameter.span(0.2F, 1.0F),
            Climate.Parameter.span(0.3F, 1.0F),
            Climate.Parameter.span(0.03F, 1.0F),
            Climate.Parameter.span(-0.3F, 0.5F),
            Climate.Parameter.span(-0.2F, 0.2F), // custom Y bounds control thickness; depth does not clip it
            Climate.Parameter.span(-1.0F, 1.0F),
            0.0F);

    public static final List<ModBiomes.SurfaceLayer> SURFACE_LAYERS = List.of(
            new ModBiomes.SurfaceLayer(Blocks.DEEPSLATE.defaultBlockState(), 0, 0, 100),
            new ModBiomes.SurfaceLayer(Blocks.TUFF.defaultBlockState(), 1, 3, 10)
    );

    // The world-bound flags make this biome valid from the Overworld bottom through world top.
    // Its parent transition still limits it to the parent's regions below the configured cutoff.
    public static final int MINIMUM_WORLD_Y = 0;
    public static final int MAXIMUM_WORLD_Y = 0;
    public static final int MINIMUM_SIZE = 256;
    public static final int MAXIMUM_SIZE = 1024;
    public static final int MINIMUM_HEIGHT = 1;
    public static final int MAXIMUM_HEIGHT = 1;
    public static final int BIOME_PRIORITY = 0;
    public static final int GUARANTEED_MINIMUM_WORLD_Y = ModBiomes.WORLD_BOTTOM_Y;
    public static final int GUARANTEED_MAXIMUM_WORLD_Y = ModBiomes.WORLD_TOP_Y;
    public static final Optional<ModBiomes.BiomeTransition> TRANSITION_BELOW_Y = Optional.empty();
    public static final boolean MINIMUM_AT_WORLD_BOTTOM = true;
    public static final boolean MAXIMUM_AT_WORLD_TOP = true;
    public static final boolean TRANSITION_ONLY = true;
    public static final boolean SNAP_TO_SURFACE = false;
    public static final boolean SURFACE_TO_WORLD_BOTTOM = false;
    public static final List<ResourceKey<Biome>> REPLACES_BIOMES = List.of();

    private BiomeTemplateBelow() {}

    public static ModBiomes.BiomeDefinition definition() {
        return new ModBiomes.BiomeDefinition(KEY, CLIMATE, BiomeTemplateBelow::create,
                SURFACE_LAYERS, MINIMUM_WORLD_Y, MAXIMUM_WORLD_Y,
                MINIMUM_SIZE, MAXIMUM_SIZE, MINIMUM_HEIGHT, MAXIMUM_HEIGHT, BIOME_PRIORITY,
                GUARANTEED_MINIMUM_WORLD_Y, GUARANTEED_MAXIMUM_WORLD_Y, TRANSITION_BELOW_Y,
                MINIMUM_AT_WORLD_BOTTOM, MAXIMUM_AT_WORLD_TOP, TRANSITION_ONLY, SNAP_TO_SURFACE,
                SURFACE_TO_WORLD_BOTTOM, REPLACES_BIOMES);
    }

    public static Biome create(BootstapContext<Biome> context) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(
                context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
        generation.addCarver(GenerationStep.Carving.AIR, net.minecraft.data.worldgen.Carvers.CAVE);
        generation.addCarver(GenerationStep.Carving.AIR,
                net.minecraft.data.worldgen.Carvers.CAVE_EXTRA_UNDERGROUND);
        generation.addCarver(GenerationStep.Carving.AIR, net.minecraft.data.worldgen.Carvers.CANYON);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 1, 4));

        BiomeSpecialEffects effects = new BiomeSpecialEffects.Builder()
                .fogColor(0x15121C)
                .waterColor(0x3F5F68)
                .waterFogColor(0x171D24)
                .skyColor(0x303040)
                .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0D))
                .build();

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(effects)
                .mobSpawnSettings(spawns.build())
                .generationSettings(generation.build())
                .build();
    }
}
