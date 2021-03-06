package com.nyfaria.nyfsquiver.core.interfaces;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.packets.PacketRename;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class QuiverRenameScreen extends Screen {

    private static final int MAX_NAME_CHARACTER_COUNT = 23;

    private static final ResourceLocation BACKGROUND = new ResourceLocation("nyfsquiver", "textures/screen.png");
    private static final int BACKGROUND_WIDTH = 170, BACKGROUND_HEIGHT = 50;

    private TextFieldWidget nameField;
    private final String defaultName;
    private String lastTickName;

    private int left, top;

    public QuiverRenameScreen(String defaultName, String name){
        super(new TranslationTextComponent("gui.nyfsquiver.title"));
        this.defaultName = defaultName;
        this.lastTickName = name;
    }

    @Override
    protected void init(){
        this.left = (this.width - BACKGROUND_WIDTH) / 2;
        this.top = (this.height - BACKGROUND_HEIGHT) / 2;

        boolean focused = this.nameField != null && this.nameField.isFocused();
        int width = 150;
        int height = 20;
        this.children.add(this.nameField = new TextFieldWidget(this.font, (this.width - width) / 2, this.top + 20, width, height, null));
        this.nameField.insertText(this.lastTickName);
        this.nameField.setCanLoseFocus(true);
        this.nameField.setFocus(focused);
        this.nameField.setMaxLength(MAX_NAME_CHARACTER_COUNT);
    }

    @Override
    public void tick(){
        this.nameField.tick();
        if(!this.lastTickName.trim().equals(this.nameField.getValue().trim())){
            this.lastTickName = this.nameField.getValue();
            NyfsQuiver.CHANNEL.sendToServer(new PacketRename(this.lastTickName.trim().isEmpty() || this.lastTickName.trim().equals(this.defaultName) ? null : this.lastTickName.trim()));
        }
    }

    @SuppressWarnings({ "deprecation", "resource" })
	@Override
    public void render(MatrixStack boop, int mouseX, int mouseY, float partialTicks){
        this.renderBackground(boop);

        RenderSystem.pushMatrix();
        RenderSystem.translated(this.left, this.top, 0);
        RenderSystem.color4f(1, 1, 1, 1);
        RenderSystem.enableAlphaTest();

        Minecraft.getInstance().textureManager.bind(BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.vertex(0, 0, 0).uv(0, 0).endVertex();
        builder.vertex(0, BACKGROUND_HEIGHT, 0).uv(0, 1).endVertex();
        builder.vertex(BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 0).uv(1, 1).endVertex();
        builder.vertex(BACKGROUND_WIDTH, 0, 0).uv(1, 0).endVertex();
        tessellator.end();

        RenderSystem.popMatrix();

        this.font.draw(boop, I18n.get("gui.nyfsquiver.name"), this.nameField.x + 2, this.top + 8, 4210752);
        this.nameField.render(boop, mouseX, mouseY, partialTicks);
        super.render(boop, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen(){
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton){
        if(mouseButton == 1){ // text field
            if(mouseX >= this.nameField.x && mouseX < this.nameField.x + this.nameField.getWidth()
                && mouseY >= this.nameField.y && mouseY < this.nameField.y + this.nameField.getHeight())
                this.nameField.setValue("");
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
        return false;
    }

    @SuppressWarnings("resource")
	@Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_){
        if(super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_))
            return true;
        InputMappings.Input mouseKey = InputMappings.getKey(p_keyPressed_1_, p_keyPressed_2_);
        if(!this.nameField.isFocused() && (p_keyPressed_1_ == 256 || Minecraft.getInstance().options.keyInventory.isActiveAndMatches(mouseKey))){
            Minecraft.getInstance().player.closeContainer();
            return true;
        }
        return false;
    }

}