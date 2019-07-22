package gvcguns;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class GVCEntityGrenade extends EntityThrowable
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

	public GVCEntityGrenade(World par1World)
    {
        super(par1World);
        this.fuse = 60;
    }

    public GVCEntityGrenade(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World, par2EntityLivingBase);
        this.fuse = 60;
    }

    public GVCEntityGrenade(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
        this.fuse = 60;
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
        
        /*IFN_Explosion ex = new IFN_Explosion(worldObj, this, posX, posY, posZ, 3.5F);
        ex.isFlaming = false;
		ex.isSmoking = true;
        ex.doExplosionA();
        ex.doExplosionB(true);*/
        
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition){}
}
