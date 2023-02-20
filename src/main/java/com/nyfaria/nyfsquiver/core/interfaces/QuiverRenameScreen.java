package com.nyfaria.nyfsquiver.core.interfaces;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import com.nyfaria.nyfsquiver.packets.PacketRename;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class QuiverRenameScreen extends Screen {

    private static final int MAX_NAME_CHARACTER_COUNT = 23;

    private static final ResourceLocation BACKGROUND = new ResourceLocation(NyfsQuiver.MODID, "textures/gui/screen.png");
    private static final int BACKGROUND_WIDTH = 170, BACKGROUND_HEIGHT = 50;
    private final String defaultName;
    private EditBox nameField;
    private String lastTickName;

    private int left, top;

    public QuiverRenameScreen(String defaultName, String name) {
        super(Component.translatable("gui.nyfsquiver.title"));
        this.defaultName = defaultName;
        this.lastTickName = name;
    }

    @Override
    protected void init() {
        this.left = (this.width - BACKGROUND_WIDTH) / 2;
        this.top = (this.height - BACKGROUND_HEIGHT) / 2;

        boolean focused = this.nameField != null && this.nameField.isFocused();
        int width = 150;
        int height = 20;
        this.nameField = new EditBox(this.font, (this.width - width) / 2, this.top + 20, width, height, null);
        this.addWidget(nameField);
        this.nameField.insertText(this.lastTickName);
        this.nameField.setCanLoseFocus(true);
        this.nameField.setFocus(focused);
        this.nameField.setMaxLength(MAX_NAME_CHARACTER_COUNT);
    }

    @Override
    public void tick() {
        this.nameField.tick();
        if (!this.lastTickName.trim().equals(this.nameField.getValue().trim())) {
            this.lastTickName = this.nameField.getValue();
            NyfsQuiver.CHANNEL.sendToServer(new PacketRename(this.lastTickName.trim().isEmpty() || this.lastTickName.trim().equals(this.defaultName) ? null : this.lastTickName.trim()));
        }
    }


    @Override
    public void renderBackground(PoseStack poseStack) {
        RenderSystem.clearColor(1, 1, 1, 1);

        RenderSystem.setShaderTexture(0, BACKGROUND);
        this.blit(poseStack, this.left, this.top, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    }

    @SuppressWarnings({"deprecation", "resource"})
    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.font.draw(poseStack, I18n.get("gui.nyfsquiver.name"), this.nameField.getX() + 2, this.top + 8, 4210752);
        this.nameField.render(poseStack, mouseX, mouseY, partialTicks);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 1) { // text field
            if (mouseX >= this.nameField.getX() && mouseX < this.nameField.getX() + this.nameField.getWidth()
                    && mouseY >= this.nameField.getY() && mouseY < this.nameField.getY() + this.nameField.getHeight())
                this.nameField.setValue("");
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
        return false;
    }

    @SuppressWarnings("resource")
    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_))
            return true;
        InputConstants.Key mouseKey = InputConstants.getKey(p_keyPressed_1_, p_keyPressed_2_);
        if (!this.nameField.isFocused() && (p_keyPressed_1_ == 256 || Minecraft.getInstance().options.keyInventory.isActiveAndMatches(mouseKey))) {
            Minecraft.getInstance().player.closeContainer();
            return true;
        }
        return false;
    }

}