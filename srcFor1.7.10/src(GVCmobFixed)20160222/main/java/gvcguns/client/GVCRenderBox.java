
package gvcguns.client; 

 
import gvcguns.GVCEntityBox;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.RenderBiped; 
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving; 
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation; 

 

public class GVCRenderBox extends RenderLiving
{ 
	
	private static final ResourceLocation skeletonTextures = new ResourceLocation("gvcguns:textures/entity/mob/box.png"); 
	private float scale;
 
 	public GVCRenderBox() 
	{ 
 		 
 		super(new GVCModelBox(), 0.5F); 
 		this.scale = 1;
	} 
 	
 	protected void preRenderScale(GVCEntityBox par1EntityGhast, float par2)
    {
    	float scale = GVCEntityBox.getMobScale()*2f;
        GL11.glScalef(this.scale, this.scale, this.scale);
    }

 	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
    {
    	this.preRenderScale((GVCEntityBox)par1EntityLivingBase, par2);
        float scale = GVCEntityBox.getMobScale()*0.3f;
        GL11.glScalef(scale, scale, scale);
    }
 	
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.skeletonTextures; 
	} 
 } 
