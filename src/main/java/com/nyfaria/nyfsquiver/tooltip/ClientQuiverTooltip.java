package com.nyfaria.nyfsquiver.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.items.QuiverType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientQuiverTooltip implements ClientTooltipComponent {
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(NyfsQuiver.MODID, "textures/gui/tooltip.png");
    private static final int MARGIN_Y = 4;
    private static final int BORDER_WIDTH = 1;
    private static final int TEX_SIZE = 128;
    private static final int SLOT_SIZE_X = 18;
    private static final int SLOT_SIZE_Y = 20;
    private final NonNullList<ItemStack> items;
    private final int weight;
    private final QuiverType quiverType;

    public ClientQuiverTooltip(QuiverTooltip p_169873_) {
        this.items = p_169873_.getItems();
        this.weight = p_169873_.getWeight();
        this.quiverType = p_169873_.getQuiverType();
    }

    public int getHeight() {
        return this.gridSizeY() * 20 + 2 + 4;
    }

    public int getWidth(Font p_169901_) {
        return this.gridSizeX() * 18 + 2;
    }


    public void renderImage(Font p_194042_, int p_194043_, int p_194044_,  GuiGraphics p_194046_) {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        boolean flag = this.weight >= 64;
        int k = 0;

        for (int l = 0; l < j; ++l) {
            for (int i1 = 0; i1 < i; ++i1) {
                int j1 = p_194043_ + i1 * 18 + 1;
                int k1 = p_194044_ + l * 20 + 1;
                this.renderSlot(j1, k1, k++, flag, p_194042_, p_194046_.pose(), p_194046_,1);
            }
        }

        this.drawBorder(p_194043_, p_194044_, i, j, p_194046_.pose(), 1,p_194046_);
    }

    private void renderSlot(int p_194027_, int p_194028_, int p_194029_, boolean p_194030_, Font p_194031_, PoseStack p_194032_, GuiGraphics p_194033_, int p_194034_) {
        if (p_194029_ >= this.items.size()) {
            this.blit(p_194032_, p_194027_, p_194028_, p_194034_, p_194030_ ? ClientQuiverTooltip.Texture.BLOCKED_SLOT : ClientQuiverTooltip.Texture.SLOT,p_194033_);
        } else {
            ItemStack itemstack = this.items.get(p_194029_);
            this.blit(p_194032_, p_194027_, p_194028_, p_194034_, ClientQuiverTooltip.Texture.SLOT,p_194033_);
            p_194033_.renderItem(itemstack, p_194027_ + 1, p_194028_ + 1, p_194029_);
            p_194033_.renderItemDecorations(p_194031_, itemstack, p_194027_ + 1, p_194028_ + 1);
            if (p_194029_ == 0) {
                AbstractContainerScreen.renderSlotHighlight(p_194033_, p_194027_ + 1, p_194028_ + 1, p_194034_);
            }

        }
    }

    private void drawBorder(int p_194020_, int p_194021_, int p_194022_, int p_194023_, PoseStack p_194024_, int p_194025_, GuiGraphics graphics) {
        this.blit(p_194024_, p_194020_, p_194021_, p_194025_, ClientQuiverTooltip.Texture.BORDER_CORNER_TOP,graphics);
        this.blit(p_194024_, p_194020_ + p_194022_ * 18 + 1, p_194021_, p_194025_, ClientQuiverTooltip.Texture.BORDER_CORNER_TOP,graphics);

        for (int i = 0; i < p_194022_; ++i) {
            this.blit(p_194024_, p_194020_ + 1 + i * 18, p_194021_, p_194025_, ClientQuiverTooltip.Texture.BORDER_HORIZONTAL_TOP,graphics);
            this.blit(p_194024_, p_194020_ + 1 + i * 18, p_194021_ + p_194023_ * 20, p_194025_, ClientQuiverTooltip.Texture.BORDER_HORIZONTAL_BOTTOM,graphics);
        }

        for (int j = 0; j < p_194023_; ++j) {
            this.blit(p_194024_, p_194020_, p_194021_ + j * 20 + 1, p_194025_, ClientQuiverTooltip.Texture.BORDER_VERTICAL,graphics);
            this.blit(p_194024_, p_194020_ + p_194022_ * 18 + 1, p_194021_ + j * 20 + 1, p_194025_, ClientQuiverTooltip.Texture.BORDER_VERTICAL,graphics);
        }

        this.blit(p_194024_, p_194020_, p_194021_ + p_194023_ * 20, p_194025_, ClientQuiverTooltip.Texture.BORDER_CORNER_BOTTOM,graphics);
        this.blit(p_194024_, p_194020_ + p_194022_ * 18 + 1, p_194021_ + p_194023_ * 20, p_194025_, ClientQuiverTooltip.Texture.BORDER_CORNER_BOTTOM,graphics);
    }

    private void blit(PoseStack p_194036_, int p_194037_, int p_194038_, int p_194039_, ClientQuiverTooltip.Texture p_194040_, GuiGraphics graphics) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        graphics.blit(TEXTURE_LOCATION, p_194037_, p_194038_, (float) p_194040_.x, (float) p_194040_.y, p_194040_.w, p_194040_.h, 128, 128);
    }

    private int gridSizeX() {
        return quiverType.getColumns();
    }

    private int gridSizeY() {
        return quiverType.getRows();
    }

    @OnlyIn(Dist.CLIENT)
    enum Texture {
        SLOT(0, 0, 18, 20),
        BLOCKED_SLOT(0, 40, 18, 20),
        BORDER_VERTICAL(0, 18, 1, 20),
        BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
        BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
        BORDER_CORNER_TOP(0, 20, 1, 1),
        BORDER_CORNER_BOTTOM(0, 60, 1, 1);

        public final int x;
        public final int y;
        public final int w;
        public final int h;

        Texture(int p_169928_, int p_169929_, int p_169930_, int p_169931_) {
            this.x = p_169928_;
            this.y = p_169929_;
            this.w = p_169930_;
            this.h = p_169931_;
        }
    }
}
