package gvcmob;

import gvcguns.GVCEntityBullet;
import gvcguns.GVCEntityBulletG;
import gvcguns.GVCEntityBulletDart;
import gvcguns.GVCEntityGrenade;
import gvcguns.GVCEntityparas;
import gvcguns.GVCGunsPlus;

import java.util.Calendar;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;

public class GVCEntityTank extends EntityMob 
{
	//private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 5, 5, 15.0F);
    private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);
    private static final String __OBFID = "CL_00001697";
    private int field_70846_g;

    public GVCEntityTank(World par1World)
    {
        super(par1World);
        this.isImmuneToFire = true;
        this.experienceValue = 10;
        this.setSize(6F, 4.0F);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityGolem.class, 0, false));

        
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(GVCMobPlus.cfg_guerrillasrach+10D);
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(30.0D);
    }

    
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
    	Entity entity;
    	
    	entity = par1DamageSource.getSourceOfDamage();

        if (entity instanceof GVCEntityBullet)
        {
        	if (entity instanceof GVCEntityBulletDart)
            {
        		par2 = par2 /5;
            }else{
        	return false;
            }
        }
        if (entity instanceof GVCEntityBulletDart)
        {
    		par2 = par2 /5;
        }
        entity = par1DamageSource.getEntity();
    	
    	return super.attackEntityFrom(par1DamageSource, par2);
    }
    
    public boolean getCanSpawnHere()
    {
    	return this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
    }
    
    protected boolean isValidLightLevel()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);

        if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, var1, var2, var3) > this.rand.nextInt(32))
        {
            return false;
        }
        else
        {
            int var4 = this.worldObj.getBlockLightValue(var1, var2, var3);

            if (this.worldObj.isThundering())
            {
                int var5 = this.worldObj.skylightSubtracted;
                this.worldObj.skylightSubtracted = 10;
                var4 = this.worldObj.getBlockLightValue(var1, var2, var3);
                this.worldObj.skylightSubtracted = var5;
            }

            return var4 <= this.rand.nextInt(8);
        }
    }
    
    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity par1Entity, float par2)
    {
        if (this.attackTime <= 0 && par2 < 10.0F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY)
        {
        	if (this.field_70846_g > 1)
        	{
                    float var9 = MathHelper.sqrt_float(par2) * 0.5F;
                    this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)this.posX, (int)this.posY, (int)this.posZ, 0);

                    for (int var10 = 0; var10 < 1; ++var10)
                    {
                    	GVCEntityBulletGe var11 = new GVCEntityBulletGe(this.worldObj, this);
                        //EntitySmallFireball var11 = new EntitySmallFireball(this.worldObj, this, var3 + this.rand.nextGaussian() * (double)var9, var5, var7 + this.rand.nextGaussian() * (double)var9);
                       // var11.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
                        var11.posX = this.posX + 1;
                        var11.posZ = this.posZ - 1;
                        var11.posY = this.posY + 4;
                        double var4 = par1Entity.posX - this.posX;
                        double var6 = par1Entity.posY + (double)par1Entity.getEyeHeight() - 1.100000023841858D - var11.posY;
                        double var8 = par1Entity.posZ - this.posZ;
                        float var13 = MathHelper.sqrt_double(var4 * var4 + var8 * var8) * 0.2F;
                        var11.setThrowableHeading(var4, var6 + (double)var10, var8, 5F, 12.0F);
                        this.playSound("gvcguns:gvcguns.fire", 3.0F, 1.5F);
                        this.worldObj.spawnEntityInWorld(var11);
                    }
        	}
        }
        else if (par2 < 50.0F)
        {
        	
        	
        	
            double var3 = par1Entity.posX - this.posX;
            double var5 = par1Entity.boundingBox.minY + (double)(par1Entity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
            double var7 = par1Entity.posZ - this.posZ;

            if (this.attackTime == 0)
            {
                ++this.field_70846_g;

                if (this.field_70846_g == 1)
                {
                    this.attackTime = 3;
                    this.func_70844_e(true);
                }
                else if (this.field_70846_g <= 4)
                {
                    this.attackTime = 60;
                }
                else
                {
                    this.attackTime = 60;
                    this.field_70846_g = 0;
                    this.func_70844_e(false);
                }

                if (this.field_70846_g > 1)
                {
                    float var9 = MathHelper.sqrt_float(par2) * 0.5F;
                    this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)this.posX, (int)this.posY, (int)this.posZ, 0);

                    for (int var10 = 0; var10 < 1; ++var10)
                    {
                    	GVCEntityBulletGeRPG var11 = new GVCEntityBulletGeRPG(this.worldObj, this);
                        //EntitySmallFireball var11 = new EntitySmallFireball(this.worldObj, this, var3 + this.rand.nextGaussian() * (double)var9, var5, var7 + this.rand.nextGaussian() * (double)var9);
                       // var11.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
                        var11.posX = this.posX + 1;
                        var11.posZ = this.posZ - 1;
                        var11.posY = this.posY + 4;
                        double var4 = par1Entity.posX - this.posX;
                        double var6 = par1Entity.posY + (double)par1Entity.getEyeHeight() - 1.100000023841858D - var11.posY;
                        double var8 = par1Entity.posZ - this.posZ;
                        float var13 = MathHelper.sqrt_double(var4 * var4 + var8 * var8) * 0.2F;
                        var11.setThrowableHeading(var4, var6 + (double)var10, var8, 5F, 12.0F);
                        this.playSound("FN5728.fnP90_s", 10.0F, 1.0F / (this.getRNG().nextFloat() * 30.0F + 30.0F));
                        this.worldObj.spawnEntityInWorld(var11);
                    }
                }
            }

            this.rotationYaw = (float)(Math.atan2(var7, var3) * 180.0D / Math.PI) - 90.0F;
            this.hasAttacked = true;
        }
    }
    
    public void onDeath(DamageSource par1DamageSource)
    {
        super.onDeath(par1DamageSource);
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3.5F, false);
        if (!this.worldObj.isRemote)
        {
    	GVCEntityGuerrillaP entityskeleton = new GVCEntityGuerrillaP(this.worldObj);
        entityskeleton.setLocationAndAngles(this.posX+2, this.posY+3, this.posZ+2, this.rotationYaw, 0.0F);
        entityskeleton.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_aks74u));
        this.worldObj.spawnEntityInWorld(entityskeleton);
        
        GVCEntityGuerrillaP entityskeleton2 = new GVCEntityGuerrillaP(this.worldObj);
        entityskeleton2.setLocationAndAngles(this.posX-2, this.posY+3, this.posZ+2, this.rotationYaw, 0.0F);
        entityskeleton2.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_aks74u));
        this.worldObj.spawnEntityInWorld(entityskeleton2);
        
        GVCEntityGuerrillaP entityskeleton3 = new GVCEntityGuerrillaP(this.worldObj);
        entityskeleton3.setLocationAndAngles(this.posX+2, this.posY+3, this.posZ-2, this.rotationYaw, 0.0F);
        entityskeleton3.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_aks74u));
        this.worldObj.spawnEntityInWorld(entityskeleton3);
        
        GVCEntityGuerrillaP entityskeleton4 = new GVCEntityGuerrillaP(this.worldObj);
        entityskeleton4.setLocationAndAngles(this.posX-2, this.posY+3, this.posZ-2, this.rotationYaw, 0.0F);
        entityskeleton4.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_aks74u));
        this.worldObj.spawnEntityInWorld(entityskeleton4);
        }
    }
    
    public void func_70844_e(boolean par1)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (par1)
        {
            var2 = (byte)(var2 | 1);
        }
        else
        {
            var2 &= -2;
        }

        this.dataWatcher.updateObject(16, Byte.valueOf(var2));
    }
    
    
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }

    

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "none";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.irongolem.hit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.irongolem.death";
    }
    
    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {
        this.playSound("gvcmob:gvcmob.tank", 1.20F, 1.0F);
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    
    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden()
    {
        super.updateRidden();

        if (this.ridingEntity instanceof EntityCreature)
        {
            EntityCreature var1 = (EntityCreature)this.ridingEntity;
            this.renderYawOffset = var1.renderYawOffset;
        }
    }

    /*protected Item func_146068_u()
    {
        return GVCGunsPlus.fn_magazine;
    }*/

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        int var3;
        int var4;

        var3 = this.rand.nextInt(3 + par2);

        for (var4 = 0; var4 < var3; ++var4)
        {
        	this.entityDropItem(new ItemStack(Blocks.iron_block, 10), 0.0F);
        }

        for (var4 = 0; var4 < var3; ++var4)
        {
        	this.entityDropItem(new ItemStack(Blocks.emerald_block, 5), 0.0F);
        }
        
        for (var4 = 0; var4 < var3; ++var4)
        {
        	this.entityDropItem(new ItemStack(Blocks.redstone_block, 5), 0.0F);
        }
    }

    protected void dropRareDrop(int par1)
    {
        
            this.entityDropItem(new ItemStack(Blocks.beacon, 1), 0.0F);
        
    }
   

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        return super.getYOffset() - 0.5D;
    }

	public static float getMobScale() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return 2;
	}
}
