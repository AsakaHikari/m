package regulararmy.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Calendar;
import java.util.UUID;

import regulararmy.entity.ai.EntityAIBreakBlock;
import regulararmy.entity.ai.EntityAIEscapeFromDrown;
import regulararmy.entity.ai.EntityAISwimming;
import regulararmy.entity.ai.IBreakBlocksMob;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;

public class EntityZombieR extends EntityRegularArmy 
{

    /**
     * Ticker used to determine the time remaining for this zombie to convert into a villager when cured.
     */

    public EntityZombieR(World par1World)
    {
        super(par1World);
        this.getNavigator().setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIEscapeFromDrown(this));
        //this.tasks.addTask(1, new EntityAIBreakDoor(this));
        //this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        //this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
        //this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        //this.tasks.addTask(5, new EntityAIMoveThroughVillage(this, 1.0D, false));
        //this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        
        //this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20);
        //this.getAttributeMap().func_111150_b(field_110186_bp).setAttribute(this.rand.nextDouble() * ForgeDummyContainer.zombieSummonBaseChance);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(14, Byte.valueOf((byte)0));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return true;
    }


    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.zombie.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.zombie.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.zombie.death";
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.zombie.step", 0.15F, 1.0F);
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected Item getDropItem()
    {
        return Items.rotten_flesh;
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected void dropRareDrop(int par1)
    {
    	super.dropRareDrop(par1);
        switch (this.rand.nextInt(3))
        {
            case 0:
                this.dropItem(Items.iron_ingot, 1);
                break;
            case 1:
                this.dropItem(Items.carrot, 1);
                break;
            case 2:
                this.dropItem(Items.potato, 1);
        }
    }

    public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData)
    {
        Object par1EntityLivingData1 = super.onSpawnWithEgg(par1EntityLivingData);

        this.addRandomArmor();
        this.enchantEquipment();

        if (this.getEquipmentInSlot(4) == null)
        {
            Calendar calendar = this.worldObj.getCurrentDate();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
            {
                this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.pumpkin_stem : Blocks.pumpkin));
                this.equipmentDropChances[4] = 0.0F;
            }
        }

        return (IEntityLivingData)par1EntityLivingData1;
    }

}
