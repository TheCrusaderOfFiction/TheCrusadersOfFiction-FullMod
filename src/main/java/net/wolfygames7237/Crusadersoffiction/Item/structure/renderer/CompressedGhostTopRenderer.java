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

import java.util.List;


@Mod.EventBusSubscriber(
        modid = "crusadersoffiction",
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class CompressedGhostTopRenderer {

    private static BlockPos rotatePos(BlockPos pos, Rotation rotation) {
        return switch (rotation) {
            case NONE -> pos;
            case CLOCKWISE_90 -> new BlockPos(-pos.getZ(), pos.getY(), pos.getX());
            case CLOCKWISE_180 -> new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());
            case COUNTERCLOCKWISE_90 -> new BlockPos(pos.getZ(), pos.getY(), -pos.getX());
        };
    }

    private static int getStructureHeight(List<StructureJsonLoader.BlockInfo> blocks, Rotation rotation) {
        int maxY = Integer.MIN_VALUE;

        for (StructureJsonLoader.BlockInfo info : blocks) {
            BlockPos p = rotatePos(info.pos(), rotation);
            maxY = Math.max(maxY, p.getY());
        }

        return maxY;
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

        // Only CompressedPlacerItem subclasses
        if (!className.endsWith("PlacerTopItem") || !className.contains("Compressed")) return;

        // Extract the prefix before "Compressed" and make lowercase
        String prefix = className.substring(0, className.indexOf("Compressed")).toLowerCase();

        // Build JSON structure path
        ResourceLocation jsonStructure = new ResourceLocation(
                "crusadersoffiction",
                "structures/compressed_top" + prefix + ".json"
        );

        if (!(mc.hitResult instanceof BlockHitResult hit)) return;

        BlockPos origin = hit.getBlockPos().above();
        Rotation rotation = switch (player.getDirection()) {
            case SOUTH -> Rotation.CLOCKWISE_90;
            case WEST -> Rotation.CLOCKWISE_180;
            case NORTH -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };

        renderGhost(event.getPoseStack(), origin, rotation, jsonStructure);
    }

    private static void renderGhost(PoseStack poseStack, BlockPos origin, Rotation rotation, ResourceLocation jsonStructure) {
        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
        MultiBufferSource buffer = mc.renderBuffers().bufferSource();

        List<StructureJsonLoader.BlockInfo> blocks =
                StructureJsonLoader.loadBlockInfos(jsonStructure);
        if (blocks.isEmpty()) return;

        // 🔑 Compute top offset
        int topOffset = getStructureHeight(blocks, rotation);

        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();

        poseStack.pushPose();
        poseStack.translate(
                origin.getX() - cam.x,
                origin.getY() - cam.y,
                origin.getZ() - cam.z
        );

        for (StructureJsonLoader.BlockInfo info : blocks) {
            BlockPos p = rotatePos(info.pos(), rotation);

            // 🔑 Shift DOWN so top aligns with origin
            p = p.offset(0, -topOffset, 0);

            BlockState state = info.block().defaultBlockState();
            BakedModel model = dispatcher.getBlockModel(state);

            poseStack.pushPose();
            poseStack.translate(p.getX(), p.getY(), p.getZ());

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
