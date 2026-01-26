package net.wolfygames7237.Crusadersoffiction.Item.structure.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Mod.EventBusSubscriber(
        modid = "crusadersoffiction",
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class CompressedGhostCenteredRenderer {

    private static BlockPos rotatePos(BlockPos pos, Rotation rotation) {
        return switch (rotation) {
            case NONE -> pos;
            case CLOCKWISE_90 -> new BlockPos(-pos.getZ(), pos.getY(), pos.getX());
            case CLOCKWISE_180 -> new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());
            case COUNTERCLOCKWISE_90 -> new BlockPos(pos.getZ(), pos.getY(), -pos.getX());
        };
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return;

        Item item = stack.getItem();
        Class<?> clazz = item.getClass();
        String className = clazz.getSimpleName();

        // Only CompressedPlacerCenteredItem subclasses
        if (!className.endsWith("PlacerCenteredItem") || !className.contains("Compressed")) return;

        // Extract prefix before "Compressed"
        String prefix = className.substring(0, className.indexOf("Compressed")).toLowerCase();

        ResourceLocation jsonStructure = new ResourceLocation(
                "crusadersoffiction",
                "structures/compressed_64" + prefix + ".json"
        );

        if (!(mc.hitResult instanceof BlockHitResult hit)) return;

        BlockPos origin = hit.getBlockPos().above();
        Rotation rotation = switch (player.getDirection()) {
            case SOUTH -> Rotation.CLOCKWISE_90;
            case WEST -> Rotation.CLOCKWISE_180;
            case NORTH -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };

        // Fetch per-item translation from interface
        Vec3 classTranslation = (item instanceof ICompressedCenteredItem transItem) ?
                transItem.getGhostTranslation() : Vec3.ZERO;

        renderGhost(event.getPoseStack(), origin, rotation, jsonStructure, classTranslation);
    }

    private static void renderGhost(PoseStack poseStack, BlockPos origin, Rotation rotation,
                                    ResourceLocation jsonStructure, Vec3 translation) {
        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
        MultiBufferSource buffer = mc.renderBuffers().bufferSource();

        List<StructureJsonLoader.BlockInfo> blocks = StructureJsonLoader.loadBlockInfos(jsonStructure);
        if (blocks.isEmpty()) return;

        // Rotate all block positions first
        List<BlockPos> rotatedPositions = blocks.stream()
                .map(b -> rotatePos(b.pos(), rotation))
                .toList();

        // Compute bounding box for rotated structure
        int minX = rotatedPositions.stream().mapToInt(BlockPos::getX).min().orElse(0);
        int maxX = rotatedPositions.stream().mapToInt(BlockPos::getX).max().orElse(0);
        int minZ = rotatedPositions.stream().mapToInt(BlockPos::getZ).min().orElse(0);
        int maxZ = rotatedPositions.stream().mapToInt(BlockPos::getZ).max().orElse(0);

        // Rotate the translation itself according to player rotation
        double transX = translation.x;
        double transZ = translation.z;
        switch (rotation) {
            case NONE -> {}
            case CLOCKWISE_90 -> {
                double tmp = transX;
                transX = -transZ;
                transZ = tmp;
            }
            case CLOCKWISE_180 -> {
                transX = -transX;
                transZ = -transZ;
            }
            case COUNTERCLOCKWISE_90 -> {
                double tmp = transX;
                transX = transZ;
                transZ = -tmp;
            }
        }

        double offsetX = -((minX + maxX) / 2.0) + transX;
        double offsetZ = -((minZ + maxZ) / 2.0) + transZ;
        double offsetY = -1.0 + translation.y; // downward + per-item Y offset

        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();

        poseStack.pushPose();
        poseStack.translate(
                origin.getX() - cam.x + offsetX,
                origin.getY() - cam.y + offsetY,
                origin.getZ() - cam.z + offsetZ
        );

        for (int i = 0; i < blocks.size(); i++) {
            StructureJsonLoader.BlockInfo info = blocks.get(i);
            BlockPos rotatedPos = rotatedPositions.get(i);

            BlockState state = info.block().defaultBlockState();
            BakedModel model = dispatcher.getBlockModel(state);

            poseStack.pushPose();
            poseStack.translate(rotatedPos.getX(), rotatedPos.getY(), rotatedPos.getZ());

            dispatcher.getModelRenderer().renderModel(
                    poseStack.last(),
                    buffer.getBuffer(RenderType.translucent()),
                    state,
                    model,
                    0.5f, 0.5f, 1.0f,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    RenderType.translucent()
            );

            poseStack.popPose();
        }

        poseStack.popPose();
    }
}
