package mod.entity;

import mod.core.ItemShell;
import mod.core.ShellCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityHE extends EntityShell{
	public byte fuse;
	public float explosive;
	public byte maxfuse=80;
	public EntityHE(World par1World) {
		super(par1World);
        fuse=-1;
        this.dataWatcher.addObject(6, this.fuse);
	}
	public EntityHE(World par1World, double par2, double par4, double par6)
    {
        this(par1World);
    }
	
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		if(this.fuse>0){
			this.fuse--;
			this.dataWatcher.updateObject(6, this.fuse);
		}else if(this.fuse==0){
			this.explode((float) (this.explosive*ShellCore.explosionRate));
		}
		 if(this.fuse>0){
         	this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
         }
	}
	
	@Override
	public void onCollidedWithBlock(BlockPos pos,double hardness,EnumFacing fd,AxisAlignedBB tAABB){
		NBTTagCompound nbt=this.getEntityData();
		if(((fd.equals(EnumFacing.WEST)||(fd.equals(EnumFacing.EAST)))&&Math.abs(this.motionX)>1.0)
				||((fd.equals(EnumFacing.UP)||(fd.equals(EnumFacing.DOWN)))&&Math.abs(this.motionY)>1.0)
				||((fd.equals(EnumFacing.SOUTH)||(fd.equals(EnumFacing.NORTH)))&&Math.abs(this.motionZ)>1.0)){
			if(this.fuse==-1){
				this.fuse=this.maxfuse;
				this.writeEntityToNBT(this.getEntityData());
			}
		}
	}
	

	public void onCollidedWithEntity(Entity entity){
		super.onCollidedWithEntity(entity);
		if(this.fuse==-1){
			this.fuse=this.maxfuse;
			this.writeEntityToNBT(this.getEntityData());
		}
	}
	
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isEntityInvulnerable( par1DamageSource))
        {
            return false;
        }
        else
        {
        	if(!worldObj.isRemote){
            this.setBeenAttacked();

            if (this.fuse<0 && par1DamageSource.getDamageType().equals("player")&&MathHelper.sqrt_double(Math.pow(motionX, 2.0)+Math.pow(motionY, 2.0)+Math.pow(motionZ, 2.0))<0.5)
            {
            	if(!this.worldObj.isRemote&&
            			!((EntityPlayer)par1DamageSource.getSourceOfDamage()).capabilities.isCreativeMode){
            		this.entityDropItem(new ItemStack(ShellCore.itemShell,1,ItemShell.getMetadataFromEntity(this)), 0.0f);
            	}
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
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt){
		super.writeEntityToNBT(nbt);
		nbt.setByte("fuse", this.fuse);
		nbt.setFloat("explosive",this.explosive);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt){
		super.readEntityFromNBT(nbt);
		this.fuse=nbt.getByte("fuse");
		this.explosive=nbt.getFloat("explosive");
	}
	

	public void explode(float size){
		EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(this.worldObj,this.posX,this.posY,this.posZ,this.setter);
		this.worldObj.spawnEntityInWorld(entitytntprimed);
		this.worldObj.createExplosion(entitytntprimed, this.posX, this.posY, this.posZ, size, true);
		this.setDead();
		entitytntprimed.setDead();
	}
}
