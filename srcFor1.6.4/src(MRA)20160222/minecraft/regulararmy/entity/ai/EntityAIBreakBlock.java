package regulararmy.entity.ai;

import java.util.ArrayList;
import java.util.List;

import regulararmy.core.Coord;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.pathfinding.AStarPathEntity;
import regulararmy.pathfinding.AStarPathFinder;
import regulararmy.pathfinding.AStarPathPoint;
import regulararmy.pathfinding.IPathFindRequester;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public class EntityAIBreakBlock extends EntityRegularAIBase implements IPathFindRequester{
	public int thePoint;
	public EngineerRequest target;
	public List<EngineerRequest> myTarget=new ArrayList();
	public int currentMyTarget;
	public EntityRegularArmy entity;
	public World worldObj;
	public AStarPathEntity entityPathEntity;
	boolean hasDone=true;

	public EntityAIBreakBlock(EntityRegularArmy b){
		this.entity=b;
		if(!(b instanceof IBreakBlocksMob)){
			throw new IllegalArgumentException("EntityAIBreakBlock requires Mob implements IBreakBlocksMob");
		}
		this.worldObj=b.worldObj;
		this.setMutexBits(1);
	}
	@Override
	public int getTacticsCost(Entity entity, AStarPathPoint start,
			AStarPathPoint current, AStarPathPoint end) {
		return 0;
	}

	@Override
	public boolean shouldExecute() {
		if(Math.abs(this.entity.motionY)>0.3)return false;
		this.target=this.entity.unit.getRequestManager().getNearest(this.entity);
		if(this.target==null){
			this.entityPathEntity=this.entity.getANavigator().getPathToXYZ
					(this.entity.unit.leader.x,this.entity.unit.leader.y,this.entity.unit.leader.z,1.2f,this);
			return this.entityPathEntity!=null;
		}else{
			this.worldObj.theProfiler.startSection("pathfind");
			int l = MathHelper.floor_double(this.entity.posX);
			int i1 = MathHelper.floor_double(this.entity.posY);
			int j1 = MathHelper.floor_double(this.entity.posZ);
			int k1 = (int)(64 + 16.0F);
			int l1 = l - k1;
			int i2 = i1 - k1;
			int j2 = j1 - k1;
			int k2 = l + k1;
			int l2 = i1 + k1;
			int i3 = j1 + k1;
			ChunkCache chunkcache = new ChunkCache(this.worldObj, l1, i2, j2, k2, l2, i3, 0);
			AStarPathFinder finder=new AStarPathFinder(chunkcache, true,false,false,true,
        		this.isEngineer(),1.4f,this);
			finder.setSetting(this.entity.getSettings());
			finder.unusablePoints.add(this.target.waitingPoint);
			this.entityPathEntity= finder.createEntityPathTo(this.entity,target.coord.x, MathHelper.floor_double(target.coord.y-this.entity.getEyeHeight()), target.coord.z, this.entity.getANavigator().getPathSearchRange(),2.0f);
			this.worldObj.theProfiler.endSection();
        
			/*
			this.entityPathEntity=this.entity.getANavigator().getPathToXYZ
				(target.coord.x+0.5, target.coord.y-this.entity.getEyeHeight()+0.5, target.coord.z+0.5,1.0f,this);
					*/
			//System.out.println("should:"+(this.entityPathEntity!=null));
			return this.entity.unit.getRequestManager().isThereNotApproved()&&this.entityPathEntity!=null;
		}
	}
	
	@Override
	public boolean continueExecuting(){
		//System.out.println("continue:"+!hasDone);
		return !hasDone&&this.entityPathEntity!=null&&!this.entity.getANavigator().noPath();
	}
	
	@Override
	public void startExecuting(){
		if(this.target!=null)this.target.approve();
		//System.out.println(this.target.coord.x+","+this.target.coord.y+","+this.target.coord.z);
		this.entity.getANavigator().setPath(this.entityPathEntity, 1.6);
		this.hasDone=false;
	}
	
	@Override
	public void resetTask(){
		this.myTarget.clear();
		this.entityPathEntity=null;
	}
	
	@Override
	public void updateTask(){
		if(this.target!=null&&!this.target.isEnable){
			this.hasDone=true;
			return;
		}
		
		//System.out.println(this.entity.getDistanceSq(target.coord.x+0.5, target.coord.y-this.entity.getEyeHeight()+0.5, target.coord.z+0.5));
		if(this.target!=null&&this.entity.getDistanceSq(target.coord.x+0.5, target.coord.y-this.entity.getEyeHeight()+0.5, target.coord.z+0.5)<12){
			if(!this.target.isSet){
				this.thePoint++;
				if(this.worldObj.getBlockId(target.coord.x, target.coord.y, target.coord.z)==0
						||this.thePoint/30>((IBreakBlocksMob)entity).getblockStrength
					(Block.blocksList[this.worldObj.getBlockId(target.coord.x, target.coord.y, target.coord.z)], 
							this.worldObj, target.coord.x, target.coord.y, target.coord.z)){
					this.worldObj.destroyBlock(target.coord.x, target.coord.y, target.coord.z, true);
					this.hasDone=true;
					this.thePoint=0;
					this.target.fulfill();
				}
			}else {
				this.worldObj.setBlock(target.coord.x, target.coord.y, target.coord.z,Block.cobblestone.blockID);
				this.hasDone=true;
				this.target.fulfill();
			}
		}
		for(int i=0;i<this.myTarget.size();i++){
			EngineerRequest t=this.myTarget.get(i);
			if(t!=null)
				if(this.entity.getDistanceSq
						(t.coord.x+0.5, t.coord.y-this.entity.getEyeHeight()+0.5, t.coord.z+0.5)<12){
				if(!t.isSet){
					if(this.worldObj.getBlockId(t.coord.x, t.coord.y, t.coord.z)==0){
						myTarget.remove(i);
					}else{
						//System.out.println("trying breaking id:"+this.worldObj.getBlockId(t.coord.x, t.coord.y, t.coord.z)+" on:"+t.coord.x+","+ t.coord.y+","+ t.coord.z);
						this.thePoint++;
						if(this.thePoint/10>((IBreakBlocksMob)entity).getblockStrength
							(Block.blocksList[this.worldObj.getBlockId(t.coord.x, t.coord.y, t.coord.z)], 
								this.worldObj, t.coord.x, t.coord.y, t.coord.z)){
							this.worldObj.destroyBlock(t.coord.x, t.coord.y, t.coord.z, true);
							this.thePoint=0;
							myTarget.remove(i);
						}
					}
				}else{
					this.worldObj.setBlock(t.coord.x, t.coord.y, t.coord.z,Block.cobblestone.blockID);
					myTarget.remove(i);
				}
			}
		}
	}
	
	public void addMyTarget(EngineerRequest e){
		System.out.println("planned to "+(e.isSet?"set ":"break ")+e.coord.x+","+e.coord.y+","+e.coord.z);
		this.myTarget.add(e);
		
	}
	
	@Override
	public boolean isEngineer(){
		return true;
	}
}
