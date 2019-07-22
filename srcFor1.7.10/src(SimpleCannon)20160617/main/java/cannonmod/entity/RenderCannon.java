package cannonmod.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;

public class RenderCannon extends RendererLivingEntity {
	
	public static ResourceLocation[] textures=new ResourceLocation[5];
	public static ResourceLocation textureTrack=new ResourceLocation("simplecannon:textures/entity/track.png");
	public static ResourceLocation textureCrossbar=new ResourceLocation("simplecannon:textures/crossbars/crossbar.png");
	
	static{
		for(int i=0;i<textures.length;i++){
			textures[i]=new ResourceLocation("simplecannon:textures/entity/cannon_"+i+".png");
		}
	}
	public RenderCannon() {
		super(new ModelDummy(),1);
		ModelDummy model=new ModelDummy();
		model.renderer=this;
		this.mainModel=model;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		
		return textures[((EntityCannon)p_110775_1_).design];
	}
	
	@Override
	public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_){
		
		super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
		
	}
	
	@Override
	protected boolean func_110813_b(EntityLivingBase p_110813_1_){
		return false;
	}
	
}
