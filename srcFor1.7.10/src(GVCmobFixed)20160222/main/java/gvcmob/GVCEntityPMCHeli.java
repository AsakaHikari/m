package gvcmob;

import gvcguns.GVCEntityBullet;
import gvcguns.GVCEntityBulletDart;
import gvcguns.GVCEntityGrenade;
import gvcguns.GVCGunsPlus;

import java.util.Calendar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
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
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;

public class GVCEntityPMCHeli extends EntityGolem implements IRangedAttackMob
{
	//private static final EntityCreature EntityCreature = null;
	private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 4, 4, 100.0F);
	//private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);
	public float field_70886_e;
    public float destPos;
    public float field_70884_g;
    public float field_70888_h;
    public float field_70889_i = 1.0F;
	
    public GVCEntityPMCHeli(World par1World)
    {
        super(par1World);
        this.setSize(4.0F, 4.0F);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAIArrowAttack(this, 1.0D, 4, 4, 100.0F));
        //this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 20.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        //this.tasks.addTask(4, new EntityAITempt(this, 1.2D, Item.carrot.itemID, false));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, true, false, IMob.mobSelector));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityGhast.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityCreeper.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityFlying.class, 0, true));
        
    }
    
    
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float par1)
    {
        return 15728880;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float par1)
    {
        return 1.0F;
    }
    
    
    
    public boolean canAttackClass(Class par1Class)
    {
        return EntityCreature.class != par1Class;
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
    
    
    public void onUpdate()
    {
    	super.onUpdate();
    	if(this.worldObj.rand.nextInt(2) == 0){
    	this.worldObj.playSoundAtEntity(this, "gvcmob:gvcmob.heli", 4.0F, 1.0F);
    	}
    	
    	int i = this.worldObj.rand.nextInt(30)+5;
    	int genY = this.worldObj.getHeightValue((int)this.posX, (int)this.posZ)+12+i;
    	
    	if (this.posY < genY)
        {
    		this.motionY += 0.2D;
        }
    	if(this.worldObj.rand.nextInt(80) == 0){
    		this.motionX += 0.3D;
    	}
    	if(this.worldObj.rand.nextInt(80) == 10){
    		this.motionX -= 0.3D;
    	}
    	if(this.worldObj.rand.nextInt(80) == 30){
    		this.motionZ += 0.3D;
    	}
    	if(this.worldObj.rand.nextInt(80) == 70){
    		this.motionZ -= 0.3D;
    	}

    }
    
    public void onDeath(DamageSource par1DamageSource)
    {
        super.onDeath(par1DamageSource);
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3.5F, false);
        if (!this.worldObj.isRemote)
        {
        	GVCEntityPMC entityskeleton = new GVCEntityPMC(this.worldObj);
        entityskeleton.setLocationAndAngles(this.posX+2, this.posY+3, this.posZ, this.rotationYaw, 0.0F);
        entityskeleton.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_g36));
        this.worldObj.spawnEntityInWorld(entityskeleton);
        
        GVCEntityPMC entityskeleton2 = new GVCEntityPMC(this.worldObj);
        entityskeleton2.setLocationAndAngles(this.posX-2, this.posY+3, this.posZ, this.rotationYaw, 0.0F);
        entityskeleton2.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_g36));
        this.worldObj.spawnEntityInWorld(entityskeleton2);
        }
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
    
    protected boolean canDespawn()
    {
        return false;
    }
    
    
    public void onLivingUpdate()
    {
        this.updateArmSwingProgress();
        float f = this.getBrightness(1.0F);

        if (f > 0.5F)
        {
            this.entityAge += 2;
        }

        
        this.field_70888_h = this.field_70886_e;
        this.field_70884_g = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);

        if (this.destPos < 0.0F)
        {
            this.destPos = 0.0F;
        }

        if (this.destPos > 1.0F)
        {
            this.destPos = 1.0F;
        }

        if (!this.onGround && this.field_70889_i < 1.0F)
        {
            this.field_70889_i = 1.0F;
        }

        this.field_70889_i = (float)((double)this.field_70889_i * 0.9D);

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.3D;
        }

        this.field_70886_e += this.field_70889_i * 2.0F;
        
        
        super.onLivingUpdate();
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
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(90.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100D);
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(30.0D);
    }


    protected void addRandomArmor()
    {
        super.addRandomArmor();
        this.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_m16a4));
    }

    public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData)
    {
        par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);

        /*GVCEntityparas entityskeleton1 = new GVCEntityparas(this.worldObj);
        entityskeleton1.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
        entityskeleton1.onSpawnWithEgg((IEntityLivingData)null);
        this.worldObj.spawnEntityInWorld(entityskeleton1);
        this.mountEntity(entityskeleton1);*/
        
        {
            this.tasks.addTask(4, this.aiArrowAttack);
            this.addRandomArmor();
            this.enchantEquipment();
        }

        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * this.worldObj.func_147462_b(this.posX, this.posY, this.posZ));

        if (this.getEquipmentInSlot(4) == null)
        {
            Calendar var2 = this.worldObj.getCurrentDate();

            if (var2.get(2) + 1 == 10 && var2.get(5) == 31 && this.rand.nextFloat() < 0.25F)
            {
                this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
                this.equipmentDropChances[4] = 0.0F;
            }
        }

        return par1EntityLivingData;
    }

    /**
     * sets this entity's combat AI.
     */
    public void setCombatTask()
    {
        
        this.tasks.removeTask(this.aiArrowAttack);
        ItemStack var1 = this.getHeldItem();

        
            this.tasks.addTask(4, this.aiArrowAttack);
        
    }
    
    /**
     * Attack the specified entity using a ranged attack.
     */
    public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2)
    {
    	GVCEntityBulletGe var3 = new GVCEntityBulletGe(this.worldObj, this);
        double var4 = par1EntityLivingBase.posX - this.posX;
        double var6 = par1EntityLivingBase.posY + (double)par1EntityLivingBase.getEyeHeight() - 1.100000023841858D - var3.posY;
        double var8 = par1EntityLivingBase.posZ - this.posZ;
        float var10 = MathHelper.sqrt_double(var4 * var4 + var8 * var8) * 0.2F;
        var3.setThrowableHeading(var4, var6 + (double)var10, var8, 1.6F, 12.0F);
        this.playSound("gvcguns:gvcguns.fire", 4.0F, 1.0F);
        this.worldObj.spawnEntityInWorld(var3);
        
        if (this.worldObj.rand.nextInt(10) == 0)
        {
        	GVCEntityGrenade var30 = new GVCEntityGrenade(this.worldObj, this);
            var30.setThrowableHeading(var4, var6 + (double)var10, var8, 1.0F, 12.0F);
            this.playSound("gvcguns:gvcguns.grenade", 1.0F, 1.5F);
            this.worldObj.spawnEntityInWorld(var30);
        }
        
        //this.worldObj.spawnEntityInWorld(var3);
    }

	public boolean isConverting() {
		return false;
	}


	public static float getMobScale() {
		// TODO 自動生成されたメソッド・スタブ
		return 8;
	}
}
