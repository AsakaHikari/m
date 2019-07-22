
package gvcmob; 

 
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderBiped; 
import net.minecraft.entity.EntityLiving; 
import net.minecraft.util.ResourceLocation; 
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

 

public class GVCRenderGuerrilla extends RenderBiped 
{ 
	
	private static final ResourceLocation skeletonTextures = new ResourceLocation("gvcmob:textures/mob/guerrilla.png"); 
	private static final ResourceLocation skeletonTextures2 = new ResourceLocation("gvcmob:textures/mob/guerrilla_d.png"); 
	private static final ResourceLocation skeletonTextures3 = new ResourceLocation("gvcmob:textures/mob/guerrilla_t.png"); 
	private static final ResourceLocation skeletonTextures4 = new ResourceLocation("gvcmob:textures/mob/guerrilla_j.png"); 
	private static final ResourceLocation skeletonTextures5 = new ResourceLocation("gvcmob:textures/mob/guerrilla_hell.png"); 
	private static final ResourceLocation skeletonTextures6 = new ResourceLocation("gvcmob:textures/mob/solider.png"); 

 
 	public GVCRenderGuerrilla() 
	{ 
 		
 		super(new GVCModelGuerrilla(), 0.5F); 
	} 
 
 
 	
 	@Override 
 	protected ResourceLocation getEntityTexture(EntityLiving par1EntityLiving) 
 	{ 
 		World world = Minecraft.getMinecraft().theWorld;
 		
 		if(GVCMobPlus.cfg_modeGorC){

 	 		if(world.getBiomeGenForCoords( (int)par1EntityLiving.posX, (int)par1EntityLiving.posZ) == BiomeGenBase.desert
 					||world.getBiomeGenForCoords((int)par1EntityLiving.posX, (int)par1EntityLiving.posZ) == BiomeGenBase.desertHills
 					||world.getBiomeGenForCoords((int)par1EntityLiving.posX, (int)par1EntityLiving.posZ) == BiomeGenBase.savanna
 					||world.getBiomeGenForCoords((int)par1EntityLiving.posX, (int)par1EntityLiving.posZ) == BiomeGenBase.savannaPlateau){
 	 			return this.skeletonTextures2;
 	 		}else if(world.getBiomeGenForCoords( (int)par1EntityLiving.posX, (int)par1EntityLiving.posZ) == BiomeGenBase.taiga
 					||world.getBiomeGenForCoords((int)par1EntityLiving.posX, (int)par1EntityLiving.posZ) == BiomeGenBase.taigaHills){
 	 			return this.skeletonTextures3;
 	 		}else if(world.getBiomeGenForCoords( (int)par1EntityLiving.posX, (int)par1EntityLiving.posZ) == BiomeGenBase.jungle
 					||world.getBiomeGenForCoords((int)par1EntityLiving.posX, (int)par1EntityLiving.posZ) == BiomeGenBase.jungleEdge
 					||world.getBiomeGenForCoords((int)par1EntityLiving.posX, (int)par1EntityLiving.posZ) == BiomeGenBase.jungleHills
 					||world.getBiomeGenForCoords((int)par1EntityLiving.posX, (int)par1EntityLiving.posZ) == BiomeGenBase.roofedForest
 					){
 	 			return this.skeletonTextures4;
 	 		}else if(world.getBiomeGenForCoords( (int)par1EntityLiving.posX, (int)par1EntityLiving.posZ) == BiomeGenBase.desert){
 	 			return this.skeletonTextures4;
 	 		}
 		}else{
 	 			return this.skeletonTextures6;
 		}
 			return this.skeletonTextures; 
 	} 
 } 

 