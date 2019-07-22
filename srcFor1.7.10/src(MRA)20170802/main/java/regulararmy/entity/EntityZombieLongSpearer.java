package regulararmy.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Calendar;
import java.util.UUID;

import regulararmy.entity.ai.EntityAIAttackOnCollide;
import regulararmy.entity.ai.EntityAIAttackWithSpear;
import regulararmy.entity.ai.EntityAIBreakBlock;
import regulararmy.entity.ai.EntityAIForwardBase;
import regulararmy.entity.ai.EntityAILearnedTarget;
import regulararmy.entity.ai.IBreakBlocksMob;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntityMob;
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

public class EntityZombieLongSpearer extends EntityZombieSpearer
{
	
    /**
     * Ticker used to determine the time remaining for this zombie to convert into a villager when cured.
     */

    public EntityZombieLongSpearer(World par1World)
    {
        super(par1World);
        this.setTurnLimitPerTick(10f);
    }
    
    @Override
    public void setAttackAI(){
		this.tasks.addTask(5, new EntityAIAttackWithSpear
				(this,  0.8f, 0.5f, 1.2f, Vec3.createVectorHelper(-width/2, 0.2f, 4f), 1.5D, true));
	}
}
