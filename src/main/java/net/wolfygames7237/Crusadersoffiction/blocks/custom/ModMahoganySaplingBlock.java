package net.wolfygames7237.Crusadersoffiction.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;

public class ModMahoganySaplingBlock extends SaplingBlock {
    public ModMahoganySaplingBlock(AbstractTreeGrower pTreeGrower, Properties pProperties) {
        super(pTreeGrower, pProperties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(Blocks.MUD) || pState.is(ModBlocks.JUNGLE_GRASS_BLOCK.get());
    }
}
