package net.wolfygames7237.Crusadersoffiction.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;

import java.util.Properties;

public class GlowingMushroom extends MushroomBlock {

    public GlowingMushroom(Properties props) {
        super(props, null);
    }

    /* -------- Only survive on glowing mushroom block -------- */
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(ModBlocks.GLOWING_MUSHROOM_BLOCK.get());
    }

    /* -------- Remove density restriction -------- */
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below)
                .is(ModBlocks.GLOWING_MUSHROOM_BLOCK.get());
    }
}
