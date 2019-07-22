package gvcmob;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class GVCEntityBulletGeRPG extends GVCEntityBulletBase
{
    public EntityLivingBase thrower;
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    protected boolean inGround;
    public int throwableShake;
    protected int knockbackStrength = 1;
    public boolean isInfinity;
    protected double damage;	
    //public int fuse;

    /**
     * Is the entity that throws this 'thing' (snowball, ender pearl, eye of ender or potion)
     */
    
    private String throwerName;
    private int ticksInGround;
    private int ticksInAir;
	private int field_145788_c;
	private int field_145786_d;
	private int field_145787_e;
	private Block field_145785_f;
	
	private static int Bdamege;
	private static float Bspeed;
	private static float Bure;
	
    
    //int i = mod_IFN_GuerrillaVsCommandGuns.RPGExplosiontime;
	@Override
	protected void entityInit() {
		if (worldObj != null) {
			isImmuneToFire = !worldObj.isRemote;
		}
		
	}

	public GVCEntityBulletGeRPG(World par1World)
    {
        super(par1World);
        //this.fuse = 30;
    }

    public GVCEntityBulletGeRPG(World par1World, EntityLivingBase par2EntityLivingBase)
    {
    	
    	super(par1World, par2EntityLivingBase);
        //this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.func_70182_d(), 1.0F);
        //this.fuse = 30;
        //this.Bdamege = damege;
        //this.Bspeed = bspeed;
        //this.Bure = bure;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 1.6F, 12F);
    }

    public GVCEntityBulletGeRPG(World par1World, double par2, double par4, double par6)
    {
    	
        super(par1World, par2, par4, par6);
        //this.fuse = 30;
    }

    public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8)
    {
        float var9 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= (double)var9;
        par3 /= (double)var9;
        par5 /= (double)var9;
        par1 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.010499999832361937D * (double)par8;
        par3 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.010499999832361937D * (double)par8;
        par5 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.010499999832361937D * (double)par8;
        par1 *= (double)par7;
        par3 *= (double)par7;
        par5 *= (double)par7;
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        float var10 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, (double)var10) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }
    
    
    /*public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_)
    {
        float f2 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
        p_70186_1_ /= (double)f2;
        p_70186_3_ /= (double)f2;
        p_70186_5_ /= (double)f2;
        p_70186_1_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_70186_8_;
        p_70186_3_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_70186_8_;
        p_70186_5_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_70186_8_;
        p_70186_1_ *= (double)p_70186_7_;
        p_70186_3_ *= (double)p_70186_7_;
        p_70186_5_ *= (double)p_70186_7_;
        this.motionX = p_70186_1_;
        this.motionY = p_70186_3_;
        this.motionZ = p_70186_5_;
        float f3 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(p_70186_1_, p_70186_5_) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(p_70186_3_, (double)f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }*/
    
    @Override
	public void setVelocity(double par1, double par3, double par5) {
		this.motionX = par1;
		this.motionY = par3;
		this.motionZ = par5;
		
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, (double)var7) * 180.0D / Math.PI);
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}
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
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        
        if (this.throwableShake > 0)
        {
            --this.throwableShake;
        }

        if (this.inGround)
        {
            if (this.worldObj.getBlock(this.field_145788_c, this.field_145786_d, this.field_145787_e) == this.field_145785_f)
            {
                ++this.ticksInGround;

                if (this.ticksInGround == 1200)
                {
                    this.setDead();
                }

                return;
            }

            this.inGround = false;
            this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        }
        else
        {
            ++this.ticksInAir;
        }

        Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
        vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null)
        {
            vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        if (!this.worldObj.isRemote)
        {
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            EntityLivingBase entitylivingbase = this.getThrower();

            for (int j = 0; j < list.size(); ++j)
            {
                Entity entity1 = (Entity)list.get(j);

                if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || this.ticksInAir >= 5))
                {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f, (double)f, (double)f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null)
                    {
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null)
            {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }

        if (movingobjectposition != null)
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal)
            {
                this.setInPortal();
            }
            else
            {
                this.onImpact(movingobjectposition);
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float var17 = 0.99F;
        //float var18 = this.getGravityVelocity();
        float var18 = 0.03F;

        if (this.isInWater())
        {
            for (int var7 = 0; var7 < 4; ++var7)
            {
                float var19 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var19, this.posY - this.motionY * (double)var19, this.posZ - this.motionZ * (double)var19, this.motionX, this.motionY, this.motionZ);
            }

            var17 = 0.8F;
        }

        this.motionX *= (double)var17;
        this.motionY *= (double)var17;
        this.motionZ *= (double)var17;
        this.motionY -= (double)var18;
        this.setPosition(this.posX, this.posY, this.posZ);
    }
    
    
    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition)
    {
        if (par1MovingObjectPosition.entityHit != null)
        {
            int var2 = 30;
            if (par1MovingObjectPosition.entityHit instanceof EntityCreeper)
            {
            	var2 = 40;
            }

            if(this.worldObj.difficultySetting == EnumDifficulty.HARD){
            	var2 = 99;
            }else{
            	var2 = 30;
            }
            if (par1MovingObjectPosition.entityHit instanceof EntityCreeper)
            {
            	var2 = 40;
            }
            
            par1MovingObjectPosition.entityHit.hurtResistantTime = 0;

            par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)var2);
            
            /*float lfd = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
            int ldam = (int)Math.ceil((double)lfd * damage * 0.1D * (isInfinity ? 0.5D : 1D));
            if (par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), ldam)) {
            
            if (par1MovingObjectPosition.entityHit instanceof EntityLivingBase) {
				EntityLivingBase lel = (EntityLivingBase)par1MovingObjectPosition.entityHit;
               if (knockbackStrength > 0) {
				if (lfd > 0.0F) {
					lel.addVelocity(
							(motionX * (double)knockbackStrength * 0D) / (double)lfd,
							(motionY * (double)knockbackStrength * 0D) / (double)lfd + 0D,
							(motionZ * (double)knockbackStrength * 0D) / (double)lfd);
				}
			 }
            }
            }*/
        
        }else {
			xTile = par1MovingObjectPosition.blockX;
			yTile = par1MovingObjectPosition.blockY;
			zTile = par1MovingObjectPosition.blockZ;
			inTile = worldObj.getBlock(xTile, yTile, zTile);
			if (inTile == Blocks.glass_pane || inTile == Blocks.flower_pot || inTile == Blocks.glass || inTile == Blocks.tallgrass || inTile == Blocks.double_plant) {
				motionX *= 0.8;
				motionY *= 0.8;
				motionZ *= 0.8;
				
				onBlockDestroyed(xTile, yTile, zTile);
			} else {
				}
				motionX = par1MovingObjectPosition.hitVec.xCoord - posX;
				motionY = par1MovingObjectPosition.hitVec.yCoord - posY;
				motionZ = par1MovingObjectPosition.hitVec.zCoord - posZ;
				inGround = true;
				if (!worldObj.isRemote) {
					//worldObj.playSoundAtEntity(this, "FN5728.bullethitBlock", 1.0F, rand.nextFloat() * 0.2F + 0.9F);
					//this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
//					this.playSound("FN5728.fnP90_s", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
				}

			}
//			
        isAirBorne = true;
		velocityChanged = true;
        
        if (!this.worldObj.isRemote)
        {
            this.setDead();
            this.explode();
        }
        
        
        
    }
    
    public void onBlockDestroyed(int blockX, int blockY, int blockZ) {
		//int bid = worldObj.getBlock(blockX, blockY, blockZ);
		int bmd = worldObj.getBlockMetadata(blockX, blockY, blockZ);
		Block block = worldObj.getBlock(blockX, blockY, blockZ);
		if(block == null) {
			return;
		}
		worldObj.playAuxSFX(2001, blockX, blockY, blockZ, (bmd  << 12));
		boolean flag = worldObj.setBlockToAir(blockX, blockY, blockZ);
		if (block != null && flag) {
			block.onBlockDestroyedByPlayer(worldObj, blockX, blockY, blockZ, bmd);
		}
	}
    
    
    private void explode()
    {
    	
        	this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3.5F, false);
        
    }
}
