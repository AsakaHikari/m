package regulararmy.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSniperSkeleton extends RenderBiped
{
    private static final ResourceLocation skeletonTextures = new ResourceLocation("monsterregulararmy:textures/entity/sniperSkeleton.png");
    private static final ResourceLocation witherSkeletonTextures = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

    public RenderSniperSkeleton()
    {
        super(new ModelSniperSkeleton(), 0.5F);
    }

    protected void scaleSkeleton(EntitySniperSkeleton par1EntitySkeleton, float par2)
    {
        if (par1EntitySkeleton.getSkeletonType() == 1)
        {
            GL11.glScalef(1.2F, 1.2F, 1.2F);
        }
    }

    protected void func_82422_c()
    {
        GL11.glTranslatef(0.09375F, 0.1875F, 0.0F);
    }

    protected ResourceLocation func_110860_a(EntitySniperSkeleton par1EntitySkeleton)
    {
        return par1EntitySkeleton.getSkeletonType() == 1 ? witherSkeletonTextures : skeletonTextures;
    }

    protected ResourceLocation func_110856_a(EntityLiving par1EntityLiving)
    {
        return this.func_110860_a((EntitySniperSkeleton)par1EntityLiving);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
    {
        this.scaleSkeleton((EntitySniperSkeleton)par1EntityLivingBase, par2);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110860_a((EntitySniperSkeleton)par1Entity);
    }
}
