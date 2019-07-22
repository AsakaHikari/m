package regulararmy.pathfinding;

import java.util.ArrayList;
import java.util.List;

import regulararmy.core.Coord;
import regulararmy.entity.EntityEngineer;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.entity.ai.EngineerRequest;
import regulararmy.entity.ai.EngineerRequest.RequestType;
import regulararmy.entity.ai.EntityAIBreakBlock;
import regulararmy.entity.ai.EntityRegularAIBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;

public class AStarPathNavigate
{
	public EntityRegularArmy theEntity;
	public World worldObj;

	/** The AStarPathEntity being followed. */
	public AStarPathEntity currentPath;
	public double speed;

	/**
	 * The number of blocks (extra) +/- in each axis that get pulled out as cache for the pathfinder's search space
	 */
	public IAttributeInstance pathSearchRange;
	public boolean noSunPathfind;

	/** Time, in number of ticks, following the current path */
	public int totalTicks;

	/**
	 * The time when the last position check was done (to detect successful movement)
	 */
	public int ticksAtLastPos;

	public int ticksPlanned;

	public double lastDistance=0;
	public double takeBackDistanceAmount=0;

	/**
	 * Coordinates of the entity's position last time a check was done (part of monitoring getting 'stuck')
	 */
	public Vec3 lastPosCheck = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);

	/**
	 * Specifically, if a wooden door block is even considered to be passable by the pathfinder
	 */
	public boolean canPassOpenWoodenDoors = true;

	/** If door blocks are considered passable even when closed */
	public boolean canPassClosedWoodenDoors;

	/** If water blocks are avoided (at least by the pathfinder) */
	public boolean avoidsWater;

	/**
	 * If the entity can swim. Swimming AI enables this and the pathfinder will also cause the entity to swim straight
	 * upwards when underwater
	 */
	public boolean canSwim;

	/**
	 * 0:Can't use engineer
	 * 1:Can use engineer
	 * 2:Follow to unit's setting*/
	public byte canUseEngineer=2;

	public List<Coord> unuseablepoints=new ArrayList();

	public int maxCost=Integer.MAX_VALUE;

	/**Points which should be checked. They are not current path but the host is passing these points on.*/
	public List<AStarPathPoint> passingPoints=new ArrayList();


	public AStarPathNavigate(EntityRegularArmy par1EntityLiving, World par2World)
	{
		this.theEntity = par1EntityLiving;
		this.worldObj = par2World;
		this.pathSearchRange = par1EntityLiving.getEntityAttribute(SharedMonsterAttributes.followRange);
	}

	public void setAvoidsWater(boolean par1)
	{
		this.avoidsWater = par1;
	}

	public boolean getAvoidsWater()
	{
		return this.avoidsWater;
	}

	public void setBreakDoors(boolean par1)
	{
		this.canPassClosedWoodenDoors = par1;
	}

	/**
	 * Sets if the entity can enter open doors
	 */
	 public void setEnterDoors(boolean par1)
	 {
		 this.canPassOpenWoodenDoors = par1;
	 }

	 /**
	  * Returns true if the entity can break doors, false otherwise
	  */
	 public boolean getCanBreakDoors()
	 {
		 return this.canPassClosedWoodenDoors;
	 }

	 /**
	  * Sets if the path should avoid sunlight
	  */
	 public void setAvoidSun(boolean par1)
	 {
		 this.noSunPathfind = par1;
	 }

	 /**
	  * Sets the speed
	  */
	 public void setSpeed(double par1)
	 {
		 this.speed = par1;
	 }

	 /**
	  * Sets if the entity can swim
	  */
	 public void setCanSwim(boolean par1)
	 {
		 this.canSwim = par1;
	 }

	 /**
	  * Gets the maximum distance that the path finding will search in.
	  */
	 public float getPathSearchRange()
	 {
		 return (float)this.pathSearchRange.getAttributeValue();
	 }

	 /**
	  * Returns the path to the given coordinates
	  */
	 public AStarPathEntity getPathToXYZ(double par1, double par3, double par5,float par6,EntityRegularAIBase ai)
	 {
		 if(!this.canFindPath())return null;
		 this.worldObj.theProfiler.startSection("pathfind");
		 int l = MathHelper.floor_double(this.theEntity.posX);
		 int i1 = MathHelper.floor_double(this.theEntity.posY);
		 int j1 = MathHelper.floor_double(this.theEntity.posZ);
		 int k1 = (int)(64 + 16.0F);
		 int l1 = l - k1;
		 int i2 = i1 - k1;
		 int j2 = j1 - k1;
		 int k2 = l + k1;
		 int l2 = i1 + k1;
		 int i3 = j1 + k1;
		 ChunkCache chunkcache = new ChunkCache(this.worldObj, l1, i2, j2, k2, l2, i3, 0);
		 AStarPathFinder finder=new AStarPathFinder(chunkcache, this.canPassOpenWoodenDoors, this.canPassClosedWoodenDoors, this.avoidsWater, this.canSwim,
				 ((IPathFindRequester)ai).isEngineer(),((IPathFindRequester)ai).getJumpHeight(),(IPathFindRequester)ai);
		 finder.setSetting(this.theEntity.getSettings());
		 finder.canUseEngineer=this.canUseEngineer==2?this.theEntity.unit.canUseEngineer:this.canUseEngineer==1?true:false;
		 finder.unusablePoints=this.unuseablepoints;
		 finder.maxCost=this.maxCost;
		 AStarPathEntity pathentity;
		 if(finder.canUseEngineer){
			 pathentity = finder.createEntityPathTo(this.theEntity.getBottomEntity(),par1, par3, par5, 20,par6,1.8f,2.8f);
		 }else{
			 pathentity = finder.createEntityPathTo(this.theEntity.getBottomEntity(),MathHelper.floor_double(par1), (int)par3, MathHelper.floor_double(par5), 20,par6);
		 }
		 this.worldObj.theProfiler.endSection();
		 return pathentity;
	 }

	 /**
	  * Try to find and set a path to XYZ. Returns true if successful.
	  */
	 public boolean tryMoveToXYZ(double x, double y, double z, double speed,float jumpHeight,EntityRegularAIBase ai)
	 {
		 AStarPathEntity AStarPathEntity = this.getPathToXYZ((double)MathHelper.floor_double(x), (double)((int)y), (double)MathHelper.floor_double(z),jumpHeight,ai);
		 return this.setPath(AStarPathEntity, speed);
	 }

	 /**
	  * Returns the path to the given EntityLiving
	  */
	 public AStarPathEntity getPathToEntityLiving(Entity par1Entity,float par1,EntityRegularAIBase ai)
	 {
		 if(!this.canFindPath())return null;
		 this.worldObj.theProfiler.startSection("pathfind");

		 int i = MathHelper.floor_double(this.theEntity.posX);
		 int j = MathHelper.floor_double(this.theEntity.posY);
		 int k = MathHelper.floor_double(this.theEntity.posZ);

		 int l = (int)(64 + 16.0F);
		 int i1 = i - l;
		 int j1 = j - l;
		 int k1 = k - l;
		 int l1 = i + l;
		 int i2 = j + l;
		 int j2 = k + l;
		 ChunkCache chunkcache = new ChunkCache(this.worldObj, i1, j1, k1, l1, i2, j2, 0);
		 AStarPathFinder finder=new AStarPathFinder(chunkcache, this.canPassOpenWoodenDoors, this.canPassClosedWoodenDoors, this.avoidsWater, this.canSwim,
				 ((IPathFindRequester)ai).isEngineer(),((IPathFindRequester)ai).getJumpHeight(),(IPathFindRequester)ai);
		 finder.setSetting(this.theEntity.getSettings());
		 finder.canUseEngineer=this.canUseEngineer==2?this.theEntity.unit.canUseEngineer:this.canUseEngineer==1?true:false;
		 finder.unusablePoints=this.unuseablepoints;
		 finder.maxCost=this.maxCost;
		 AStarPathEntity pathentity = finder.createEntityPathTo(this.theEntity.getBottomEntity(), par1Entity, 20,par1);
		 this.worldObj.theProfiler.endSection();
		 return pathentity;
	 }

	 /**
	  * Try to find and set a path to EntityLiving. Returns true if successful.
	  */
	 public boolean tryMoveToEntityLiving(Entity par1Entity, double par2,float par3,EntityRegularAIBase ai)
	 {
		 AStarPathEntity AStarPathEntity = this.getPathToEntityLiving(par1Entity,par3,ai);
		 return AStarPathEntity != null ? this.setPath(AStarPathEntity, par2) : false;
	 }

	 /**
	  * sets the active path data if path is 100% unique compared to old path, checks to adjust path for sun avoiding
	  * ents and stores end coords
	  */
	 public boolean setPath(AStarPathEntity par1AStarPathEntity, double par2)
	 {
		 if (par1AStarPathEntity == null)
		 {
			 this.currentPath = null;
			 return false;
		 }
		 else
		 {
			 if (!par1AStarPathEntity.isSamePath(this.currentPath))
			 {
				 if(this.currentPath!=null)
					 this.currentPath.disablePath(this.theEntity.unit.leader);
				 this.currentPath = par1AStarPathEntity;
				 this.currentPath.enablePath(this.theEntity.unit.leader,this.theEntity);
			 }

			 if (this.noSunPathfind)
			 {
				 this.removeSunnyPath();
			 }

			 if (this.currentPath.getCurrentPathLength() == 0)
			 {
				 return false;
			 }else if(this.currentPath.getCurrentPathIndex()>=this.currentPath.getCurrentPathLength()){
				 return false;
			 }
			 else
			 {
				 this.speed = par2;
				 Vec3 vec3 = this.getEntityPosition(this.theEntity.getBottomEntity());
				 this.ticksAtLastPos = this.totalTicks;
				 this.lastPosCheck.xCoord = vec3.xCoord;
				 this.lastPosCheck.yCoord = vec3.yCoord;
				 this.lastPosCheck.zCoord = vec3.zCoord;
				 this.lastDistance=vec3.distanceTo(this.currentPath.getPosition(this.theEntity.getBottomEntity()));
				 this.takeBackDistanceAmount=0;
				 this.ticksPlanned=100;
				 return true;
			 }
		 }
	 }

	 /**
	  * gets the actively used AStarPathEntity
	  */
	 public AStarPathEntity getPath()
	 {
		 return this.currentPath;
	 }

	 public void onUpdateNavigation()
	 {
		 ++this.totalTicks;

		 if (!this.noPath())
		 {
			 if (this.canNavigate())
			 {
				 this.pathFollow();
			 }
			 if(!this.noPath()){
				 //System.out.println("Coord:"+this.getCurrentPathPoint().xCoord+","+this.getCurrentPathPoint().yCoord+","+this.getCurrentPathPoint().zCoord);
				 //System.out.println("wait:"+this.shouldWait()+"(num):"+this.getCurrentPathPoint().requests);
				 if (!this.shouldWait())
				 {
					 if(this.theEntity.isSneaking()){
						 this.theEntity.setSneaking(false);
					 }
					 Vec3 vec3=null;
					 if(this.getCurrentPathPoint().onLadder && this.getCurrentPathPoint().coordsLadder!=null && !this.getCurrentPathPoint().coordsLadder.isEmpty()){
						 Coord c=this.getCurrentPathPoint().coordsLadder.get(0);
						 Block ladder=this.worldObj.getBlock(c.x,c.y,c.z);
						 if(ladder instanceof BlockLadder){
							 ForgeDirection dir=ForgeDirection.UNKNOWN;
							 switch(this.worldObj.getBlockMetadata(c.x,c.y,c.z)){
							 case 2:
								 dir=ForgeDirection.SOUTH;
								 break;
							 case 3:
								 dir=ForgeDirection.NORTH;
								 break;
							 case 4:
								 dir=ForgeDirection.EAST;
								 break;
							 case 5:
								 dir=ForgeDirection.WEST;
								 break;
							 }
							 vec3=Vec3.createVectorHelper(dir.offsetX+0.5+c.x, dir.offsetY+c.y, dir.offsetZ+c.z+0.5);
						 }else{
							 this.clearAStarPathEntity();
						 }
					 }else{
						 vec3 = this.currentPath.getPosition(this.theEntity);
					 }
					 if (vec3 != null)
					 {
						 this.theEntity.getMoveHelper().setMoveTo(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.speed);
					 }
				 }else{
					 this.theEntity.setSneaking(true);
				 }
			 }
		 }
	 }

	 public void pathFollow()
	 {
		 Entity follower=this.theEntity.getBottomEntity();
		 Vec3 vec3 = this.getEntityPosition(follower);
		 //System.out.println(this.currentPath.getPosition(this.theEntity));
		 if(this.currentPath.getCurrentPathIndex()!=0){

			 /*
        	if(vec3.distanceTo(this.currentPath.getVectorFromIndex(theEntity, this.currentPath.getCurrentPathIndex()))+
        		vec3.distanceTo(this.currentPath.getVectorFromIndex(theEntity, this.currentPath.getCurrentPathIndex()-1))>8.0f){
        	this.clearAStarPathEntity();
        	System.out.println("course out");
        	return;
        	}
			  */
		 }

		 /**i is the number of the last point before its height changes from the entity's height*/
		 int i = this.currentPath.getCurrentPathLength();
		 for (int j = this.currentPath.getCurrentPathIndex(); j < this.currentPath.getCurrentPathLength(); ++j)
		 {
			 AStarPathPoint point=this.currentPath.getPathPointFromIndex(j);
			 //System.out.println(j+":"+point.yCoord+","+point.yOffset+","+(int)vec3.yCoord);
			 if ((point.yCoord+MathHelper.floor_float(point.yOffset)) != (int)vec3.yCoord)
			 {
				 i = j;
				 break;
			 }
		 }


		 int k;

		 boolean hasCurrentPointChanged=false;
		 /**Move point forward if the point is near enough and on the same height*/
		 if(this.currentPath.getCurrentPoint().requests.size()==0){
			 for (k = this.currentPath.getCurrentPathIndex(); k < i; ++k)
			 {

				 float maxDistance=this.currentPath.entitySize<follower.width+0.4f ? 0.2f : (this.currentPath.entitySize-follower.width)*(this.currentPath.entitySize-follower.width);
				 //System.out.println("distance:"+vec3.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, k))+"at "+this.currentPath.getVectorFromIndex(this.theEntity, k)+" maxdist:"+maxDistance);
				 if (vec3.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, k)) < maxDistance)
				 {
					 //System.out.println("nearby enough");

					 this.currentPath.setCurrentPathIndex(k+1);
					 this.ticksAtLastPos=this.totalTicks;
					 hasCurrentPointChanged=true;
					 break;
				 }
			 }
		 }


		 /**Move point forward if the point is straight from the point now on*/
		 /*
        k = MathHelper.ceiling_float_int(this.theEntity.width);
        int l = (int)this.theEntity.height + 1;
        int i1 = k;
        for (int j1 = i - 1; j1 >= this.currentPath.getCurrentPathIndex(); --j1)
        {
            if (this.currentPath.getPathPointFromIndex
            (this.currentPath.getCurrentPathIndex()).numberOfBlocksToBreak==0
            &&this.currentPath.getPathPointFromIndex(this.currentPath.getCurrentPathIndex()).numberOfBlocksToPut==0
            &&this.isDirectPathBetweenPoints(vec3, this.currentPath.getVectorFromIndex(this.theEntity, j1), k, l, i1))
            {
                this.currentPath.setCurrentPathIndex(j1);
                break;
            }
        }
		  */

		 if(this.currentPath.getCurrentPathLength()<=this.currentPath.getCurrentPathIndex()){
			 this.clearAStarPathEntity();
			 //System.out.println("path over1");
			 return;
		 }
		 if(hasCurrentPointChanged){
			 int var1=this.currentPath.getCurrentPathIndex();
			 this.currentPath.setCurrentPathIndex(this.getLastDirectPoint(currentPath, currentPath.getCurrentPathIndex(),follower));
			 //System.out.println("Update! "+var1+" -> "+this.currentPath.getCurrentPathIndex()+"/"+(this.currentPath.getCurrentPathLength()-1));
			 if(this.currentPath.getCurrentPathLength()<=this.currentPath.getCurrentPathIndex()){
				 this.clearAStarPathEntity();
				 //System.out.println("path over2");
				 return;
			 }
			 this.ticksPlanned=0;
			 this.passingPoints.clear();
			 for(int j=var1;j<=this.currentPath.getCurrentPathIndex();j++){
				 this.ticksPlanned+=this.currentPath.getPathPointFromIndex(j).tickToNext;
				 this.passingPoints.add(this.currentPath.getPathPointFromIndex(j));
			 }
			 this.lastDistance=vec3.distanceTo(this.currentPath.getPosition(follower));
			 this.takeBackDistanceAmount=0;
			 //System.out.println("NextTick:"+this.ticksPlanned);
		 }
		 double distance=vec3.distanceTo(this.currentPath.getPosition(follower));
		 this.takeBackDistanceAmount+=(distance<this.lastDistance)?0:(distance-this.lastDistance);
		 this.lastDistance=distance;

		 if(this.takeBackDistanceAmount>3){
			 this.clearAStarPathEntity();
			 //System.out.println("take back");
			 return;
		 }

		 if(this.totalTicks%20==10 && this.currentPath!=null){
			 for(int i1=0;i1<this.passingPoints.size();i1++)n:{
				 if(this.currentPath!=null){
					 AStarPathPoint p=this.passingPoints.get(i1);
					 if(this.getCurrentPathPoint().distanceTo(p)>distance){
						 this.passingPoints.remove(i1);
						 i1--;
						 continue;
					 }
					 List<EngineerRequest> list=(this.getRequestsTemp(p));
					 for(EngineerRequest point:list){
						 if(this.canUseEngineer==2?this.theEntity.unit.canUseEngineer:this.canUseEngineer==1?true:false){
							 if(this.getCurrentPathPoint().requestsTemp==null){
								 this.getCurrentPathPoint().requestsTemp=new ArrayList();
							 }
							 if(!this.getCurrentPathPoint().requestsTemp.contains(point)){
								 this.getCurrentPathPoint().requestsTemp.add(point);
								 //System.out.println("request:"+point.toString());
							 }

						 }else{
							 //System.out.println("footing disappeared");
							 this.clearAStarPathEntity();
							 break n;
						 }
					 }
				 }
			 }

		 }
		 if (this.totalTicks - this.ticksAtLastPos > this.ticksPlanned)
		 {
			 this.clearAStarPathEntity();
			 //System.out.println("time out");
			 this.ticksAtLastPos = this.totalTicks;
			 this.lastPosCheck.xCoord = vec3.xCoord;
			 this.lastPosCheck.yCoord = vec3.yCoord;
			 this.lastPosCheck.zCoord = vec3.zCoord;
		 }
		 //System.out.println(this.lastDistanceDifference);
	 }

	 /**
	  * If null path or reached the end
	  */
	 public boolean noPath()
	 {
		 return this.currentPath == null || this.currentPath.isFinished();
	 }

	 /**
	  * sets active AStarPathEntity to null
	  */
	 public void clearAStarPathEntity()
	 {
		 //System.out.println("path cleared");
		 if(this.currentPath!=null)
			 this.currentPath.disablePath(this.theEntity.unit.leader);
		 this.currentPath = null;
	 }

	 public Vec3 getEntityPosition(Entity e)
	 {
		 return Vec3.createVectorHelper(e.posX, e.posY, e.posZ);
	 }

	 /**
	  * Gets the safe pathing Y position for the entity depending on if it can path swim or not
	  */
	 public int getPathableYPos()
	 {
		 if (this.theEntity.isInWater() && this.canSwim)
		 {
			 int i = (int)this.theEntity.boundingBox.minY;
			 Block j = this.worldObj.getBlock(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ));
			 int k = 0;

			 do
			 {
				 if (j != Blocks.water && j != Blocks.flowing_water)
				 {
					 return i;
				 }

				 ++i;
				 j = this.worldObj.getBlock(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ));
				 ++k;
			 }
			 while (k <= 16);

			 return (int)this.theEntity.boundingBox.minY;
		 }
		 else
		 {
			 return (int)(this.theEntity.boundingBox.minY + 0.5D);
		 }
	 }

	 /**
	  * If on ground or swimming and can swim
	  */
	 public boolean canFindPath()
	 {
		 return this.theEntity.getBottomEntity().onGround || this.canSwim && this.isInFluid();
	 }

	 public boolean canNavigate(){
		 return this.theEntity.getBottomEntity().onGround || ((this.theEntity.getBottomEntity() instanceof EntityLivingBase) && ((EntityLivingBase)this.theEntity.getBottomEntity()).isOnLadder()) ||this.canSwim && this.isInFluid();
	 }

	 /**
	  * Returns true if the entity is in water or lava, false otherwise
	  */
	 public boolean isInFluid()
	 {
		 return this.theEntity.isInWater() || this.theEntity.handleLavaMovement();
	 }

	 public boolean shouldWait(){
		 if(this.theEntity instanceof EntityEngineer){
			 if(!this.getCurrentPathPoint().requests.isEmpty()){
				 double relativeX=this.theEntity.posX-(this.getCurrentPathPoint().xCoord+0.5);
				 double relativeY=this.theEntity.posY-this.getCurrentPathPoint().yCoord;
				 double relativeZ=this.theEntity.posZ-(this.getCurrentPathPoint().zCoord+0.5);
				 double sqdis=relativeX*relativeX+relativeY*relativeY+relativeZ*relativeZ;
				 if(sqdis<6){
					 return true;
				 }
			 }
				 
			 
		 }else{
			 List<Entity> entityList=this.worldObj.getEntitiesWithinAABBExcludingEntity(this.theEntity, this.theEntity.boundingBox.expand(1,1,1));
			 for(Entity e:entityList){
				 if(e instanceof EntityEngineer){
					 double relativeX=this.theEntity.posX-e.posX;
					 double relativeY=this.theEntity.posY-e.posY;
					 double relativeZ=this.theEntity.posZ-e.posZ;
					 double sqdis=relativeX*relativeX+relativeY*relativeY+relativeZ*relativeZ;
					 double relrot=(this.theEntity.rotationYawHead-Math.atan2(relativeZ, relativeX)*180/Math.PI+630)%360;
					 //System.out.println("rot:"+relrot);
					 if(!(relrot>70 && relrot<290)){
						 return true;
					 }
					 
				 }
			 }
		 }
		 {
			 double relativeX=this.theEntity.posX-this.theEntity.unit.leader.x-0.5;
			 double relativeY=this.theEntity.posY-this.theEntity.unit.leader.y-0.5;
			 double relativeZ=this.theEntity.posZ-this.theEntity.unit.leader.z-0.5;
			 double sqdis=relativeX*relativeX+relativeY*relativeY+relativeZ*relativeZ;
			 if(sqdis<4){
				 double relrot=(this.theEntity.rotationYawHead-Math.atan2(relativeZ, relativeX)*180/Math.PI+630)%360;
				 //System.out.println("rot:"+relrot);
				 if(relrot>70 && relrot<290){
					 return true;
				 }
			 }
		 }
		 return false;
	 }

	 public AStarPathPoint getCurrentPathPoint(){
		 return this.currentPath.getPathPointFromIndex(this.currentPath.getCurrentPathIndex());
	 }

	 /**
	  * Trims path data from the end to the first sun covered block
	  */
	 public void removeSunnyPath()
	 {
		 if (!this.worldObj.canBlockSeeTheSky
				 (MathHelper.floor_double(this.theEntity.posX), (int)(this.theEntity.boundingBox.minY + 0.5D), MathHelper.floor_double(this.theEntity.posZ)))
		 {
			 for (int i = 0; i < this.currentPath.getCurrentPathLength(); ++i)
			 {
				 AStarPathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);

				 if (this.worldObj.canBlockSeeTheSky(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord))
				 {
					 this.currentPath.setCurrentPathLength(i - 1);
					 return;
				 }
			 }
		 }
	 }

	 public int getLastDirectPoint(AStarPathEntity path,int indexStart,Entity entity){
		 if(indexStart>=path.getCurrentPathLength()-1)return indexStart;
		 AStarPathPoint last;
		 if(indexStart<0){
			 last=path.getPathPointFromIndex(indexStart);
		 }else{
			 last=path.getPathPointFromIndex(indexStart-1);
		 }
		 AStarPathPoint now=path.getPathPointFromIndex(indexStart);
		 if(!(last.numberOfBlocksToBreak==0&&last.numberOfBlocksToPut==0)){
			 return indexStart;
		 }
		 if(last.yCoord+last.yOffset+entity.stepHeight<now.yCoord+now.yOffset)return indexStart;
		 int xProgress=now.xCoord-last.xCoord;
		 int zProgress=now.zCoord-last.zCoord;
		 ForgeDirection lastDirection=ForgeDirection.UNKNOWN;
		 if(xProgress>0)lastDirection=ForgeDirection.EAST;
		 if(xProgress<0)lastDirection=ForgeDirection.WEST;
		 if(zProgress>0)lastDirection=ForgeDirection.SOUTH;
		 if(zProgress<0)lastDirection=ForgeDirection.NORTH;
		 if(lastDirection==ForgeDirection.UNKNOWN)return indexStart;
		 for(int index=indexStart;index<path.getCurrentPathLength();index++){
			 now=path.getPathPointFromIndex(index);
			 if(!(now.numberOfBlocksToBreak==0&&now.numberOfBlocksToPut==0)){
				 return (index==indexStart)?index:index-1;
			 }
			 if(last.yCoord+last.yOffset+entity.stepHeight<now.yCoord+now.yOffset){
				 return index;
			 }
			 int x=now.xCoord-last.xCoord;
			 int z=now.zCoord-last.zCoord;
			 if((xProgress>0)^(x>0)||(zProgress>0)^(z>0)){
				 return index-1;
			 }
			 ForgeDirection dir=ForgeDirection.UNKNOWN;
			 if(x>0)dir=ForgeDirection.EAST;
			 if(x<0)dir=ForgeDirection.WEST;
			 if(z>0)dir=ForgeDirection.SOUTH;
			 if(z<0)dir=ForgeDirection.NORTH;
			 if(dir==ForgeDirection.UNKNOWN)return index;
			 if(lastDirection != dir){
				 switch(dir){
				 case EAST:
					 xProgress=1;
					 for(int i=0;i<zProgress;i++){
						 if(!this.isPassableOnPoint(entity, now.xCoord,now.yCoord,now.zCoord-i-1)){
							 return index-i-2;
						 }
					 }
					 break;
				 case WEST:
					 xProgress=-1;
					 for(int i=0;i<zProgress;i++){
						 if(!this.isPassableOnPoint(entity, now.xCoord,now.yCoord,now.zCoord-i-1)){
							 return index-i-2;
						 }
					 }
					 break;
				 case NORTH:
					 zProgress=-1;
					 for(int i=0;i<xProgress;i++){
						 if(!this.isPassableOnPoint(entity, now.xCoord-i-1,now.yCoord,now.zCoord)){
							 return index-i-2;
						 }
					 }
					 break;
				 case SOUTH:
					 zProgress=1;
					 for(int i=0;i<xProgress;i++){
						 if(!this.isPassableOnPoint(entity, now.xCoord-i-1,now.yCoord,now.zCoord)){
							 return index-i-2;
						 }
					 }
					 break;

				 default:
					 break;
				 }

				 lastDirection=dir;
			 }else{
				 xProgress+=x;
				 zProgress+=z;
				 if((MathHelper.abs_int(xProgress)>1&&MathHelper.abs_int(zProgress)>1)){
					 return index-1;
				 }
			 }
			 last=now;

		 }
		 return path.getCurrentPathLength()-1;
	 }

	 public boolean isPassableOnPoint(Entity entity,int x,float yBottom,int z){
		 float stepHeight=0f;
		 for(int h=MathHelper.floor_float(yBottom);h<yBottom+entity.height+1;h++){
			 if(!AStarPathFinder.isBlockPassable(entity.worldObj,entity, x, h, z)){
				 float step=(float) (AStarPathFinder.getMaxY(worldObj, entity, x, h, z,entity.width>1?2:1)-yBottom);
				 if(step<entity.stepHeight){
					 stepHeight=step;
				 }else if(yBottom+stepHeight<AStarPathFinder.getMinY(worldObj, entity, x, h, z,entity.width>1?2:1)){
				 }else{
					 return false;
				 }
			 }
		 }
		 return true;
	 }

	 /**
	  * Returns true when an entity of specified size could safely walk in a straight line between the two points. Args:
	  * pos1, pos2, entityXSize, entityYSize, entityZSize
	  */
	  public boolean isDirectPathBetweenPoints(Vec3 entityVec, Vec3 pointVec, int xWidth, int yHeight, int zWidth)
	  {

		  int entityX_int = MathHelper.floor_double(entityVec.xCoord);
		  int entityZ_int = MathHelper.floor_double(entityVec.zCoord);
		  double relativeX = pointVec.xCoord - entityVec.xCoord;
		  double relativeZ = pointVec.zCoord - entityVec.zCoord;
		  double squareDistance = relativeX * relativeX + relativeZ * relativeZ;

		  if (squareDistance < 1.0E-8D)
		  {
			  return false;
		  }
		  else
		  {
			  double reciprocalOfDistance = 1.0D / MathHelper.sqrt_double(squareDistance);
			  /**relativeX and relativeZ are normalized*/
			  relativeX *= reciprocalOfDistance;
			  relativeZ *= reciprocalOfDistance;
			  xWidth += 2;
			  zWidth += 2;

			  if (!this.isSafeToStandAt(entityX_int, (int)entityVec.yCoord, entityZ_int, xWidth, yHeight, zWidth, entityVec, relativeX, relativeZ))
			  {
				  return false;
			  }
			  else
			  {
				  xWidth -= 2;
				  zWidth -= 2;
				  double reciprocalOfRelativeX = 1.0D / Math.abs(relativeX);
				  double reciprocalOfRelativeZ = 1.0D / Math.abs(relativeZ);
				  double xEntityDisplacement = (double)(entityX_int * 1) - entityVec.xCoord;
				  double zEntityDisplacement = (double)(entityZ_int * 1) - entityVec.zCoord;

				  if (relativeX >= 0.0D)
				  {
					  ++xEntityDisplacement;
				  }

				  if (relativeZ >= 0.0D)
				  {
					  ++zEntityDisplacement;
				  }

				  xEntityDisplacement /= relativeX;
				  zEntityDisplacement /= relativeZ;
				  int xSign = relativeX < 0.0D ? -1 : 1;
				  int zSign = relativeZ < 0.0D ? -1 : 1;
				  int pointX_int = MathHelper.floor_double(pointVec.xCoord);
				  int pointZ_int = MathHelper.floor_double(pointVec.zCoord);
				  int relativeX_int = pointX_int - entityX_int;
				  int relativeZ_int = pointZ_int - entityZ_int;

				  do
				  {
					  if (relativeX_int * xSign <= 0 && relativeZ_int * zSign <= 0)
					  {
						  return true;
					  }

					  if (xEntityDisplacement < zEntityDisplacement)
					  {
						  xEntityDisplacement += reciprocalOfRelativeX;
						  entityX_int += xSign;
						  relativeX_int = pointX_int - entityX_int;
					  }
					  else
					  {
						  zEntityDisplacement += reciprocalOfRelativeZ;
						  entityZ_int += zSign;
						  relativeZ_int = pointZ_int - entityZ_int;
					  }
				  }
				  while (this.isSafeToStandAt
						  (entityX_int, (int)entityVec.yCoord, entityZ_int, xWidth, yHeight, zWidth, entityVec, relativeX, relativeZ));

				  return false;
			  }
		  }
	  }

	  /**
	   * Returns true when an entity could stand at a position, including solid blocks under the entire entity. Args:
	   * xOffset, yOffset, zOffset, entityXSize, entityYSize, entityZSize, originPosition, vecX, vecZ
	   */
	  public boolean isSafeToStandAt(int par1, int par2, int par3, int par4, int par5, int par6, Vec3 par7Vec3, double par8, double par10)
	  {
		  int k1 = par1 - par4 / 2;
		  int l1 = par3 - par6 / 2;

		  if (!this.isPositionClear(k1, par2, l1, par4, par5, par6, par7Vec3, par8, par10))
		  {
			  return false;
		  }
		  else
		  {
			  for (int i2 = k1; i2 < k1 + par4; ++i2)
			  {
				  for (int j2 = l1; j2 < l1 + par6; ++j2)
				  {
					  double d2 = (double)i2 + 0.5D - par7Vec3.xCoord;
					  double d3 = (double)j2 + 0.5D - par7Vec3.zCoord;

					  if (d2 * par8 + d3 * par10 >= 0.0D)
					  {
						  Block k2 = this.worldObj.getBlock(i2, par2 - 1, j2);

						  if (k2 ==Blocks.air)
						  {
							  return false;
						  }

						  Material material = k2.getMaterial();

						  if (material == Material.water && !this.theEntity.isInWater())
						  {
							  return false;
						  }

						  if (material == Material.lava)
						  {
							  return false;
						  }
					  }
				  }
			  }

			  return true;
		  }
	  }

	  /**
	   * Returns true if an entity does not collide with any solid blocks at the position. Args: xOffset, yOffset,
	   * zOffset, entityXSize, entityYSize, entityZSize, originPosition, vecX, vecZ
	   */
	  public boolean isPositionClear(int par1, int par2, int par3, int par4, int par5, int par6, Vec3 par7Vec3, double par8, double par10)
	  {
		  for (int k1 = par1; k1 < par1 + par4; ++k1)
		  {
			  for (int l1 = par2; l1 < par2 + par5; ++l1)
			  {
				  for (int i2 = par3; i2 < par3 + par6; ++i2)
				  {
					  double d2 = (double)k1 + 0.5D - par7Vec3.xCoord;
					  double d3 = (double)i2 + 0.5D - par7Vec3.zCoord;

					  if (d2 * par8 + d3 * par10 >= 0.0D)
					  {
						  Block j2 = this.worldObj.getBlock(k1, l1, i2);

						  if (j2 !=Blocks.air && !j2.getBlocksMovement(this.worldObj, k1, l1, i2))
						  {
							  return false;
						  }
					  }
				  }
			  }
		  }

		  return true;
	  }

	  public List<EngineerRequest> getRequestsTemp(AStarPathPoint newPathPoint){
		  if(newPathPoint.onLadder)return new ArrayList();
		  int x=newPathPoint.xCoord;
		  int y=newPathPoint.yCoord;
		  int z=newPathPoint.zCoord;
		  AStarPathPoint previous=newPathPoint.previous;
		  boolean canUseEngineer=this.canUseEngineer==2?this.theEntity.unit.canUseEngineer:this.canUseEngineer==1?true:false;
		  Entity follower=this.theEntity.getBottomEntity();

		  int x__=x-previous.xCoord;
		  int z__=z-previous.zCoord;
		  List<EngineerRequest> result=new ArrayList();

		  ForgeDirection dir=ForgeDirection.UNKNOWN;
		  if(x__>0)dir=ForgeDirection.EAST;
		  if(x__<0)dir=ForgeDirection.WEST;
		  if(z__>0)dir=ForgeDirection.SOUTH;
		  if(z__<0)dir=ForgeDirection.NORTH;
		  boolean isAbove=(x==previous.xCoord)&&z==previous.zCoord;

		  if(this.currentPath.entitySize==1){
			  boolean putBlockUnder=false;
			  if(!AStarPathFinder.isBlockRidable(this.worldObj,follower,x,y-1,z,this.canSwim,follower.width)){
				  //System.out.println("not ridable");

				  if(!canUseEngineer){
					  result.add(new EngineerRequest(new Coord(x,y-1,z),RequestType.PUT_BLOCK));
					  return result;
				  }


				  //end
				  result.add(new EngineerRequest(new Coord(x,y-1,z),RequestType.PUT_BLOCK));
				  putBlockUnder=true;
			  }
			  //These commands decides which blocks should be broken.
			  double underMaxY=AStarPathFinder.getMaxY(follower.worldObj,follower,  x, y-1, z,follower.width>1?2:1);
			  //AxisAlignedBB aabbUnder=this.getAABB(e.worldObj,e, x, y-1, z);

			  /*
        double maxY=Math.max(aabbUnder==null?y:aabbUnder.maxY,
        		this.getAABB(e.worldObj, previous.xCoord, previous.yCoord-1, previous.zCoord)==null?
        				previous.yCoord:this.getAABB(e.worldObj, previous.xCoord, previous.yCoord-1, previous.zCoord).maxY)+e.height;
        	for(int j=y;j<maxY;j++){
				if(!this.isBlockPassable(e,x, j, z)
						&&this.getAABB(e.worldObj, x, j, z).minY<maxY
						&&this.getAABB(e.worldObj, x, j, z).maxY>previous.yCoord+previous.yOffset+this.jumpheight){
					//System.out.println("not passable");
					if(!this.canUseEngineer)return false;
		        		newPathPoint.addBlocksToBreak(new Coord(x,j,z));
				}
			}

        	for(int j=MathHelper.ceiling_float_int(previous.yCoord+e.height);j<maxY;j++){
				if(!this.isBlockPassable(e,previous.xCoord, j, previous.zCoord)
						&&this.getAABB(e.worldObj,previous.xCoord, j, previous.zCoord).minY<maxY
						&&this.getAABB(e.worldObj,previous.xCoord, j, previous.zCoord).maxY>previous.yCoord+previous.yOffset+this.jumpheight){
					if(!this.canUseEngineer)return false;
		        		newPathPoint.addBlocksToBreak(new Coord(previous.xCoord,j,previous.zCoord));
				}
			}
			   */

			  float maxY=Math.max(newPathPoint.yOffset+((float)newPathPoint.yCoord),
					  previous.yOffset+((float)previous.yCoord))+follower.height;
			  for(int j=y;j<maxY;j++){
				  if(!AStarPathFinder.isBlockPassable(follower.worldObj,follower,x, j, z)
						  &&AStarPathFinder.getMinY(follower.worldObj,follower, x, j, z,follower.width>1?2:1)<maxY)n:{
					  //System.out.println("not passable");

					  if(follower instanceof EntityLivingBase && this.worldObj.getBlock(x, j, z).isLadder(this.worldObj, x, j, z, (EntityLivingBase)follower)){
						  switch(dir){
						  case EAST:
							  if(this.worldObj.getBlockMetadata(x, j, z)==4){

								  break n;
							  }
							  break;
						  case NORTH:
							  if(this.worldObj.getBlockMetadata(x, j, z)==3){

								  break n;
							  }
							  break;
						  case SOUTH:
							  if(this.worldObj.getBlockMetadata(x, j, z)==2) {

								  break n;
							  }
							  break;
						  case WEST:
							  if(this.worldObj.getBlockMetadata(x, j, z)==5){

								  break n;
							  }
							  break;
						  default:

							  break n;

						  }
					  }
					  if(!canUseEngineer){
						  //System.out.println("Deny ("+x+","+y+","+z+") Wall");
						  result.add(new EngineerRequest(new Coord(x,j,z),RequestType.BREAK));
						  return result;
					  }
					  if(follower.worldObj.getBlock(x,j,z).getBlockHardness(follower.worldObj, x,j,z)<0)return result;
					  result.add(new EngineerRequest(new Coord(x,j,z),RequestType.BREAK));
				  }
			  }

		  }else{
			  boolean putBlockUnder=false;
			  boolean ridableFlag=false;
			  int offY=newPathPoint.yOffset<0?-1:0;
			  if(canUseEngineer){
				  ridableFlag=true;
				  int offXmin=newPathPoint.dirFromPrev.offsetX>0?1:0;
				  int offXmax=newPathPoint.dirFromPrev.offsetX<0?1:0;
				  int offZmin=newPathPoint.dirFromPrev.offsetZ>0?1:0;
				  int offZmax=newPathPoint.dirFromPrev.offsetZ<0?1:0;
				
				  for(int x_=x-offXmax;x_>=x-1+offXmin;x_--){
					  for(int z_=z-offZmax;z_>=z-1+offZmin;z_--){
						  if(!AStarPathFinder.isBlockRidable(follower.worldObj,follower,x_,y-1+offY,z_,this.canSwim,follower.width)){
							  result.add(new EngineerRequest(new Coord(x_,y-1+offY,z_),RequestType.PUT_BLOCK));
							  ridableFlag=false;
						  }
					  }
				  }
			  }else{
				  for(int x_=x;x_>=x-1;x_--){
					  for(int z_=z;z_>=z-1;z_--){
						  if(AStarPathFinder.isBlockRidable(follower.worldObj,follower,x_,y-1,z_,this.canSwim,follower.width)){
							  ridableFlag=true;

						  }
					  }
				  }
				  if(!ridableFlag){
					  result.add(new EngineerRequest(new Coord(x,y-1,z),RequestType.PUT_BLOCK));
					  return result;
				  }
			  }

			  //These commands decides which blocks should be broken.
			  double underMaxY=AStarPathFinder.getMaxY(follower.worldObj,follower,  x, y-1, z,2);
			  //AxisAlignedBB aabbUnder=AStarPathFinder.getAABB(e.worldObj,e, x, y-1, z);

			  /*
        double maxY=Math.max(aabbUnder==null?y:aabbUnder.maxY,
        		AStarPathFinder.getAABB(e.worldObj, previous.xCoord, previous.yCoord-1, previous.zCoord)==null?
        				previous.yCoord:AStarPathFinder.getAABB(e.worldObj, previous.xCoord, previous.yCoord-1, previous.zCoord).maxY)+e.height;
        	for(int j=y;j<maxY;j++){
				if(!AStarPathFinder.isBlockPassable(e,x, j, z)
						&&AStarPathFinder.getAABB(e.worldObj, x, j, z).minY<maxY
						&&AStarPathFinder.getAABB(e.worldObj, x, j, z).maxY>previous.yCoord+previous.yOffset+this.jumpheight){
					//System.out.println("not passable");
					if(!canUseEngineer)return false;
		        		newPathPoint.addBlocksToBreak(new Coord(x,j,z));
				}
			}

        	for(int j=MathHelper.ceiling_float_int(previous.yCoord+e.height);j<maxY;j++){
				if(!AStarPathFinder.isBlockPassable(e,previous.xCoord, j, previous.zCoord)
						&&AStarPathFinder.getAABB(e.worldObj,previous.xCoord, j, previous.zCoord).minY<maxY
						&&AStarPathFinder.getAABB(e.worldObj,previous.xCoord, j, previous.zCoord).maxY>previous.yCoord+previous.yOffset+this.jumpheight){
					if(!canUseEngineer)return false;
		        		newPathPoint.addBlocksToBreak(new Coord(previous.xCoord,j,previous.zCoord));
				}
			}
			   */

			  float maxY=Math.max(newPathPoint.yOffset+((float)newPathPoint.yCoord),
					  previous.yOffset+((float)previous.yCoord))+follower.height;
			  float minY=Math.max(newPathPoint.yOffset+((float)newPathPoint.yCoord),
					  previous.yOffset+((float)previous.yCoord))+0.1f;
			  //System.out.println("maxy:"+maxY+" miny:"+minY);
			  boolean waterFlow=false;
			  int maxYToPlaceLadder=0;
			  for(int j=y+offY;j<maxY;j++){
				  for(int x_=x;x_>=x-1;x_--){
					  for(int z_=z;z_>=z-1;z_--){
						  if(!AStarPathFinder.isBlockPassable(follower.worldObj,follower,x_, j, z_)
								  &&AStarPathFinder.getMinY(follower.worldObj,follower,x_, j, z_,1)<maxY
								  &&AStarPathFinder.getMaxY(follower.worldObj,follower,x_, j, z_,1)>minY)n:{
							  //System.out.println("not passable");

							  if(follower instanceof EntityLivingBase && this.worldObj.getBlock(x_, j, z_).isLadder(this.worldObj, x_, j, z_, (EntityLivingBase)follower)){
								  switch(dir){
								  case EAST:
									  if(this.worldObj.getBlockMetadata(x_, j, z_)==4){

										  break n;
									  }
									  break;
								  case NORTH:
									  if(this.worldObj.getBlockMetadata(x_, j, z_)==3){

									  }
									  break;
								  case SOUTH:
									  if(this.worldObj.getBlockMetadata(x_, j, z_)==2) {

										  break n;
									  }
									  break;
								  case WEST:
									  if(this.worldObj.getBlockMetadata(x_, j, z_)==5){

										  break n;
									  }
									  break;
								  default:

									  break n;

								  }
							  }
							  //System.out.println("not passable");
							  if(!canUseEngineer){
								  //System.out.println("Deny ("+x+","+y+","+z+") Wall");
								  result.add(new EngineerRequest(new Coord(x_,j,z_),RequestType.BREAK));
								  return result;
							  }
							  if(follower.worldObj.getBlock(x_,j,z_).getBlockHardness(follower.worldObj, x_,j,z_)<0){
								  //System.out.println("Deny ("+x+","+y+","+z+") Unbreakable");
								  return result;
							  }
							  //System.out.println("break:"+new Coord(x_,j,z_));
							  result.add(new EngineerRequest(new Coord(x_,j,z_),RequestType.BREAK));

						  }
					  }
				  }
			  }
			  for(int x_=previous.xCoord;x_>=x-1;x_--){
				  for(int z_=previous.zCoord;z_>=z-1;z_--){
					  for(int j=MathHelper.ceiling_float_int(previous.yCoord+follower.height);j<maxY;j++){
						  if(!AStarPathFinder.isBlockPassable(follower.worldObj,follower,x_, j, z_)
								  &&AStarPathFinder.getMinY(follower.worldObj,follower,x_, j, z_,1)<maxY
								  &&AStarPathFinder.getMaxY(follower.worldObj,follower,x_, j, z_,1)>minY)n:{
							  if(follower instanceof EntityLivingBase && this.worldObj.getBlock(x_, j, z_).isLadder(this.worldObj, x_, j, z_, (EntityLivingBase)follower)){
								  break n;
							  }
							  if(!canUseEngineer){
								  //System.out.println("Deny ("+x+","+y+","+z+") Wall(previous)");
								  result.add(new EngineerRequest(new Coord(x_,j,z_),RequestType.BREAK));
								  return result;
							  }
							  if(follower.worldObj.getBlock(x_, j, z_).getBlockHardness(follower.worldObj, x_, j, z_)<0){
								  //System.out.println("Deny ("+x+","+y+","+z+") Unbreakable(previous)");
								  return result;
							  }
							  //System.out.println("breakP:"+new Coord(x_,j,z_));
							  result.add(new EngineerRequest(new Coord(x_,j,z_),RequestType.BREAK));

						  }
					  }
				  }
			  }
		  }
		  return result;
	  }


}
