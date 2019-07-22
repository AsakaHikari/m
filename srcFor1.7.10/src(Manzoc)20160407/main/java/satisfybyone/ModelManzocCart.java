package satisfybyone;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelManzocCart extends ModelBase {
	public ModelRenderer bar;

	public ModelManzocCart() {
		bar = new ModelRenderer(this, 0, 0);

		int width = 16;
		int height = 16;
		int length = 64;
		bar.textureHeight = 32;
		bar.textureWidth = 256;
		bar.addBox(-length / 2, -width / 2, -1, length, width, height, 0f);
		bar.rotateAngleX = ((float) Math.PI / 2F);

	}

	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_,
			float p_78088_4_, float p_78088_5_, float p_78088_6_,
			float p_78088_7_) {
		GL11.glScalef(1.5f, 1.5f, 1.5f);
		bar.render(p_78088_7_);
	}
}
