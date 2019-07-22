package regulararmy.entity;

import regulararmy.core.MonsterRegularArmyCore;
import regulararmy.entity.ai.EntityAIArrowAttack;
import regulararmy.entity.ai.EntityAIEscapeFromDrown;
import regulararmy.entity.ai.EntityAIForwardBase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySniperSkeleton extends EntitySkeletonR {
	public EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 100, 120, 40.0F);

	public EntitySniperSkeleton(World par1World) {
		super(par1World);
		this.doRideHorses=false;
	}
	
	@Override
	public EntityAIArrowAttack getAIArrow(){
		return this.aiArrowAttack;
	}
	
	@Override
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
        	entityarrow.rotationPitch=(float) (((d3+0.07*-MathHelper.cos((float) (d3*4))+0.07)*180.0f/(float)Math.PI)+(d2*d2+d1*d1)*0.017f)+this.rand.nextFloat()*10-5;
        }else{
        	entityarrow.rotationPitch=(float) (((d3+0.03*-MathHelper.cos((float) (d3*4))+0.03)*180.0f/(float)Math.PI)+(d2*d2+d1*d1)*0.017f)+this.rand.nextFloat()*10-5;
        }
        entityarrow.motionY = (double)1.6*MathHelper.sin((float) (entityarrow.rotationPitch/180.0f*Math.PI));
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
        entityarrow.setDamage((double)(MonsterRegularArmyCore.isBowgun?20.0f:par2 * 5.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.difficultySetting.getDifficultyId() * 0.11F));

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

	public static float getCrowdCostPerBlock(){
		return 2;
	}
	
    public static float getFightRange(){
    	return 30;
    }

}
