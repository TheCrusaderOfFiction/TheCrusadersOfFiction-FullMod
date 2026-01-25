package net.wolfygames7237.Crusadersoffiction.blocks.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CrusadersOfFiction.MOD_ID);

    public static final RegistryObject<BlockEntityType<ForgeBlockEntity>> FORGE_BE =
            BLOCK_ENTITIES.register("forge_block_entity", () ->
                    BlockEntityType.Builder.of(ForgeBlockEntity::new,
                            ModBlocks.FORGE.get()).build(null));

    public static final RegistryObject<BlockEntityType<StructureBuilderBlockEntity>> STRUCTURE_BUILDER_BE =
            BLOCK_ENTITIES.register("structure_builder_block_entity", () ->
                    BlockEntityType.Builder.of(StructureBuilderBlockEntity::new,
                            ModBlocks.STRUCTURE_BUILDER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockCompressorBlockEntity>> BLOCK_COMPRESSOR_BE =
            BLOCK_ENTITIES.register("block_compressor_block_entity", () ->
                    BlockEntityType.Builder.of(BlockCompressorBlockEntity::new,
                            ModBlocks.BLOCK_COMPRESSOR.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
