package gvcguns;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class GVCEntityGurenadeBulletLVG extends GVCEntityGrenade
{
	public int fuse;
	
    public EntityLivingBase thrower;
    public double power = 2.5D;	    
    public double punch = 1;		
    public boolean flame = false;
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public float explosionSize;

	public GVCEntityGurenadeBulletLVG(World par1World)
    {
        super(par1World);
        this.fuse = 60;
    }

    public GVCEntityGurenadeBulletLVG(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World, par2EntityLivingBase);
        this.fuse = 60;
    }

    public GVCEntityGurenadeBulletLVG(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
        this.fuse = 60;
    }

    
    protected float func_70182_d()
    {
        return 2.0F;
    }
    
    
    /*protected float func_70182_d()
    {
        return 5F;
    }*/
    
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float par1)
    {
        return 15728880;
    }
    
    public float getBrightness(float par1)
    {
        return 1.0F;
    }
    
    protected boolean isValidLightLevel()
    {
        return true;
    }
    
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        
        
        if (this.fuse-- <= 0)
        {
        	
        	
            this.setDead();

            
            
            if (!this.worldObj.isRemote)
            {
                this.explode();
                
            }
        }
        else
        {
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    public void explode()
    {
        float var1 = 2.7F;
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, var1, false);
        
        GVCExplosion ex = new GVCExplosion(worldObj, this, posX, posY, posZ, 2.7F);
        ex.isFlaming = true;
		ex.isSmoking = false;
        ex.doExplosionA();
        ex.doExplosionB(true);
        
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition){}
}
