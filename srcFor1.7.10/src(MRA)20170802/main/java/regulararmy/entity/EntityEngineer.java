package regulararmy.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Calendar;
import java.util.UUID;

import regulararmy.entity.ai.EntityAIBreakBlock;
import regulararmy.entity.ai.EntityAIEscapeFromDrown;
import regulararmy.entity.ai.EntityAIForwardBase;
import regulararmy.entity.ai.EntityAIShareTarget;
import regulararmy.entity.ai.EntityRegularAIBase;
import regulararmy.entity.ai.IBreakBlocksMob;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
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

public class EntityEngineer extends EntityZombieR implements IBreakBlocksMob
{
   public EntityAIBreakBlock breakBlockAI=new EntityAIBreakBlock(this);
   public EntityAIShareTarget shareTargetAI=new EntityAIShareTarget(this);
    /**
     * Ticker used to determine the time remaining for this zombie to convert into a villager when cured.
     */

    public EntityEngineer(World par1World)
    {
        super(par1World);
        this.tasks.addTask(2, breakBlockAI);
        this.targetTasks.addTask(4, this.shareTargetAI);
        this.doRideHorses=false;
    }
    
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23);
        //this.getAttributeMap().func_111150_b(field_110186_bp).setAttribute(this.rand.nextDouble() * ForgeDummyContainer.zombieSummonBaseChance);
    }

    /**
     * Makes entity wear random armor based on difficulty
     */
    protected void addRandomArmor()
    {
    	this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_pickaxe));
    }


	@Override
	public float getblockStrength(Block block, 
			World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
        float hardness = block.getBlockHardness(world, x, y, z);
        if (hardness < 0.0F)
        {
            return Float.MAX_VALUE;
        }
        ItemStack stack = this.getHeldItem();
        float f = (stack == null ? 1.0F : stack.getItem().getDigSpeed(stack, block, metadata));

        if (f > 1.0F)
        {
            int i = EnchantmentHelper.getEfficiencyModifier(this);
            ItemStack itemstack = this.getHeldItem();

            if (i > 0 && itemstack != null)
            {
                f+= (float)(i * i + 1);
            }
        }

        if (this.isPotionActive(Potion.digSpeed))
        {
            f *= 1.0F + (float)(this.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
        }

        if (this.isPotionActive(Potion.digSlowdown))
        {
            f *= 1.0F - (float)(this.getActivePotionEffect(Potion.digSlowdown).getAmplifier() + 1) * 0.2F;
        }

        if (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this))
        {
            f /= 3.0F;
        }

        if (!this.onGround)
        {
            f /= 3.0F;
        }
        return (f < 0 ? 0 : f);
	}
	
}
