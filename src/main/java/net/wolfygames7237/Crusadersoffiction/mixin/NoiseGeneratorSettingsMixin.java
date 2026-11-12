package net.wolfygames7237.Crusadersoffiction.mixin;

import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.wolfygames7237.Crusadersoffiction.worldgen.biome.ModBiomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Adds custom biome terrain layers to the surface rules used by noise-based terrain generation. */
@Mixin(NoiseGeneratorSettings.class)
public abstract class NoiseGeneratorSettingsMixin {
    @Unique
    private SurfaceRules.RuleSource crusaders$surfaceRulesWithBiomeLayers;

    @Inject(method = "surfaceRule", at = @At("RETURN"), cancellable = true)
    private void crusaders$addBiomeSurfaceLayers(CallbackInfoReturnable<SurfaceRules.RuleSource> callback) {
        SurfaceRules.RuleSource customized = crusaders$surfaceRulesWithBiomeLayers;
        if (customized == null) {
            synchronized (this) {
                customized = crusaders$surfaceRulesWithBiomeLayers;
                if (customized == null) {
                    customized = withBiomeSurfaceLayers(callback.getReturnValue());
                    crusaders$surfaceRulesWithBiomeLayers = customized;
                }
            }
        }
        callback.setReturnValue(customized);
    }

    @Unique
    private static SurfaceRules.RuleSource withBiomeSurfaceLayers(SurfaceRules.RuleSource vanillaRules) {
        List<ModBiomes.BiomeDefinition> biomes = ModBiomes.definitions().stream()
                .sorted(Comparator.comparingInt(definition -> definition.surfaceLayers().stream()
                        .mapToInt(ModBiomes.SurfaceLayer::priority).min().orElse(Integer.MAX_VALUE)))
                .toList();
        List<SurfaceRules.RuleSource> profileRules = new ArrayList<>();

        for (ModBiomes.BiomeDefinition biome : biomes) {
            List<ModBiomes.SurfaceLayer> layers = biome.surfaceLayers().stream()
                    .sorted(Comparator.comparingInt(ModBiomes.SurfaceLayer::priority))
                    .toList();
            for (ModBiomes.SurfaceLayer layer : layers) {
                SurfaceRules.RuleSource blockRule = SurfaceRules.state(layer.blockState());
                if (layer.minimumDepth() == 0 && layer.maximumDepth() == 0) {
                    profileRules.add(SurfaceRules.ifTrue(SurfaceRules.isBiome(biome.key()),
                            SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, blockRule)));
                    continue;
                }

                // stoneDepthCheck compares against 1 + offset, so these offsets map to zero-based depths.
                SurfaceRules.ConditionSource beforeMinimum = SurfaceRules.stoneDepthCheck(
                        layer.minimumDepth() - 1, false, CaveSurface.FLOOR);
                SurfaceRules.RuleSource depthRange;
                if (layer.toBiomeBottom()) {
                    // The biome condition below stops this layer where the biome itself ends.
                    depthRange = SurfaceRules.ifTrue(SurfaceRules.not(beforeMinimum), blockRule);
                } else {
                    SurfaceRules.ConditionSource throughMaximum = SurfaceRules.stoneDepthCheck(
                            layer.maximumDepth(), false, CaveSurface.FLOOR);
                    depthRange = SurfaceRules.ifTrue(throughMaximum,
                            SurfaceRules.ifTrue(SurfaceRules.not(beforeMinimum), blockRule));
                }
                profileRules.add(SurfaceRules.ifTrue(SurfaceRules.isBiome(biome.key()), depthRange));
            }
        }

        SurfaceRules.RuleSource customProfiles = SurfaceRules.sequence(
                profileRules.toArray(SurfaceRules.RuleSource[]::new));
        return SurfaceRules.sequence(customProfiles, vanillaRules);
    }
}
