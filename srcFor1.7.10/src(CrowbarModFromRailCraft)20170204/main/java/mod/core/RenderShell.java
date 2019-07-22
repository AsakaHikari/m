package mod.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
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
    /** instance of ModelBoat for rendering */
    protected ModelBase modelBoat;
    private ResourceLocation tex = new ResourceLocation("shellmod:textures/entity/shell.png");

    public RenderShell(String texturePath)
    {
    	this.shadowSize=0.8f;
    	tex=new ResourceLocation(texturePath);
    	this.modelBoat = new ModelChunkLoader();
    }

    public void doRenderShell(EntityShell par1EntityShell, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        //System.out.println((par2+RenderManager.renderPosX)+","+(par4+RenderManager.renderPosY)+","+(par6+RenderManager.renderPosZ));
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        
        this.bindTexture(tex);
        this.modelBoat.render(par1EntityShell, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        
    }


    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.tex;
    }
    
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