// Date: 2015/01/30 21:54:18
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package gvcguns.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GVCModelparas extends ModelBase
{
  //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
  
  public GVCModelparas()
  {
    textureWidth = 128;
    textureHeight = 64;
    
      Shape1 = new ModelRenderer(this, 0, 0);
      Shape1.addBox(0F, 0F, 0F, 12, 1, 8);
      Shape1.setRotationPoint(-6F, 23F, -4F);
      Shape1.setTextureSize(128, 64);
      Shape1.mirror = true;
      setRotation(Shape1, 0F, 0F, 0F);
      Shape2 = new ModelRenderer(this, 0, 10);
      Shape2.addBox(-0.5F, 0F, 0F, 1, 30, 1);
      Shape2.setRotationPoint(-23F, 0F, 0F);
      Shape2.setTextureSize(128, 64);
      Shape2.mirror = true;
      setRotation(Shape2, 0F, 0F, -0.6283185F);
      Shape3 = new ModelRenderer(this, 0, 10);
      Shape3.addBox(-0.5F, 0F, 0F, 1, 30, 1);
      Shape3.setRotationPoint(23F, 0F, 0F);
      Shape3.setTextureSize(128, 64);
      Shape3.mirror = true;
      setRotation(Shape3, 0F, 0F, 0.6283185F);
      Shape4 = new ModelRenderer(this, 5, 10);
      Shape4.addBox(0F, 0F, 0F, 46, 6, 12);
      Shape4.setRotationPoint(-23F, -5F, -6F);
      Shape4.setTextureSize(128, 64);
      Shape4.mirror = true;
      setRotation(Shape4, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    Shape1.render(f5);
    Shape2.render(f5);
    Shape3.render(f5);
    Shape4.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    //super.setRotationAngles(f, f1, f2, f3, f4, f5);
  }

}
