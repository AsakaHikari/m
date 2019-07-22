package mod.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.core.ShellCore;
import mod.entity.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderShell extends Render<EntityShell>
{
    private ResourceLocation shellTex = new ResourceLocation("shellmod:textures/entity/shell.png");
    private ResourceLocation shellTexTop = new ResourceLocation("shellmod:textures/entity/shell_top.png");
    private float size;

    public RenderShell(float size,String sideTexturePath,String topTexturePath,RenderManager renderManagerIn)
    {
    	super(renderManagerIn);
    	this.size=size;
    	shellTex=new ResourceLocation(sideTexturePath);
    	shellTexTop=new ResourceLocation(topTexturePath);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
   
    public void doRender(EntityShell par1EntityShell, double par2, double par4, double par6, float par8, float par9)
    {
        GlStateManager.pushMatrix();
        //System.out.println((par2+RenderManager.renderPosX)+","+(par4+RenderManager.renderPosY)+","+(par6+RenderManager.renderPosZ));
        GlStateManager.translate((float)par2, (float)par4+par1EntityShell.height/2, (float)par6);
        GlStateManager.enableRescaleNormal();
        float f2 = this.size;
        GlStateManager.scale(f2 / 1.0F, f2 / 1.0F, f2 / 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f3 = 0;
        float f4 = 1.0f;
        float f5 = 1.0f;
        float f6 = 0;
        
        float f7 = 1.0F;
        float f8 = 0.5F;
        float f9 = 0.5F;
        this.bindTexture(shellTex);
        boolean doFuse=false;
        if(par1EntityShell instanceof EntityHE){
        	int fuse=par1EntityShell.getDataWatcher().getWatchableObjectByte(6);
        	if(fuse>0 && fuse/5%2==0){
        		doFuse=true;
        		GlStateManager.disableLighting();
        		GlStateManager.enableBlend();
        		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
        		GlStateManager.color(2.0F, 2.0F, 2.0F, 1.0f);
        	}
        }
        
        for(int i=0;i<4;i++){
        	GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        	worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        	
        	worldrenderer.pos((double)(0.0F - f8), (double)(0.0F - f9), 0.0D).tex( (double)f3, (double)f6).normal(0.0F, 1.0F, 0.0F).endVertex();
        	worldrenderer.pos((double)(f7 - f8), (double)(0.0F - f9), 0.0D).tex((double)f4, (double)f6).normal(0.0F, 1.0F, 0.0F).endVertex();
        	worldrenderer.pos((double)(f7 - f8), (double)(1.0F - f9), 0.0D).tex((double)f4, (double)f5).normal(0.0F, 1.0F, 0.0F).endVertex();
        	worldrenderer.pos((double)(0.0F - f8), (double)(1.0F - f9), 0.0D).tex( (double)f3, (double)f5).normal(0.0F, 1.0F, 0.0F).endVertex();
        	tessellator.draw();
        }
        //this.bindTexture(shellTexTop);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        for(int i=0;i<2;i++){
        	GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        	worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        	
        	worldrenderer.pos((double)(0.0F - f8), (double)(0.0F - f9), 0.0D).tex( (double)f3, (double)f6).normal(1.0F, 0.0F, 0.0F).endVertex();
        	worldrenderer.pos((double)(f7 - f8), (double)(0.0F - f9), 0.0D).tex((double)f4, (double)f6).normal(1.0F, 0.0F, 0.0F).endVertex();
        	worldrenderer.pos((double)(f7 - f8), (double)(1.0F - f9), 0.0D).tex((double)f4, (double)f5).normal(1.0F, 0.0F, 0.0F).endVertex();
        	worldrenderer.pos((double)(0.0F - f8), (double)(1.0F - f9), 0.0D).tex( (double)f3, (double)f5).normal(1.0F, 0.0F, 0.0F).endVertex();
        	tessellator.draw();
        }
        
        if(doFuse){
        	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
        }
        
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        
    }
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */

    protected ResourceLocation getEntityTexture(EntityShell par1EntityFireball)
    {
        return this.shellTex;
    }

   
}