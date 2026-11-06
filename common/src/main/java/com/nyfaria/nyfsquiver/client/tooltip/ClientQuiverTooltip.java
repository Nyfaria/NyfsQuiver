package com.nyfaria.nyfsquiver.client.tooltip;

import com.nyfaria.nyfsquiver.Constants;
import com.nyfaria.nyfsquiver.api.QuiverType;
import com.nyfaria.nyfsquiver.item.tooltip.QuiverTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class ClientQuiverTooltip implements ClientTooltipComponent {
    public static final Identifier TEXTURE_LOCATION = Constants.modLoc( "textures/gui/tooltip.png");
    private static final int MARGIN_Y = 4;
    private static final int BORDER_WIDTH = 1;
    private static final int TEX_SIZE = 128;
    private static final int SLOT_SIZE_X = 18;
    private static final int SLOT_SIZE_Y = 20;
    private final NonNullList<ItemStack> items;
    private final int weight;
    private final QuiverType quiverType;

    public ClientQuiverTooltip(QuiverTooltip quiverTooltip) {
        this.items = quiverTooltip.getItems();
        this.weight = quiverTooltip.getWeight();
        this.quiverType = quiverTooltip.getQuiverType();
    }

    public int getHeight(Font font) {
        return this.gridSizeY() * 20 + 2 + 4;
    }

    public int getWidth(Font p_169901_) {
        return this.gridSizeX() * 18 + 2;
    }


    public void extractImage(Font p_194042_, int p_194043_, int p_194044_, int width, int height, GuiGraphicsExtractor p_194046_) {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        boolean flag = this.weight >= 64;
        int k = 0;

        for (int l = 0; l < j; ++l) {
            for (int i1 = 0; i1 < i; ++i1) {
                int j1 = p_194043_ + i1 * 18 + 1;
                int k1 = p_194044_ + l * 20 + 1;
                this.renderSlot(j1, k1, k++, flag, p_194042_, p_194046_,1);
            }
        }

        this.drawBorder(p_194043_, p_194044_, i, j, 1,p_194046_);
    }

    private void renderSlot(int p_194027_, int p_194028_, int p_194029_, boolean p_194030_, Font p_194031_, GuiGraphicsExtractor p_194033_, int p_194034_) {
        if (p_194029_ >= this.items.size()) {
            this.blit(p_194027_, p_194028_, p_194034_, p_194030_ ? ClientQuiverTooltip.Texture.BLOCKED_SLOT : ClientQuiverTooltip.Texture.SLOT,p_194033_);
        } else {
            ItemStack itemstack = this.items.get(p_194029_);
            this.blit(p_194027_, p_194028_, p_194034_, ClientQuiverTooltip.Texture.SLOT,p_194033_);
            p_194033_.item(itemstack, p_194027_ + 1, p_194028_ + 1, p_194029_);
            p_194033_.itemDecorations(p_194031_, itemstack, p_194027_ + 1, p_194028_ + 1);
            if (p_194029_ == 0) {
                p_194033_.fill(p_194027_ + 1, p_194028_ + 1, p_194027_ + 17, p_194028_ + 17, 0x80FFFFFF);
            }

        }
    }

    private void drawBorder(int p_194020_, int p_194021_, int p_194022_, int p_194023_, int p_194025_, GuiGraphicsExtractor graphics) {
        this.blit(p_194020_, p_194021_, p_194025_, ClientQuiverTooltip.Texture.BORDER_CORNER_TOP,graphics);
        this.blit(p_194020_ + p_194022_ * 18 + 1, p_194021_, p_194025_, ClientQuiverTooltip.Texture.BORDER_CORNER_TOP,graphics);

        for (int i = 0; i < p_194022_; ++i) {
            this.blit(p_194020_ + 1 + i * 18, p_194021_, p_194025_, ClientQuiverTooltip.Texture.BORDER_HORIZONTAL_TOP,graphics);
            this.blit(p_194020_ + 1 + i * 18, p_194021_ + p_194023_ * 20, p_194025_, ClientQuiverTooltip.Texture.BORDER_HORIZONTAL_BOTTOM,graphics);
        }

        for (int j = 0; j < p_194023_; ++j) {
            this.blit(p_194020_, p_194021_ + j * 20 + 1, p_194025_, ClientQuiverTooltip.Texture.BORDER_VERTICAL,graphics);
            this.blit(p_194020_ + p_194022_ * 18 + 1, p_194021_ + j * 20 + 1, p_194025_, ClientQuiverTooltip.Texture.BORDER_VERTICAL,graphics);
        }

        this.blit(p_194020_, p_194021_ + p_194023_ * 20, p_194025_, ClientQuiverTooltip.Texture.BORDER_CORNER_BOTTOM,graphics);
        this.blit(p_194020_ + p_194022_ * 18 + 1, p_194021_ + p_194023_ * 20, p_194025_, ClientQuiverTooltip.Texture.BORDER_CORNER_BOTTOM,graphics);
    }

    private void blit(int p_194037_, int p_194038_, int p_194039_, ClientQuiverTooltip.Texture p_194040_, GuiGraphicsExtractor graphics) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE_LOCATION, p_194037_, p_194038_, (float) p_194040_.x, (float) p_194040_.y, p_194040_.w, p_194040_.h, 128, 128);
    }

    private int gridSizeX() {
        return quiverType.columns();
    }

    private int gridSizeY() {
        return quiverType.rows();
    }


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
