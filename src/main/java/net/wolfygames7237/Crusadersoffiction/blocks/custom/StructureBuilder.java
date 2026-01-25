package net.wolfygames7237.Crusadersoffiction.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.wolfygames7237.Crusadersoffiction.blocks.entity.StructureBuilderBlockEntity;
import net.wolfygames7237.Crusadersoffiction.blocks.entity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class StructureBuilder extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    // Selection / outline shape (for highlighting)
    private static final VoxelShape SHAPE = Block.box(-16, 0, -16, 32, 32, 32); // example large shape

    public StructureBuilder(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        // Check if the placement area is free of collisions
        if (!canPlaceAt(level, pos)) {
            return null; // prevents placement
        }

        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /** Check if all blocks in the VoxelShape are replaceable (no collisions) */
    private boolean canPlaceAt(Level level, BlockPos pos) {
        // Move shape to world coordinates
        VoxelShape shape = SHAPE.move(pos.getX(), pos.getY(), pos.getZ());

        // Get integer bounds
        int minX = (int) Math.floor(shape.min(Direction.Axis.X));
        int minY = (int) Math.floor(shape.min(Direction.Axis.Y));
        int minZ = (int) Math.floor(shape.min(Direction.Axis.Z));

        int maxX = (int) Math.ceil(shape.max(Direction.Axis.X));
        int maxY = (int) Math.ceil(shape.max(Direction.Axis.Y));
        int maxZ = (int) Math.ceil(shape.max(Direction.Axis.Z));

        // Iterate over all blocks in the bounding box
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos checkPos = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(checkPos);
                    VoxelShape collision = state.getCollisionShape(level, checkPos);
                    if (!collision.isEmpty()) {
                        return false; // cannot place here
                    }
                }
            }
        }

        return true;
    }

    @Override
    public BlockState rotate(BlockState state, net.minecraft.world.level.block.Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, net.minecraft.world.level.block.Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        // This is the outline/selection shape (for interaction highlight)
        // Expand as needed (e.g., 2x2x2 blocks)
        return Block.box(-16, 0, -16, 32, 3, 32);
    }


    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof StructureBuilderBlockEntity builderBE) {
                builderBE.drops();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof StructureBuilderBlockEntity builderBE)) return InteractionResult.PASS;

        // Allow interaction anywhere in the expanded shape
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, builderBE, pos);
        }

        return InteractionResult.CONSUME;
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StructureBuilderBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.STRUCTURE_BUILDER_BE.get() ? StructureBuilderBlockEntity::ticker : null;
    }
}
