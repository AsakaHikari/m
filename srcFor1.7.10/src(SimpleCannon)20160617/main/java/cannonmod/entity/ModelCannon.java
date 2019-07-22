package cannonmod.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

public class ModelCannon extends ModelBase {
	//fields
	ModelRenderer carriage;
	ModelRenderer cannon;
	ModelRenderer tracks;
	ModelRenderer body;
	RenderCannon renderer;
	
	public float trackScale;

	public ModelCannon(int barrel,int calibre,int posOfBarrel,RenderCannon renderer)
	{
		this.textureHeight=128;
		this.textureWidth=256;
		this.renderer=renderer;
		setTextureOffset("carriage.UcarriageL", 0, 0);
	    setTextureOffset("carriage.Lcarriage", 0, 0);
	    setTextureOffset("carriage.UcarriageR", 0, 0);
	    setTextureOffset("cannon.barrelB", 0, 0);
	    setTextureOffset("cannon.barrelL", 0, 0);
	    setTextureOffset("cannon.barrelR", 0, 0);
	    setTextureOffset("cannon.barrelT", 0, 0);
	    setTextureOffset("cannon.breech", 0, 0);
	    setTextureOffset("tracks.trackL", 0, 0);
	    setTextureOffset("tracks.trackR", 0, 0);
	    setTextureOffset("body.stand", 0, 0);
	    setTextureOffset("body.chassis", 0, 0);
	    setTextureOffset("body.engine", 0, 0);
	    setTextureOffset("body.bottom", 0, 0);
		
		int l=2*calibre+barrel/2+16;
		this.trackScale=(float)l/64f;
		tracks = new ModelRenderer(this, "tracks");
		tracks.setRotationPoint(0F, (24F-(l/4))/this.trackScale, 0F);
		setRotation(tracks, 0F, 0F, 0F);
		tracks.mirror = true;
		/*
		tracks.addBox("trackL", (calibre+16)/2-l/8, 5-l/4, -l/2, l/8, l/4, l);
		tracks.addBox("trackR", -(calibre+16)/2, 5-l/4, -l/2, l/8, l/4, l);
		*/
		tracks.addBox("trackL", ((calibre+16)/2)/this.trackScale-8, 0, -32, 8, 16, 64);
		tracks.addBox("trackR", -((calibre+16)/2)/this.trackScale, 0, -32, 8, 16, 64);
		body = new ModelRenderer(this, "body");
		body.setRotationPoint(0F, 24F-(l/4), 0F);
		setRotation(body, 0F, 0F, 0F);
		body.mirror = true;
		
		body.addBox("stand", -(calibre+16)/2F, -calibre/2-calibre/4, -(calibre+6)/2, calibre+16, calibre/4, calibre+16);
		body.addBox("chassis", -(calibre+16)/2, -calibre/2, -(l-2)/2, calibre+16, calibre/2, l-2);
		body.addBox("engine", -calibre/2, -calibre/2-calibre/8, -calibre-3, calibre, calibre/8, calibre/2);
		body.addBox("bottom", -(calibre+16)/2+l/8, 0, -(l-4)/2, calibre+16-l/4, l/8, l-4);


		carriage = new ModelRenderer(this, "carriage");
		carriage.setRotationPoint(0F, 24-(l/4+calibre/2+calibre/4+calibre/4+calibre/8*9), 0F);
		setRotation(carriage, 0F, 0F, 0F);
		carriage.mirror = true;
		carriage.addBox("Lcarriage", -(calibre+6)/2, calibre/8*9, -(calibre+6)/2, calibre+6, calibre/4, calibre+6);
		carriage.addBox("UcarriageL", (calibre+6)/2-2, -calibre/8, -calibre*3/8, 2, calibre*5/4, calibre*3/4);
		carriage.addBox("UcarriageR", -(calibre+6)/2, -calibre/8, -calibre*3/8, 2, calibre*5/4, calibre*3/4);
		cannon = new ModelRenderer(this, "cannon");
		cannon.setRotationPoint(0F, 24-(l/4+calibre/2+calibre/4+calibre/4+calibre/8*9), 0F);
		setRotation(cannon, 0F, 0F, 0F);
		cannon.mirror = true;
		cannon.addBox("barrelB", -(calibre/2-1), calibre/2-1, -barrel+calibre*3/8+posOfBarrel, calibre-2, 2, barrel);
		cannon.addBox("barrelL", calibre/2-1, -(calibre/2-1), -barrel+calibre*3/8+posOfBarrel, 2, calibre-2, barrel);
		cannon.addBox("barrelR", -calibre/2-1, -(calibre/2-1), -barrel+calibre*3/8+posOfBarrel, 2, calibre-2, barrel);
		cannon.addBox("barrelT", -(calibre/2-1), -calibre/2-1, -barrel+calibre*3/8+posOfBarrel, calibre-2, 2, barrel);
		cannon.addBox("breech", -(calibre/2-1), -calibre/2+1, calibre*3/8+posOfBarrel-2, calibre-2, calibre-2, 2);
	}


	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if (entity.riddenByEntity == Minecraft.getMinecraft().thePlayer
				&& Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
			this.body.render(f5);
			RenderManager.instance.renderEngine.bindTexture(RenderCannon.textureTrack);
			GL11.glScalef(this.trackScale, this.trackScale, this.trackScale);
			this.tracks.render(f5);
		} else {
			this.cannon.render(f5);
			this.carriage.render(f5);
			this.body.render(f5);
			RenderManager.instance.renderEngine.bindTexture(RenderCannon.textureTrack);
			GL11.glScalef(this.trackScale, this.trackScale, this.trackScale);
			this.tracks.render(f5);
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3,
			float f4, float f5, Entity e) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
		// System.out.println("yawhead="+f4+" pitch="+f3);
		// EntityCannon entity=(EntityCannon)e;
		this.cannon.rotateAngleX = f4 / 180 * (float) Math.PI;
		this.cannon.rotateAngleY = f3 / 180 * (float) Math.PI;
		this.carriage.rotateAngleY= f3 / 180 * (float) Math.PI;
	}
	
	

}
