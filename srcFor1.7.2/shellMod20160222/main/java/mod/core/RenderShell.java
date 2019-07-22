package mod.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mod.entity.*;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderShell extends Render
{
    private ResourceLocation shellTex = new ResourceLocation("shellmod:textures/entity/shell.png");
    private ResourceLocation shellTexTop = new ResourceLocation("shellmod:textures/entity/shell_top.png");
    private float size;

    public RenderShell(float size,String sideTexturePath,String topTexturePath)
    {
    	this.size=size;
    	shellTex=new ResourceLocation(sideTexturePath);
    	shellTexTop=new ResourceLocation(topTexturePath);
    }

    public void doRenderShell(EntityShell par1EntityShell, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float f2 = this.size;
        GL11.glScalef(f2 / 1.0F, f2 / 1.0F, f2 / 1.0F);
        IIcon icon = ShellCore.itemShell.getIconFromDamage(0);
        Tessellator tessellator = Tessellator.instance;
        float f3 = 0;
        float f4 = 1.0f;
        float f5 = 1.0f;
        float f6 = 0;
        
        float f7 = 1.0F;
        float f8 = 0.5F;
        float f9 = 0.5F;
        this.bindTexture(shellTex);
        
        for(int i=0;i<4;i++){
        	GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        	tessellator.startDrawingQuads();
        	tessellator.setNormal(0.0F, 1.0F, 0.0F);
        	tessellator.addVertexWithUV((double)(0.0F - f8), (double)(0.0F - f9), 0.0D, (double)f3, (double)f6);
        	tessellator.addVertexWithUV((double)(f7 - f8), (double)(0.0F - f9), 0.0D, (double)f4, (double)f6);
        	tessellator.addVertexWithUV((double)(f7 - f8), (double)(1.0F - f9), 0.0D, (double)f4, (double)f5);
        	tessellator.addVertexWithUV((double)(0.0F - f8), (double)(1.0F - f9), 0.0D, (double)f3, (double)f5);
        	tessellator.draw();
        }
        this.bindTexture(shellTexTop);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        for(int i=0;i<2;i++){
        	GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        	tessellator.startDrawingQuads();
        	tessellator.setNormal(1.0F, 0.0F, 0.0F);
        	tessellator.addVertexWithUV((double)(0.0F - f8), (double)(0.0F - f9), 0.0D, (double)f3, (double)f6);
        	tessellator.addVertexWithUV((double)(f7 - f8), (double)(0.0F - f9), 0.0D, (double)f4, (double)f6);
        	tessellator.addVertexWithUV((double)(f7 - f8), (double)(1.0F - f9), 0.0D, (double)f4, (double)f5);
        	tessellator.addVertexWithUV((double)(0.0F - f8), (double)(1.0F - f9), 0.0D, (double)f3, (double)f5);
        	tessellator.draw();
        }
        
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        
    }

    protected ResourceLocation getFireballTextures(EntityShell par1EntityFireball)
    {
        return this.shellTex;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.getFireballTextures((EntityShell)par1Entity);
    }
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    /*protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.shellTex;
    }
*/
    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderShell((EntityShell)par1Entity, par2, par4, par6, par8, par9);
    }
}