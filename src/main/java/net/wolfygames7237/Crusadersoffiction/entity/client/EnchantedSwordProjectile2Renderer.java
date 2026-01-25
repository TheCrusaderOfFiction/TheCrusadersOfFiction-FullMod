package net.wolfygames7237.Crusadersoffiction.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.wolfygames7237.Crusadersoffiction.entity.custom.EnchantedSwordProjectileEntity2;
import net.wolfygames7237.Crusadersoffiction.entity.layers.ModModelLayers;

public class EnchantedSwordProjectile2Renderer extends EntityRenderer<EnchantedSwordProjectileEntity2> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(CrusadersOfFiction.MOD_ID, "textures/entity/enchantsword2.png");
    protected EnchantedSwordProjectile2Model model;

    public EnchantedSwordProjectile2Renderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new EnchantedSwordProjectile2Model(pContext.bakeLayer(ModModelLayers.ENCHANTED_SWORD_PROJECTILE2_LAYER));
        this.shadowRadius = 0.5f;
    }

    public void render(EnchantedSwordProjectileEntity2 entity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTick, entity.xRotO, entity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(entity)), false, false);

        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 0.5f);
        pPoseStack.popPose();
        super.render(entity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EnchantedSwordProjectileEntity2 pEntity) {
        return TEXTURE;
    }
}
