
package gvcmob; 

 
import net.minecraft.client.renderer.entity.RenderBiped; 
import net.minecraft.entity.EntityLiving; 
import net.minecraft.util.ResourceLocation; 

 

public class GVCRenderGuerrillaSG extends RenderBiped 
{ 
	
	private static final ResourceLocation skeletonTextures = new ResourceLocation("gvcmob:textures/mob/guerrillasg.png");
	private static final ResourceLocation skeletonTextures2 = new ResourceLocation("gvcmob:textures/mob/solider.png"); 

 
 	public GVCRenderGuerrillaSG() 
	{ 
 		
 		super(new GVCModelGuerrilla(), 0.5F); 
	} 
 
 
 	
 	@Override 
 	protected ResourceLocation getEntityTexture(EntityLiving par1EntityLiving) 
 	{ 
 		if(GVCMobPlus.cfg_modeGorC){
 			return this.skeletonTextures; 
 		}else{
 			return this.skeletonTextures2; 
 		}
 	} 
 } 

 