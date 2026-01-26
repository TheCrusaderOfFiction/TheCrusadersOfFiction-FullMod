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
import net.wolfygames7237.Crusadersoffiction.Item.structure.renderer.ICompressedGhostYOffset;

import java.util.Optional;

public class SugarcanefarmCompressedPlacerItem extends Item
        implements ICompressedGhostYOffset {

    public SugarcanefarmCompressedPlacerItem(Properties properties) {
        super(properties);
    }

    // 🔽 Ghost + placement vertical offset
    @Override
    public double getGhostYOffset() {
        return -4.0; // move structure down 1 block
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

        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;

        // Apply Y offset to placement origin
        BlockPos origin = context.getClickedPos()
                .above()
                .offset(0, (int) getGhostYOffset(), 0);

        if (!canPlaceStructure(serverLevel, origin)) {
            return InteractionResult.FAIL;
        }

        placeStructure(serverLevel, origin, player);

        if (!player.isCreative()) {
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    private boolean canPlaceStructure(ServerLevel level, BlockPos origin) {
        StructureTemplateManager structureManager = level.getStructureManager();
        ResourceLocation structureId =
                new ResourceLocation("crusadersoffiction", "compressed_64sugarcanefarm");

        Optional<StructureTemplate> templateOpt = structureManager.get(structureId);
        if (templateOpt.isEmpty()) return false;

        StructureTemplate structure = templateOpt.get();

        for (StructureTemplate.StructureBlockInfo info :
                structure.filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), Blocks.AIR)) {

            BlockPos worldPos = origin.offset(info.pos());
            if (!level.getBlockState(worldPos).canBeReplaced()) {
                return false;
            }
        }

        return true;
    }

    private void placeStructure(ServerLevel level, BlockPos origin, Player player) {
        StructureTemplateManager structureManager = level.getStructureManager();
        ResourceLocation structureId =
                new ResourceLocation("crusadersoffiction", "compressed_64sugarcanefarm");

        Optional<StructureTemplate> templateOpt = structureManager.get(structureId);
        if (templateOpt.isEmpty()) return;

        Direction dir = player.getDirection();
        Rotation rotation = switch (dir) {
            case SOUTH -> Rotation.CLOCKWISE_90;
            case WEST -> Rotation.CLOCKWISE_180;
            case NORTH -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(rotation)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(false);

        templateOpt.get().placeInWorld(
                level,
                origin,
                origin,
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
