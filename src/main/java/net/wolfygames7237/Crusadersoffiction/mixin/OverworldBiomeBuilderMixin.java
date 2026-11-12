package net.wolfygames7237.Crusadersoffiction.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.wolfygames7237.Crusadersoffiction.worldgen.biome.ModBiomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/** Adds every entry in ModBiomes to vanilla's Overworld climate map. */
@Mixin(OverworldBiomeBuilder.class)
public abstract class OverworldBiomeBuilderMixin {
    @Inject(method = "addBiomes", at = @At("TAIL"))
    private void crusaders$addRegisteredBiomes(
            Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> collector,
            CallbackInfo callback) {
        // Vanilla's registry bootstrap runs before our datapack biome entries exist.
        // The data run sets this flag so the runtime-only climate entries are skipped.
        if (Boolean.getBoolean("crusadersoffiction.datagen")) {
            return;
        }

        for (ModBiomes.BiomeDefinition definition : ModBiomes.definitions()) {
            collector.accept(Pair.of(definition.climate(), definition.key()));
        }
    }
}
