package regulararmy.entity;

import java.awt.Color;
import java.util.Calendar;

import regulararmy.core.MonsterRegularArmyCore;
import regulararmy.entity.ai.EntityAIArrowAttack;
import regulararmy.entity.ai.EntityAIAttackOnCollide;
import regulararmy.entity.ai.EntityAIEscapeFromDrown;
import regulararmy.entity.ai.EntityAIForwardBase;
import regulararmy.entity.ai.EntityAILearnedTarget;
import regulararmy.entity.ai.EntityAISwimming;
import regulararmy.entity.command.MonsterUnit;
import regulararmy.pathfinding.AStarPathPoint;
import regulararmy.pathfinding.IPathFindRequester;
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
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
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

public class EntitySkeletonR extends EntityRegularArmy implements IRangedAttackMob
{
    public EntityAIArrowAttack aiArrowAttack ;
    public EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this,  1.2D, false);

    public EntitySkeletonR(World par1World)
    {
        super(par1World);
        aiArrowAttack= new EntityAIArrowAttack(this, 1.0D, 20, MonsterRegularArmyCore.isMachinebow?22:60, 15.0F);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIEscapeFromDrown(this));
        this.tasks.addTask(4, new EntityAIForwardBase(this,1.2f));
        //this.tasks.addTask(2, new EntityAIRestrictSun(this));
        //this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        //this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(3, new EntityAILearnedTarget(this,0,true));

        if (par1World != null && !par1World.isRemote)
        {
        	this.setCombatTask();
        }
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
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

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.skeleton.step", 0.15F, 1.0F);
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
    	/*
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote)
        {
            float f = this.getBrightness(1.0F);

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)))
            {
                boolean flag = true;
                ItemStack itemstack = this.getCurrentItemOrArmor(4);

                if (itemstack != null)
                {
                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + this.rand.nextInt(2));

                        if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage())
                        {
                            this.renderBrokenItemStack(itemstack);
                            this.setCurrentItemOrArmor(4, (ItemStack)null);
                        }
                    }

                    flag = false;
                }

                if (flag)
                {
                    this.setFire(8);
                }
            }
        }
*/
       
        NBTTagCompound nbt=this.getEntityData();
        
        super.onLivingUpdate();
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden()
    {
        super.updateRidden();

        if (this.ridingEntity instanceof EntityCreature)
        {
            EntityCreature entitycreature = (EntityCreature)this.ridingEntity;
            this.renderYawOffset = entitycreature.renderYawOffset;
        }
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        int j;
        int k;

        {
            j = this.rand.nextInt(1+3 + par2);

            for (k = 0; k < j; ++k)
            {
                this.dropItem(Items.arrow, 1);
            }
        }

        j = this.rand.nextInt(3 + par2);

        for (k = 0; k < j; ++k)
        {
            this.dropItem(Items.bone, 1);
        }
    }

    /**
     * Makes entity wear random armor based on difficulty
     */
    protected void addRandomArmor()
    {
        super.addRandomArmor();
        this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData)
    {
    	this.tasks.addTask(5, this.getAIArrow());
        this.addRandomArmor();
        this.enchantEquipment();
        par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);

        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * this.worldObj.func_147462_b(this.posX, this.posY, this.posZ));

        if (this.getEquipmentInSlot(4) == null)
        {
            Calendar calendar = this.worldObj.getCurrentDate();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
            {
                this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.pumpkin_stem : Blocks.pumpkin));
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
        this.tasks.removeTask(this.aiAttackOnCollide);
        this.tasks.removeTask(this.getAIArrow());
        ItemStack itemstack = this.getHeldItem();

        if (itemstack != null && itemstack.getItem() == Items.bow)
        {
            this.tasks.addTask(5, this.getAIArrow());
        }
        else
        {
            this.tasks.addTask(5, this.aiAttackOnCollide);
        }
    }
    protected Entity findPlayerToAttack()
    {
        double d0 = 64.0D;
        return this.worldObj.getClosestVulnerablePlayerToEntity(this, d0);
    }
    /**
     * Attack the specified entity using a ranged attack.
     */
    public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2)
    {
        EntityArrow entityarrow = new EntityArrow(this.worldObj, this, par1EntityLivingBase, 1.6F, (float)(3 - this.worldObj.difficultySetting.getDifficultyId()*0.5));
        double d1=MathHelper.sqrt_double((par1EntityLivingBase.posX-entityarrow.posX)*(par1EntityLivingBase.posX-entityarrow.posX)+
        		(par1EntityLivingBase.posZ-entityarrow.posZ)*(par1EntityLivingBase.posZ-entityarrow.posZ));
        double d2=par1EntityLivingBase.posY+ (double)par1EntityLivingBase.getEyeHeight()-0.1-entityarrow.posY;
        //double d3=(d2+0.025*(d2*d2+d1*d1)*0.25)/(Math.sqrt(d1*d1+d2*d2)-0.01*(d1*d1+d2*d2)*0.25);
        double d3=Math.atan2(d2, d1);
        if(d2>0){
        	entityarrow.rotationPitch=(float) (((d3+0.07*-MathHelper.cos((float) (d3*4))+0.07)*180.0f/(float)Math.PI)+(d2*d2+d1*d1)*0.01f)+this.rand.nextFloat()*14-7;
        }else{
        	entityarrow.rotationPitch=(float) (((d3+0.03*-MathHelper.cos((float) (d3*4))+0.03)*180.0f/(float)Math.PI)+(d2*d2+d1*d1)*0.01f)+this.rand.nextFloat()*14-7;
        }
        entityarrow.motionY = (double)1.6*MathHelper.sin((float) (entityarrow.rotationPitch/180.0f*Math.PI));
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
        entityarrow.setDamage((double)(par2 * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.difficultySetting.getDifficultyId() * 0.11F));

        if (i > 0)
        {
            entityarrow.setDamage(entityarrow.getDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0)
        {
            entityarrow.setKnockbackStrength(j);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 )
        {
            entityarrow.setFire(100);
        }

        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entityarrow);
    }

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack)
    {
        super.setCurrentItemOrArmor(par1, par2ItemStack);

        if (!this.worldObj.isRemote && par1 == 0)
        {
            this.setCombatTask();
        }
    }
    
    public void setEquipmentsFromTier(int tier){
    	
    	this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
		this.addRandomArmorFromTier( tier);
		this.enchantEquipmentFromTier( tier);
		this.setHorsesFromTier(tier);
	}

    public EntityAIArrowAttack getAIArrow(){
    	return this.aiArrowAttack;
    }
    @Override
    public void readFromNBT(NBTTagCompound nbt){
    	super.readFromNBT(nbt);
    	this.setCombatTask();
    }

    
}
