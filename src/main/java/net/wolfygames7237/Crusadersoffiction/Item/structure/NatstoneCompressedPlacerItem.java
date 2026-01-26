package net.wolfygames7237.Crusadersoffiction.Item.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class NatstoneCompressedPlacerItem extends Item {

    public NatstoneCompressedPlacerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.FAIL;
        }

        BlockPos origin = context.getClickedPos().above();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;

        // Prevent placement if blocked
        if (!canPlaceStructure(serverLevel, origin)) {
            return InteractionResult.FAIL;
        }

        placeStructure(serverLevel, origin, player);

        // Consume item
        if (!player.isCreative()) {
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    private boolean canPlaceStructure(ServerLevel level, BlockPos origin) {
        StructureTemplateManager structureManager = level.getStructureManager();

        ResourceLocation structureId =
                new ResourceLocation("crusadersoffiction", "compressed_64natstone");

        Optional<StructureTemplate> template = structureManager.get(structureId);
        if (template.isEmpty()) return false;

        StructureTemplate structure = template.get();

        for (StructureTemplate.StructureBlockInfo info :
                structure.filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), Blocks.AIR)) {

            BlockPos worldPos = origin.offset(info.pos());

            if (!level.getBlockState(worldPos).canBeReplaced()) {
                return false;
            }
        }

        return true;
    }

    private void placeStructure(ServerLevel level, BlockPos pos, Player player) {
        StructureTemplateManager structureManager = level.getStructureManager();

        ResourceLocation structureId =
                new ResourceLocation("crusadersoffiction", "compressed_64natstone");

        Optional<StructureTemplate> template = structureManager.get(structureId);
        if (template.isEmpty()) return;

        Direction dir = player.getDirection();

        Rotation rotation = switch (dir) {
            case SOUTH -> Rotation.CLOCKWISE_90;
            case WEST -> Rotation.CLOCKWISE_180;
            case EAST -> Rotation.NONE;
            case NORTH -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(rotation)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(false);

        template.get().placeInWorld(
                level,
                pos,
                pos,
                settings,
                level.getRandom(),
                Block.UPDATE_ALL
        );
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
