package com.nyfaria.nyfsquiver.client.render.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nyfaria.nyfsquiver.NyfsQuiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import javax.swing.text.html.parser.Entity;

public class QuiverModel<T extends LivingEntity> extends EntityModel<T> {
	private final ModelRenderer arrows;
	private final ModelRenderer bone;
	public boolean displayArrows;
	private final ModelRenderer quiver_r1;

	public QuiverModel(boolean displayArrows) {
		texWidth = 32;
		texHeight = 32;

		arrows = new ModelRenderer(this);
		arrows.setPos(-11.0F, 8.0F, 8.0F);
		setRotationAngle(arrows, 0.0F, 0.0F, 0.7854F);
		arrows.texOffs(16, 0).addBox(4.0F, -18.0F, -14.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
		arrows.texOffs(16, 2).addBox(5.0F, -19.0F, -15.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);
		arrows.texOffs(16, 2).addBox(7.0F, -19.0F, -15.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);
		arrows.texOffs(16, 8).addBox(3.0F, -19.0F, -11.0F, 6.0F, 3.0F, 0.0F, 0.0F, false);
		arrows.texOffs(16, 8).addBox(3.0F, -19.0F, -13.0F, 6.0F, 3.0F, 0.0F, 0.0F, false);

		bone = new ModelRenderer(this);
		bone.setPos(0.0F, 24.0F, 0.0F);


		quiver_r1 = new ModelRenderer(this);
		quiver_r1.setPos(0.0F, -7.0F, -12.0F);
		bone.addChild(quiver_r1);
		setRotationAngle(quiver_r1, 0.0F, 0.0F, 0.7854F);
		quiver_r1.texOffs(0, 0).addBox(-10.0F, -12.5F, 6.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
	}

    @Override
    public void setupAnim(T pEntityIn, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

    }


    @Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){



		if(!displayArrows) {
			arrows.render(matrixStack, buffer, packedLight, packedOverlay);
		}
		bone.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}