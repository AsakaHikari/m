// Date: 2015/02/25 23:20:16
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package gvcmob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GVCModelAAG extends ModelBase
{
  //fields
    ModelRenderer head;
    ModelRenderer body;
    ModelRenderer rightarm;
    ModelRenderer leftarm;
    ModelRenderer rightleg;
    ModelRenderer leftleg;
    ModelRenderer Shape2;
    ModelRenderer Shape1;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
  
  public GVCModelAAG()
  {
    textureWidth = 64;
    textureHeight = 128;
    
      head = new ModelRenderer(this, 0, 0);
      head.addBox(-4F, -8F, -4F, 8, 8, 8);
      head.setRotationPoint(0F, 7F, 3F);
      head.setTextureSize(64, 128);
      head.mirror = true;
      setRotation(head, 0F, 0F, 0F);
      body = new ModelRenderer(this, 16, 16);
      body.addBox(-4F, 0F, -2F, 8, 12, 4);
      body.setRotationPoint(0F, 7F, 3F);
      body.setTextureSize(64, 128);
      body.mirror = true;
      setRotation(body, 0F, 0F, 0F);
      rightarm = new ModelRenderer(this, 40, 16);
      rightarm.addBox(-3F, -2F, -2F, 4, 12, 4);
      rightarm.setRotationPoint(-5F, 9F, 3F);
      rightarm.setTextureSize(64, 128);
      rightarm.mirror = true;
      setRotation(rightarm, -1.570796F, 0F, 0F);
      leftarm = new ModelRenderer(this, 40, 16);
      leftarm.addBox(-1F, -2F, -2F, 4, 12, 4);
      leftarm.setRotationPoint(5F, 9F, 3F);
      leftarm.setTextureSize(64, 128);
      leftarm.mirror = true;
      setRotation(leftarm, -1.570796F, 0F, 0F);
      rightleg = new ModelRenderer(this, 0, 16);
      rightleg.addBox(-2F, 0F, -2F, 4, 12, 4);
      rightleg.setRotationPoint(-2F, 20F, 3F);
      rightleg.setTextureSize(64, 128);
      rightleg.mirror = true;
      setRotation(rightleg, -1.570796F, 0.4363323F, 0F);
      leftleg = new ModelRenderer(this, 0, 16);
      leftleg.addBox(-2F, 0F, -2F, 4, 12, 4);
      leftleg.setRotationPoint(2F, 20F, 3F);
      leftleg.setTextureSize(64, 128);
      leftleg.mirror = true;
      setRotation(leftleg, -1.570796F, -0.4363323F, 0F);
      Shape2 = new ModelRenderer(this, 0, 50);
      Shape2.addBox(0F, 0F, 0F, 16, 2, 16);
      Shape2.setRotationPoint(-8F, 22F, -8F);
      Shape2.setTextureSize(64, 128);
      Shape2.mirror = true;
      setRotation(Shape2, 0F, 0F, 0F);
      Shape1 = new ModelRenderer(this, 0, 33);
      Shape1.addBox(0F, 0F, 0F, 18, 14, 2);
      Shape1.setRotationPoint(-9F, 8F, -10F);
      Shape1.setTextureSize(64, 128);
      Shape1.mirror = true;
      setRotation(Shape1, 0F, 0F, 0F);
      Shape3 = new ModelRenderer(this, 0, 69);
      Shape3.addBox(-2F, -2F, -5F, 4, 4, 10);
      Shape3.setRotationPoint(0F, 6F, -10F);
      Shape3.setTextureSize(64, 128);
      Shape3.mirror = true;
      setRotation(Shape3, 0F, 0F, 0F);
      Shape4 = new ModelRenderer(this, 29, 69);
      Shape4.addBox(-1F, -1F, -17F, 2, 2, 12);
      Shape4.setRotationPoint(0F, 6F, -10F);
      Shape4.setTextureSize(64, 128);
      Shape4.mirror = true;
      setRotation(Shape4, 0F, 0F, 0F);
      Shape5 = new ModelRenderer(this, 0, 84);
      Shape5.addBox(0F, 0F, 0F, 6, 8, 2);
      Shape5.setRotationPoint(-9F, 0F, -10F);
      Shape5.setTextureSize(64, 128);
      Shape5.mirror = true;
      setRotation(Shape5, 0F, 0F, 0F);
      Shape6 = new ModelRenderer(this, 0, 84);
      Shape6.addBox(0F, 0F, 0F, 6, 8, 2);
      Shape6.setRotationPoint(3F, 0F, -10F);
      Shape6.setTextureSize(64, 128);
      Shape6.mirror = true;
      setRotation(Shape6, 0F, 0F, 0F);
  }
  
  public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
  {
    //super.render(entity, f, f1, f2, f3, f4, f5);
	  this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
    head.render(par7);
    body.render(par7);
    rightarm.render(par7);
    leftarm.render(par7);
    rightleg.render(par7);
    leftleg.render(par7);
    Shape2.render(par7);
    Shape1.render(par7);
    Shape3.render(par7);
    Shape4.render(par7);
    Shape5.render(par7);
    Shape6.render(par7);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
  {
	  this.head.rotateAngleY = par4 / (180F / (float)Math.PI);
      this.head.rotateAngleX = par5 / (180F / (float)Math.PI);
      
      this.Shape3.rotateAngleY = par4 / (180F / (float)Math.PI);
      this.Shape3.rotateAngleX = par5 / (180F / (float)Math.PI);
      this.Shape4.rotateAngleY = par4 / (180F / (float)Math.PI);
      this.Shape4.rotateAngleX = par5 / (180F / (float)Math.PI);
  }

}
