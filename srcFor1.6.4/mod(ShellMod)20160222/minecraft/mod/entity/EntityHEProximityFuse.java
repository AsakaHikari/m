package mod.entity;

import java.util.List;

import mod.core.ShellCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityHEProximityFuse extends EntityHE{
	public Entity targetEntity;
	public double squareDistanceToTarget=1000;
	public EntityHEProximityFuse(World par1World) {
		super(par1World);
	}
	public EntityHEProximityFuse(World par1World, double par2, double par4, double par6)
	{
		this(par1World);
	}


	@Override
	public void onSplitUpdate(int times,int maxSplit){
		super.onSplitUpdate(times,maxSplit);
		if((this.motionX*this.motionX+this.motionY*this.motionY+this.motionZ*this.motionZ)>1 ){
			if(targetEntity==null){
				List<Entity> entityList=this.worldObj.getEntitiesWithinAABBExcludingEntity(this,this.boundingBox.expand(3, 3, 3));
				if(entityList.size()>0){
					Entity e=this.targetEntity=entityList.get(0);
				}
			}
			if(this.targetEntity!=null){
				double x=this.posX-this.targetEntity.posX;
				double y=this.posY-this.targetEntity.posY;
				double z=this.posZ-this.targetEntity.posZ;
				double dist=x*x+y*y+z*z;
				if(this.squareDistanceToTarget<dist){
					this.explode((float) (this.explosive*ShellCore.explosionRate));
				}else{
					this.squareDistanceToTarget=dist;
				}
			}
		}
	}


}
