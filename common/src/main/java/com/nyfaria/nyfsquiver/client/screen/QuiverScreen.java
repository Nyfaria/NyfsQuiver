package com.nyfaria.nyfsquiver.client.screen;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.api.Dimension;
import com.nyfaria.nyfsquiver.api.Rectangle;
import com.nyfaria.nyfsquiver.menu.QuiverMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class QuiverScreen extends AbstractContainerScreen<QuiverMenu> {
    private static final Identifier GUI_TEXTURE = Constants.modLoc( "textures/gui/backpack_container.png");
    private static final Identifier SLOT_TEXTURE = Constants.mcLoc("textures/gui/container/hopper.png");

    public QuiverScreen(QuiverMenu handler, Inventory player, Component title) {
        super(handler, player, title, handler.getDimension().getWidth(), handler.getDimension().getHeight());
        Dimension dimension = handler.getDimension();
        this.titleLabelY = 7;
        this.inventoryLabelX = handler.getPlayerInvSlotPosition(dimension, 0, 0).x;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        renderBackgroundTexture(graphics, new Rectangle(x, y, imageWidth, imageHeight));
        for (Slot slot : getMenu().slots) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, SLOT_TEXTURE, x + slot.x - 1, y + slot.y - 1, 43, 19, 18, 18, 256, 256);
        }
    }

    public void renderBackgroundTexture(GuiGraphicsExtractor graphics, Rectangle bounds) {
        int x = bounds.x, y = bounds.y, width = bounds.width, height = bounds.height;
        int xTextureOffset = 0;
        int yTextureOffset = 66;
        // 9 Patch Texture
        // Center
        graphics.blit(GUI_TEXTURE, x + 8, y + 8, x + width - 8, y + height - 8, (114 + xTextureOffset) / 256f, (248 + xTextureOffset) / 256f, (132 + yTextureOffset) / 256f, (182 + yTextureOffset) / 256f);
        // Sides
        graphics.blit(GUI_TEXTURE, x + 8, y, x + width - 8, y + 8, (114 + xTextureOffset) / 256f, (248 + xTextureOffset) / 256f, (124 + yTextureOffset) / 256f, (132 + yTextureOffset) / 256f);
        graphics.blit(GUI_TEXTURE, x + 8, y + height - 8, x + width - 8, y + height, (114 + xTextureOffset) / 256f, (248 + xTextureOffset) / 256f, (182 + yTextureOffset) / 256f, (190 + yTextureOffset) / 256f);
        graphics.blit(GUI_TEXTURE, x, y + 8, x + 8, y + height - 8, (106 + xTextureOffset) / 256f, (114 + xTextureOffset) / 256f, (132 + yTextureOffset) / 256f, (182 + yTextureOffset) / 256f);
        graphics.blit(GUI_TEXTURE, x + width - 8, y + 8, x + width, y + height - 8, (248 + xTextureOffset) / 256f, (256 + xTextureOffset) / 256f, (132 + yTextureOffset) / 256f, (182 + yTextureOffset) / 256f);
        // Four Corners
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 106 + xTextureOffset, 124 + yTextureOffset, 8, 8, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x + width - 8, y, 248 + xTextureOffset, 124 + yTextureOffset, 8, 8, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y + height - 8, 106 + xTextureOffset, 182 + yTextureOffset, 8, 8, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x + width - 8, y + height - 8, 248 + xTextureOffset, 182 + yTextureOffset, 8, 8, 256, 256);
    }
}
