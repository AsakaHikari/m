package mod.entity;

import java.util.ArrayList;
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
	public List<Entity> exculdeEntities;
	public double squareDistanceToTarget=1000;
	public boolean didMoveFast;
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
			if(!this.didMoveFast){
				this.exculdeEntities=new ArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this,this.getEntityBoundingBox().expand(3, 3, 3)));
			}
			this.didMoveFast=true;
			if(targetEntity==null){
				List<Entity> entityList=this.worldObj.getEntitiesWithinAABBExcludingEntity(this,this.getEntityBoundingBox().expand(3, 3, 3));
				if(entityList.size()>0){
					for(Entity e:entityList){
						if(!this.exculdeEntities.contains(e)){
							this.targetEntity=e;
						}
					}
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
