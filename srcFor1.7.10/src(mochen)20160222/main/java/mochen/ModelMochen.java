package mochen;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
 
public class ModelMochen extends ModelBase {
	//variables init:
    public ModelRenderer ball1;
    public ModelRenderer ball2;
    public ModelRenderer bottom;
    public ModelRenderer ear1;
    public ModelRenderer ear2;
    public ModelRenderer main;
    public ModelRenderer ring;
    public ModelRenderer tail1;
    public ModelRenderer tail2;
    public ModelRenderer top;
    public ModelRenderer face;
    //fields
    ModelRenderer main_hat;
    ModelRenderer upper1_1;
    ModelRenderer slope1_1;
    ModelRenderer slope1_2;
    ModelRenderer slope1_3;
    ModelRenderer slope1_4;
    ModelRenderer lower1_1;
    ModelRenderer lower1_2;
    ModelRenderer upper2_1;
    ModelRenderer slope2_1;
    ModelRenderer slope2_2;
    ModelRenderer slope2_3;
    ModelRenderer slope2_4;
    ModelRenderer lower2_1;
    ModelRenderer lower2_2;
    ModelRenderer upper3_1;
    ModelRenderer upper3_2;
    ModelRenderer upper3_3;
    ModelRenderer slope3_1;
    ModelRenderer slope3_2;
    ModelRenderer slope3_3;
    ModelRenderer slope3_4;
    ModelRenderer lower3_1;
    ModelRenderer lower3_2;
    ModelRenderer upper4_1;
    ModelRenderer upper4_2;
    ModelRenderer upper4_3;
    ModelRenderer slope4_1;
    ModelRenderer slope4_2;
    ModelRenderer slope4_3;
    ModelRenderer slope4_4;
    ModelRenderer lower4_1;
    ModelRenderer lower4_2;
  
 
    public ModelMochen() {
    	this.textureHeight=256;
    	this.textureWidth=256;

        //constructor:
        ball1 = new ModelRenderer(this, 114, 7);
        ball1.addBox(-1.5F, -1F, -1.5F, 3, 2, 3);
        ball1.setRotationPoint(7F, 8, -9F);
        ball1.rotateAngleY = 0.5235987755982988F;

        ball2 = new ModelRenderer(this,114, 19);
        ball2.addBox(-1.5F, -1F, -1.5F, 3, 2, 3);
        ball2.setRotationPoint(-7F, 8F, -9F);
        ball2.rotateAngleY = 5.759586531581287F;

        bottom = new ModelRenderer(this, 0, 91);
        bottom.addBox(-13.0F, 8.0F, -13.0F, 26, 2, 26);
        bottom.setRotationPoint(0F, 15F, 0F);

        ear1 = new ModelRenderer(this,114, 0);
        ear1.addBox(-3, -3F, -0.5f, 6, 6, 1);
        ear1.setRotationPoint(8F, 9F, -9F);
        ear1.rotateAngleX = -0.5235987755982988F;
        ear1.rotateAngleY = -0.5235987755982988F;
        ear1.rotateAngleZ = -5.497787143782138F;

        ear2 = new ModelRenderer(this,114, 12);
        ear2.addBox(-3, -3F, -0.5f, 6, 6, 1);
        ear2.setRotationPoint(-8F, 9F, -9F);
        ear2.rotateAngleX = -5.759586531581287F;
        ear2.rotateAngleY = -5.759586531581287F;
        ear2.rotateAngleZ = -5.497787143782138F;

        main = new ModelRenderer(this,0, 52);
        main.addBox(-15.0F, -1.0F, -15.0F, 30, 9, 30);
        main.setRotationPoint(0F, 15F, 0F);

        ring = new ModelRenderer(this,80, 0);
        ring.addBox(0F, 0F, -2F, 1,1,4);
        ring.setTextureOffset(80, 5).addBox(1f, 0f ,-2f, 4, 1 ,1);
        ring.setTextureOffset(90, 0).addBox(0f, 0f ,2f, 4, 1 ,1);
        ring.setTextureOffset(90, 2).addBox(4f, 0f ,-1f, 1, 1 ,4);
        ring.setRotationPoint(9F, 6F, -5F);
        ring.rotateAngleY = 0.5235987755982988F;

        tail1 = new ModelRenderer(this,0, 125);
        tail1.addBox(-1.5F, 0F, -1.5F, 3, 3, 20);
        tail1.setRotationPoint(1F, 16F, 14F);
        tail1.rotateAngleY=0.3f;

        tail2 = new ModelRenderer(this,46, 125);
        tail2.addBox(-1.5F, 0F, -1.5F, 3, 3, 20);
        tail2.setRotationPoint(-1F, 16F, 14F);
        tail2.rotateAngleY=-0.3f;

        top = new ModelRenderer(this,0, 0);
        top.addBox(-9F, -6F, -9F, 18, 3, 18);
        top.setRotationPoint(0F, 15F, 0F);

        face = new ModelRenderer(this,0, 21);
        face.addBox(-13.0F, -6.0F, -13.0F, 26, 5, 26);
        face.setRotationPoint(0F, 15F, 0F);
        

        main_hat = new ModelRenderer(this, 0, 217);
        main_hat.addBox(-10F, -2.5F, -10F, 20, 5, 20);
        main_hat.setRotationPoint(0F, 0F, 0F);
        
        upper1_1 = new ModelRenderer(this, 0, 242);
        upper1_1.addBox(-5F, -0.5F, -3F, 5, 1, 6);
        upper1_1.setRotationPoint(-9F, 1F, 0F);
        
        slope1_1 = new ModelRenderer(this, 22, 242);
        slope1_1.addBox(-5F, -0.5F, -1F, 5, 1, 2);
        slope1_1.setRotationPoint(-9F, 2F, -7.5F);
        setRotation(slope1_1, 1.047198F, 0F, 0F);
        
        slope1_2 = new ModelRenderer(this, 22, 245);
        slope1_2.addBox(-5F, -0.5F, -1F, 5, 1, 2);
        slope1_2.setRotationPoint(-9F, 2F, -2.5F);
        setRotation(slope1_2, -1.047198F, 0F, 0F);
        
        slope1_3 = new ModelRenderer(this, 36, 242);
        slope1_3.addBox(-5F, -0.5F, -1F, 5, 1, 2);
        slope1_3.setRotationPoint(-9F, 2F, 2.5F);
        setRotation(slope1_3, 1.047198F, 0F, 0F);
        
        slope1_4 = new ModelRenderer(this, 36, 245);
        slope1_4.addBox(-5F, -0.5F, -1F, 5, 1, 2);
        slope1_4.setRotationPoint(-9F, 2F, 7.5F);
        setRotation(slope1_4, -1.047198F, 0F, 0F);
        
        lower1_1 = new ModelRenderer(this, 50, 242);
        lower1_1.addBox(-5F, -0.5F, -3F, 5, 1, 6);
        lower1_1.setRotationPoint(-9F, 2F, -5F);
        setRotation(lower1_1, 0F, 0F, -0.2617994F);
        
        lower1_2 = new ModelRenderer(this, 72, 242);
        lower1_2.addBox(-5F, -0.5F, -3F, 5, 1, 6);
        lower1_2.setRotationPoint(-9F, 2F, 5F);
        setRotation(lower1_2, 0F, 0F, -0.2617994F);
        
        upper2_1 = new ModelRenderer(this, 0, 249);
        upper2_1.addBox(0F, -0.5F, -3F, 5, 1, 6);
        upper2_1.setRotationPoint(9F, 1F, 0F);
        
        slope2_1 = new ModelRenderer(this, 22, 249);
        slope2_1.addBox(0F, -0.5F, -1F, 5, 1, 2);
        slope2_1.setRotationPoint(9F, 2F, -7.5F);
        setRotation(slope2_1, 1.047198F, 0F, 0F);
        
        slope2_2 = new ModelRenderer(this, 22, 252);
        slope2_2.addBox(0F, -0.5F, -1F, 5, 1, 2);
        slope2_2.setRotationPoint(9F, 2F, -2.5F);
        setRotation(slope2_2, -1.047198F, 0F, 0F);
        
        slope2_3 = new ModelRenderer(this, 36, 249);
        slope2_3.addBox(0F, -0.5F, -1F, 5, 1, 2);
        slope2_3.setRotationPoint(9F, 2F, 2.5F);
        setRotation(slope2_3, 1.047198F, 0F, 0F);
        
        slope2_4 = new ModelRenderer(this, 36, 252);
        slope2_4.addBox(0F, -0.5F, -1F, 5, 1, 2);
        slope2_4.setRotationPoint(9F, 2F, 7.5F);
        setRotation(slope2_4, -1.047198F, 0F, 0F);
        
        lower2_1 = new ModelRenderer(this, 50, 249);
        lower2_1.addBox(0F, -0.5F, -3F, 5, 1, 6);
        lower2_1.setRotationPoint(9F, 2F, -5F);
        setRotation(lower2_1, 0F, 0F, 0.2617994F);
        
        lower2_2 = new ModelRenderer(this, 72, 249);
        lower2_2.addBox(0F, -0.5F, -3F, 5, 1, 6);
        lower2_2.setRotationPoint(9F, 2F, 5F);
        setRotation(lower2_2, 0F, 0F, 0.2617994F);
        /*
        upper3_1 = new ModelRenderer(this, 94, 224);
        upper3_1.addBox(-5F, -0.5F, -5F, 7, 1, 7);
        upper3_1.setRotationPoint(-9F, 2F, -9F);
        setRotation(upper3_1, -0.3f, 0.0f, 0.3f);
        */
        upper3_2 = new ModelRenderer(this, 122, 234);
        upper3_2.addBox(-3F, -0.5F, -5F, 6, 1, 5);
        upper3_2.setRotationPoint(0F, 1F, -9F);
        /*
        upper3_3 = new ModelRenderer(this, 94, 232);
        upper3_3.addBox(-2F, -0.5F, -5F, 7, 1, 7);
        upper3_3.setRotationPoint(9F, 2F, -9F);
        */
        slope3_1 = new ModelRenderer(this, 144, 228);
        slope3_1.addBox(-1F, -0.5F, -5F, 2, 1, 5);
        slope3_1.setRotationPoint(-7.5F, 2F, -9F);
        setRotation(slope3_1, 0F, 0F, -1.047198F);
        
        slope3_2 = new ModelRenderer(this, 144, 234);
        slope3_2.addBox(-1F, -0.5F, -5F, 2, 1, 5);
        slope3_2.setRotationPoint(-2.5F, 2F, -9F);
        setRotation(slope3_2, 0F, 0F, 1.047198F);
        
        slope3_3 = new ModelRenderer(this, 158, 228);
        slope3_3.addBox(-1F, -0.5F, -5F, 2, 1, 5);
        slope3_3.setRotationPoint(2.5F, 2F, -9F);
        setRotation(slope3_3, 0F, 0F, -1.047198F);
        
        slope3_4 = new ModelRenderer(this, 158, 234);
        slope3_4.addBox(-1F, -0.5F, -5F, 2, 1, 5);
        slope3_4.setRotationPoint(7.5F, 2F, -9F);
        setRotation(slope3_4, 0F, 0F, 1.047198F);
        
        lower3_1 = new ModelRenderer(this, 172, 226);
        lower3_1.addBox(-3F, -0.5F, -5F, 6, 1, 6);
        lower3_1.setRotationPoint(-5F, 2F, -9F);
        setRotation(lower3_1, 0.2617994F, 0F, 0F);
        
        lower3_2 = new ModelRenderer(this, 172, 233);
        lower3_2.addBox(-3F, -0.5F, -5F, 6, 1, 6);
        lower3_2.setRotationPoint(5F, 2F, -9F);
        setRotation(lower3_2, 0.2617994F, 0F, 0F);
        
        upper4_1 = new ModelRenderer(this, 94, 240);
        upper4_1.addBox(-5F, -0.5F, -2F, 7, 1, 7);
        upper4_1.setRotationPoint(-9F, 1F, 9F);
        
        upper4_2 = new ModelRenderer(this, 122, 250);
        upper4_2.addBox(-3F, -0.5F, 0F, 6, 1, 5);
        upper4_2.setRotationPoint(0F, 1F, 9F);
        
        upper4_3 = new ModelRenderer(this, 94, 248);
        upper4_3.addBox(-2F, -0.5F, -2F, 7, 1, 7);
        upper4_3.setRotationPoint(9F, 1F, 9F);
        
        slope4_1 = new ModelRenderer(this, 144, 244);
        slope4_1.addBox(-1F, -0.5F, 0F, 2, 1, 5);
        slope4_1.setRotationPoint(-7.5F, 2F, 9F);
        setRotation(slope4_1, 0F, 0F, -1.047198F);
        
        slope4_2 = new ModelRenderer(this, 144, 250);
        slope4_2.addBox(-1F, -0.5F, 0F, 2, 1, 5);
        slope4_2.setRotationPoint(-2.5F, 2F, 9F);
        setRotation(slope4_2, 0F, 0F, 1.047198F);
        
        slope4_3 = new ModelRenderer(this, 158, 244);
        slope4_3.addBox(-1F, -0.5F, 0F, 2, 1, 5);
        slope4_3.setRotationPoint(2.5F, 2F, 9F);
        setRotation(slope4_3, 0F, 0F, -1.047198F);
        
        slope4_4 = new ModelRenderer(this, 158, 250);
        slope4_4.addBox(-1F, -0.5F, 0F, 2, 1, 5);
        slope4_4.setRotationPoint(7.5F, 2F, 9F);
        setRotation(slope4_4, 0F, 0F, 1.047198F);
        
        lower4_1 = new ModelRenderer(this, 172, 242);
        lower4_1.addBox(-3F, -0.5F, 0F, 6, 1, 6);
        lower4_1.setRotationPoint(-5F, 2F, 9F);
        setRotation(lower4_1, -0.2617994F, 0F, 0F);
        
        lower4_2 = new ModelRenderer(this, 172, 249);
        lower4_2.addBox(-3F, -0.5F, 0F, 6, 1, 6);
        lower4_2.setRotationPoint(5F, 2F, 9F);
        setRotation(lower4_2, -0.2617994F, 0F, 0F);
    }
 
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
    	this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    	super.render(entity, f, f1, f2, f3, f4, f5);
    	byte i=((EntityMochen)entity).getAmountNum();
    	//System.out.println(i);
    	GL11.glScalef(0.4f,0.4f,0.4f);
    	GL11.glTranslatef(0.0f, (35f+3.0f*i)*f5, 0.0f);
    	GL11.glPushMatrix();
    	GL11.glScalef(1+0.2f*i, 1-0.15f*i, 1f+0.2f*i); 
    	GL11.glTranslatef(0.0f, (0.5f+0.5f*i)*f5, 0.0f);
        bottom.render(f5);
        main.render(f5);
       // top.render(f5);
        face.render(f5);
        GL11.glPopMatrix();
        ball1.render(f5);
        ball2.render(f5);
        ear1.render(f5);
        ear2.render(f5);
        ring.render(f5);
        GL11.glTranslatef(0.0f,-1.0f*f5*i,0.0f);
        tail1.render(f5);
        tail2.render(f5);
        GL11.glPushMatrix();
        GL11.glScalef(0.9f,1.2f,0.9f);
        GL11.glTranslatef(0.0F,4.0f*f5, 0.0F);
        main_hat.render(f5);
        upper1_1.render(f5);
        slope1_1.render(f5);
        slope1_2.render(f5);
        slope1_3.render(f5);
        slope1_4.render(f5);
        lower1_1.render(f5);
        lower1_2.render(f5);
        upper2_1.render(f5);
        slope2_1.render(f5);
        slope2_2.render(f5);
        slope2_3.render(f5);
        slope2_4.render(f5);
        lower2_1.render(f5);
        lower2_2.render(f5);
       // upper3_1.render(f5);
        upper3_2.render(f5);
        //upper3_3.render(f5);
        slope3_1.render(f5);
        slope3_2.render(f5);
        slope3_3.render(f5);
        slope3_4.render(f5);
        lower3_1.render(f5);
        lower3_2.render(f5);
        upper4_1.render(f5);
        upper4_2.render(f5);
        upper4_3.render(f5);
        slope4_1.render(f5);
        slope4_2.render(f5);
        slope4_3.render(f5);
        slope4_4.render(f5);
        lower4_1.render(f5);
        lower4_2.render(f5);
        GL11.glPopMatrix();
    }
  
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
    	this.tail1.rotateAngleX=0.3f*MathHelper.sin(p_78087_3_*0.1f);
    	this.tail2.rotateAngleX=0.3f*MathHelper.sin(p_78087_3_*0.15f);
    	this.tail1.rotateAngleY=0.3f+0.1f*MathHelper.sin(p_78087_3_*0.08f);
    	this.tail2.rotateAngleY=-0.3f-0.1f*MathHelper.sin(p_78087_3_*0.11f);
    }
   
   private void setRotation(ModelRenderer model, float x, float y, float z)
   {
     model.rotateAngleX = x;
     model.rotateAngleY = y;
     model.rotateAngleZ = z;
   }
   

 }


