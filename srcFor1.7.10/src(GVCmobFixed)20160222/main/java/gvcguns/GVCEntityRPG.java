package gvcguns;

import java.util.List;

import littleMaidMobX.LMM_EntityLittleMaid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class GVCEntityRPG extends EntityThrowable
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
	
	private int Bdamege;
	private float Bspeed;
	private float Bure;
	private float Explosionlv;
	private boolean Flame;
	private boolean Blockbreak;
    
    //int i = mod_IFN_GuerrillaVsCommandGuns.RPGExplosiontime;
	@Override
	protected void entityInit() {
		if (worldObj != null) {
			isImmuneToFire = !worldObj.isRemote;
		}
		
	}

	public GVCEntityRPG(World par1World)
    {
        super(par1World);
        //this.fuse = 30;
    }

    public GVCEntityRPG(World par1World, EntityLivingBase par2EntityLivingBase, int damege, float bspeed, float bure, float explv, boolean fb, boolean bb)
    {
    	
    	super(par1World, par2EntityLivingBase);
        //this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.func_70182_d(), 1.0F);
        //this.fuse = 30;
        this.Bdamege = damege;
        //this.Bspeed = bspeed;
        //this.Bure = bure;
        this.Explosionlv = explv;
        this.Flame = fb;
        this.Blockbreak = bb;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, bspeed, bure);
    }

    public GVCEntityRPG(World par1World, double par2, double par4, double par6)
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
        par1 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.001499999832361937D * (double)par8;
        par3 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.001499999832361937D * (double)par8;
        par5 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.001499999832361937D * (double)par8;
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
    
 // ホーミングミサイルの向いている方向
    /*public Vec3 getLookVec()
    {
        return getLook(1.0F);
    }
    public Vec3 getLook(float par1)
    {
        if (par1 == 1.0F)
        {
            float f = MathHelper.cos(-rotationYaw * 0.01745329F - (float)Math.PI);
            float f2 = MathHelper.sin(-rotationYaw * 0.01745329F - (float)Math.PI);
            float f4 = -MathHelper.cos(-rotationPitch * 0.01745329F);
            float f6 = MathHelper.sin(-rotationPitch * 0.01745329F);
            return this.worldObj.getWorldVec3Pool().getVecFromPool(f2 * f4, f6, f * f4);
        }
        else
        {
            float f1 = prevRotationPitch + (rotationPitch - prevRotationPitch) * par1;
            float f3 = prevRotationYaw + (rotationYaw - prevRotationYaw) * par1;
            float f5 = MathHelper.cos(-f3 * 0.01745329F - (float)Math.PI);
            float f7 = MathHelper.sin(-f3 * 0.01745329F - (float)Math.PI);
            float f8 = -MathHelper.cos(-f1 * 0.01745329F);
            float f9 = MathHelper.sin(-f1 * 0.01745329F);
            return this.worldObj.getWorldVec3Pool().getVecFromPool(f7 * f8, f9, f5 * f8);
        }
    }*/
    
    
    public void onUpdate()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        //Vec3 lookAt = this.getLookVec();
        //lookAt.rotateAroundY((float)Math.PI*2);
        
        
        
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
        float var18 = 0.02F;

        if (this.isInWater())
        {
            for (int var7 = 0; var7 < 4; ++var7)
            {
                float var19 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var19, this.posY - this.motionY * (double)var19, this.posZ - this.motionZ * (double)var19, this.motionX, this.motionY, this.motionZ);
            }

            var17 = 0.8F;
        }

        
        /*EntityMob entity = null;
        List llist = this.worldObj.getEntitiesWithinAABBExcludingEntity(entity, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(10.0D, 10.0D, 10.0D));
        double d0 = 8.0D;
        EntityLivingBase entitylivingbase = this.getThrower();
        if(llist!=null){
            for (int lj = 0; lj < llist.size(); lj++) {
            	
            	Entity entity1 = (Entity)llist.get(lj);
            	if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || this.ticksInAir >= 5))
                {
            		d0 = 8.0D;
                        double d1 = (entity1.posX - this.posX) / d0;
                        double d2 = (entity1.posY + (double)entity1.getEyeHeight() - this.posY) / d0;
                        double d3 = (entity1.posZ - this.posZ) / d0;
                        double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
                        double d5 = 1.0D - d4;

                        if (d5 > 0.0D)
                        {
                            d5 *= d5;
                            this.motionX += d1 / d4 * d5 * 1.1D;
                            this.motionY += d2 / d4 * d5 * 1.1D;
                            this.motionZ += d3 / d4 * d5 * 1.1D;
                        }
                }
            }
        }
        
        this.moveEntity(this.motionX, this.motionY, this.motionZ);*/
        
        this.motionX *= (double)var17;
        this.motionY *= (double)var17;
        this.motionZ *= (double)var17;
        this.motionY += (double)var18;
        this.setPosition(this.posX, this.posY, this.posZ);
        
        /*EntityMob entity = null;
        List llist = this.worldObj.getEntitiesWithinAABBExcludingEntity(entity, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(2.0D, 2.0D, 2.0D));
        double d0 = 0.0D;
        EntityLivingBase entitylivingbase = this.getThrower();
        if(llist!=null){
            for (int lj = 0; lj < llist.size(); lj++) {
            	
            	Entity entity1 = (Entity)llist.get(lj);
            	if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || this.ticksInAir >= 10))
                {
            	if (!this.worldObj.isRemote)
                {
                    this.setDead();
                    this.explode();
                }
                }
            }
        }*/
        
        
        
        
    }
    
    
    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition)
    {
        if (par1MovingObjectPosition.entityHit != null)
        {
            int var2 = this.Bdamege;

            /*if(GVCGunsPlus.cfg_FriendFireLMM == true){
             if (par1MovingObjectPosition.entityHit instanceof LMM_EntityLittleMaid)
             {
                var2 = 0;
             }
            }*/
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
    	/*GVCExplosion ex = new GVCExplosion(worldObj, this, posX, posY, posZ, 3.5F);
        ex.isFlaming = false;
		ex.isSmoking = true;
        ex.doExplosionA();
        ex.doExplosionB(true);*/
        this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, Explosionlv, Flame, Blockbreak);
        
    }
}
