package net.wolfygames7237.Crusadersoffiction.worldgen.biome;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.mixin.LevelChunkSectionBiomeAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/** Counts configured blocks after a new chunk finishes generating and updates its biome palette. */
@Mod.EventBusSubscriber(modid = CrusadersOfFiction.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PostGenerationBiomeUpdater {
    private static final Map<ServerLevel, Set<Long>> PENDING_NEW_CHUNKS = new HashMap<>();

    private PostGenerationBiomeUpdater() {}

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getChunk() instanceof LevelChunk chunk)
                || !(chunk.getLevel() instanceof ServerLevel level)) return;

        if (event.isNewChunk()) {
            synchronized (PENDING_NEW_CHUNKS) {
                PENDING_NEW_CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>())
                        .add(chunk.getPos().toLong());
            }
        }
        processReadyChunks(level);
    }

    private static void processReadyChunks(ServerLevel level) {
        Set<Long> pending;
        synchronized (PENDING_NEW_CHUNKS) {
            pending = new HashSet<>(PENDING_NEW_CHUNKS.getOrDefault(level, Set.of()));
        }

        for (long packedPos : pending) {
            int chunkX = ChunkPos.getX(packedPos);
            int chunkZ = ChunkPos.getZ(packedPos);
            LevelChunk chunk = level.getChunkSource().getChunkNow(chunkX, chunkZ);
            if (chunk == null || !allRuleAreasLoaded(level, chunk.getPos())) continue;

            for (ModBiomes.BlockCountBiomeUpdate update : ModBiomes.postGenerationUpdates()) {
                applyUpdate(level, chunk, update);
            }
            synchronized (PENDING_NEW_CHUNKS) {
                Set<Long> levelPending = PENDING_NEW_CHUNKS.get(level);
                if (levelPending != null) {
                    levelPending.remove(packedPos);
                    if (levelPending.isEmpty()) PENDING_NEW_CHUNKS.remove(level);
                }
            }
        }
    }

    private static boolean allRuleAreasLoaded(ServerLevel level, ChunkPos center) {
        for (ModBiomes.BlockCountBiomeUpdate update : ModBiomes.postGenerationUpdates()) {
            int radius = update.horizontalRadius();
            int minimumChunkX = Math.floorDiv(center.getMinBlockX() - radius, 16);
            int maximumChunkX = Math.floorDiv(center.getMinBlockX() + 15 + radius, 16);
            int minimumChunkZ = Math.floorDiv(center.getMinBlockZ() - radius, 16);
            int maximumChunkZ = Math.floorDiv(center.getMinBlockZ() + 15 + radius, 16);
            for (int chunkX = minimumChunkX; chunkX <= maximumChunkX; chunkX++) {
                for (int chunkZ = minimumChunkZ; chunkZ <= maximumChunkZ; chunkZ++) {
                    if (level.getChunkSource().getChunkNow(chunkX, chunkZ) == null) return false;
                }
            }
        }
        return true;
    }

    private static void applyUpdate(ServerLevel level, LevelChunk chunk,
                                    ModBiomes.BlockCountBiomeUpdate update) {
        ModBiomes.BiomeDefinition targetDefinition = ModBiomes.definition(update.targetBiome());
        if (targetDefinition == null) return;

        int radius = update.horizontalRadius();
        ChunkPos chunkPos = chunk.getPos();
        int chunkMinX = chunkPos.getMinBlockX();
        int chunkMinZ = chunkPos.getMinBlockZ();
        int originX = chunkMinX - radius;
        int originZ = chunkMinZ - radius;
        int width = 16 + radius * 2;
        int countMinY = Math.max(update.countMinimumWorldY(), level.getMinBuildHeight());
        int countMaxY = Math.min(update.countMaximumWorldY(), level.getMaxBuildHeight() - 1);
        if (countMinY > countMaxY) return;

        Set<Block> countedBlocks = Set.copyOf(update.countedBlocks());
        int verticalRadius = update.verticalRadius();
        int[][][] summedCounts = countBlocksInVolume(level, originX, originZ, width,
                countMinY, countMaxY, countedBlocks);

        Holder<Biome> targetBiome = level.registryAccess().registryOrThrow(Registries.BIOME)
                .getHolderOrThrow(update.targetBiome());
        var biomeRegistry = level.registryAccess().registryOrThrow(Registries.BIOME);
        LevelChunkSection[] sections = chunk.getSections();
        @SuppressWarnings("unchecked")
        PalettedContainer<Holder<Biome>>[] biomePalettes = new PalettedContainer[sections.length];
        boolean[] changedSections = new boolean[sections.length];
        int minimumUpdateY = Math.max(update.updateMinimumWorldY(), targetDefinition.effectiveMinimumWorldY());
        int maximumUpdateY = Math.min(update.updateMaximumWorldY(), targetDefinition.effectiveMaximumWorldY());

        boolean anyChanges = false;
        for (int quartX = 0; quartX < 4; quartX++) {
            for (int quartZ = 0; quartZ < 4; quartZ++) {
                int blockX = chunkMinX + quartX * 4 + 2;
                int blockZ = chunkMinZ + quartZ * 4 + 2;
                int surfaceY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockX, blockZ) - 1;
                int columnMinimumY = minimumUpdateY;
                int columnMaximumY = maximumUpdateY;
                if (targetDefinition.surfaceToWorldBottom()) {
                    columnMaximumY = Math.min(columnMaximumY, surfaceY);
                } else if (targetDefinition.snapToSurface()) {
                    columnMinimumY = Math.max(columnMinimumY, surfaceY - targetDefinition.maximumHeight() + 1);
                    columnMaximumY = Math.min(columnMaximumY, surfaceY);
                }
                if (columnMinimumY > columnMaximumY) continue;

                for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
                    int sectionBaseY = level.getMinBuildHeight() + sectionIndex * 16;
                    for (int localQuartY = 0; localQuartY < 4; localQuartY++) {
                        int worldQuartY = sectionBaseY + localQuartY * 4;
                        if (worldQuartY < columnMinimumY || worldQuartY > columnMaximumY) continue;
                        int centerY = worldQuartY + 2;
                        int countY0 = Math.max(countMinY, centerY - verticalRadius);
                        int countY1 = Math.min(countMaxY, centerY + verticalRadius);
                        int x0 = blockX - radius - originX;
                        int x1 = blockX + radius + 1 - originX;
                        int z0 = blockZ - radius - originZ;
                        int z1 = blockZ + radius + 1 - originZ;
                        int y0 = countY0 - countMinY;
                        int y1 = countY1 - countMinY + 1;
                        if (countY0 > countY1 || sumVolume(summedCounts,
                                x0, x1, z0, z1, y0, y1) < update.minimumBlockCount()) continue;
                        var appliedKey = targetDefinition.transitionBelowY()
                                .filter(transition -> worldQuartY < transition.belowWorldY())
                                .map(ModBiomes.BiomeTransition::targetBiome)
                                .orElse(update.targetBiome());
                        ModBiomes.BiomeDefinition appliedDefinition = ModBiomes.definition(appliedKey);
                        if (appliedDefinition != null
                                && (worldQuartY < appliedDefinition.effectiveMinimumWorldY()
                                || worldQuartY > appliedDefinition.effectiveMaximumWorldY())) continue;
                        Holder<Biome> appliedBiome = appliedKey.equals(update.targetBiome())
                                ? targetBiome
                                : biomeRegistry.getHolderOrThrow(appliedKey);
                        if (biomePalettes[sectionIndex] == null) {
                            biomePalettes[sectionIndex] = sections[sectionIndex].getBiomes().recreate();
                        }
                        PalettedContainer<Holder<Biome>> palette = biomePalettes[sectionIndex];
                        if (!palette.get(quartX, localQuartY, quartZ).equals(appliedBiome)) {
                            palette.set(quartX, localQuartY, quartZ, appliedBiome);
                            changedSections[sectionIndex] = true;
                            anyChanges = true;
                        }
                    }
                }
            }
        }

        for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
            if (!changedSections[sectionIndex]) continue;
            ((LevelChunkSectionBiomeAccessor) (Object) sections[sectionIndex])
                    .crusaders$setBiomes(biomePalettes[sectionIndex]);
            chunk.setUnsaved(true);
        }
    }

    /** Builds a summed-volume table for block matches in the configured horizontal area and Y band. */
    private static int[][][] countBlocksInVolume(ServerLevel level, int originX, int originZ, int width,
                                                  int minimumY, int maximumY, Set<Block> countedBlocks) {
        int height = maximumY - minimumY + 1;
        int[][][] prefix = new int[width + 1][width + 1][height + 1];
        Map<Long, LevelChunk> loadedChunks = new HashMap<>();
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

        for (int localX = 0; localX < width; localX++) {
            int worldX = originX + localX;
            for (int localZ = 0; localZ < width; localZ++) {
                int worldZ = originZ + localZ;
                int chunkX = Math.floorDiv(worldX, 16);
                int chunkZ = Math.floorDiv(worldZ, 16);
                long chunkKey = ChunkPos.asLong(chunkX, chunkZ);
                LevelChunk sourceChunk = loadedChunks.computeIfAbsent(chunkKey,
                        key -> level.getChunkSource().getChunkNow(chunkX, chunkZ));
                if (sourceChunk == null) continue;

                for (int y = minimumY; y <= maximumY; y++) {
                    blockPos.set(worldX, y, worldZ);
                    int matching = countedBlocks.contains(sourceChunk.getBlockState(blockPos).getBlock()) ? 1 : 0;
                    int localY = y - minimumY;
                    prefix[localX + 1][localZ + 1][localY + 1] = matching
                            + prefix[localX][localZ + 1][localY + 1]
                            + prefix[localX + 1][localZ][localY + 1]
                            + prefix[localX + 1][localZ + 1][localY]
                            - prefix[localX][localZ][localY + 1]
                            - prefix[localX][localZ + 1][localY]
                            - prefix[localX + 1][localZ][localY]
                            + prefix[localX][localZ][localY];
                }
            }
        }
        return prefix;
    }

    private static int sumVolume(int[][][] prefix, int x0, int x1, int z0, int z1, int y0, int y1) {
        return prefix[x1][z1][y1] - prefix[x0][z1][y1] - prefix[x1][z0][y1] - prefix[x1][z1][y0]
                + prefix[x0][z0][y1] + prefix[x0][z1][y0] + prefix[x1][z0][y0] - prefix[x0][z0][y0];
    }
}
