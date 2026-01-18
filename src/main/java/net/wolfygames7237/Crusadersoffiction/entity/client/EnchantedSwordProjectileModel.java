package net.wolfygames7237.Crusadersoffiction.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class EnchantedSwordProjectileModel <T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("crusadersoffiction", "enchanted_sword_projectile_layer"), "main");
    private final ModelPart bb_main;

    public EnchantedSwordProjectileModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(5, 7).addBox(2.0F, 14.5F, 13.0F, -1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.0F, 12.0F, -7.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 48).addBox(-10.0F, 7.5F, -1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0F, -2.0F, 17.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(38, 48).addBox(-8.5F, -1.0F, -1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -2.0F, 9.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(11, 10).addBox(2.5F, -1.0F, 13.0F, -1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 12.0F, -1.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(28, 48).addBox(0.0F, 0.0F, -1.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -2.0F, -6.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition cube_r6 = bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(24, 48).addBox(0.0F, 0.0F, -1.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -2.0F, 7.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition cube_r7 = bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(20, 48).addBox(0.5F, -1.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 48).addBox(1.0F, -1.5F, -2.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -29.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition cube_r8 = bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(34, 48).addBox(1.0F, -1.0F, -1.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -2.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition cube_r9 = bb_main.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(32, 48).addBox(1.0F, -1.0F, -1.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -2.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition cube_r10 = bb_main.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(44, 32).addBox(1.0F, -3.0F, -14.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(44, 26).addBox(0.0F, -2.0F, -14.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(-4, 0).addBox(-1.0F, -2.0F, -6.0F, 4.0F, 0.0F, 22.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(1.0F, -4.0F, -6.0F, 0.0F, 4.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition cube_r11 = bb_main.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(44, 24).addBox(-5.0F, -2.0F, 0.0F, 12.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 48).addBox(0.0F, -7.0F, 0.0F, 2.0F, 12.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(44, 22).addBox(-5.0F, -1.0F, -1.0F, 12.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 40).addBox(1.0F, -7.0F, -1.0F, 0.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition cube_r12 = bb_main.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(4, 48).addBox(0.0F, -1.0F, -1.0F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(14, 48).addBox(1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -25.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
