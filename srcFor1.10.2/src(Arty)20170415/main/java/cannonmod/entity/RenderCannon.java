package cannonmod.entity;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import cannonmod.core.CannonCore;
import cannonmod.entity.EntityCannon.BannerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderCannon extends RenderLivingBase<EntityCannon>{

	public static ResourceLocation[] textures=new ResourceLocation[5];
	public static ResourceLocation textureTrack=new ResourceLocation("simplecannon:textures/entity/track.png");
	public static ResourceLocation textureCrossbar=new ResourceLocation("simplecannon:textures/crossbars/crossbar.png");
	public static ResourceLocation textureWheel=new ResourceLocation("simplecannon:textures/entity/wheel.png");

	/**a130*/
	private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
	private static final ResourceLocation KRISTOFFER_PAINTING_TEXTURE = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
	private final Minecraft mc = Minecraft.getMinecraft();
	private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
	private final ModelResourceLocation mapModel = new ModelResourceLocation("item_frame", "map");
	private final RenderItem itemRenderer;
	private final ModelBanner bannerModel = new ModelBanner();
	static{
		for(int i=0;i<textures.length;i++){
			textures[i]=new ResourceLocation("simplecannon:textures/entity/cannon_"+i+".png");
		}
	}
	/**a130*/
	public RenderCannon(RenderManager renderManager,RenderItem itemRenderer) {
		super(renderManager,new ModelDummy(),1);
		ModelDummy model=new ModelDummy();
		model.renderer=this;
		this.mainModel=model;
		this.itemRenderer=itemRenderer;
	}

	@Override
	public void doRender(EntityCannon entity, double x, double y, double z, float entityYaw, float partialTicks){
		//System.out.println("tick:"+partialTicks);

		/**a130*/
		float yaw = this.interpolateRotation(entity.prevRotationYaw+180, entity.rotationYaw+180, partialTicks);
		float yawHead = this.interpolateRotation(entity.prevRotationYawHead+180, entity.rotationYawHead+180, partialTicks);
		float pitch = this.interpolateRotation(entity.prevRotationPitch, entity.rotationPitch, partialTicks);
		entity.getThisModel(this).setRotationAngles(yaw, yawHead-yaw, pitch, entity, partialTicks);
		//GlStateManager.rotate(-yaw, 0,1,0);
		super.doRender(entity, x,y,z,entityYaw,partialTicks);
		if ((!entity.getPassengers().isEmpty())&& entity.getPassengers().get(0) == Minecraft.getMinecraft().thePlayer
				&& Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
		}else{
			GlStateManager.pushMatrix();{
				//BlockPos blockpos = entity.getHangingPosition();

				double l=entity.calibre/5d+entity.lengthOfBarrel/20d+1;
				GlStateManager.translate(x,y+l/4, z);
				this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				GlStateManager.pushMatrix();{
					GlStateManager.translate(0,entity.calibre*3d/20d, 0);
					GlStateManager.rotate( - yawHead, 0.0F, 1.0F, 0.0F);
					//GlStateManager.translate(1.0*MathHelper.sin((float) (entity.rotationYawHead/180*Math.PI)),0,-1.0*MathHelper.cos((float) (entity.rotationYawHead/180*Math.PI)));
					GlStateManager.rotate(-pitch, 1.0F, 0.0F, 0.0F);
					GlStateManager.translate(0,0,0.3-entity.lengthOfBarrel/10d+entity.calibre*3d/80d);
					//GlStateManager.translate(0.0F, 0.0F, 0.4375F);
					GlStateManager.pushMatrix();{
						GlStateManager.translate(-entity.calibre/20d-0.125,0,0);
						GlStateManager.rotate(90, 1.0F, 0.0f, 0.0F);
						GlStateManager.rotate(90, 0.0F, 1.0f, 0.0F);
						//GlStateManager.rotate(180, 0.0F, 0.0f, 1.0F);
						this.renderItem(entity,0,true,true);
					} GlStateManager.popMatrix();

					GlStateManager.pushMatrix();{
						GlStateManager.translate(entity.calibre/20d+0.125,0,0);
						GlStateManager.rotate(90, 1.0F, 0.0f, 0.0F);
						GlStateManager.rotate(-90, 0.0F, 1.0f, 0.0F);
						this.renderItem(entity,1,false,true);
					}GlStateManager.popMatrix();

				}GlStateManager.popMatrix();
				GlStateManager.pushMatrix();{
					//GlStateManager.translate(0,-l/8,0);
					GlStateManager.rotate(- yaw, 0.0F, 1.0F, 0.0F);
					GlStateManager.pushMatrix();{
						GlStateManager.translate(-entity.calibre/20f-0.8,0,0);
						GlStateManager.rotate(90, 0.0F, 1.0f, 0.0F);
						this.renderItem(entity,2,false,false);
					}GlStateManager.popMatrix();
					GlStateManager.pushMatrix();{
						GlStateManager.translate(entity.calibre/20f+0.8,0,0);
						GlStateManager.rotate(-90, 0.0F, 1.0f, 0.0F);
						this.renderItem(entity,3,false,false);
					}GlStateManager.popMatrix();
				}GlStateManager.popMatrix();
			}GlStateManager.popMatrix();
		}
		//this.renderName(entity, x + (double)((float)entity.facingDirection.getFrontOffsetX() * 0.3F), y - 0.25D, z + (double)((float)entity.facingDirection.getFrontOffsetZ() * 0.3F));
	}

	private void renderItem(EntityCannon entity,int symbolNum,boolean mirror,boolean laid)
	{
		ItemStack itemstack = entity.inventory[entity.getSymbolSlotNum(symbolNum)];

		if (itemstack != null)
		{
			double l=entity.calibre/5d+entity.lengthOfBarrel/20d+1;
			EntityItem entityitem = new EntityItem(entity.worldObj, 0.0D, 0.0D, 0.0D, itemstack);
			Item item = entityitem.getEntityItem().getItem();
			entityitem.getEntityItem().stackSize = 1;
			entityitem.hoverStart = 0.0F;
			GlStateManager.pushMatrix();

			GlStateManager.disableLighting();

			if(item ==Items.FILLED_MAP){
				GlStateManager.rotate(0f, 0.0F, 0.0F, 1.0F);
			}else{
				//GlStateManager.rotate(45f, 0.0F, 0.0F, 1.0F);
			}

			if (item instanceof net.minecraft.item.ItemMap)
			{
				this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
				float f = 0.0078125F;
				GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
				GlStateManager.translate(-64.0F, -64.0F, 0.0F);
				MapData mapdata = Items.FILLED_MAP.getMapData(entityitem.getEntityItem(), entity.worldObj);
				GlStateManager.translate(0.0F, 0.0F, -1.0F);

				if (mapdata != null)
				{
					this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
				}
			}else if(item == Items.PAINTING){
				GlStateManager.pushMatrix();
				GlStateManager.enableRescaleNormal();
				this.renderManager.renderEngine.bindTexture(KRISTOFFER_PAINTING_TEXTURE);
				EntityPainting.EnumArt art = entity.getArt(symbolNum);
				float f = 0.0625F;
				//GlStateManager.scale(0.0625F, 0.0625F, 0.0625F);
				if(laid){
					boolean isWidthLonger=art.sizeX>art.sizeY;
					float scale;
					if(isWidthLonger){
						GlStateManager.rotate(90,0,0,1);
						scale=entity.calibre/10f/art.sizeY;

						GlStateManager.scale(scale,scale,scale);
					}else{
						scale=entity.calibre/10f/art.sizeX;

						GlStateManager.scale(scale,scale,scale);
					}
				}else{
					double scale;
					scale=l/4f/art.sizeY;
					GlStateManager.scale(scale,scale,scale);
				}

				if (this.renderOutlines)
				{
					GlStateManager.enableColorMaterial();
					GlStateManager.enableOutlineMode(this.getTeamColor(entity));
				}

				this.renderPainting(entity, art.sizeX, art.sizeY, art.offsetX, art.offsetY,mirror);

				if (this.renderOutlines)
				{
					GlStateManager.disableOutlineMode();
					GlStateManager.disableColorMaterial();
				}

				GlStateManager.disableRescaleNormal();
				GlStateManager.popMatrix();
			}else if(item == Items.BANNER){
				boolean flag = entity.worldObj != null;
				int i = 0;
				long j = flag ? entity.worldObj.getTotalWorldTime():0l;
				GlStateManager.pushMatrix();
				float f = 0.6666667F;

				float f3 = (float)(entity.posX * 7 + entity.posY * 9 + entity.posZ * 13) + (float)j ;
				this.bannerModel.bannerSlate.rotateAngleX = (-0.01f+0.01F * MathHelper.cos(f3 * (float)Math.PI * 0.02F)) * (float)Math.PI;

				GlStateManager.enableRescaleNormal();
				ResourceLocation resourcelocation = this.getBannerResourceLocation(itemstack,entity.bannerDatas[symbolNum]);

				if (resourcelocation != null)
				{
					this.bindTexture(resourcelocation);
					GlStateManager.pushMatrix();
					if(laid){
						GlStateManager.translate(0,0,entity.calibre*0.01);
						if(entity.lengthOfBarrel>entity.calibre*4){
							float scale=entity.calibre/10f*16f/20f;
							GlStateManager.scale(scale,scale,scale);
						}else{

							float scale=entity.calibre/10f*16f/20f;
							GlStateManager.scale(scale,entity.lengthOfBarrel/20f*16f/40f,scale);
						}
					}else{
						GlStateManager.translate(0,l/8,0.15);
						GlStateManager.rotate(180,0,0,1);
						float scale=(float)l/4*16/40;

						GlStateManager.scale(scale,scale,scale);
					}
					//this.bannerModel.renderBanner();
					this.bannerModel.bannerSlate.render(0.0625F);
					GlStateManager.popMatrix();
				}

				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.popMatrix();
			}
			else
			{
				float scale=entity.calibre/10f;
				GlStateManager.scale(scale,scale,scale);
				if(laid){
					if (mirror)
					{
						GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
					}else{
						GlStateManager.rotate(-180.0F, 0.0F, 0.0F, 1.0F);
					}
				}
				GlStateManager.pushAttrib();
				RenderHelper.enableStandardItemLighting();
				this.itemRenderer.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
				RenderHelper.disableStandardItemLighting();
				GlStateManager.popAttrib();
			}


			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}
	private void renderPainting(EntityCannon painting, int width, int height, int textureU, int textureV,boolean mirror)
	{
		float f = (float)(-width) / 2.0F;
		float f1 = (float)(-height) / 2.0F;
		float f2 = 0.5F;
		float f3 = 0.75F;
		float f4 = 0.8125F;
		float f5 = 0.0F;
		float f6 = 0.0625F;
		float f7 = 0.75F;
		float f8 = 0.8125F;
		float f9 = 0.001953125F;
		float f10 = 0.001953125F;
		float f11 = 0.7519531F;
		float f12 = 0.7519531F;
		float f13 = 0.0F;
		float f14 = 0.0625F;
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		if(!mirror){
			vertexbuffer.pos((double)f+width, (double)f1, -0.5D).tex((double)(textureU+width)/256, (double)(textureV+height)/256).normal(0.0F, 0.0F, -1.0F).endVertex();
			vertexbuffer.pos((double)f, (double)f1, -0.5D).tex((double)textureU/256, (double)(textureV+height)/256).normal(0.0F, 0.0F, -1.0F).endVertex();
			vertexbuffer.pos((double)f, (double)f1+height, -0.5D).tex((double)textureU/256, (double)textureV/256).normal(0.0F, 0.0F, -1.0F).endVertex();
			vertexbuffer.pos((double)f+width, (double)f1+height, -0.5D).tex((double)(textureU+width)/256, (double)textureV/256).normal(0.0F, 0.0F, -1.0F).endVertex();
		}else{
			vertexbuffer.pos((double)f+width, (double)f1, -0.5D).tex((double)(textureU+width)/256, (double)textureV/256).normal(0.0F, 0.0F, -1.0F).endVertex();
			vertexbuffer.pos((double)f, (double)f1, -0.5D).tex((double)textureU/256, (double)textureV/256).normal(0.0F, 0.0F, -1.0F).endVertex();
			vertexbuffer.pos((double)f, (double)f1+height, -0.5D).tex((double)textureU/256, (double)(textureV+height)/256).normal(0.0F, 0.0F, -1.0F).endVertex();
			vertexbuffer.pos((double)f+width, (double)f1+height, -0.5D).tex((double)(textureU+width)/256, (double)(textureV+height)/256).normal(0.0F, 0.0F, -1.0F).endVertex();
		}
		tessellator.draw();
		/*
        for (int i = 0; i < width / 16; ++i)
        {
            for (int j = 0; j < height / 16; ++j)
            {
                float f15 = f + (float)((i + 1) * 16);
                float f16 = f + (float)(i * 16);
                float f17 = f1 + (float)((j + 1) * 16);
                float f18 = f1 + (float)(j * 16);
                //this.setLightmap(painting, (f15 + f16) / 2.0F, (f17 + f18) / 2.0F);
                float f19 = (float)(textureU + width - i * 16) / 256.0F;
                float f20 = (float)(textureU + width - (i + 1) * 16) / 256.0F;
                float f21 = (float)(textureV + height - j * 16) / 256.0F;
                float f22 = (float)(textureV + height - (j + 1) * 16) / 256.0F;
                Tessellator tessellator = Tessellator.getInstance();
                VertexBuffer vertexbuffer = tessellator.getBuffer();
                vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
                if(mirror){
                	vertexbuffer.pos((double)f15, (double)f18, -0.5D).tex((double)f20, (double)f22).normal(0.0F, 0.0F, -1.0F).endVertex();
                	vertexbuffer.pos((double)f16, (double)f18, -0.5D).tex((double)f19, (double)f22).normal(0.0F, 0.0F, -1.0F).endVertex();
                	vertexbuffer.pos((double)f16, (double)f17, -0.5D).tex((double)f19, (double)f21).normal(0.0F, 0.0F, -1.0F).endVertex();
                	vertexbuffer.pos((double)f15, (double)f17, -0.5D).tex((double)f20, (double)f21).normal(0.0F, 0.0F, -1.0F).endVertex();
                }else{
                	vertexbuffer.pos((double)f15, (double)f18, -0.5D).tex((double)f20, (double)f21).normal(0.0F, 0.0F, -1.0F).endVertex();
                	vertexbuffer.pos((double)f16, (double)f18, -0.5D).tex((double)f19, (double)f21).normal(0.0F, 0.0F, -1.0F).endVertex();
                	vertexbuffer.pos((double)f16, (double)f17, -0.5D).tex((double)f19, (double)f22).normal(0.0F, 0.0F, -1.0F).endVertex();
                	vertexbuffer.pos((double)f15, (double)f17, -0.5D).tex((double)f20, (double)f22).normal(0.0F, 0.0F, -1.0F).endVertex();
                }
                vertexbuffer.pos((double)f15, (double)f17, 0.5D).tex(0.75D, 0.0D).normal(0.0F, 0.0F, 1.0F).endVertex();
                vertexbuffer.pos((double)f16, (double)f17, 0.5D).tex(0.8125D, 0.0D).normal(0.0F, 0.0F, 1.0F).endVertex();
                vertexbuffer.pos((double)f16, (double)f18, 0.5D).tex(0.8125D, 0.0625D).normal(0.0F, 0.0F, 1.0F).endVertex();
                vertexbuffer.pos((double)f15, (double)f18, 0.5D).tex(0.75D, 0.0625D).normal(0.0F, 0.0F, 1.0F).endVertex();
                vertexbuffer.pos((double)f15, (double)f17, -0.5D).tex(0.75D, 0.001953125D).normal(0.0F, 1.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f16, (double)f17, -0.5D).tex(0.8125D, 0.001953125D).normal(0.0F, 1.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f16, (double)f17, 0.5D).tex(0.8125D, 0.001953125D).normal(0.0F, 1.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f15, (double)f17, 0.5D).tex(0.75D, 0.001953125D).normal(0.0F, 1.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f15, (double)f18, 0.5D).tex(0.75D, 0.001953125D).normal(0.0F, -1.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f16, (double)f18, 0.5D).tex(0.8125D, 0.001953125D).normal(0.0F, -1.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f16, (double)f18, -0.5D).tex(0.8125D, 0.001953125D).normal(0.0F, -1.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f15, (double)f18, -0.5D).tex(0.75D, 0.001953125D).normal(0.0F, -1.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f15, (double)f17, 0.5D).tex(0.751953125D, 0.0D).normal(-1.0F, 0.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f15, (double)f18, 0.5D).tex(0.751953125D, 0.0625D).normal(-1.0F, 0.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f15, (double)f18, -0.5D).tex(0.751953125D, 0.0625D).normal(-1.0F, 0.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f15, (double)f17, -0.5D).tex(0.751953125D, 0.0D).normal(-1.0F, 0.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f16, (double)f17, -0.5D).tex(0.751953125D, 0.0D).normal(1.0F, 0.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f16, (double)f18, -0.5D).tex(0.751953125D, 0.0625D).normal(1.0F, 0.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f16, (double)f18, 0.5D).tex(0.751953125D, 0.0625D).normal(1.0F, 0.0F, 0.0F).endVertex();
                vertexbuffer.pos((double)f16, (double)f17, 0.5D).tex(0.751953125D, 0.0D).normal(1.0F, 0.0F, 0.0F).endVertex();
                tessellator.draw();
            }
        }
		 */
	}


	@Nullable
	private ResourceLocation getBannerResourceLocation(ItemStack stack,EntityCannon.BannerData data){
		if(data==null){
			data=new BannerData();
			data.patterns = null;

			if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10))
			{
				NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("BlockEntityTag");

				if (nbttagcompound.hasKey("Patterns"))
				{
					data.patterns = nbttagcompound.getTagList("Patterns", 10).copy();
				}

				if (nbttagcompound.hasKey("Base", 99))
				{
					data.baseColor = nbttagcompound.getInteger("Base");
				}
				else
				{
					data.baseColor = stack.getMetadata() & 15;
				}
			}
			else
			{
				data.baseColor = stack.getMetadata() & 15;
			}

			data.patternList = null;
			data.colorList = null;
			data.patternResourceLocation = "";
			data.patternDataSet = true;
			data.patternList = Lists.<TileEntityBanner.EnumBannerPattern>newArrayList();
			data.colorList = Lists.<EnumDyeColor>newArrayList();
			data.patternList.add(TileEntityBanner.EnumBannerPattern.BASE);
			data.colorList.add(EnumDyeColor.byDyeDamage(data.baseColor));
			data.patternResourceLocation = "b" + data.baseColor;

			if (data.patterns != null)
			{
				for (int i = 0; i < data.patterns.tagCount(); ++i)
				{
					NBTTagCompound nbttagcompound = data.patterns.getCompoundTagAt(i);
					TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern = TileEntityBanner.EnumBannerPattern.getPatternByID(nbttagcompound.getString("Pattern"));

					if (tileentitybanner$enumbannerpattern != null)
					{
						data.patternList.add(tileentitybanner$enumbannerpattern);
						int j = nbttagcompound.getInteger("Color");
						data.colorList.add(EnumDyeColor.byDyeDamage(j));
						data.patternResourceLocation = data.patternResourceLocation + tileentitybanner$enumbannerpattern.getPatternID() + j;
					}
				}
			}
		}

		return BannerTextures.BANNER_DESIGNS.getResourceLocation(data.patternResourceLocation, data.patternList, data.colorList);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCannon entity) {
		return textures[((EntityCannon)entity).design];
	}
	/**a130*/
	@Override
	protected void rotateCorpse(EntityCannon entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
	{

	}
	/**a130*/
	/**
	 * Renders an entity's name above its head
	 */
	@Override
	protected void renderLivingLabel(EntityCannon entityIn, String str, double x, double y, double z, int maxDistance)
	{
		double d0 = entityIn.getDistanceSqToEntity(this.renderManager.renderViewEntity);

		if (d0 <= (double)(maxDistance * maxDistance))
		{
			boolean flag = entityIn.isSneaking();
			float f = this.renderManager.playerViewY;
			float f1 = this.renderManager.playerViewX;
			boolean flag1 = this.renderManager.options.thirdPersonView == 2;
			float f2 = entityIn.height + 0.5F+entityIn.calibre/10*2 - (flag ? 0.25F : 0.0F);
			int i = "deadmau5".equals(str) ? -10 : 0;
			EntityRenderer.func_189692_a(this.getFontRendererFromRenderManager(), str, (float)x, (float)y + f2, (float)z, i, f, f1, flag1, flag);
		}
	}

}
