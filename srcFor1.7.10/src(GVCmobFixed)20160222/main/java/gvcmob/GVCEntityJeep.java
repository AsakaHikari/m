package gvcmob;

import gvcguns.GVCEntityBullet;
import gvcguns.GVCEntityBulletDart;
import gvcguns.GVCEntityBulletG;
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
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;

public class GVCEntityJeep extends EntityMob //implements IRangedAttackMob
{
	//private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 5, 5, 15.0F);
    private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);
    private static final String __OBFID = "CL_00001697";

    public GVCEntityJeep(World par1World)
    {
        super(par1World);
        this.setSize(3.0F, 1.5F);
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

        if (par1World != null && !par1World.isRemote)
        {
           // this.setCombatTask();
        }
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(GVCMobPlus.cfg_guerrillasrach+10D);
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(30.0D);
    }

    public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2)
    {
    	
    }
    
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(13, new Byte((byte)0));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.skeleton.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.skeleton.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.skeleton.death";
    }

    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {
        this.playSound("mob.skeleton.step", 0.15F, 1.0F);
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        
        {
            return false;
        }
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


    protected void dropFewItems(boolean par1, int par2)
    {
        int var3;
        int var4;

        var3 = this.rand.nextInt(3 + par2);

        for (var4 = 0; var4 < var3; ++var4)
        {
        	this.entityDropItem(new ItemStack(Blocks.iron_block, 5), 0.0F);
        }

        for (var4 = 0; var4 < var3; ++var4)
        {
        	this.entityDropItem(new ItemStack(Blocks.emerald_block, 2), 0.0F);
        }
        
        for (var4 = 0; var4 < var3; ++var4)
        {
        	this.entityDropItem(new ItemStack(Blocks.redstone_block, 2), 0.0F);
        }
    }
    /**
     * Makes entity wear random armor based on difficulty
     */
    protected void addRandomArmor()
    {
        super.addRandomArmor();
        //this.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_ak74));
    }

    public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData)
    {
        par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);

        if(this.worldObj.rand.nextInt(9)== 0){
        	GVCEntityAAG entityskeleton = new GVCEntityAAG(this.worldObj);
            entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            entityskeleton.onSpawnWithEgg((IEntityLivingData)null);
            this.worldObj.spawnEntityInWorld(entityskeleton);
            entityskeleton.mountEntity(this);
        }else{
        	GVCEntityGuerrillaMG entityskeleton = new GVCEntityGuerrillaMG(this.worldObj);
            entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            entityskeleton.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_pkm));
            entityskeleton.onSpawnWithEgg((IEntityLivingData)null);
            this.worldObj.spawnEntityInWorld(entityskeleton);
            entityskeleton.mountEntity(this);
        }
        return par1EntityLivingData;
    }

    /**
     * sets this entity's combat AI.
     */
    /*
    public void setCombatTask()
    {
        this.tasks.removeTask(this.aiAttackOnCollide);
        this.tasks.removeTask(this.aiArrowAttack);
        ItemStack var1 = this.getHeldItem();

        
            this.tasks.addTask(4, this.aiArrowAttack);
        
    }
*/
    public void onDeath(DamageSource par1DamageSource)
    {
        super.onDeath(par1DamageSource);
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3.5F, false);
        
    }
    

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

       // this.setCombatTask();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        //par1NBTTagCompound.setByte("SkeletonType", (byte)this.getSkeletonType());
    }

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack)
    {
        super.setCurrentItemOrArmor(par1, par2ItemStack);

        if (!this.worldObj.isRemote && par1 == 0)
        {
            //this.setCombatTask();
        }
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        return super.getYOffset() - 0.5D;
    }

	public static float getMobScale() {
		// TODO �ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ�ｿｽ黷ｽ�ｿｽ�ｿｽ�ｿｽ\�ｿｽb�ｿｽh�ｿｽE�ｿｽX�ｿｽ^�ｿｽu
		return 3;
	}
}
