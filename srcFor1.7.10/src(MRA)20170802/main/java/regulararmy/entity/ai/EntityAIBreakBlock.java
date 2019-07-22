package regulararmy.entity.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import regulararmy.core.Coord;
import regulararmy.core.MonsterRegularArmyCore;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.entity.ai.EngineerRequest.RequestType;
import regulararmy.pathfinding.AStarPathEntity;
import regulararmy.pathfinding.AStarPathFinder;
import regulararmy.pathfinding.AStarPathPoint;
import regulararmy.pathfinding.IPathFindRequester;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public class EntityAIBreakBlock extends EntityRegularAIBase implements IPathFindRequester{
	public int thePoint;
	public EngineerRequest target;
	public int currentMyTarget;
	public EntityRegularArmy entity;
	public World worldObj;
	public AStarPathEntity entityPathEntity;
	public boolean isSlow=false;
	public boolean hasDone=true;
	
	//public List<EngineerRequest> setRequests;
	//public List<EngineerRequest> breakRequests;

	public EntityAIBreakBlock(EntityRegularArmy b){
		this.entity=b;
		if(!(b instanceof IBreakBlocksMob)){
			throw new IllegalArgumentException("EntityAIBreakBlock requires Mob implements IBreakBlocksMob");
		}
		this.worldObj=b.worldObj;
		this.setMutexBits(3);
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
			if(this.entity.getAttackTarget()==null){
				this.entityPathEntity=this.entity.getANavigator().getPathToXYZ
						(this.entity.unit.leader.x,this.entity.unit.leader.y,this.entity.unit.leader.z,1.2f,this);
			}else{
				EntityLivingBase e=this.entity.getAttackTarget();
				this.entityPathEntity=this.entity.getANavigator().getPathToXYZ
						(e.posX,e.posY,e.posZ,1.2f,this);
			}

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
					!this.isSlow,1.4f,this);
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
		
		this.entity.getANavigator().setPath(this.entityPathEntity, 1.2);
		this.hasDone=false;
	}

	@Override
	public void resetTask(){
		this.entityPathEntity=null;
	}

	@Override
	public void updateTask(){
		
		if(this.target!=null&&!this.target.isEnable){
			this.hasDone=true;
			return;
		}
		/*
		//System.out.println(this.entity.getDistanceSq(target.coord.x+0.5, target.coord.y-this.entity.getEyeHeight()+0.5, target.coord.z+0.5));
		if(this.target!=null&&this.entity.getDistanceSq(target.coord.x+0.5, target.coord.y-this.entity.getEyeHeight()+0.5, target.coord.z+0.5)<12){
			if(!this.target.isSet){
				if(this.isSlow){
					this.thePoint++;
				}else{
					this.thePoint+=3;
				}
				if(this.worldObj.getBlock(target.coord.x, target.coord.y, target.coord.z)==Blocks.air
						||this.thePoint/30>((IBreakBlocksMob)entity).getblockStrength
					(this.worldObj.getBlock(target.coord.x, target.coord.y, target.coord.z), 
							this.worldObj, target.coord.x, target.coord.y, target.coord.z)){
					boolean flag=true;
					for(int i=0;i<MonsterRegularArmyCore.blocksDoNotDrop.length;i++){
						Block b=MonsterRegularArmyCore.blocksDoNotDrop[i];
						if(b==this.worldObj.getBlock(target.coord.x, target.coord.y, target.coord.z)&&(MonsterRegularArmyCore.blocksDoNotDropMeta[i]==-1||MonsterRegularArmyCore.blocksDoNotDropMeta[i]==this.worldObj.getBlockMetadata(target.coord.x, target.coord.y, target.coord.z))){
							flag=false;
							break;
						}
					}
					this.worldObj.func_147480_a(target.coord.x, target.coord.y, target.coord.z, flag);
					this.hasDone=true;
					this.thePoint=0;
					this.target.fulfill();
				}
			}else {
				this.worldObj.setBlock(target.coord.x, target.coord.y, target.coord.z,MonsterRegularArmyCore.blockMonster);
				this.hasDone=true;
				this.target.fulfill();
			}
		}
		 */
		List<EngineerRequest> targets=this.entityPathEntity.getCurrentPoint().requests;
		List<EngineerRequest> targetsTemp=this.entityPathEntity.getCurrentPoint().requestsTemp;
		
		
		if(this.isSlow){
			this.thePoint++;
		}else{
			this.thePoint+=3;
		}
		//System.out.println("point:"+this.thePoint);
		boolean shouldPutBlock=false;
		for(int i=0;i<targets.size();i++){
			EngineerRequest t=targets.get(i);
			if(t.isSet!=RequestType.BREAK){
				shouldPutBlock=true;
				break;
			}
		}
		if(targetsTemp!=null){
			try{
				Collections.sort(targetsTemp, new RequestComparator(this.entity));
			}catch(Exception e){
			}
			for(int i=0;i<targetsTemp.size();i++){
				EngineerRequest t=targetsTemp.get(i);
				if(t.getSquareDistance(this.entity.posX,this.entity.posY,this.entity.posZ)<10){

					if(t.isSet==RequestType.PUT_BLOCK){
						if(this.worldObj.getBlock(t.coord.x, t.coord.y, t.coord.z).isOpaqueCube()){
							targetsTemp.remove(i);
						}else if(this.thePoint>50){
							this.thePoint=0;
							this.worldObj.setBlock(t.coord.x, t.coord.y, t.coord.z,MonsterRegularArmyCore.blockMonster);
							targetsTemp.remove(i);
						}
					}else if(t.isSet==RequestType.PUT_LADDER){
						if(this.thePoint>50){
							this.thePoint=0;
							int meta=2;
							switch(t.dir){
							case EAST:
								meta=4;
								break;
							case NORTH:
								meta=3;
								break;
							case SOUTH:
								meta=2;
								break;
							case WEST:
								meta=5;
								break;
							default:
								break;

							}
							this.worldObj.setBlock(t.coord.x, t.coord.y, t.coord.z,Blocks.ladder,
									Blocks.ladder.onBlockPlaced(this.worldObj, t.coord.x, t.coord.y, t.coord.z, meta, 0,0,0, 0), 2);

							targetsTemp.remove(i);
						}
					}else if(!shouldPutBlock){

						if(this.worldObj.getBlock(t.coord.x, t.coord.y, t.coord.z)==Blocks.air){

							targetsTemp.remove(i);
						}else{
							//System.out.println("trying breaking id:"+this.worldObj.getBlockId(t.coord.x, t.coord.y, t.coord.z)+" on:"+t.coord.x+","+ t.coord.y+","+ t.coord.z);

							if(this.thePoint>60 &&this.thePoint/10>((IBreakBlocksMob)entity).getblockStrength
									(this.worldObj.getBlock(t.coord.x, t.coord.y, t.coord.z), 
											this.worldObj, t.coord.x, t.coord.y, t.coord.z)){
								boolean flag=true;
								for(Block b:MonsterRegularArmyCore.blocksDoNotDrop){
									if(b==this.worldObj.getBlock(t.coord.x, t.coord.y, t.coord.z)){
										flag=false;
										break;
									}
								}
								this.worldObj.func_147480_a(t.coord.x, t.coord.y, t.coord.z, flag);
								this.thePoint=0;
								targetsTemp.remove(i);

							}
						}
					}
				}

			}
		}
		if(targets!=null){
			Collections.sort(targets, new RequestComparator(this.entity));
			for(int i=0;i<targets.size();i++){
				EngineerRequest t=targets.get(i);
				if(t.isSet==RequestType.PUT_BLOCK){
					if(this.worldObj.getBlock(t.coord.x, t.coord.y, t.coord.z).isOpaqueCube()){
						targets.remove(i);
					}else if(this.thePoint>50){
						this.thePoint=0;
						this.worldObj.setBlock(t.coord.x, t.coord.y, t.coord.z,MonsterRegularArmyCore.blockMonster);
						targets.remove(i);
					}
				}else if(t.isSet==RequestType.PUT_LADDER){
					if(this.thePoint>50){
						this.thePoint=0;
						int meta=2;
						switch(t.dir){
						case EAST:
							meta=4;
							break;
						case NORTH:
							meta=3;
							break;
						case SOUTH:
							meta=2;
							break;
						case WEST:
							meta=5;
							break;
						default:
							break;

						}
						this.worldObj.setBlock(t.coord.x, t.coord.y, t.coord.z,Blocks.ladder,
								Blocks.ladder.onBlockPlaced(this.worldObj, t.coord.x, t.coord.y, t.coord.z, meta, 0,0,0, 0), 2);

						targets.remove(i);
					}
				}else if(!shouldPutBlock){
					if(this.entity.getDistanceSq
							(t.coord.x+0.5, t.coord.y-this.entity.getEyeHeight()+0.5, t.coord.z+0.5)<16){
						if(this.worldObj.getBlock(t.coord.x, t.coord.y, t.coord.z)==Blocks.air){

							targets.remove(i);
						}else{
							//System.out.println("trying breaking id:"+this.worldObj.getBlockId(t.coord.x, t.coord.y, t.coord.z)+" on:"+t.coord.x+","+ t.coord.y+","+ t.coord.z);

							if(this.thePoint>60 &&this.thePoint/10>((IBreakBlocksMob)entity).getblockStrength
									(this.worldObj.getBlock(t.coord.x, t.coord.y, t.coord.z), 
											this.worldObj, t.coord.x, t.coord.y, t.coord.z)){
								boolean flag=true;
								for(Block b:MonsterRegularArmyCore.blocksDoNotDrop){
									if(b==this.worldObj.getBlock(t.coord.x, t.coord.y, t.coord.z)){
										flag=false;
										break;
									}
								}
								this.worldObj.func_147480_a(t.coord.x, t.coord.y, t.coord.z, flag);
								this.thePoint=0;
								targets.remove(i);
							}
						}
					}

				}
			}
		}
		
	}

	@Override
	public boolean isEngineer(){
		return true;
	}

	@Override
	public float getJumpHeight() {
		return this.entity.data.jumpHeight;
	}

	@Override
	public float getCrowdCost() {
		return this.entity.data.crowdCostPerBlock;
	}

	@Override
	public float getFightRange() {
		return this.entity.data.fightRange;
	}
	public static class RequestComparator implements Comparator<EngineerRequest> {
		public EntityRegularArmy entity;
		public RequestComparator(EntityRegularArmy entity){
			this.entity=entity;
		}
		@Override
		public int compare(EngineerRequest o1, EngineerRequest o2) {
			if(o1.getSquareDistance(entity.posX, entity.posY, entity.posZ)>o2.getSquareDistance(entity.posX, entity.posY, entity.posZ)){
				return 1;
			}else return -1;
		}
	}
}
