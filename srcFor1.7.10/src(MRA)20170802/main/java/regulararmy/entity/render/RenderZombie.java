package regulararmy.entity.render;

import regulararmy.entity.EntityZombieR;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderZombie extends RenderBiped
{
    private static final ResourceLocation zombiePigmanTextures = new ResourceLocation("textures/entity/zombie_pigman.png");
    private static final ResourceLocation zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
    private static final ResourceLocation zombieVillagerTextures = new ResourceLocation("textures/entity/zombie/zombie_villager.png");
    private ModelBiped field_82434_o;
    private ModelZombieVillager zombieVillagerModel;
    protected ModelBiped modelZombie;
    protected ModelBiped modelZombieChild;
    protected ModelBiped modelZombieVillager;
    protected ModelBiped modelZombieVillagerChild;
    private int field_82431_q = 1;

    public RenderZombie()
    {
        super(new ModelZombie(), 0.5F, 1.0F);
        this.field_82434_o = this.modelBipedMain;
        this.zombieVillagerModel = new ModelZombieVillager();
    }

    protected void func_82421_b()
    {
        this.field_82423_g = new ModelZombie(1.0F, true);
        this.field_82425_h = new ModelZombie(0.5F, true);
        this.modelZombie = this.field_82423_g;
        this.modelZombieChild = this.field_82425_h;
        this.modelZombieVillager = new ModelZombieVillager(1.0F, 0.0F, true);
        this.modelZombieVillagerChild = new ModelZombieVillager(0.5F, 0.0F, true);
    }

    protected int shouldRenderPass(EntityZombieR par1EntityZombie, int par2, float par3)
    {
        this.func_82427_a(par1EntityZombie);
        return super.shouldRenderPass(par1EntityZombie, par2, par3);
    }

    public void doRender(EntityZombieR par1EntityZombie, double par2, double par4, double par6, float par8, float par9)
    {
        this.func_82427_a(par1EntityZombie);
        super.doRender(par1EntityZombie, par2, par4, par6, par8, par9);
    }

    protected ResourceLocation getEntityTexture(EntityZombieR par1EntityZombie)
    {
        return zombieTextures;
    }

    protected void renderEquippedItems(EntityZombieR par1EntityZombie, float par2)
    {
        this.func_82427_a(par1EntityZombie);
        super.renderEquippedItems(par1EntityZombie, par2);
    }

    private void func_82427_a(EntityZombieR par1EntityZombie)
    {
    	this.mainModel = this.field_82434_o;
    	this.field_82423_g = this.modelZombie;
    	this.field_82425_h = this.modelZombieChild;
    	this.modelBipedMain = (ModelBiped)this.mainModel;
    }

    protected void rotateCorpse(EntityZombieR p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        super.rotateCorpse(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    protected void renderEquippedItems(EntityLiving p_77029_1_, float p_77029_2_)
    {
        this.renderEquippedItems((EntityZombieR)p_77029_1_, p_77029_2_);
    }
    protected ResourceLocation getEntityTexture(EntityLiving p_110775_1_)
    {
        return this.getEntityTexture((EntityZombieR)p_110775_1_);
    }

    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityZombieR)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    /**
    **
    * Queries whether should render the specified pass or not.
    */
   protected int shouldRenderPass(EntityLiving p_77032_1_, int p_77032_2_, float p_77032_3_)
   {
       return this.shouldRenderPass((EntityZombieR)p_77032_1_, p_77032_2_, p_77032_3_);
   }

   /**
    * Queries whether should render the specified pass or not.
    */
   protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_)
   {
       return this.shouldRenderPass((EntityZombieR)p_77032_1_, p_77032_2_, p_77032_3_);
   }

   protected void renderEquippedItems(EntityLivingBase p_77029_1_, float p_77029_2_)
   {
       this.renderEquippedItems((EntityZombieR)p_77029_1_, p_77029_2_);
   }

   protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
   {
       this.rotateCorpse((EntityZombieR)p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
   }

   /**
    * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
    * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
    * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
    * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
    */
   public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
   {
       this.doRender((EntityZombieR)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   /**
    * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
    */
   protected ResourceLocation getEntityTexture(Entity p_110775_1_)
   {
       return this.getEntityTexture((EntityZombieR)p_110775_1_);
   }

   /**
    * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
    * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
    * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
    * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
    */
   public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
   {
       this.doRender((EntityZombieR)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }
}
