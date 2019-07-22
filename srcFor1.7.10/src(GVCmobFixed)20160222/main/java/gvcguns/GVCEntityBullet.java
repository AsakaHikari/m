package gvcguns;

import java.util.List;

import littleMaidMobX.LMM_EntityLittleMaid;
import littleMaidMobX.LMM_EntityLittleMaidAvatar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class GVCEntityBullet extends EntityThrowable
{
    private static final boolean isDebugMessage = false;
	public EntityLivingBase thrower;
	protected Block inBlock;
	private int inData;
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    protected boolean inGround;
    public int throwableShake;
    protected int knockbackStrength;
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
	
    
    //int i = mod_IFN_GuerrillaVsCommandGuns.RPGExplosiontime;
	@Override
	protected void entityInit() {
		if (worldObj != null) {
			isImmuneToFire = !worldObj.isRemote;
		}
		
	}

	public GVCEntityBullet(World par1World)
    {
        super(par1World);
        //this.fuse = 30;
    }

    public GVCEntityBullet(World par1World, EntityLivingBase par2EntityLivingBase, int damege, float bspeed, float bure)
    {
    	
    	super(par1World, par2EntityLivingBase);
        //this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.func_70182_d(), 1.0F);
        //this.fuse = 30;
        this.Bdamege = damege;
        //this.Bspeed = bspeed;
        //this.Bure = bure;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, bspeed, bure);
    }

    public GVCEntityBullet(World par1World, double par2, double par4, double par6)
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
    
    @Override
	public void setPositionAndRotation2(double par1, double par3, double par5,
			float par7, float par8, int par9) {
		// 着弾後に変な移動が起こるのを抑制
		if (!inGround) {
			setPosition(par1, par3, par5);
			setRotation(par7, par8);
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
        
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f) * 180.0D / Math.PI);
        }

        Block block = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);

        if (block.getMaterial() != Material.air)
        {
            block.setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);

            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ)))
            {
                this.inGround = true;
            }
        }
        
        if (this.inGround)
        {
            if (this.worldObj.getBlock(this.field_145788_c, this.field_145786_d, this.field_145787_e) == this.field_145785_f)
            {
                ++this.ticksInGround;

                if (this.ticksInGround == 10)
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

        float f2;
        float f4;
        
        if (movingobjectposition != null)
        {
        	if (movingobjectposition.entityHit != null)
            {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal)
            {
                this.setInPortal();
            }
            else
            {
                this.onImpact(movingobjectposition);
            }
            }else
            {
                this.xTile = movingobjectposition.blockX;
                this.yTile = movingobjectposition.blockY;
                this.zTile = movingobjectposition.blockZ;
                this.inBlock = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
                this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                this.motionX = (double)((float)(movingobjectposition.hitVec.xCoord - this.posX));
                this.motionY = (double)((float)(movingobjectposition.hitVec.yCoord - this.posY));
                this.motionZ = (double)((float)(movingobjectposition.hitVec.zCoord - this.posZ));
                f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                this.posX -= this.motionX / (double)f2 * 0.05000000074505806D;
                this.posY -= this.motionY / (double)f2 * 0.05000000074505806D;
                this.posZ -= this.motionZ / (double)f2 * 0.05000000074505806D;
                this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                this.inGround = true;
                //this.arrowShake = 7;
                //this.setIsCritical(false);

                if (this.inBlock.getMaterial() != Material.air)
                {
                    this.inBlock.onEntityCollidedWithBlock(this.worldObj, this.xTile, this.yTile, this.zTile, this);
                }
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

        this.motionX *= (double)var17;
        this.motionY *= (double)var17;
        this.motionZ *= (double)var17;
        this.motionY += (double)var18;
        this.setPosition(this.posX, this.posY, this.posZ);
    }
    
    
    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition var1)
    {
        if (var1.entityHit != null)
        {
            int var2 = this.Bdamege;

            if(GVCGunsPlus.cfg_FriendFireLMM == true){
             if (var1.entityHit instanceof LMM_EntityLittleMaid)
             {
                var2 = 0;
             }
             if (var1.entityHit instanceof LMM_EntityLittleMaidAvatar)
             {
                var2 = 0;
             }
            }
            var1.entityHit.hurtResistantTime = 0;

            var1.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)var2);
            
            Entity lel = (Entity)var1.entityHit;
			// 繝弱ャ繧ｯ繝舌ャ繧ｯ
			/*lel.addVelocity((motionX/2D),
							(-motionY-1D),
							(motionZ/2D));*/
			
			if (!this.worldObj.isRemote)
	        {
	            this.setDead();
	            //this.explode();
	        }
        }else {
        	Block lblock = worldObj.getBlock(var1.blockX, var1.blockY, var1.blockZ);
			int lmeta = worldObj.getBlockMetadata(var1.blockX, var1.blockY, var1.blockZ);
			if (checkDestroyBlock(var1, var1.blockX, var1.blockY, var1.blockZ, lblock, lmeta)) {
				if (!this.worldObj.isRemote)
		        {
				onBreakBlock(var1, var1.blockX, var1.blockY, var1.blockZ, lblock, lmeta);
		        }
			} else {
				// 雋ｫ騾壹〒縺阪↑縺九▲縺?
				posX = var1.hitVec.xCoord;
				posY = var1.hitVec.yCoord;
				posZ = var1.hitVec.zCoord;
				motionX = 0;
				motionY = 0;
				motionZ = 0;
				inGround = true;
				inBlock = lblock;
				xTile = var1.blockX;
				yTile = var1.blockY;
				zTile = var1.blockZ;
				setPosition(posX, posY, posZ);
				// 逹?ｼｾ繝代?繝?ぅ繧ｯ繝ｫ
				for (int i = 0; i < 8; ++i) {
//					worldObj.spawnParticle("snowballpoof", this.posX, this.posY,
					worldObj.spawnParticle("smoke",
							var1.hitVec.xCoord, var1.hitVec.yCoord, var1.hitVec.zCoord,
							0.0D, 0.0D, 0.0D);
				}
				
			}
			for (int j = 0; j < 8; ++j)
	        {
	            this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
	        }
			
			if (!this.worldObj.isRemote)
	        {
	            this.setDead();
	            //this.explode();
	        }
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
    
    /**
	 * 遐ｴ螢雁ｯｾ雎｡縺ｧ縺ゅｋ縺九?遒ｺ隱?
	 * @param var1
	 * @param pX
	 * @param pY
	 * @param pZ
	 * @param pBlock
	 * @param pMetadata
	 * @return
	 */
	public boolean checkDestroyBlock(MovingObjectPosition var1, int pX, int pY, int pZ, Block pBlock, int pMetadata) {
		if ((pBlock instanceof BlockPane && pBlock.getMaterial() == Material.glass)
				|| (pBlock instanceof BlockFlowerPot)
				|| (pBlock instanceof BlockTNT)
				|| (pBlock instanceof BlockTallGrass)
				|| (pBlock instanceof BlockDoublePlant)
				
				
				
				) {
			return true;
		}
		return false;
	}
    
    /**
	 * 遐ｴ螢雁虚菴?
	 * @param var1
	 * @param pX
	 * @param pY
	 * @param pZ
	 * @param pBlock
	 * @param pMetadata
	 * @return
	 */
	public boolean onBreakBlock(MovingObjectPosition var1, int pX, int pY, int pZ, Block pBlock, int pMetadata) {
		this.Debug("destroy: %d, %d, %d", pX, pY, pZ);
		if (pBlock instanceof BlockTNT) {
			removeBlock(pX, pY, pZ, pBlock, pMetadata);
			pBlock.onBlockDestroyedByExplosion(worldObj, pX, pY, pZ, new Explosion(worldObj, getThrower(), pX, pY, pZ, 0.0F));
			return true;
		} else {
			removeBlock(pX, pY, pZ, pBlock, pMetadata);
			pBlock.onBlockDestroyedByPlayer(worldObj, pX, pY, pZ, pMetadata);
			//this.entityDropItem(new ItemStack(pBlock), 1);
			return false;
		}
	}
	
	public static void Debug(String pText, Object... pData) {
		// 繝?ヰ繝?げ繝｡繝?そ繝ｼ繧ｸ
		if (isDebugMessage) {
			System.out.println(String.format("GunsBase-" + pText, pData));
		}
	}
	
	/**
	 * 繝悶Ο繝?け遐ｴ螢頑凾縺ｮ蜍穂ｽ?
	 * @param pX
	 * @param pY
	 * @param pZ
	 * @param pBlock
	 * @param pMetadata
	 */
	protected void removeBlock(int pX, int pY, int pZ, Block pBlock, int pMetadata) {
		worldObj.playAuxSFX(2001, pX, pY, pZ, Block.getIdFromBlock(pBlock) + (pMetadata << 12));
		worldObj.setBlockToAir(pX, pY, pZ);	
	}
    
    
    private void explode()
    {
    	
        	this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3.5F, false);
        
    }
    
}
