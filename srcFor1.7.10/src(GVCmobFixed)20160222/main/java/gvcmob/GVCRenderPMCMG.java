
package gvcmob; 

 
import net.minecraft.client.renderer.entity.RenderBiped; 
import net.minecraft.entity.EntityLiving; 
import net.minecraft.util.ResourceLocation; 

 

public class GVCRenderPMCMG extends RenderBiped 
{ 
	
	private static final ResourceLocation skeletonTextures = new ResourceLocation("gvcmob:textures/mob/pmcmg.png"); 

 
 	public GVCRenderPMCMG() 
	{ 
 		
 		super(new GVCModelGuerrilla(), 0.5F); 
	} 
 
 
 	
 	@Override 
 	protected ResourceLocation getEntityTexture(EntityLiving par1EntityLiving) 
 	{ 
 		return this.skeletonTextures; 
 	} 
 } 

 