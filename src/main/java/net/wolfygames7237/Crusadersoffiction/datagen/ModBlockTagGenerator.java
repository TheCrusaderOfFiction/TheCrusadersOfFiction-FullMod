package net.wolfygames7237.Crusadersoffiction.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
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
                        (ModBlocks.WYSTERIUM_BLOCK.get()),
                (Blocks.BEDROCK)
                        );


        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.WYSTERIUM_BLOCK.get());

        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                .add(ModBlocks.WYSTERIUM_ORE.get());

        this.tag(ModTags.Blocks.NEEDS_WYSTERIUM_TOOL)
                .add(Blocks.BEDROCK);



    }
}
