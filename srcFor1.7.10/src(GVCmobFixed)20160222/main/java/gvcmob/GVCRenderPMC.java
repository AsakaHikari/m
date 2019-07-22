
package gvcmob; 

 
import net.minecraft.client.renderer.entity.RenderBiped; 
import net.minecraft.entity.EntityLiving; 
import net.minecraft.util.ResourceLocation; 

 

public class GVCRenderPMC extends RenderBiped 
{ 
	
	private static final ResourceLocation skeletonTextures = new ResourceLocation("gvcmob:textures/mob/pmc.png"); 

 
 	public GVCRenderPMC() 
	{ 
 		
 		super(new GVCModelGuerrilla(), 0.5F); 
	} 
 
 
 	
 	@Override 
 	protected ResourceLocation getEntityTexture(EntityLiving par1EntityLiving) 
 	{ 
 		return this.skeletonTextures; 
 	} 
 } 

 