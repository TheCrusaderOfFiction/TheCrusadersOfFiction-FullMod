package net.wolfygames7237.Crusadersoffiction.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;

public class ModBorealSaplingBlock extends SaplingBlock {
    public ModBorealSaplingBlock(AbstractTreeGrower pTreeGrower, Properties pProperties) {
        super(pTreeGrower, pProperties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(Blocks.SNOW_BLOCK) || pState.is(Blocks.BLUE_ICE);
    }
}
