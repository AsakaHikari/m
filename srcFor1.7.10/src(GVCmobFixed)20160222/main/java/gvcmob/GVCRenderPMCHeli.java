
package gvcmob; 

 
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.RenderBiped; 
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving; 
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation; 

 

public class GVCRenderPMCHeli extends RenderLiving
{ 
	
	private static final ResourceLocation skeletonTextures = new ResourceLocation("gvcmob:textures/mob/heli_pmc.png"); 
	private float scale;
 
 	public GVCRenderPMCHeli() 
	{ 
 		 
 		super(new GVCModelHeli(), 0.5F); 
 		this.scale = 1;
	} 
 	
 	protected void preRenderScale(GVCEntityPMCHeli par1EntityGhast, float par2)
    {
    	float scale = GVCEntityPMCHeli.getMobScale()*2f;
        GL11.glScalef(this.scale, this.scale, this.scale);
    }

 	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
    {
    	this.preRenderScale((GVCEntityPMCHeli)par1EntityLivingBase, par2);
        float scale = GVCEntityPMCHeli.getMobScale()*0.3f;
        GL11.glScalef(scale, scale, scale);
    }
 	
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.skeletonTextures; 
	} 
 } 
