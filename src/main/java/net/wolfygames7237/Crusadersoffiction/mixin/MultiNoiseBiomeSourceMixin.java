package net.wolfygames7237.Crusadersoffiction.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.wolfygames7237.Crusadersoffiction.worldgen.biome.ModBiomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/** Enforces custom-biome placement bounds and priority after climate selection. */
@Mixin(MultiNoiseBiomeSource.class)
public abstract class MultiNoiseBiomeSourceMixin {
    @Shadow
    private Climate.ParameterList<Holder<Biome>> parameters() {
        throw new AssertionError();
    }

    @Unique
    private volatile Climate.ParameterList<Holder<Biome>> crusaders$vanillaFallback;

    @Inject(method = "getNoiseBiome", at = @At("RETURN"), cancellable = true)
    private void crusaders$limitCustomBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler,
                                             CallbackInfoReturnable<Holder<Biome>> callback) {
        int worldY = quartY * 4;
        int worldX = quartX * 4;
        int worldZ = quartZ * 4;
        Climate.TargetPoint target = sampler.sample(quartX, quartY, quartZ);

        Holder<Biome> vanillaSelection = vanillaFallback().findValue(target);
        ResourceKey<Biome> vanillaKey = vanillaSelection.unwrapKey().orElse(null);
        ModBiomes.BiomeDefinition replacement = ModBiomes.replacementFor(vanillaKey);
        if (replacement != null) {
            java.util.Optional<Holder<Biome>> transition = transitionHolder(replacement, worldY);
            if (transition.isPresent()) {
                callback.setReturnValue(transition.orElseThrow());
                return;
            }

            boolean surfaceAnchored = replacement.snapToSurface() || replacement.surfaceToWorldBottom();
            boolean validY = surfaceAnchored
                    ? snappedBiomeContainsY(worldX, worldY, worldZ, quartX, quartY, quartZ, sampler, replacement)
                    : insideBiomeHeight(worldX, worldY, worldZ, replacement);
            if (validY) {
                holderFor(replacement.key()).ifPresent(callback::setReturnValue);
            } else {
                callback.setReturnValue(vanillaSelection);
            }
            return;
        }

        ModBiomes.BiomeDefinition climateWinner = highestPriorityClimateMatch(
                worldX, worldY, worldZ, quartX, quartZ, sampler);
        if (climateWinner != null) {
            java.util.Optional<Holder<Biome>> transition = transitionHolder(climateWinner, worldY);
            if (transition.isPresent()) {
                callback.setReturnValue(transition.orElseThrow());
            } else {
                holderFor(climateWinner.key()).ifPresent(callback::setReturnValue);
            }
            return;
        }

        ResourceKey<Biome> selectedKey = callback.getReturnValue().unwrapKey().orElse(null);
        if (selectedKey == null) return;

        ModBiomes.BiomeDefinition definition = ModBiomes.definition(selectedKey);
        if (definition == null) return;
        if (!definition.replacesBiomes().isEmpty()) {
            callback.setReturnValue(vanillaSelection);
            return;
        }

        if (definition.transitionOnly() || !insideBiomeArea(worldX, worldZ, definition)) {
            callback.setReturnValue(vanillaFallback().findValue(target));
            return;
        }

        java.util.Optional<Holder<Biome>> transition = transitionHolder(definition, worldY);
        if (transition.isPresent()) {
            callback.setReturnValue(transition.orElseThrow());
            return;
        }

        boolean snappedToSurface = definition.snapToSurface()
                || definition.surfaceToWorldBottom();
        snappedToSurface = snappedToSurface
                && snappedBiomeContainsY(worldX, worldY, worldZ, quartX, quartY, quartZ, sampler, definition);
        if ((definition.snapToSurface() || definition.surfaceToWorldBottom()) ? !snappedToSurface
                : !insideBiomeHeight(worldX, worldY, worldZ, definition)) {
            callback.setReturnValue(vanillaFallback().findValue(target));
            return;
        }

    }

    @Unique
    private ModBiomes.BiomeDefinition highestPriorityClimateMatch(int x, int y, int z,
                                                                  int quartX, int quartZ,
                                                                  Climate.Sampler sampler) {
        ModBiomes.BiomeDefinition winner = null;
        for (ModBiomes.BiomeDefinition candidate : ModBiomes.definitions()) {
            // Transition-only definitions are registry targets, never independent biome choices.
            if (candidate.transitionOnly() || !candidate.replacesBiomes().isEmpty()) continue;
            boolean transitionsBelow = transitionTarget(candidate, y) != null;
            boolean canSpawnHere;
            if (candidate.surfaceToWorldBottom()) {
                canSpawnHere = snappedBiomeContainsY(x, y, z, quartX, Math.floorDiv(y, 4), quartZ,
                        sampler, candidate);
            } else if (transitionsBelow || candidate.spansEntireWorldY()) {
                canSpawnHere = spawnsInGuaranteedBand(candidate, x, z, quartX, quartZ, sampler);
            } else {
                boolean snapsToSurface = candidate.snapToSurface()
                        && snappedBiomeContainsY(x, y, z, quartX, Math.floorDiv(y, 4), quartZ, sampler, candidate);
                // Establish the biome's horizontal/climate identity in its guaranteed
                // band, then use its configured vertical span at this Y. Rechecking
                // climate noise at every Y can let vanilla biomes cut off a tall biome.
                canSpawnHere = spawnsInGuaranteedBand(candidate, x, z, quartX, quartZ, sampler)
                        && (candidate.snapToSurface()
                        ? snapsToSurface
                        : insideBiomeHeight(x, y, z, candidate));
            }
            if (insideBiomeArea(x, z, candidate)
                    && canSpawnHere
                    && (winner == null || candidate.priority() > winner.priority())) {
                winner = candidate;
            }
        }
        return winner;
    }

    @Unique
    private boolean spawnsInGuaranteedBand(ModBiomes.BiomeDefinition biome, int x, int z,
                                           int quartX, int quartZ, Climate.Sampler sampler) {
        int firstQuartY = Math.floorDiv(biome.guaranteedMinimumWorldY() + 3, 4);
        int lastQuartY = Math.floorDiv(biome.guaranteedMaximumWorldY(), 4);
        for (int quartY = firstQuartY; quartY <= lastQuartY; quartY++) {
            int worldY = quartY * 4;
            Climate.TargetPoint target = sampler.sample(quartX, quartY, quartZ);
            boolean snapsToSurface = biome.snapToSurface()
                    && snappedBiomeContainsY(x, worldY, z, quartX, quartY, quartZ, sampler, biome);
            if (climateContains(biome.climate(), target)
                    && (snapsToSurface || insideBiomeHeight(x, worldY, z, biome))) {
                return true;
            }
        }
        return false;
    }

    @Unique
    private static ResourceKey<Biome> transitionTarget(ModBiomes.BiomeDefinition biome, int worldY) {
        return biome.transitionBelowY()
                .filter(transition -> worldY < transition.belowWorldY())
                .map(ModBiomes.BiomeTransition::targetBiome)
                .orElse(null);
    }

    @Unique
    private java.util.Optional<Holder<Biome>> transitionHolder(ModBiomes.BiomeDefinition parent, int worldY) {
        ResourceKey<Biome> targetKey = transitionTarget(parent, worldY);
        if (targetKey == null) return java.util.Optional.empty();
        ModBiomes.BiomeDefinition targetDefinition = ModBiomes.definition(targetKey);
        if (targetDefinition != null
                && (worldY < targetDefinition.effectiveMinimumWorldY()
                || worldY > targetDefinition.effectiveMaximumWorldY())) {
            return java.util.Optional.empty();
        }
        return holderFor(targetKey);
    }

    @Unique
    private static boolean climateContains(Climate.ParameterPoint point, Climate.TargetPoint target) {
        return contains(point.temperature(), target.temperature())
                && contains(point.humidity(), target.humidity())
                && contains(point.continentalness(), target.continentalness())
                && contains(point.erosion(), target.erosion())
                && contains(point.weirdness(), target.weirdness());
    }

    @Unique
    private static boolean snappedBiomeContainsY(int x, int y, int z, int quartX, int quartY, int quartZ,
                                                  Climate.Sampler sampler, ModBiomes.BiomeDefinition biome) {
        // The configured maximum Y limits ordinary biome placement, but snapping
        // deliberately lets the biome's top reach terrain above that limit.
        if (y < biome.effectiveMinimumWorldY()) return false;

        // Find the first surface-like climate sample above this position. This anchors
        // the biome's vertical span to terrain instead of treating every depth-zero
        // climate sample as a valid place to start a floating biome.
        int lastQuartY = Math.floorDiv(ModBiomes.WORLD_TOP_Y, 4);
        for (int sampleQuartY = quartY; sampleQuartY <= lastQuartY; sampleQuartY++) {
            Climate.TargetPoint sample = sampler.sample(quartX, sampleQuartY, quartZ);
            if (Math.abs(Climate.unquantizeCoord(sample.depth())) > 0.05F
                    || !climateContains(biome.climate(), sample)) continue;
            int surfaceY = sampleQuartY * 4;
            return y <= surfaceY && (biome.surfaceToWorldBottom()
                    || y >= surfaceY - biome.maximumHeight() + 1);
        }
        return false;
    }

    @Unique
    private static boolean contains(Climate.Parameter parameter, long value) {
        return value >= parameter.min() && value <= parameter.max();
    }

    @Unique
    private java.util.Optional<Holder<Biome>> holderFor(ResourceKey<Biome> key) {
        return parameters().values().stream()
                .map(Pair::getSecond)
                .filter(holder -> holder.unwrapKey().map(key::equals).orElse(false))
                .findFirst();
    }

    @Unique
    private Climate.ParameterList<Holder<Biome>> vanillaFallback() {
        Climate.ParameterList<Holder<Biome>> cached = crusaders$vanillaFallback;
        if (cached != null) return cached;

        synchronized (this) {
            cached = crusaders$vanillaFallback;
            if (cached == null) {
                List<Pair<Climate.ParameterPoint, Holder<Biome>>> vanillaEntries = parameters().values().stream()
                        .filter(entry -> entry.getSecond().unwrapKey()
                                .map(key -> ModBiomes.definition(key) == null)
                                .orElse(true))
                        .toList();
                cached = new Climate.ParameterList<>(vanillaEntries);
                crusaders$vanillaFallback = cached;
            }
        }
        return cached;
    }

    @Unique
    private static boolean insideBiomeArea(int x, int z, ModBiomes.BiomeDefinition biome) {
        int minimum = Math.max(1, biome.minimumSize());
        int maximum = Math.max(minimum, biome.maximumSize());
        int cellSize = maximum;
        int cellX = Math.floorDiv(x, cellSize);
        int cellZ = Math.floorDiv(z, cellSize);
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
                int currentCellX = cellX + offsetX;
                int currentCellZ = cellZ + offsetZ;
                long seed = (((long) currentCellX) * 341873128712L)
                        ^ (((long) currentCellZ) * 132897987541L)
                        ^ biome.key().location().hashCode();
                long mixed = mix(seed);
                int diameter = minimum + (int) Long.remainderUnsigned(mixed, (long) maximum - minimum + 1L);
                long radius = Math.max(1, diameter / 2L);
                long centerX = (long) currentCellX * cellSize + cellSize / 2L;
                long centerZ = (long) currentCellZ * cellSize + cellSize / 2L;
                long dx = x - centerX;
                long dz = z - centerZ;
                if (dx * dx + dz * dz <= radius * radius) return true;
            }
        }
        return false;
    }

    @Unique
    private static boolean insideBiomeHeight(int x, int y, int z, ModBiomes.BiomeDefinition biome) {
        if (biome.spansEntireWorldY()) {
            return y >= biome.effectiveMinimumWorldY() && y <= biome.effectiveMaximumWorldY();
        }

        int maximum = Math.max(1, biome.maximumSize());
        int cellX = Math.floorDiv(x, maximum);
        int cellZ = Math.floorDiv(z, maximum);
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
                int currentCellX = cellX + offsetX;
                int currentCellZ = cellZ + offsetZ;
                long seed = (((long) currentCellX) * 341873128712L)
                        ^ (((long) currentCellZ) * 132897987541L)
                        ^ biome.key().location().hashCode();
                long mixed = mix(seed);
                int minimumSize = Math.max(1, biome.minimumSize());
                int diameter = minimumSize + (int) Long.remainderUnsigned(
                        mixed, (long) maximum - minimumSize + 1L);
                long radius = Math.max(1, diameter / 2L);
                long centerX = (long) currentCellX * maximum + maximum / 2L;
                long centerZ = (long) currentCellZ * maximum + maximum / 2L;
                long dx = x - centerX;
                long dz = z - centerZ;
                if (dx * dx + dz * dz > radius * radius) continue;

                int heightRange = biome.maximumHeight() - biome.minimumHeight() + 1;
                int height = biome.minimumHeight()
                        + (int) Long.remainderUnsigned(mix(seed ^ 0x9e3779b97f4a7c15L), heightRange);
                int firstGuaranteedSampleY = Math.floorDiv(biome.guaranteedMinimumWorldY() + 3, 4) * 4;
                int lastGuaranteedSampleY = Math.floorDiv(biome.guaranteedMaximumWorldY(), 4) * 4;
                int lowestBottom = Math.max(biome.effectiveMinimumWorldY(), firstGuaranteedSampleY - height + 1);
                int highestBottom = Math.min(biome.effectiveMaximumWorldY() - height + 1, lastGuaranteedSampleY);
                int bottomRange = highestBottom - lowestBottom + 1;
                if (bottomRange < 1) continue;
                int bottom = lowestBottom
                        + (int) Long.remainderUnsigned(mix(seed ^ 0x632be59bd9b4e019L), bottomRange);
                if (y >= bottom && y < bottom + height) return true;
            }
        }
        return false;
    }

    @Unique
    private static long mix(long value) {
        value = (value ^ (value >>> 30)) * 0xbf58476d1ce4e5b9L;
        value = (value ^ (value >>> 27)) * 0x94d049bb133111ebL;
        return value ^ (value >>> 31);
    }
}
