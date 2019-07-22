package mod.entity;

import mod.core.ShellCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class EntityHE extends EntityShell{
	public byte fuse;
	public float explosive;
	public EntityHE(World par1World) {
		super(par1World);
        fuse=-1;
	}
	public EntityHE(World par1World, double par2, double par4, double par6)
    {
        super(par1World);
        fuse=-1;
    }
	
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		if(this.fuse>0){
			this.fuse--;
		}else if(this.fuse==0){
			this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, this.explosive, true);
			this.setDead();
		}
		 if(this.fuse>0){
         	this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
         }
         
	}
	
	@Override
	public void onCollidedWithBlock(int x,int y,int z,ForgeDirection fd){
		
		if(((fd.equals(ForgeDirection.WEST)||(fd.equals(ForgeDirection.EAST)))&&Math.abs(this.motionX)>1.0)
				||((fd.equals(ForgeDirection.UP)||(fd.equals(ForgeDirection.DOWN)))&&Math.abs(this.motionY)>1.0)
				||((fd.equals(ForgeDirection.SOUTH)||(fd.equals(ForgeDirection.NORTH)))&&Math.abs(this.motionZ)>1.0)){
			if(this.fuse==-1){
				this.fuse=80;
			}
		}
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
}
