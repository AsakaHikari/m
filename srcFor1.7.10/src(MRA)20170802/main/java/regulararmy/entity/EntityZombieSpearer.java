package regulararmy.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.UUID;

import regulararmy.entity.ai.EntityAIAttackOnCollide;
import regulararmy.entity.ai.EntityAIAttackWithSpear;
import regulararmy.entity.ai.EntityAIBreakBlock;
import regulararmy.entity.ai.EntityAIForwardBase;
import regulararmy.entity.ai.EntityAILearnedTarget;
import regulararmy.entity.ai.EntityAIShareTarget;
import regulararmy.entity.ai.EntityMoveHelperEx;
import regulararmy.entity.ai.IBreakBlocksMob;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;

public class EntityZombieSpearer extends EntityZombieR
{
	
	public float rotationLimitPerTick=20;
	
	@Override
	public void onUpdate(){
		super.onUpdate();
	}
	
	public EntityZombieSpearer(World par1World)
	{
		super(par1World);
		this.setAttackAI();
		this.tasks.addTask(4, new EntityAIForwardBase(this,1.2f));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		//this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		this.targetTasks.addTask(3, new EntityAILearnedTarget(this,0,true));
		this.targetTasks.addTask(4, new EntityAIShareTarget(this));
		this.dataWatcher.addObject(16,0);//Type of spear
		this.setTurnLimitPerTick(20f);
	}
	
	public void setAttackAI(){
		this.tasks.addTask(5, new EntityAIAttackWithSpear
				(this,  0.8f, 0.5f, 1.2f, Vec3.createVectorHelper(-width/2, 0.3f, 1.5f), 1.5D, true));
	}

    public int getSpearType(){
    	return this.dataWatcher.getWatchableObjectInt(16);
    }
    
    public void setSpearType(int i){
    	this.dataWatcher.updateObject(16, i);
    	this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5+i*3);
    }

    @Override
    public void setEquipmentsFromTier(int tier){
    	int i = this.worldObj.rand.nextInt(2);
        float f = this.worldObj.difficultySetting == EnumDifficulty.HARD ? 0.1F : 0.25F;

        if (this.worldObj.rand.nextInt(64) < tier)
        {
            ++i;
        }

        if (this.worldObj.rand.nextInt(64) < tier)
        {
            ++i;
        }

        if (this.worldObj.rand.nextInt(64) < tier)
        {
        	++i;
        }

        {
        	ItemStack itemstack = this.getEquipmentInSlot(0);
        	if (itemstack == null)
        	{
        		switch(i){
        		case 0:
        			this.setSpearType(0);
        			break;
        		case 1:
        			this.setSpearType(1);
        			break;
        		case 2:
        			this.setSpearType(2);
        			break;
        		case 3:
        			this.setSpearType(3);
        			break;
        			default:
        				this.setSpearType(3);
            			break;
        		}
        	}
        }
        for (int j = 4; j > 0; --j)
        {
        	ItemStack itemstack = this.getEquipmentInSlot(j);
        	if (itemstack == null)
        	{
        		ItemStack weapon=this.getRandomEquip(j,(byte) i);
        		if(weapon!=null){
        			this.setCurrentItemOrArmor(j , weapon);
        		}
        	}
        }

    }
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("spear", this.getSpearType());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
       this.setSpearType(nbt.getInteger("spear"));
    }
  
    
}
