package regulararmy.entity.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import regulararmy.entity.EntityZombieSpearer;
import regulararmy.entity.model.ModelZombieSpearer;

@SideOnly(Side.CLIENT)
public class RenderZombieSpearer extends RenderBiped
{
    private static final ResourceLocation[] textures = {new ResourceLocation("monsterregulararmy:textures/entity/spearer_bamboo.png"),
    	new ResourceLocation("monsterregulararmy:textures/entity/spearer_stone.png"),
    	new ResourceLocation("monsterregulararmy:textures/entity/spearer_iron.png"),
    	new ResourceLocation("monsterregulararmy:textures/entity/spearer_diamond.png")};

    public RenderZombieSpearer(ModelBiped model)
    {
        super(model, 0.5F,1.0f);
    }


    protected ResourceLocation getEntityTexture(EntityZombieSpearer par1EntitySkeleton)
    {
        return textures[par1EntitySkeleton.getSpearType()];
    }

    protected ResourceLocation getEntityTexture(EntityLiving p_110775_1_)
    {
        return this.getEntityTexture((EntityZombieSpearer)p_110775_1_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.getEntityTexture((EntityZombieSpearer)par1Entity);
    }
}
