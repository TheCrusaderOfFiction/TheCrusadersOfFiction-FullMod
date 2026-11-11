package net.wolfygames7237.Crusadersoffiction.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CrusadersOfFiction.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.WYSTERIUM_ORE.get(),
                        ModBlocks.WYSTERIUM_BLOCK.get(),
                        Blocks.BEDROCK,
                        Blocks.BLUE_ICE
                        );

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(Blocks.SNOW_BLOCK);

        for (Block block : BuiltInRegistries.BLOCK) {
            // If a block currently requires a tool, bump it up
            if (block.defaultBlockState().requiresCorrectToolForDrops()) {
                // For example, move EVERYTHING to require Netherite (Level 10)

            }
        }

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(BlockTags.LOGS);

        this.tag(ModTags.Blocks.NEEDS_WOOD_TOOL);

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(Blocks.SNOW_BLOCK)
                .remove(BlockTags.IRON_ORES);

        this.tag(ModTags.Blocks.NEEDS_COPPER_TOOL)
                .addTag(BlockTags.IRON_ORES);

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.FORGE.get(), Blocks.BLUE_ICE);

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.WYSTERIUM_BLOCK.get());

        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                .add(ModBlocks.WYSTERIUM_ORE.get());

        this.tag(ModTags.Blocks.NEEDS_WYSTERIUM_TOOL)
                .add(Blocks.BEDROCK);

        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.MAHOGANY_LOG.get())
                .add(ModBlocks.MAHOGANY_WOOD.get())
                .add(ModBlocks.STRIPPED_MAHOGANY_LOG.get())
                .add(ModBlocks.STRIPPED_MAHOGANY_WOOD.get())
                .add(ModBlocks.BOREAL_LOG.get())
                .add(ModBlocks.BOREAL_WOOD.get())
                .add(ModBlocks.STRIPPED_BOREAL_LOG.get())
                .add(ModBlocks.STRIPPED_BOREAL_WOOD.get());

        this.tag(BlockTags.PLANKS)
                .add(ModBlocks.MAHOGANY_PLANKS.get())
                .add(ModBlocks.BOREAL_PLANKS.get());

    }
}
