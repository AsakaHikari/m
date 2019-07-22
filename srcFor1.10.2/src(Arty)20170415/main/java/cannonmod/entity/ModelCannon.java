package cannonmod.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class ModelCannon extends ModelBase {
	//fields
	ModelRenderer carriage;
	ModelRenderer cannon;
	ModelRenderer tracks;
	ModelRenderer body;
	RenderCannon renderer;
	/**a130*/
	public int wheelNum=9;
	ModelRenderer[] wheelsL=new ModelRenderer[wheelNum];
	ModelRenderer[] wheelsR=new ModelRenderer[wheelNum];
	public int vehicleLengthx16;
	
	public float trackScale;
	public float prevPartialTicks;
	/**a130*/
	public ModelCannon(int barrel,int calibre,int posOfBarrel,int design,boolean hasTurret,RenderCannon renderer)
	{
		 
		this.textureHeight=128;
		this.textureWidth=256;
		this.renderer=renderer;
		/**a120*/
		setTextureOffset("carriage.UcarriageL", 0, 0);
	    setTextureOffset("carriage.Lcarriage", 0, 0);
	    setTextureOffset("carriage.UcarriageR", 0, 0);
	    setTextureOffset("carriage.carriageBox", 0, 0);
	    setTextureOffset("carriage.cupola", 0, 0);
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
	    setTextureOffset("body.schurzenL", 0, 0);
	    setTextureOffset("body.schurzenR", 0, 0);
	    /**a130*/
	    for(int i=1;i<wheelNum-1;i++){
	    	setTextureOffset("wheelL"+i+".box",0,0);
	    	setTextureOffset("wheelR"+i+".box",0,0);
	    }
	    setTextureOffset("wheelL0"+".box",0,64);
    	setTextureOffset("wheelR0"+".box",0,64);
    	setTextureOffset("wheelL"+(wheelNum-1)+".box",0,64);
    	setTextureOffset("wheelR"+(wheelNum-1)+".box",0,64);
		
		int l=2*calibre+barrel/2+16;
		this.vehicleLengthx16=l;
		this.trackScale=(float)l/(32f*(wheelNum-1));
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
		/**a130*/
		int center=(wheelNum-1)/2;
		for(int i=0;i<wheelNum;i++){
	    	this.wheelsL[i]=new ModelRenderer(this,"wheelL"+i);
	    	this.wheelsR[i]=new ModelRenderer(this,"wheelR"+i);
		}
	    for(int i=1;i<wheelNum-1;i++){
	    	wheelsL[i].setRotationPoint(0,24/this.trackScale-16,(i-center)*32);
	    	wheelsR[i].setRotationPoint(0,24/this.trackScale-16,(i-center)*32);
	    	wheelsL[i].addBox("box",((calibre+16)/2)/this.trackScale-16, -16, -16, 16, 32, 32);
	    	wheelsR[i].addBox("box",-((calibre+16)/2)/this.trackScale, -16, -16, 16, 32, 32);
	    }
	    wheelsL[0].setRotationPoint(0,24/this.trackScale-24,-center*32+8);
    	wheelsR[0].setRotationPoint(0,24/this.trackScale-24,-center*32+8);
    	wheelsL[0].addBox("box",((calibre+16)/2)/this.trackScale-16, -8, -8, 16, 16, 16);
    	wheelsR[0].addBox("box",-((calibre+16)/2)/this.trackScale, -8, -8, 16, 16, 16);
    	wheelsL[wheelNum-1].setRotationPoint(0,24/this.trackScale-24,center*32-8);
    	wheelsR[wheelNum-1].setRotationPoint(0,24/this.trackScale-24,center*32-8);
    	wheelsL[wheelNum-1].addBox("box",((calibre+16)/2)/this.trackScale-16, -8, -8, 16, 16, 16);
    	wheelsR[wheelNum-1].addBox("box",-((calibre+16)/2)/this.trackScale, -8, -8, 16, 16, 16);
		
		body = new ModelRenderer(this, "body");
		body.setRotationPoint(0F, 24F-(l/4), 0F);
		setRotation(body, 0F, 0F, 0F);
		body.mirror = true;
		
		body.addBox("stand", -(calibre+16)/2F, -calibre/2-calibre/4, -(calibre+6)/2, calibre+16, calibre/4, calibre+16);
		body.addBox("chassis", -(calibre+16)/2, -calibre/2, -(l-2)/2, calibre+16, calibre/2+(l/4)-(int)(32*this.trackScale), l-2);
		body.addBox("engine", -calibre/2, -calibre/2-calibre/8, -calibre-3, calibre, calibre/8, calibre/2);
		body.addBox("bottom", -(calibre+16)/2+(int)(32*this.trackScale)/2+1, l/4-l/8, -(l-4)/2, calibre+16-(int)(32*this.trackScale)-2, l/16, l-4);
		/**a120*/
		if(design!=0){
			/**a130*/
			body.addBox("schurzenL", -(calibre+16)/2-l/8+(int)(l/8)-3, -l/8, -(l-4-4)/2, 1, l/4, l-4-4);
			body.addBox("schurzenR", (calibre+16)/2+l/8-(int)(l/8)+2, -l/8, -(l-4-4)/2, 1, l/4, l-4-4);
		}


		carriage = new ModelRenderer(this, "carriage");
		/**a120*/
		carriage.setRotationPoint(0F, 24-(l/4+calibre/2+calibre/4+calibre/4+calibre/2), 0F);
		setRotation(carriage, 0F, 0F, 0F);
		carriage.mirror = true;
		/**a120*/
		/**a130*/
		if(hasTurret){
			carriage.addBox("carriageBox", -(calibre+6)/2, -calibre*3/4, -(calibre+6)/2, calibre+6, calibre*3/2, calibre+6);
			carriage.addBox("cupola", -calibre/4, -calibre, -calibre/4, calibre/2, calibre/4, calibre/2);
		}else{
			carriage.addBox("Lcarriage", -(calibre+6)/2, calibre/8*9, -(calibre+6)/2, calibre+6, calibre/4, calibre+6);
			carriage.addBox("UcarriageL", (calibre+6)/2-2, -calibre/8, -calibre*3/8, 2, calibre*5/4, calibre*3/4);
			carriage.addBox("UcarriageR", -(calibre+6)/2, -calibre/8, -calibre*3/8, 2, calibre*5/4, calibre*3/4);
		}
		cannon = new ModelRenderer(this, "cannon");
		/**a120*/
		cannon.setRotationPoint(0F, 24-(l/4+calibre/2+calibre/4+calibre/4+calibre/2), 0F);
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
		
		GlStateManager.pushMatrix();
		GlStateManager.rotate(entity.rotationYaw+180,0,1,0);
		if ((!entity.getPassengers().isEmpty())&& entity.getPassengers().get(0) == Minecraft.getMinecraft().thePlayer
				&& Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
			this.body.render(f5);
			/**a130*/
			//this.renderer.bindTexture(RenderCannon.textureTrack);
			//GL11.glScalef(this.trackScale, this.trackScale, this.trackScale);
			//this.tracks.render(f5);
			this.renderer.bindTexture(RenderCannon.textureWheel);
			//GL11.glScalef(this.trackScale, this.trackScale, this.trackScale);
			for(int i=0;i<wheelNum;i++){
				this.wheelsL[i].render(f5*this.trackScale);
				this.wheelsR[i].render(f5*this.trackScale);
			}
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
			EntityCannon cannon=(EntityCannon)entity;
			{
				double zM=this.vehicleLengthx16/32d;
				double zm=-zM;
				double x
				vertexbuffer.pos(this.vehicleLengthx16/32d, (double)f1, -0.5D).tex((double)(textureU+width)/256, (double)textureV/256).normal(0.0F, 0.0F, -1.0F).endVertex();
				vertexbuffer.pos((double)f, (double)f1, -0.5D).tex((double)textureU/256, (double)textureV/256).normal(0.0F, 0.0F, -1.0F).endVertex();
				vertexbuffer.pos((double)f, (double)f1+height, -0.5D).tex((double)textureU/256, (double)(textureV+height)/256).normal(0.0F, 0.0F, -1.0F).endVertex();
				vertexbuffer.pos((double)f+width, (double)f1+height, -0.5D).tex((double)(textureU+width)/256, (double)(textureV+height)/256).normal(0.0F, 0.0F, -1.0F).endVertex();
			}
			tessellator.draw();
		} else {
			this.cannon.render(f5);
			this.carriage.render(f5);
			this.body.render(f5);
			/**a130*/
			//this.renderer.bindTexture(RenderCannon.textureTrack);
			//GL11.glScalef(this.trackScale, this.trackScale, this.trackScale);
			//this.tracks.render(f5);
			this.renderer.bindTexture(RenderCannon.textureWheel);
			//GL11.glScalef(this.trackScale, this.trackScale, this.trackScale);
			for(int i=0;i<wheelNum;i++){
				this.wheelsL[i].render(f5*this.trackScale);
				this.wheelsR[i].render(f5*this.trackScale);
			}
		}
		GlStateManager.popMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float yaw,float yawHead,float pitch, EntityCannon entityIn,float partialTicks) {
		// System.out.println("yawhead="+f4+" pitch="+f3);
		// EntityCannon entity=(EntityCannon)e;
		this.cannon.rotateAngleX = pitch / 180 * (float) Math.PI;
		this.cannon.rotateAngleY = yawHead / 180 * (float) Math.PI;
		this.carriage.rotateAngleY= yawHead / 180 * (float) Math.PI;
		if(!entityIn.getPassengers().isEmpty()){
			Entity riddenByEntity=entityIn.getPassengers().get(0);
			if (riddenByEntity != null && riddenByEntity instanceof EntityLivingBase
					&& (entityIn.engine==0 || entityIn.fuelLeft>0))
			{
				EntityLivingBase entitylivingbase = (EntityLivingBase)riddenByEntity;
				double turnAmountL=0;
				double turnAmountR=0;
				float pt=partialTicks;
				if(partialTicks<this.prevPartialTicks){
					pt+=1;
				}
				if(entitylivingbase.moveForward>0.001){
					turnAmountL=entityIn.speedMultiplier/this.trackScale*(pt-this.prevPartialTicks);
					turnAmountR=turnAmountL;
				}else if(entitylivingbase.moveForward<-0.001){
					turnAmountL=entityIn.speedMultiplier/this.trackScale*(pt-this.prevPartialTicks)*-0.5;
					turnAmountR=turnAmountL;
				}
				if(entitylivingbase.moveStrafing>0.001){
					double v=entityIn.turningSpeed/180*Math.PI*entityIn.vehicleWidth/2/this.trackScale*(pt-this.prevPartialTicks);
					turnAmountL-=v;
					turnAmountR+=v;
				}else if(entitylivingbase.moveStrafing<-0.001){
					double v=entityIn.turningSpeed/180*Math.PI*entityIn.vehicleWidth/2/this.trackScale*(pt-this.prevPartialTicks);
					turnAmountL+=v;
					turnAmountR-=v;
				}
				if(!(entitylivingbase instanceof EntityPlayer)){
					float rotationYaw=entityIn.rotationYaw;
					if(entityIn.rotationYaw-entitylivingbase.rotationYaw>180){
						rotationYaw-=360;
					}else if(entityIn.rotationYaw-entitylivingbase.rotationYaw<-180){
						rotationYaw+=360;
					}
					if(rotationYaw-entitylivingbase.rotationYaw>entityIn.turningSpeed){
						double v=entityIn.turningSpeed/180*Math.PI*entityIn.vehicleWidth/2/this.trackScale*(pt-this.prevPartialTicks);
						turnAmountL-=v;
						turnAmountR+=v;
					}else if(rotationYaw-entitylivingbase.rotationYaw<-entityIn.turningSpeed){
						double v=entityIn.turningSpeed/180*Math.PI*entityIn.vehicleWidth/2/this.trackScale*(pt-this.prevPartialTicks);
						turnAmountL-=v;
						turnAmountR+=v;
					}else{
						double v=(rotationYaw-entitylivingbase.rotationYaw)/180*Math.PI*entityIn.vehicleWidth/2/this.trackScale*(pt-this.prevPartialTicks);
						turnAmountL-=v;
						turnAmountR+=v;
					}
				}
				for(int i=0;i<wheelNum;i++){
					this.wheelsL[i].rotateAngleX+=turnAmountL;
					this.wheelsR[i].rotateAngleX+=turnAmountR;
				}
			}
		}
		this.prevPartialTicks=partialTicks;
		
	}
	
	

}
