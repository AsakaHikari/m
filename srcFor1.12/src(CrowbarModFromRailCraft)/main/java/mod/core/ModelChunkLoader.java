package mod.core;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelChunkLoader extends ModelBase
{
	//fields
	ModelRenderer core;
	ModelRenderer rotater;

	public ModelChunkLoader()
	{
		textureWidth = 64;
		textureHeight = 32;
		setTextureOffset("rotater.p1", 0, 0);
		setTextureOffset("rotater.p2", 32, 0);
		setTextureOffset("rotater.p3", 0, 0);
		setTextureOffset("rotater.p4", 32, 0);

		core = new ModelRenderer(this, 0, 16);
		core.addBox(-2F, -2F, -2F, 4, 4, 4);
		core.setRotationPoint(0F, 16F, 0F);
		core.setTextureSize(64, 32);
		core.mirror = true;
		setRotation(core, 0F, 0F, 0F);
		rotater = new ModelRenderer(this, "rotater");
		rotater.setRotationPoint(0F, 16F, 0F);
		setRotation(rotater, 0F, 0F, 0F);
		rotater.mirror = true;
		rotater.addBox("p1", 6F, -1F, -8F, 2, 2, 14);
		rotater.addBox("p2", -6F, -1F, 6F, 14, 2, 2);
		rotater.addBox("p3", -8F, -1F, -6F, 2, 2, 14);
		rotater.addBox("p4", -8F, -1F, -8F, 14, 2, 2);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5,entity);
		setRotation(rotater,rotater.rotateAngleX+=0.1,rotater.rotateAngleY+=0.04,rotater.rotateAngleZ+=0.07);
		setRotation(core,core.rotateAngleX-=0.02,core.rotateAngleY-=0.03,core.rotateAngleZ-=0.01);
		core.render(f5);
		rotater.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
