package net.wolfygames7237.Crusadersoffiction.Item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.wolfygames7237.Crusadersoffiction.blocks.ModBlocks;

public class JungleGrass extends Item {

    public JungleGrass(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        // Check if clicked block is mud
        if (state.is(Blocks.MUD)) {

            // Replace with your block
            level.setBlock(pos,
                    ModBlocks.JUNGLE_GRASS_BLOCK.get().defaultBlockState(),
                    Block.UPDATE_ALL
            );

            // Optional: consume item
            context.getItemInHand().shrink(1);

            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
}
