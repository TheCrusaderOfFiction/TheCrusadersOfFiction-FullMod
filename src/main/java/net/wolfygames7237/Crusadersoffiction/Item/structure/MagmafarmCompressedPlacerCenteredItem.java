package net.wolfygames7237.Crusadersoffiction.Item.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
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
import net.minecraft.world.phys.Vec3;
import net.wolfygames7237.Crusadersoffiction.Item.structure.renderer.ICompressedCenteredItem;

import java.util.Optional;

public class MagmafarmCompressedPlacerCenteredItem extends Item implements ICompressedCenteredItem {

    public MagmafarmCompressedPlacerCenteredItem(Properties properties) {
        super(properties);
    }

    @Override
    public Vec3 getGhostTranslation() {
        // Optional fine-tuning translation (X, Y, Z)
        return new Vec3(10, 1, 10);
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
        ResourceLocation structureId = new ResourceLocation("crusadersoffiction", "compressed_64mobfarm");
        Optional<StructureTemplate> templateOpt = structureManager.get(structureId);
        if (templateOpt.isEmpty()) return false;

        StructureTemplate template = templateOpt.get();

        for (StructureTemplate.StructureBlockInfo info :
                template.filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), Blocks.AIR)) {
            BlockPos worldPos = origin.offset(info.pos());
            if (!level.getBlockState(worldPos).canBeReplaced()) return false;
        }

        return true;
    }

    private void placeStructure(ServerLevel level, BlockPos origin, Player player) {
        StructureTemplateManager structureManager = level.getStructureManager();
        ResourceLocation structureId = new ResourceLocation("crusadersoffiction", "compressed_64mobfarm");
        Optional<StructureTemplate> templateOpt = structureManager.get(structureId);
        if (templateOpt.isEmpty()) return;

        StructureTemplate template = templateOpt.get();
        Vec3i size = template.getSize();

        // Centering offsets
        double centerX = size.getX() / 2.0;
        double centerZ = size.getZ() / 2.0;

        // Get per-item fine translation
        Vec3 translation = Vec3.ZERO;
        if (this instanceof ICompressedCenteredItem) {
            translation = ((ICompressedCenteredItem) this).getGhostTranslation();
        }

        double offsetX = -centerX + translation.x;
        double offsetZ = -centerZ + translation.z;
        double offsetY = -1 + translation.y; // downward translation

        // Determine rotation
        Direction dir = player.getDirection();
        Rotation rotation = switch (dir) {
            case SOUTH -> Rotation.CLOCKWISE_90;
            case WEST -> Rotation.CLOCKWISE_180;
            case NORTH -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };

        // Rotate offsets to match rotation
        BlockPos rotatedOffset = switch (rotation) {
            case NONE -> new BlockPos((int) offsetX, 0, (int) offsetZ);
            case CLOCKWISE_90 -> new BlockPos((int) -offsetZ, 0, (int) offsetX);
            case CLOCKWISE_180 -> new BlockPos((int) -offsetX, 0, (int) -offsetZ);
            case COUNTERCLOCKWISE_90 -> new BlockPos((int) offsetZ, 0, (int) -offsetX);
        };

        BlockPos finalOrigin = origin.offset(rotatedOffset).below(1); // apply downward translation

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(rotation)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(false);

        template.placeInWorld(
                level,
                finalOrigin,
                finalOrigin,
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
