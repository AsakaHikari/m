// Date: 2015/04/05 11:52:48
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package gvcguns.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GVCModelItemRPK74 extends ModelBase
{
  //fields
    ModelRenderer Grip;
    ModelRenderer Re1;
    ModelRenderer Re2;
    ModelRenderer Hand1;
    ModelRenderer Hand2;
    ModelRenderer Bre1;
    ModelRenderer Bre2;
    ModelRenderer saito1;
    ModelRenderer saito2;
    ModelRenderer magazine;
    ModelRenderer Suto1;
    ModelRenderer Suto2;
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
  
  public GVCModelItemRPK74()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      Grip = new ModelRenderer(this, 0, 0);
      Grip.addBox(0F, 0F, 0F, 2, 5, 2);
      Grip.setRotationPoint(0F, 1F, 0F);
      Grip.setTextureSize(64, 32);
      Grip.mirror = true;
      setRotation(Grip, 0.1745329F, 0F, 0F);
      Re1 = new ModelRenderer(this, 9, 0);
      Re1.addBox(0F, 0F, 0F, 2, 2, 9);
      Re1.setRotationPoint(0F, -1F, -6F);
      Re1.setTextureSize(64, 32);
      Re1.mirror = true;
      setRotation(Re1, 0F, 0F, 0F);
      Re2 = new ModelRenderer(this, 34, 0);
      Re2.addBox(0F, 0F, 0F, 2, 1, 10);
      Re2.setRotationPoint(0F, -2F, -8F);
      Re2.setTextureSize(64, 32);
      Re2.mirror = true;
      setRotation(Re2, 0F, 0F, 0F);
      Hand1 = new ModelRenderer(this, 0, 13);
      Hand1.addBox(0F, 0F, 0F, 2, 2, 5);
      Hand1.setRotationPoint(0F, -1F, -11F);
      Hand1.setTextureSize(64, 32);
      Hand1.mirror = true;
      setRotation(Hand1, 0F, 0F, 0F);
      Hand2 = new ModelRenderer(this, 14, 13);
      Hand2.addBox(0F, 0F, 0F, 2, 1, 3);
      Hand2.setRotationPoint(0F, -2F, -11F);
      Hand2.setTextureSize(64, 32);
      Hand2.mirror = true;
      setRotation(Hand2, 0F, 0F, 0F);
      Bre1 = new ModelRenderer(this, 18, 11);
      Bre1.addBox(-0.5F, 0F, 0F, 1, 1, 13);
      Bre1.setRotationPoint(1F, -0.5F, -24F);
      Bre1.setTextureSize(64, 32);
      Bre1.mirror = true;
      setRotation(Bre1, 0F, 0F, 0F);
      Bre2 = new ModelRenderer(this, 35, 13);
      Bre2.addBox(-0.5F, 0F, 0F, 1, 2, 3);
      Bre2.setRotationPoint(1F, -2F, -14F);
      Bre2.setTextureSize(64, 32);
      Bre2.mirror = true;
      setRotation(Bre2, 0F, 0F, 0F);
      saito1 = new ModelRenderer(this, 44, 13);
      saito1.addBox(-0.5F, 0F, 0F, 1, 2, 1);
      saito1.setRotationPoint(1F, -2.5F, -22F);
      saito1.setTextureSize(64, 32);
      saito1.mirror = true;
      setRotation(saito1, 0F, 0F, 0F);
      saito2 = new ModelRenderer(this, 44, 16);
      saito2.addBox(-0.5F, 0F, 0F, 1, 1, 2);
      saito2.setRotationPoint(1F, -3F, -9F);
      saito2.setTextureSize(64, 32);
      saito2.mirror = true;
      setRotation(saito2, 0F, 0F, 0F);
      magazine = new ModelRenderer(this, 51, 13);
      magazine.addBox(0F, 0F, 0F, 2, 9, 3);
      magazine.setRotationPoint(0F, 1F, -5F);
      magazine.setTextureSize(64, 32);
      magazine.mirror = true;
      setRotation(magazine, -0.3316126F, 0F, 0F);
      Suto1 = new ModelRenderer(this, 0, 22);
      Suto1.addBox(0F, 0F, 0F, 2, 2, 8);
      Suto1.setRotationPoint(0F, -1F, 3F);
      Suto1.setTextureSize(64, 32);
      Suto1.mirror = true;
      setRotation(Suto1, -0.1570796F, 0F, 0F);
      Suto2 = new ModelRenderer(this, 21, 25);
      Suto2.addBox(0F, 0F, 0F, 2, 2, 5);
      Suto2.setRotationPoint(0F, -1F, 6F);
      Suto2.setTextureSize(64, 32);
      Suto2.mirror = true;
      setRotation(Suto2, 0F, 0F, 0F);
      Shape1 = new ModelRenderer(this, 51, 26);
      Shape1.addBox(-0.5F, 0F, 0F, 1, 2, 2);
      Shape1.setRotationPoint(1F, 1F, -2F);
      Shape1.setTextureSize(64, 32);
      Shape1.mirror = true;
      setRotation(Shape1, 0F, 0F, 0F);
      Shape2 = new ModelRenderer(this, 37, 25);
      Shape2.addBox(-0.5F, 0F, 0F, 1, 2, 0);
      Shape2.setRotationPoint(1F, -4F, -21.5F);
      Shape2.setTextureSize(64, 32);
      Shape2.mirror = true;
      setRotation(Shape2, 0F, 0F, 0F);
      Shape3 = new ModelRenderer(this, 40, 25);
      Shape3.addBox(-0.5F, 0F, 0F, 1, 1, 0);
      Shape3.setRotationPoint(1F, -4F, -7F);
      Shape3.setTextureSize(64, 32);
      Shape3.mirror = true;
      setRotation(Shape3, 0F, 0F, 0F);
      Shape4 = new ModelRenderer(this, 60, 0);
      Shape4.addBox(-0.5F, 0F, 0F, 1, 8, 1);
      Shape4.setRotationPoint(1F, 0F, -22F);
      Shape4.setTextureSize(64, 32);
      Shape4.mirror = true;
      setRotation(Shape4, 0F, 0F, 0.3490659F);
      Shape5 = new ModelRenderer(this, 60, 0);
      Shape5.addBox(-0.5F, 0F, 0F, 1, 8, 1);
      Shape5.setRotationPoint(1F, 0F, -22F);
      Shape5.setTextureSize(64, 32);
      Shape5.mirror = true;
      setRotation(Shape5, 0F, 0F, -0.3490659F);
      Shape6 = new ModelRenderer(this, 51, 13);
      Shape6.addBox(0F, 0F, 0F, 2, 1, 3);
      Shape6.setRotationPoint(0F, 1F, -5F);
      Shape6.setTextureSize(64, 32);
      Shape6.mirror = true;
      setRotation(Shape6, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    Grip.render(f5);
    Re1.render(f5);
    Re2.render(f5);
    Hand1.render(f5);
    Hand2.render(f5);
    Bre1.render(f5);
    Bre2.render(f5);
    saito1.render(f5);
    saito2.render(f5);
    magazine.render(f5);
    Suto1.render(f5);
    Suto2.render(f5);
    Shape1.render(f5);
    Shape2.render(f5);
    Shape3.render(f5);
    Shape4.render(f5);
    Shape5.render(f5);
    Shape6.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
  }

}
