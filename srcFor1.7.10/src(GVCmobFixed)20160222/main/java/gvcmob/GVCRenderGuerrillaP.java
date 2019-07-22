
package gvcmob; 

 
import net.minecraft.client.renderer.entity.RenderBiped; 
import net.minecraft.entity.EntityLiving; 
import net.minecraft.util.ResourceLocation; 

 

public class GVCRenderGuerrillaP extends RenderBiped 
{ 
	
	private static final ResourceLocation skeletonTextures = new ResourceLocation("gvcmob:textures/mob/guerrillaP.png"); 
	private static final ResourceLocation skeletonTextures1 = new ResourceLocation("gvcmob:textures/mob/guerrillaP1.png"); 
	private static final ResourceLocation skeletonTextures3 = new ResourceLocation("gvcmob:textures/mob/soliderP.png"); 
	private static final ResourceLocation skeletonTextures4 = new ResourceLocation("gvcmob:textures/mob/soliderP1.png"); 

 
 	public GVCRenderGuerrillaP() 
	{ 
 		
 		super(new GVCModelGuerrillaP(), 0.5F); 
	} 
 
 
 	
 	@Override 
 	protected ResourceLocation getEntityTexture(EntityLiving par1EntityLiving) 
 	{ if(GVCMobPlus.cfg_modeGorC){
 		if(par1EntityLiving.onGround){
 		return this.skeletonTextures; 
 		}
 		else{
 			return this.skeletonTextures1;
 		}
 	}else{
 		if(par1EntityLiving.onGround){
 	 		return this.skeletonTextures3; 
 	 		}
 	 		else{
 	 			return this.skeletonTextures4;
 	 		}
 	}
 	} 
 } 

 