package mod.entity;

import mod.core.ShellCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBigHE extends EntityHE{
	public EntityBigHE(World par1World) {
		super(par1World);
        
        this.setSize(1.98F, 1.98F);
        this.setPosition(this.posX, this.posY+=this.height/2, this.posZ);
        this.yOffset=this.height/2.0f;
        this.stepHeight=0.6f;
        reflection=0.7f;
        destruction=4;
        damage=4.0f;
        this.explosive=5.6f;
	}
	public EntityBigHE(World par1World, double par2, double par4, double par6)
    {
        super(par1World);
        this.setSize(1.98F, 1.98F);
        this.setLocationAndAngles(par2, par4, par6, this.rotationYaw, this.rotationPitch);
        this.setPosition(par2, par4, par6);
        this.yOffset=this.height/2.0f;
        this.stepHeight=0.6f;
        reflection=0.7f;
        destruction=3;
        damage=4.0f;
        this.explosive=5.6f;
    }
	
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
        	if(!worldObj.isRemote){
            this.setBeenAttacked();

            if (this.fuse<0&&par1DamageSource.getDamageType().equals("player")&&MathHelper.sqrt_double(Math.pow(motionX, 2.0)+Math.pow(motionY, 2.0)+Math.pow(motionZ, 2.0))<0.5)
            {
            	if(!this.worldObj.isRemote&&!((EntityPlayer)par1DamageSource.getSourceOfDamage()).capabilities.isCreativeMode)this.entityDropItem(new ItemStack(ShellCore.itemShell,1,5), 0.0f);
                this.setDead();
                return true;
            }
            else
            {
                return false;
            }
        	}
        }
        return false;
    }
	
	
}
