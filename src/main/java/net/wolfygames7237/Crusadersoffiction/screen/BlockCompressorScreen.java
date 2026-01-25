package net.wolfygames7237.Crusadersoffiction.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;

public class BlockCompressorScreen extends AbstractContainerScreen<BlockCompressorMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(CrusadersOfFiction.MOD_ID,
                    "textures/gui/block_compressor_gui.png");

    public BlockCompressorScreen(BlockCompressorMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        inventoryLabelY = 10000;
        titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 90, y + 37, 176, 0,menu.getScaledProgress() , 8);
        }
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        renderBackground(gfx);
        super.render(gfx, mouseX, mouseY, delta);
        renderTooltip(gfx, mouseX, mouseY);
    }
}