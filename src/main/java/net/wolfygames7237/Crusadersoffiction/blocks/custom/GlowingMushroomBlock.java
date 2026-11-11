package net.wolfygames7237.Crusadersoffiction.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;

public class GlowingMushroomBlock extends Block implements BonemealableBlock {

    private static final int SPREAD_ATTEMPTS = 1;
    /** Scheduled behavior interval in game ticks: 1 runs every tick, 2 every other tick. */
    private static final int TICK_INTERVAL = 500;

    public GlowingMushroomBlock(Properties props) {
        super(props.randomTicks());
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide && oldState.getBlock() != this) {
            level.scheduleTick(pos, this, Math.max(1, TICK_INTERVAL));
        }
    }

    /** Starts scheduled ticking for blocks first placed by terrain generation. */
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.getBlockTicks().hasScheduledTick(pos, this)) {
            level.scheduleTick(pos, this, Math.max(1, TICK_INTERVAL));
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Spread block itself
        for (int i = 0; i < SPREAD_ATTEMPTS; i++) {
            BlockPos target = pos.offset(
                    random.nextInt(3) - 1,
                    random.nextInt(5) - 3,
                    random.nextInt(3) - 1
            );

            if (!level.isLoaded(target)) continue;

            if (level.getBlockState(target).is(Blocks.MUD)) {

                level.setBlock(target,
                        this.defaultBlockState(),
                        Block.UPDATE_ALL);
            }
        }

        // Random mushroom growth
        if (random.nextInt(6) == 0) { // chance per tick
            tryGrowMushroom(level, pos, random);
        }

        if (level.getBlockState(pos).is(this)) {
            level.scheduleTick(pos, this, Math.max(1, TICK_INTERVAL));
        }
    }

    /* -------- Mushroom growth helper -------- */
    private void tryGrowMushroom(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos target = pos.offset(
                random.nextInt(5) - 2,
                1,
                random.nextInt(5) - 2
        );

        if (level.isEmptyBlock(target)
                && level.getBlockState(target.below())
                .is(ModBlocks.GLOWING_MUSHROOM_BLOCK.get())) {

            level.setBlock(target,
                    ModBlocks.GLOWING_MUSHROOM.get().defaultBlockState(),
                    Block.UPDATE_ALL);
        }
    }

    /* -------- Bonemeal support -------- */
    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos,
                                         BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random,
                                     BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random,
                                BlockPos pos, BlockState state) {

        // Spawn several mushrooms
        for (int i = 0; i < 8; i++) {
            tryGrowMushroom(level, pos, random);
        }
    }
}
