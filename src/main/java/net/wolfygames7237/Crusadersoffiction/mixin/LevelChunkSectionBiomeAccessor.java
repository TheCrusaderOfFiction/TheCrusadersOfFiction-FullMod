package net.wolfygames7237.Crusadersoffiction.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Allows the post-generation biome pass to replace biome palette entries in a chunk section. */
@Mixin(LevelChunkSection.class)
public interface LevelChunkSectionBiomeAccessor {
    @Accessor("biomes")
    PalettedContainerRO<Holder<Biome>> crusaders$getBiomes();

    @Accessor("biomes")
    void crusaders$setBiomes(PalettedContainerRO<Holder<Biome>> biomes);
}
