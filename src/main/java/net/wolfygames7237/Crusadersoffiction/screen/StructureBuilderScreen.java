package net.wolfygames7237.Crusadersoffiction.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.wolfygames7237.Crusadersoffiction.CrusadersOfFiction;
import net.minecraft.client.gui.components.Button;
import net.wolfygames7237.Crusadersoffiction.blocks.entity.StructureBuilderBlockEntity;

import java.util.function.Supplier;

public class StructureBuilderScreen extends AbstractContainerScreen<StructureBuilderMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(CrusadersOfFiction.MOD_ID, "textures/gui/structure_builder_gui.png");
    public static final int GRID_WIDTH = 5;
    public static final int GRID_HEIGHT = 4;
    public static final int INPUT_COUNT = GRID_WIDTH * GRID_HEIGHT; // 20

    public StructureBuilderScreen(StructureBuilderMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        // Hide default labels
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;

        int guiX = (width - imageWidth) / 2;
        int guiY = (height - imageHeight) / 2;

        // --- Add Craft button ---
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}