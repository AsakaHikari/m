package gvcguns;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class GVCZoomEvent {

	public String ads;
	public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
	protected static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
	public boolean zoomtype;
	
	  @SideOnly(Side.CLIENT)
	  @SubscribeEvent
	  public void onEvent(RenderGameOverlayEvent.Pre event)
	  {
		  Minecraft minecraft = FMLClientHandler.instance().getClient();
		  World world = FMLClientHandler.instance().getWorldClient();
			ScaledResolution var21 = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
			int x1 = var21.getScaledWidth();//画面の横幅をxに代入
			int y1 = var21.getScaledHeight();//画面の縦幅をyに代入
			Entity entity = minecraft.renderViewEntity;
			EntityPlayer entityplayer = (EntityPlayer)entity;
			//EntityPlayer entityplayer = event.player;
			ItemStack itemstack = ((EntityPlayer)(entityplayer)).getCurrentEquippedItem();
			FontRenderer fontrenderer = minecraft.fontRenderer;
	        minecraft.entityRenderer.setupOverlayRendering();
	        //OpenGlHelper.
	        
	        String j = null;
	        int var6 = x1 - 80;
            int var7 = y1 - 25;
            int i2 = 0;
            
            
            if (!minecraft.playerController.enableEverythingIsScrewedUpMode())
            {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                //minecraft.getTextureManager().bindTexture(widgetsTexPath);
                //InventoryPlayer inventoryplayer = minecraft.thePlayer.inventory;
                //this.drawTexturedModalRect(x1 / 2 - 91, y1 - 22, 0, 0, 182, 22);
                //this.drawTexturedModalRect(x1 / 2 - 91 - 1 + inventoryplayer.currentItem * 20, y1 - 22 - 1, 0, 22, 24, 22);
                //minecraft.getTextureManager().bindTexture(icons);
                //GL11.glEnable(GL11.GL_BLEND);
                //OpenGlHelper.glBlendFunc(775, 769, 1, 0);
                //this.drawTexturedModalRect(x1 / 2 - 7, y1 / 2 - 7, 0, 0, 16, 16);
                //OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                minecraft.mcProfiler.startSection("bossHealth");
                this.renderBossHealth(minecraft);
                minecraft.mcProfiler.endSection();

                RenderHelper.disableStandardItemLighting();
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                minecraft.mcProfiler.endSection();
                GL11.glDisable(GL11.GL_BLEND);
                GuiIngameForge.renderCrosshairs = true;
            }
            
            
            
            
            if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
    		{
			if(itemstack != null && itemstack.getItem() instanceof GVCItemGunBase){
				j = "aaaaa";
				int RGB = 0xFFFFFF;
				GL11.glEnable(GL11.GL_BLEND);
				//GL11.glPushMatrix();
				//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				//GL11.glEnable(GL11.GL_BLEND);
				//drawTexturedModalRect(x1-100+0, y1-45+0, i2, 0, 6, 12);
                //minecraft.fontRenderer.drawStringWithShadow(j, var6, var7, RGB);
            	//String d = String.valueOf(itemstack.getMaxDamage() - itemstack.getItemDamage());
            	//minecraft.fontRenderer.drawStringWithShadow("×", x1-100+8+0, y1-42+0, 0xFFFFFF);
            	//String d = String.format("%1$3d", itemstack.getMaxDamage() - itemstack.getItemDamage());
            	//fontrenderer.drawStringWithShadow(d, x1-100+12+0, y1-50+0, 0xFFFFFF);
            	//GL11.glPopMatrix();
            	//String dayText = tm.makeLocalTime(entityplayer.worldObj.getWorldTime());
            	//fontrenderer.drawString(dayText, DAY_WIDTH / 2-55, DAY_HEIGHT /2+22, 0x4D0000);
            	//this.drawfont(minecraft, x1, y1, itemstack, fontrenderer);
            	
				if(entityplayer.isSneaking() || GVCGunsPlus.adstype == 1){
				if(itemstack != null && itemstack.getItem() == GVCGunsPlus.fn_svd){
				    ads = "gvcguns:textures/misc/scope_svd.png";
				    this.renderPumpkinBlur(minecraft, x1, y1);
				    ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 8.0D, "cameraZoom", "field_78503_V");
				}
				else if(itemstack != null && itemstack.getItem() == GVCGunsPlus.fn_m110){
					ads = "gvcguns:textures/misc/scope_m110.png";
					this.renderPumpkinBlur(minecraft, x1, y1);
					ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 8.0D, "cameraZoom", "field_78503_V");
				}
				else if(itemstack != null && itemstack.getItem() == GVCGunsPlus.fn_m82a3){
					ads = "gvcguns:textures/misc/scope_m82a3.png";
					this.renderPumpkinBlur(minecraft, x1, y1);
					ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 8.0D, "cameraZoom", "field_78503_V");
				}
				else if(itemstack != null && itemstack.getItem() == GVCGunsPlus.fn_r700){
					ads = "gvcguns:textures/misc/scope_r700.png";
					this.renderPumpkinBlur(minecraft, x1, y1);
					ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 8.0D, "cameraZoom", "field_78503_V");
				}
				else if(itemstack != null && itemstack.getItem() == GVCGunsPlus.fn_ak74sp){
					ads = "gvcguns:textures/misc/acog_ak74.png";
					this.renderPumpkinBlur(minecraft, x1, y1);
					ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 4.0D, "cameraZoom", "field_78503_V");
				}
				else if(itemstack != null && itemstack.getItem() == GVCGunsPlus.fn_m16a4sp){
					ads = "gvcguns:textures/misc/acog_m16a4.png";
					this.renderPumpkinBlur(minecraft, x1, y1);
					ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 4.0D, "cameraZoom", "field_78503_V");
				}
				else if(itemstack != null && itemstack.getItem() == GVCGunsPlus.fn_g36){
					ads = "gvcguns:textures/misc/acog_g36.png";
					this.renderPumpkinBlur(minecraft, x1, y1);
					ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 4.0D, "cameraZoom", "field_78503_V");
				}
				else if(itemstack != null && itemstack.getItem() == GVCGunsPlus.fn_fim92){
					ads = "gvcguns:textures/misc/scope_fim92.png";
					this.renderPumpkinBlur(minecraft, x1, y1);
					ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 2.0D, "cameraZoom", "field_78503_V");
				}
				else if(itemstack != null && itemstack.getItem() == GVCGunsPlus.fn_mbtlaw){
					ads = "gvcguns:textures/misc/scope_mbtlaw.png";
					this.renderPumpkinBlur(minecraft, x1, y1);
					ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 2.0D, "cameraZoom", "field_78503_V");
				}
				else if(itemstack != null && itemstack.getItem() instanceof Item){
					ads = "gvcguns:textures/misc/null.png";
					ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 1.0D, "cameraZoom", "field_78503_V");
				}
				//this.renderPumpkinBlur(minecraft, x1, y1);
				
				}else{
					ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 1.0D, "cameraZoom", "field_78503_V");
				}
				GuiIngameForge.renderCrosshairs = false;
				GuiIngameForge.renderBossHealth = true;
				this.zoomtype = true;
				GL11.glPushMatrix();
				GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				String d = String.format("%1$3d", itemstack.getMaxDamage() - itemstack.getItemDamage());
				String d1 = String.format("%1$3d", itemstack.getMaxDamage());
            	fontrenderer.drawStringWithShadow(d+" /"+d1, x1-70, y1-30+0, 0xFFFFFF);
				//minecraft.fontRenderer.drawStringWithShadow(j, var6, var7, RGB);
				GL11.glPopMatrix();
				GL11.glPopAttrib();
			}else if(itemstack != null && itemstack.getItem() instanceof GVCItemGunBaseSG){
				j = "aaaaa";
				int RGB = 0xFFFFFF;
				GuiIngameForge.renderCrosshairs = false;
				this.zoomtype = true;
				GL11.glPushMatrix();
				GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
				String d = String.format("%1$3d", itemstack.getMaxDamage() - itemstack.getItemDamage());
				String d1 = String.format("%1$3d", itemstack.getMaxDamage());
            	fontrenderer.drawStringWithShadow(d+" /"+d1, x1-70, y1-30+0, 0xFFFFFF);
				//minecraft.fontRenderer.drawStringWithShadow(j, var6, var7, RGB);
				GL11.glPopMatrix();
				GL11.glPopAttrib();
			}else if(itemstack != null && itemstack.getItem() instanceof GVCItemGunBaseGL){
					j = "aaaaa";
					int RGB = 0xFFFFFF;
					GuiIngameForge.renderCrosshairs = false;
					this.zoomtype = true;
					GL11.glPushMatrix();
					GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
					String d = String.format("%1$3d", itemstack.getMaxDamage() - itemstack.getItemDamage());
					String d1 = String.format("%1$3d", itemstack.getMaxDamage());
	            	fontrenderer.drawStringWithShadow(d+" /"+d1, x1-70, y1-30+0, 0xFFFFFF);
					//minecraft.fontRenderer.drawStringWithShadow(j, var6, var7, RGB);
					GL11.glPopMatrix();
					GL11.glPopAttrib();
			}else{
				if(this.zoomtype == true){
				GuiIngameForge.renderCrosshairs = true;
				ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 1.0D, "cameraZoom", "field_78503_V");
				this.zoomtype = false;
				}
				
			}
    		}
	  }

	  @SideOnly(Side.CLIENT)
	  protected void renderPumpkinBlur(Minecraft minecraft, int p_73836_1_, int p_73836_2_)
	    {
		    GL11.glEnable(GL11.GL_BLEND);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glPushMatrix();
	        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
	        GL11.glDepthMask(false);
	        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glDisable(GL11.GL_ALPHA_TEST);
	        minecraft.getTextureManager().bindTexture(new ResourceLocation(ads));
	        Tessellator tessellator = Tessellator.instance;
	        tessellator.startDrawingQuads();
	        tessellator.addVertexWithUV(0.0D, (double)p_73836_2_, -90.0D, 0.0D, 1.0D);
	        tessellator.addVertexWithUV((double)p_73836_1_, (double)p_73836_2_, -90.0D, 1.0D, 1.0D);
	        tessellator.addVertexWithUV((double)p_73836_1_, 0.0D, -90.0D, 1.0D, 0.0D);
	        tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
	        tessellator.draw();
	        GL11.glPopMatrix();
	        GL11.glPopAttrib();
	        GL11.glDepthMask(true);
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GL11.glEnable(GL11.GL_ALPHA_TEST);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    }
	
	  /**
	     * Renders dragon's (boss) health on the HUD
	     */
	    protected void renderBossHealth(Minecraft mc)
	    {
	        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0)
	        {
	            --BossStatus.statusBarTime;
	            FontRenderer fontrenderer = mc.fontRenderer;
	            ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
	            int i = scaledresolution.getScaledWidth();
	            short short1 = 182;
	            int j = i / 2 - short1 / 2;
	            int k = (int)(BossStatus.healthScale * (float)(short1 + 1));
	            byte b0 = 12;
	            this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);
	            this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);

	            if (k > 0)
	            {
	                this.drawTexturedModalRect(j, b0, 0, 79, k, 5);
	            }

	            String s = BossStatus.bossName;
	            fontrenderer.drawStringWithShadow(s, i / 2 - fontrenderer.getStringWidth(s) / 2, b0 - 10, 16777215);
	            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	            mc.getTextureManager().bindTexture(icons);
	        }
	    }
	  
	  /*public void drawfont(Minecraft minecraft, int p_73836_1_, int p_73836_2_, ItemStack itemstack, FontRenderer fontrenderer){
		  GL11.glEnable(GL11.GL_BLEND);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glDepthMask(false);
	        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glDisable(GL11.GL_ALPHA_TEST);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        String d = String.valueOf(itemstack.getMaxDamage() - itemstack.getItemDamage());
	        fontrenderer.drawString(d, p_73836_1_-100+12+0, p_73836_2_-50+0, 0x4D0000);
	        
	        GL11.glDepthMask(true);
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GL11.glEnable(GL11.GL_ALPHA_TEST);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	  }*/
	  
	  
	  public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
	    {
			float zLevel = 0;
	        float var7 = 0.00390625F;
	        float var8 = 0.00390625F;
	        Tessellator var9 = Tessellator.instance;
	        GL11.glDisable(GL11.GL_LIGHTING);//明るく表示する
	        var9.startDrawingQuads();
	        var9.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)zLevel, (double)((float)(par3 + 0) * var7), (double)((float)(par4 + par6) * var8));
	        var9.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)zLevel, (double)((float)(par3 + par5) * var7), (double)((float)(par4 + par6) * var8));
	        var9.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)zLevel, (double)((float)(par3 + par5) * var7), (double)((float)(par4 + 0) * var8));
	        var9.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)zLevel, (double)((float)(par3 + 0) * var7), (double)((float)(par4 + 0) * var8));
	        var9.draw();
	    }
	  
}
