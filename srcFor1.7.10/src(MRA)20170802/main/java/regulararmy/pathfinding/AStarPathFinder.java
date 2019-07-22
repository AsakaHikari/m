package regulararmy.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import regulararmy.analysis.FinderSettings;
import regulararmy.core.Coord;
import regulararmy.entity.EntityEngineer;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.entity.ai.*;
import regulararmy.entity.ai.EngineerRequest.RequestType;
import regulararmy.entity.command.RegularArmyLeader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.pathfinding.*;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;

public class AStarPathFinder
{
	/** Used to find obstacles */
	private IBlockAccess worldMap;

	/** The points in the path */
	private IntHashMap pointMap = new IntHashMap();

	/** Selection of path points to add to the path */
	private AStarPathPoint[] pathOptions = new AStarPathPoint[32];

	/** should the PathFinder go through wodden door blocks */
	private boolean isWoddenDoorAllowed;

	/**
	 * should the PathFinder disregard BlockMovement type materials in its path
	 */
	private boolean isMovementBlockAllowed;
	private boolean canSwim;

	/** tells the FathFinder to not stop pathing underwater */
	private boolean canEntityDrown;

	public boolean throughBase=false;

	public Map<Integer,Float> usedCrowdCost=new HashMap();

	public float jumpheight;

	public IPathFindRequester ai;

	private boolean lowerEngineerCost=false;

	private List<AStarPathPoint> openedPointList;

	private int firstDistanceFromDestination;

	public boolean canUseEngineer=true;

	public FinderSettings settings;

	public List<Coord> unusablePoints=new ArrayList();

	public int maxCost=Integer.MAX_VALUE;

	public int maxLength=Integer.MAX_VALUE;

	public float entityWidth=1.8f;

	public float entityHeight=1.8f;

	public int entitySize;

	public int baseTickToNext;
	
	public static boolean logHead=false;
	public static boolean logAcceptOrDeny=false;
	public static boolean logPath=false;
	public static boolean logRemoveUnne=false;

	public AStarPathFinder(IBlockAccess par1IBlockAccess, boolean par2, boolean par3, boolean par4, boolean par5,boolean lowerEngineerCost,float par6,IPathFindRequester ai)
	{
		this.worldMap = par1IBlockAccess;
		this.isWoddenDoorAllowed = par2;
		this.isMovementBlockAllowed = par3;
		this.canSwim = par4;
		this.canEntityDrown = par5;
		this.jumpheight=par6;
		this.lowerEngineerCost=lowerEngineerCost;

		this.ai=ai;
	}

	/**
	 * Creates a path from one entity to another within a minimum distance
	 */
	public AStarPathEntity createEntityPathTo(Entity entity, Entity par2Entity, float par3,float par4)
	{

		return this.createEntityPathTo(entity, par2Entity.posX, par2Entity.boundingBox.minY, par2Entity.posZ, par3,par4,entity.width,entity.height);
	}

	/**
	 * Creates a path from an entity to a specified location within a minimum distance
	 */
	public AStarPathEntity createEntityPathTo(Entity entity, int par2, int par3, int par4, float par5,float par6)
	{
		return this.createEntityPathTo(entity, (double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), par5,par6,entity.width,entity.height);
	}

	/**
	 * Internal implementation of creating a path from an entity to a point
	 */
	public AStarPathEntity createEntityPathTo(Entity par1Entity, double destX, double destY, double destZ, float maxDistance,float minDistance,float width,float height)
	{
		this.entityHeight=height;
		this.entityWidth=width;
		if(par1Entity instanceof EntityLivingBase){
			this.baseTickToNext=(int) (1/( (EntityLivingBase)par1Entity).getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
		}

		//System.out.println("from"+par1Entity.posX+","+par1Entity.posY+","+par1Entity.posZ);
		//System.out.println("for"+par2+","+par4+","+par6);
		/*
    	if(!this.canUseEngineer&&
    			!this.isBlockRidable(par1Entity,  MathHelper.floor_double(destX ), MathHelper.floor_double(destY)-1, MathHelper.floor_double(destZ ))){
    		return null;
    	}
		 */
		//System.out.println("from"+par1Entity.posX+","+par1Entity.posY+","+par1Entity.posZ);
		//System.out.println("for"+par2+","+par4+","+par6);
		this.entitySize=this.entityWidth<1?1:2;
		this.canUseEngineer=this.ai.isEngineer();
		this.openedPointList=new ArrayList((int) (maxDistance*32));
		this.pointMap.clearMap();
		this.usedCrowdCost.clear();
		boolean flag = this.canSwim;
		int i = MathHelper.floor_double(par1Entity.boundingBox.minY);

		if (this.canEntityDrown && par1Entity.isInWater())
		{
			i = (int)par1Entity.boundingBox.minY;

			for (Block j = this.worldMap.getBlock(MathHelper.floor_double(par1Entity.posX), i, MathHelper.floor_double(par1Entity.posZ));
					j  instanceof BlockLiquid || j instanceof BlockFluidBase; 
					j = this.worldMap.getBlock(MathHelper.floor_double(par1Entity.posX), i, MathHelper.floor_double(par1Entity.posZ)))
			{
				++i;
			}

			flag = this.canSwim;
			this.canSwim = false;
		}
		else
		{
			i = MathHelper.floor_double(par1Entity.boundingBox.minY);
		}
		int x1=MathHelper.floor_double(par1Entity.posX),z1=MathHelper.floor_double(par1Entity.posZ);
		AxisAlignedBB aabb=par1Entity.boundingBox;
		//System.out.println("entityAABB:"+aabb);
		if(this.entitySize==1){
			for(int x=MathHelper.floor_double(aabb.minX+0.1);x<aabb.maxX-0.1;x++){
				for(int z=MathHelper.floor_double(aabb.minZ+0.1);z<aabb.maxZ-0.1;z++){
					if(this.isBlockRidable(par1Entity.worldObj,par1Entity, x, i-1, z,this.canSwim,this.entityWidth)){
						x1=x;
						z1=z;
					}
				}
			}
		}else{
			for(int x=MathHelper.ceiling_double_int(aabb.minX+0.1);x<aabb.maxX-0.1;x++){
				for(int z=MathHelper.ceiling_double_int(aabb.minZ+0.1);z<aabb.maxZ-0.1;z++){
					if(this.isBlockRidable(par1Entity.worldObj,par1Entity, x, i-1, z,this.canSwim,this.entityWidth)){
						x1=x;
						z1=z;
					}
				}
			}
		}
		AStarPathPoint AStarPathPoint = new AStarPathPoint(x1, i, z1);
		AStarPathPoint AStarPathPoint1 = new AStarPathPoint(MathHelper.floor_double(destX), MathHelper.floor_double(destY), MathHelper.floor_double(destZ));
		AStarPathEntity pathentity;
		pathentity = this.addToPath(par1Entity, AStarPathPoint, AStarPathPoint1,  maxDistance,minDistance,this.entitySize);
		if(pathentity!=null && pathentity.getCurrentPathLength()<2)return null;
		this.canSwim = flag;
		/*
        boolean flag1=false;
        for(int j=0;j<pathentity.points.length;j++){
        	if(pathentity.points[j].blocksToBreak.length!=0){
        		flag1=true;
        		break;
        	}
        }
		 */
		
		if(logPath && pathentity!=null){
			for(int i1=0;i1<pathentity.getCurrentPathLength();i1++){
				AStarPathPoint point=pathentity.getPathPointFromIndex(i1);
				System.out.println
				(i1+": "+point.xCoord+","+point.yCoord+"("+point.yOffset+"),"+point.zCoord
						+" put:"+point.numberOfBlocksToPut+" break:"+point.numberOfBlocksToBreak);
				if(point.numberOfBlocksToPut>0){
					System.out.println("Put at");
					for(int j=0;j<point.numberOfBlocksToPut;j++){
						System.out.println(point.blocksToPut[j]);
					}
				}
				if(point.numberOfBlocksToBreak>0){
					System.out.println("Break at");
					for(int j=0;j<point.numberOfBlocksToBreak;j++){
						System.out.println(point.blocksToBreak[j]);
					}
				}
				
			}
			//System.out.println(pathentity.getTotalCost());
		}
	
		return pathentity;
	}

	private AStarPathEntity addToPath(Entity par1Entity, AStarPathPoint startPathPoint, AStarPathPoint endPathPoint, float maxDistance,float par6,int size)
	{
		startPathPoint.totalCost = 0;
		startPathPoint.distanceToNext = startPathPoint.func_75832_b(endPathPoint);
		startPathPoint.distanceToTarget = startPathPoint.distanceToNext;
		AStarPathPoint headPathPoint=startPathPoint;
		AStarPathPoint nearestPathPoint=startPathPoint;
		this.openedPointList.add(headPathPoint);
		headPathPoint.index=0;
		headPathPoint.isHead=true;
		double underMaxY=this.getMaxY(par1Entity.worldObj,par1Entity, startPathPoint.xCoord,startPathPoint.yCoord-1,startPathPoint.zCoord,size);
		if(Double.isNaN(underMaxY)){
			//System.out.println("point"+newPathPoint.toCoord().toString()+"is air");
			startPathPoint.yOffset=-1;

		}else{
			//System.out.println("point"+newPathPoint.toCoord().toString()+"is solid("+(underMaxY-(double)y)+")");
			startPathPoint.yOffset=(float)underMaxY-(float)startPathPoint.yCoord;
		}
		//System.out.println("underMaxY:"+underMaxY);

		{
			if (startPathPoint.equals(endPathPoint))
			{
				return this.createEntityPath(startPathPoint, startPathPoint,true,this.entitySize,par1Entity);
			}
			{
				float f = (float)(endPathPoint.xCoord - headPathPoint.xCoord);
				float f1 = (float)(endPathPoint.yCoord - headPathPoint.yCoord);
				float f2 = (float)(endPathPoint.zCoord - headPathPoint.zCoord);
				if(f * f + f1 * f1 + f2 * f2<par6*par6){
					return this.createEntityPath(startPathPoint, startPathPoint,true,this.entitySize,par1Entity);
				}

			}
			/**
			 * bit0:EAST(X+)
			 * bit1:WEST(X-)
			 * bit2:SOUTH(Z+)
			 * bit3:NORTH(Z-)*/
			byte binary=0x0f;
			if(size==1){
				for(int y=startPathPoint.yCoord;y<startPathPoint.yCoord+par1Entity.height;y++){

					Block b=par1Entity.worldObj.getBlock(startPathPoint.xCoord, y,startPathPoint.zCoord);
					if(!b.isOpaqueCube()&&par1Entity.boundingBox!=null&&b.getCollisionBoundingBoxFromPool(par1Entity.worldObj, startPathPoint.xCoord, y,startPathPoint.zCoord)!=null){
						AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(startPathPoint.xCoord, y,startPathPoint.zCoord,startPathPoint.xCoord+1, y+1,startPathPoint.zCoord+1);
						List<AxisAlignedBB> aabbList=new ArrayList();
						b.addCollisionBoxesToList(par1Entity.worldObj, startPathPoint.xCoord, y,startPathPoint.zCoord, aabb, aabbList, par1Entity);
						for(int i=0;i<aabbList.size();i++){
							boolean flagEast=(aabbList.get(i).minX>par1Entity.boundingBox.maxX);
							boolean flagWest=(aabbList.get(i).maxX<par1Entity.boundingBox.minX);
							boolean flagSouth=(aabbList.get(i).minZ>par1Entity.boundingBox.maxZ);
							boolean flagNorth=(aabbList.get(i).maxZ<par1Entity.boundingBox.minZ);
							if(!flagEast&&!flagWest){
								if(flagSouth){
									binary&=0xfb;
								}else if(flagNorth){
									binary&=0xf7;
								}
							}
							if(!flagSouth&&!flagNorth){
								if(flagEast){
									binary&=0xfe;
								}else if(flagWest){
									binary&=0xfd;
								}
							}
						}
					}
				}
			}else{


				for(int y=startPathPoint.yCoord;y<startPathPoint.yCoord+par1Entity.height;y++){
					for(int x=startPathPoint.xCoord;x>=startPathPoint.xCoord-1;x--){
						for(int z=startPathPoint.zCoord;z>=startPathPoint.zCoord+1;z--){
							Block b=par1Entity.worldObj.getBlock(x, y,z);
							if(!b.isOpaqueCube()&&par1Entity.boundingBox!=null&&b.getCollisionBoundingBoxFromPool(par1Entity.worldObj,x, y,z)!=null){
								AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(x, y,z,x+1, y+1,z+1);
								List<AxisAlignedBB> aabbList=new ArrayList();
								b.addCollisionBoxesToList(par1Entity.worldObj, startPathPoint.xCoord, y,startPathPoint.zCoord, aabb, aabbList, par1Entity);
								for(int i=0;i<aabbList.size();i++){
									boolean flagEast=(aabbList.get(i).minX>par1Entity.boundingBox.maxX);
									boolean flagWest=(aabbList.get(i).maxX<par1Entity.boundingBox.minX);
									boolean flagSouth=(aabbList.get(i).minZ>par1Entity.boundingBox.maxZ);
									boolean flagNorth=(aabbList.get(i).maxZ<par1Entity.boundingBox.minZ);
									if(!flagEast&&!flagWest){
										if(flagSouth){
											binary&=0xfb;
										}else if(flagNorth){
											binary&=0xf7;
										}
									}
									if(!flagSouth&&!flagNorth){
										if(flagEast){
											binary&=0xfe;
										}else if(flagWest){
											binary&=0xfd;
										}
									}
								}
							}
						}
					}
				}
			}
			double maxheight=startPathPoint.yCoord+startPathPoint.yOffset+this.jumpheight;
			if((binary&0x01)>0){
				this.openPointSide(par1Entity, startPathPoint, endPathPoint, startPathPoint.xCoord+1, MathHelper.ceiling_double_int(maxheight), startPathPoint.zCoord, (float) maxheight,size);
			}
			if((binary&0x02)>0){
				this.openPointSide(par1Entity, startPathPoint, endPathPoint, startPathPoint.xCoord-1, MathHelper.ceiling_double_int(maxheight), startPathPoint.zCoord, (float) maxheight,size);
			}
			if((binary&0x04)>0){
				this.openPointSide(par1Entity, startPathPoint, endPathPoint, startPathPoint.xCoord, MathHelper.ceiling_double_int(maxheight), startPathPoint.zCoord+1, (float) maxheight,size);
			}
			if((binary&0x08)>0){
				this.openPointSide(par1Entity, startPathPoint, endPathPoint, startPathPoint.xCoord, MathHelper.ceiling_double_int(maxheight), startPathPoint.zCoord-1, (float) maxheight,size);
			}
			this.openPointSide(par1Entity, startPathPoint, endPathPoint, startPathPoint.xCoord, MathHelper.ceiling_double_int(maxheight), startPathPoint.zCoord, (float) maxheight,size);
			this.openedPointList.remove(startPathPoint);
			int lowestcost=Integer.MAX_VALUE;
			float nearestDistance=Float.MAX_VALUE;
			if(this.openedPointList.size()==0)return null;
			for(int j=0;j<this.openedPointList.size();j++){
				AStarPathPoint p=this.openedPointList.get(j);
				if(p.totalCost<=lowestcost){
					headPathPoint=p;
					lowestcost=(int) headPathPoint.totalCost;

				}
				if(p.distanceTo(endPathPoint)<nearestDistance){
					nearestPathPoint=p;
					nearestDistance=p.distanceTo(endPathPoint);
				}
			}
			if(!(this.ai instanceof EntityAIBreakBlock)){
				//System.out.println(headPathPoint.xCoord+","+headPathPoint.yCoord+","+headPathPoint.zCoord+"'s cost is"+headPathPoint.totalCost);
			}
		}

		for(int i=0;i<maxDistance*4*(this.canUseEngineer?4:1);i++)
		{
			if (headPathPoint.equals(endPathPoint))
			{
				return this.createEntityPath(startPathPoint, headPathPoint,true,this.entitySize,par1Entity);
			}
			{
				float f = (float)(endPathPoint.xCoord - headPathPoint.xCoord);
				float f1 = (float)(endPathPoint.yCoord - headPathPoint.yCoord);
				float f2 = (float)(endPathPoint.zCoord - headPathPoint.zCoord);
				if(f * f + f1 * f1 + f2 * f2<par6*par6){
					return this.createEntityPath(startPathPoint, headPathPoint,true,this.entitySize,par1Entity);
				}
				if(i>this.maxLength){
					return this.createEntityPath(startPathPoint, nearestPathPoint,false,this.entitySize,par1Entity);
				}
			}
			if(par1Entity instanceof EntityRegularArmy){
				RegularArmyLeader leader=((EntityRegularArmy) par1Entity).unit.leader;
				if(MathHelper.abs_int(leader.x-headPathPoint.xCoord)
						+ MathHelper.abs_int(leader.y-headPathPoint.yCoord)
						+ MathHelper.abs_int(leader.z-headPathPoint.zCoord)<=2){
					headPathPoint.isNearBase=true;
				}else if(!this.throughBase && headPathPoint.previous.isNearBase){
					//System.out.println("nearBase");
					return this.createEntityPath(startPathPoint, headPathPoint.previous,true,this.entitySize,par1Entity);
				}
			}
			if(this.canUseEngineer&&headPathPoint.func_75832_b(endPathPoint)>startPathPoint.distanceToTarget+100){
				this.openedPointList.remove(headPathPoint);
			}else{
				this.openPathAround(par1Entity, headPathPoint, endPathPoint, maxDistance,size);
			}
			this.openedPointList.remove(headPathPoint);
			int lowestcost=Integer.MAX_VALUE;
			float nearestDistance=Float.MAX_VALUE;
			if(this.openedPointList.size()==0)return null;
			for(int j=0;j<this.openedPointList.size();j++){
				AStarPathPoint p=this.openedPointList.get(j);
				if(p.totalCost<=lowestcost){
					headPathPoint=p;
					lowestcost=(int) headPathPoint.totalCost;

				}
				if(p.distanceTo(endPathPoint)<nearestDistance){
					nearestPathPoint=p;
					nearestDistance=p.distanceTo(endPathPoint);
				}
			}
			if(logHead){
				System.out.println
				(headPathPoint.xCoord+","+headPathPoint.yCoord+"("+headPathPoint.yOffset+"),"+headPathPoint.zCoord
						+" put:"+headPathPoint.numberOfBlocksToPut+" break:"+headPathPoint.numberOfBlocksToBreak);
				System.out.println("RealCost:"+headPathPoint.totalRealCost+" HCost:"+headPathPoint.getHeuristicCost());
			}
		}

		return this.createEntityPath(startPathPoint, nearestPathPoint,false,this.entitySize,par1Entity);
	}



	/**
	 * populates pathOptions with available points and returns the number of options found (args: unused1, currentPoint,
	 * unused2, targetPoint, maxDistance)
	 */
	private void openPathAround(Entity par1Entity, AStarPathPoint currentPoint, AStarPathPoint endPoint, float par5,int size)
	{
		//AxisAlignedBB aabb=this.getAABB(par1Entity.worldObj, currentPoint.xCoord, currentPoint.yCoord-1, currentPoint.zCoord);
		//double maxheight=(aabb==null?currentPoint.yCoord:aabb.maxY)+this.jumpheight;
		double maxheight=currentPoint.yCoord+currentPoint.yOffset+this.jumpheight;
		/*
    	boolean flag1=true;
    	for(int i=MathHelper.ceiling_double_int(maxheight-this.jumpheight+par1Entity.height);i<Math.min(maxheight+par1Entity.height, this.worldMap.getHeight());i++){

    		if(!isBlockPassable(par1Entity,currentPoint.xCoord, i, currentPoint.zCoord)){
    			AxisAlignedBB aabb2=this.getAABB(par1Entity.worldObj, currentPoint.xCoord, i+1, currentPoint.zCoord);
    			if(aabb2==null){

    			}else{
    				double d=aabb2.minY-par1Entity.height+1;
    				maxheight=maxheight>d?d:maxheight;
    				flag1=false;
    				break;
    			}
    		}
    	}
		 */
		//System.out.println("maxheight="+maxheight);
		//System.out.println("("+currentPoint.xCoord+","+currentPoint.yCoord+","+currentPoint.zCoord+") offset:"+currentPoint.yOffset+" ->");
		this.openPointSide(par1Entity, currentPoint, endPoint, currentPoint.xCoord, MathHelper.ceiling_double_int(maxheight), currentPoint.zCoord, (float) maxheight,size);
		this.openPointSide(par1Entity, currentPoint, endPoint, currentPoint.xCoord+1, MathHelper.ceiling_double_int(maxheight), currentPoint.zCoord, (float) maxheight,size);
		this.openPointSide(par1Entity, currentPoint, endPoint, currentPoint.xCoord, MathHelper.ceiling_double_int(maxheight), currentPoint.zCoord+1, (float) maxheight,size);
		this.openPointSide(par1Entity, currentPoint, endPoint, currentPoint.xCoord-1, MathHelper.ceiling_double_int(maxheight), currentPoint.zCoord, (float) maxheight,size);
		this.openPointSide(par1Entity, currentPoint, endPoint, currentPoint.xCoord, MathHelper.ceiling_double_int(maxheight), currentPoint.zCoord-1, (float) maxheight,size);
		//this.openPointDiagonal(par1Entity, currentPoint, endPoint, currentPoint.xCoord+1,MathHelper.floor_float(maxheight), currentPoint.zCoord+1, maxheight);
		//this.openPointDiagonal(par1Entity, currentPoint, endPoint, currentPoint.xCoord+1,MathHelper.floor_float(maxheight), currentPoint.zCoord-1, maxheight);
		//this.openPointDiagonal(par1Entity, currentPoint, endPoint, currentPoint.xCoord-1,MathHelper.floor_float(maxheight), currentPoint.zCoord-1, maxheight);
		//this.openPointDiagonal(par1Entity, currentPoint, endPoint, currentPoint.xCoord-1,MathHelper.floor_float(maxheight), currentPoint.zCoord+1, maxheight);
		this.openedPointList.remove(currentPoint);
	}



	private void openPointSide(Entity par1Entity,AStarPathPoint currentPoint,AStarPathPoint endPoint,int x,int y,int z,float maxheight,int size){
		/*while(y>=1){
    		if(isBlockRidable(par1Entity,x,y,z)){
   				boolean flag=true;
   				for(int j=y+1;j<Block.blocksList[this.worldMap.getBlockId(x, y, z)].getCollisionBoundingBoxFromPool(par1Entity.worldObj, x, y, z).maxY
   						+MathHelper.ceiling_double_int(par1Entity.height);j++){
   					if(isBlockRidable(par1Entity,x,j,z)
   							&&Block.blocksList[this.worldMap.getBlockId(x, j, z)].getCollisionBoundingBoxFromPool(par1Entity.worldObj, x, j, z).minY
   							<y+par1Entity.height){
   						flag=false;
   						break;
   					}
   				}
   				if(flag){
   					openPoint(par1Entity,currentPoint,endPoint,x,y+1,z);
   					if(y>=currentPoint.yCoord-1)break;
   				}
   				y-=MathHelper.ceiling_double_int(par1Entity.height);
    		}else y-=1;

    	}*/
		for(;y>=1;y--){
			double maxY=this.getMaxY(par1Entity.worldObj, par1Entity, x, y-1, z,size);
			if((Double.isNaN(maxY)?y:maxY)<=maxheight&&!(x==currentPoint.xCoord&&y==currentPoint.yCoord&&z==currentPoint.zCoord)){
				openPoint(par1Entity,currentPoint,endPoint,x,y,z,size);
			}
			if(y<=currentPoint.yCoord-1&&isBlockRidable(par1Entity.worldObj,par1Entity,x,y,z,this.canSwim,this.entityWidth))break;
		}
	}



	public void openPointDiagonal(EntityRegularArmy par1Entity,AStarPathPoint currentPoint,AStarPathPoint endPoint,int x,int y,int z,float maxheight,int size){
		while(y>=1){
			if(isBlockRidable(par1Entity.worldObj,par1Entity,x,y,z,this.canSwim,this.entityWidth)){
				boolean flag=true;
				for(int j=y+1;j<this.worldMap.getBlock(x, y, z).getCollisionBoundingBoxFromPool(par1Entity.worldObj, x, y, z).maxY
						+MathHelper.ceiling_double_int(par1Entity.height);j++){
					if(isBlockRidable(par1Entity.worldObj,par1Entity,x,j,z,this.canSwim,this.entityWidth)
							&&this.worldMap.getBlock(x, j, z).getCollisionBoundingBoxFromPool(par1Entity.worldObj, x, j, z).minY
							<y+par1Entity.height){
						flag=false;
						break;
					}
					if(isBlockRidable(par1Entity.worldObj,par1Entity,currentPoint.xCoord,j,z,this.canSwim,this.entityWidth)
							&&this.worldMap.getBlock(currentPoint.xCoord, j, z).getCollisionBoundingBoxFromPool(par1Entity.worldObj, currentPoint.xCoord, j, z).minY
							<y+par1Entity.height){
						flag=false;
						break;
					}
					if(isBlockRidable(par1Entity.worldObj,par1Entity,x,j,currentPoint.zCoord,this.canSwim,this.entityWidth)
							&&this.worldMap.getBlock(x, j, currentPoint.zCoord).getCollisionBoundingBoxFromPool(par1Entity.worldObj, x, j, currentPoint.zCoord).minY
							<y+par1Entity.height){
						flag=false;
						break;
					}
				}
				if(flag){
					openPoint(par1Entity,currentPoint,endPoint,x,y+1,z,size);
					if(y>=currentPoint.yCoord-1)break;
				}
				y-=MathHelper.ceiling_double_int(par1Entity.height);
			}else y-=1;
		}
	}

	/**
	 * Returns a mapped point or creates and adds one
	 */
	private boolean openPoint(Entity par1Entity,AStarPathPoint previous,AStarPathPoint end,int x, int y, int z,int size)
	{

		AStarPathPoint newPathPoint = null;

		//System.out.println("opened:"+x+","+y+","+z);

		newPathPoint = new AStarPathPoint(x, y, z);

		if(this.unusablePoints.contains(newPathPoint.toCoord()))return false;

		if(newPathPoint.isAssigned())return false;
		newPathPoint.previous=previous;
		newPathPoint.index=previous.index+1;
		newPathPoint.beforeRequested=previous.beforeRequested+1;
		int x__=x-previous.xCoord;
		int z__=z-previous.zCoord;
		boolean isAbove=(x==previous.xCoord)&&z==previous.zCoord;
		if(isAbove){
			//System.out.println("Above");
		}

		ForgeDirection dir=ForgeDirection.UNKNOWN;
		if(x__>0)dir=ForgeDirection.EAST;
		if(x__<0)dir=ForgeDirection.WEST;
		if(z__>0)dir=ForgeDirection.SOUTH;
		if(z__<0)dir=ForgeDirection.NORTH;
		newPathPoint.dirFromPrev=dir;

		if(size==1){
			boolean putBlockUnder=false;
			if(!isBlockRidable(par1Entity.worldObj,par1Entity,x,y-1,z,this.canSwim,this.entityWidth)){
				//System.out.println("not ridable");

				if(!this.canUseEngineer){
					//System.out.println("Deny ("+x+","+y+","+z+") Unridable");
					return false;
				}

				//Putting blocks shouldn't causes suffocation.
				AStarPathPoint p=previous;
				for(int i=0;i<8;i++){
					if(p.xCoord==x&&p.zCoord==z&&p.yCoord<=y&&p.yCoord+this.entityHeight>=y-1){
						return false;
					}
					if(p.previous==null){
						break;
					}else{
						p=p.previous;
					}
				}
				//they cannot place block under 4 blocks
				if(p.yCoord>=y+3){
					return false;
				}
				//end
				newPathPoint.addBlocksToPut(new Coord(x,y-1,z));
				putBlockUnder=true;
			}
			//These commands decides which blocks should be broken.
			double underMaxY=this.getMaxY(par1Entity.worldObj,par1Entity,  x, y-1, z,1);
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
			if(putBlockUnder){
				//System.out.println("point"+newPathPoint.toCoord().toString()+"puts a block");
				newPathPoint.yOffset=0;
			}else if(Double.isNaN(underMaxY)){
				//System.out.println("point"+newPathPoint.toCoord().toString()+"is air");
				newPathPoint.yOffset=-1;

			}else{
				//System.out.println("point"+newPathPoint.toCoord().toString()+"is solid("+(aabbUnder.maxY-(double)y)+")");
				newPathPoint.yOffset=(float)underMaxY-(float)y;
			}

			float maxY=Math.max(newPathPoint.yOffset+((float)newPathPoint.yCoord),
					previous.yOffset+((float)previous.yCoord))+this.entityHeight;
			for(int j=y;j<maxY;j++){
				if(!this.isBlockPassable(par1Entity.worldObj,par1Entity,x, j, z)
						&&this.getMinY(par1Entity.worldObj,par1Entity, x, j, z,1)<maxY)n:{
					//System.out.println("not passable");

					if(par1Entity instanceof EntityLivingBase && this.worldMap.getBlock(x, j, z).isLadder(worldMap, x, j, z, (EntityLivingBase)par1Entity)){
						/*
						switch(dir){
						case EAST:
							if(this.worldMap.getBlockMetadata(x, j, z)==4){
								if(j==y){
									newPathPoint.onLadder=true;
								}
								newPathPoint.addCoordsLadder(new Coord(x,j,z));
								break n;
							}
							break;
						case NORTH:
							if(this.worldMap.getBlockMetadata(x, j, z)==3){
								if(j==y){
									newPathPoint.onLadder=true;
								}
								newPathPoint.addCoordsLadder(new Coord(x,j,z));
								break n;
							}
							break;
						case SOUTH:
							if(this.worldMap.getBlockMetadata(x, j, z)==2) {
								if(j==y){
									newPathPoint.onLadder=true;
								}
								newPathPoint.addCoordsLadder(new Coord(x,j,z));
								break n;
							}
							break;
						case WEST:
							if(this.worldMap.getBlockMetadata(x, j, z)==5){
								if(j==y){
									newPathPoint.onLadder=true;
								}
								newPathPoint.addCoordsLadder(new Coord(x,j,z));
								break n;
							}
							break;
						default:
							if(j==y){
								newPathPoint.onLadder=true;
							}
							newPathPoint.addCoordsLadder(new Coord(x,j,z));
							break n;
						}
						 */
						if(j==y){
							newPathPoint.onLadder=true;
						}
						newPathPoint.addCoordsLadder(new Coord(x,j,z));
						break n;
					}
					if(!this.canUseEngineer){
						//System.out.println("Deny ("+x+","+y+","+z+") Wall");
						return false;
					}
					if(par1Entity.worldObj.getBlock(x,j,z).getBlockHardness(par1Entity.worldObj, x,j,z)<0)return false;
					newPathPoint.addBlocksToBreak(new Coord(x,j,z));
				}
			}

			for(int j=MathHelper.ceiling_float_int(previous.yCoord+this.entityHeight);j<maxY;j++){
				if(!this.isBlockPassable(par1Entity.worldObj,par1Entity,previous.xCoord, j, previous.zCoord)
						&&this.getMinY(par1Entity.worldObj,par1Entity,previous.xCoord, j, previous.zCoord,1)<maxY
						)n:{
					if(par1Entity instanceof EntityLivingBase && this.worldMap.getBlock(x, j, z).isLadder(worldMap, x, j, z, (EntityLivingBase)par1Entity)){
						newPathPoint.onLadder=true;
						newPathPoint.addCoordsLadder(new Coord(x,j,z));
						break n;
					}
					if(!this.canUseEngineer){
						//System.out.println("Deny ("+x+","+y+","+z+") Wall(previous)");
						return false;
					}
					if(par1Entity.worldObj.getBlock(previous.xCoord,j,previous.zCoord).getBlockHardness(par1Entity.worldObj, previous.xCoord,j,previous.zCoord)<0)return false;
					newPathPoint.addBlocksToBreak(new Coord(previous.xCoord,j,previous.zCoord));

				}
			}
		}else{
			double underMaxY=this.getMaxY(par1Entity.worldObj,par1Entity,  x, y-1, z,2);
			boolean putBlockUnder=false;
			boolean ridableFlag=false;
			if(this.canUseEngineer){
				ridableFlag=true;
				for(int x_=x;x_>=x-1;x_--){
					for(int z_=z;z_>=z-1;z_--){
						if(!isBlockRidable(par1Entity.worldObj,par1Entity,x_,y-1,z_,this.canSwim,this.entityWidth)){
							ridableFlag=false;
							//they cannot place block under 4 blocks
							if(previous.yCoord>=y+3){
								return false;
							}
						}
					}
				}
			}else{
				for(int x_=x;x_>=x-1;x_--){
					for(int z_=z;z_>=z-1;z_--){
						if(isBlockRidable(par1Entity.worldObj,par1Entity,x_,y-1,z_,this.canSwim,this.entityWidth)){
							ridableFlag=true;

						}
					}
				}
			}
			if(!ridableFlag)m:{
				//System.out.println("not ridable");
				if(!this.canUseEngineer){
					//System.out.println("Deny ("+x+","+y+","+z+") Unridable");
					return false;
				}

				List<Coord> listTmp=new ArrayList();
				int yoff=(underMaxY-y)<0?-1:0;
				int yoffprev=(previous.yOffset)<0?-1:0;
				switch(dir){
				case EAST:
					listTmp.add(new Coord(x,y-1+yoff,z));
					listTmp.add(new Coord(x,y-1+yoff,z-1));
					listTmp.add(new Coord(previous.xCoord,Math.min(previous.yCoord, y)-1+yoffprev,previous.zCoord));
					listTmp.add(new Coord(previous.xCoord,Math.min(previous.yCoord, y)-1+yoffprev,previous.zCoord-1));
					break;
				case WEST:
					listTmp.add(new Coord(x-1,y-1+yoff,z));
					listTmp.add(new Coord(x-1,y-1+yoff,z-1));
					listTmp.add(new Coord(previous.xCoord-1,Math.min(previous.yCoord, y)-1+yoffprev,previous.zCoord));
					listTmp.add(new Coord(previous.xCoord-1,Math.min(previous.yCoord, y)-1+yoffprev,previous.zCoord-1));
					break;

				case SOUTH:
					listTmp.add(new Coord(x,y-1+yoff,z));
					listTmp.add(new Coord(x-1,y-1+yoff,z));
					listTmp.add(new Coord(previous.xCoord,Math.min(previous.yCoord, y)-1+yoffprev,previous.zCoord));
					listTmp.add(new Coord(previous.xCoord-1,Math.min(previous.yCoord, y)-1+yoffprev,previous.zCoord));
					break;
				case NORTH:
					listTmp.add(new Coord(x,y-1+yoff,z-1));
					listTmp.add(new Coord(x-1,y-1+yoff,z-1));
					listTmp.add(new Coord(previous.xCoord,Math.min(previous.yCoord, y)-1+yoffprev,previous.zCoord-1));
					listTmp.add(new Coord(previous.xCoord-1,Math.min(previous.yCoord, y)-1+yoffprev,previous.zCoord-1));
					break;
				default:
					//System.out.println("Deny ("+x+","+y+","+z+") Above");
					//return false;
					break m;

				}
				List<Coord> coordsToPutBlock=new ArrayList();

				for(Coord c:listTmp){
					if(!this.isBlockRidable(par1Entity.worldObj,par1Entity, c.x, c.y, c.z,this.canSwim,this.entityWidth)
							||this.getMaxY(par1Entity.worldObj,par1Entity, c.x, c.y, c.z, 1)<c.y+1)n:{
						if(previous.blocksToPut!=null){
							for(Coord c1:previous.blocksToPut){
								if(c1!=null && c1.equals(c))break n;
							}
						}
						coordsToPutBlock.add(c);
						putBlockUnder=true;
					}
				}
				//Putting blocks shouldn't causes suffocation.

				AStarPathPoint p=previous;
				for(int i=0;i<10;i++){
					for(Coord c:coordsToPutBlock){
						if(p.xCoord-c.x<=0&&p.xCoord-c.x>=-1
								&&p.zCoord-c.z<=0&&p.zCoord-c.z>=-1
								&&p.yCoord<c.y&&p.yCoord+this.entityHeight>=c.y){
							//System.out.println("Deny ("+x+","+y+","+z+") Suffocation");
							return false;
						}
					}
					if(p.previous==null){
						break;
					}else{
						p=p.previous;
					}
				}
				
				/*
			if(y-previous.yCoord>0){
				switch(dir){
				case EAST:
					coordsToPutBlock.add(new Coord(previous.xCoord-1,previous.yCoord-1,previous.zCoord));
					coordsToPutBlock.add(new Coord(previous.xCoord-1,previous.yCoord-1,previous.zCoord-1));
					break;
				case WEST:
					coordsToPutBlock.add(new Coord(previous.xCoord,previous.yCoord-1,previous.zCoord));
					coordsToPutBlock.add(new Coord(previous.xCoord,previous.yCoord-1,previous.zCoord-1));
					break;

				case SOUTH:
					coordsToPutBlock.add(new Coord(previous.xCoord,previous.yCoord-1,previous.zCoord-1));
					coordsToPutBlock.add(new Coord(previous.xCoord-1,previous.yCoord-1,previous.zCoord-1));
					break;
				case NORTH:
					coordsToPutBlock.add(new Coord(previous.xCoord,previous.yCoord-1,previous.zCoord));
					coordsToPutBlock.add(new Coord(previous.xCoord-1,previous.yCoord-1,previous.zCoord));
					break;
				default:
					break;
				}
			}*/

				//end
				for(Coord c:coordsToPutBlock){
					//System.out.println("put:"+c);
					newPathPoint.addBlocksToPut(c);
				}


			}
			//These commands decides which blocks should be broken.

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
			if(putBlockUnder){
				//System.out.println("point"+newPathPoint.toCoord().toString()+"puts a block");
				newPathPoint.yOffset=0;
			}else if(Double.isNaN(underMaxY)){
				//System.out.println("point"+newPathPoint.toCoord().toString()+"is air");
				newPathPoint.yOffset=-1;

			}else{
				//System.out.println("point"+newPathPoint.toCoord().toString()+"is solid("+(aabbUnder.maxY-(double)y)+")");
				newPathPoint.yOffset=(float)underMaxY-(float)y;
			}

			float maxY=Math.max(newPathPoint.yOffset+((float)newPathPoint.yCoord),
					previous.yOffset+((float)previous.yCoord))+this.entityHeight;
			boolean waterFlow=false;
			int maxYToPlaceLadder=0;
			List<Coord> laddersCoords=new ArrayList();
			if(this.canUseEngineer){
				AStarPathPoint p=previous;
				for(int i=0;i<5;i++){
					if(p.laddersToPut!=null){
						for(Coord c:p.laddersToPut){
							if(c!=null){
								laddersCoords.add(c);
							}
						}
					}
					if(p.previous==null){
						break;
					}else{
						p=p.previous;
					}
				}

			}
			for(int j=y;j<maxY;j++){
				for(int x_=x;x_>=x-1;x_--){
					for(int z_=z;z_>=z-1;z_--){
						if(laddersCoords.contains(new Coord(x_,j,z_))){
							if(j==y){
								newPathPoint.onLadder=true;
							}
							newPathPoint.addCoordsLadder(new Coord(x_,j,z_));
						}
						if(!this.isBlockPassable(par1Entity.worldObj,par1Entity,x_, j, z_)
								&&this.getMinY(par1Entity.worldObj,par1Entity, x_, j, z_,2)<maxY)n:{
							//System.out.println("not passable");
							//System.out.println("checking "+x_+","+j+","+z_);
							if(par1Entity instanceof EntityLivingBase && (this.worldMap.getBlock(x_, j, z_).isLadder(worldMap, x_, j, z_, (EntityLivingBase)par1Entity))){
								/*
								switch(dir){
								case EAST:
									if(this.worldMap.getBlockMetadata(x_, j, z_)==4){
										if(j==y){
											newPathPoint.onLadder=true;
										}
										newPathPoint.addCoordsLadder(new Coord(x_,j,z_));
										break n;
									}
									break;
								case NORTH:
									if(this.worldMap.getBlockMetadata(x_, j, z_)==3){
										if(j==y){
											newPathPoint.onLadder=true;
										}
										newPathPoint.addCoordsLadder(new Coord(x_,j,z_));
										break n;
									}
									break;
								case SOUTH:
									if(this.worldMap.getBlockMetadata(x_, j, z_)==2) {
										if(j==y){
											newPathPoint.onLadder=true;
										}
										newPathPoint.addCoordsLadder(new Coord(x_,j,z_));
										break n;
									}
									break;
								case WEST:
									if(this.worldMap.getBlockMetadata(x_, j, z_)==5){
										if(j==y){
											newPathPoint.onLadder=true;
										}
										newPathPoint.addCoordsLadder(new Coord(x_,j,z_));
										break n;
									}
									break;
								default:
									if(j==y){
										newPathPoint.onLadder=true;
									}
									newPathPoint.addCoordsLadder(new Coord(x_,j,z_));
									break n;

								}*/

								if(j==y){
									newPathPoint.onLadder=true;
								}
								newPathPoint.addCoordsLadder(new Coord(x_,j,z_));
								break n;
							}

							//System.out.println("not passable");
							if(!this.canUseEngineer){
								//System.out.println("Deny ("+x+","+y+","+z+") Wall");
								return false;
							}
							if(par1Entity.worldObj.getBlock(x_,j,z_).getBlockHardness(par1Entity.worldObj, x_,j,z_)<0){
								//System.out.println("Deny ("+x+","+y+","+z+") Unbreakable");
								return false;
							}
							//System.out.println("break:"+new Coord(x_,j,z_));
							newPathPoint.addBlocksToBreak(new Coord(x_,j,z_));
							if(!waterFlow && isAbove){
								if(this.isBlockLiquid(par1Entity.worldObj,x_, j, z_))waterFlow=true;
								if(this.isBlockLiquid(par1Entity.worldObj,x_+1, j, z_))waterFlow=true;
								if(this.isBlockLiquid(par1Entity.worldObj,x_-1, j, z_))waterFlow=true;
								if(this.isBlockLiquid(par1Entity.worldObj,x_, j, z_+1))waterFlow=true;
								if(this.isBlockLiquid(par1Entity.worldObj,x_, j, z_-1))waterFlow=true;
								if(this.isBlockLiquid(par1Entity.worldObj,x_, j+1, z_))waterFlow=true;

							}
							if(waterFlow){
								maxYToPlaceLadder=j;
							}
						}
					}
				}
			}
			for(int x_=previous.xCoord;x_>=x-1;x_--){
				for(int z_=previous.zCoord;z_>=z-1;z_--){
					for(int j=MathHelper.ceiling_float_int(previous.yCoord+this.entityHeight);j<maxY;j++){
						if(!this.isBlockPassable(par1Entity.worldObj,par1Entity,x_, j, z_)
								&&this.getMinY(par1Entity.worldObj,par1Entity,x_, j, z_,2)<maxY
								)n:{
							if(par1Entity instanceof EntityLivingBase && this.worldMap.getBlock(x_, j, z_).isLadder(worldMap, x_, j, z_, (EntityLivingBase)par1Entity)){
								newPathPoint.addCoordsLadder(new Coord(x_,j,z_));
								break n;
							}
							if(!this.canUseEngineer){
								//System.out.println("Deny ("+x+","+y+","+z+") Wall(previous)");
								return false;
							}
							if(par1Entity.worldObj.getBlock(x_, j, z_).getBlockHardness(par1Entity.worldObj, x_, j, z_)<0){
								//System.out.println("Deny ("+x+","+y+","+z+") Unbreakable(previous)");
								return false;
							}
							//System.out.println("breakP:"+new Coord(x_,j,z_));
							newPathPoint.addBlocksToBreak(new Coord(x_, j, z_));

							if(!waterFlow&& isAbove){
								if(this.isBlockLiquid(par1Entity.worldObj,x_, j, z_))waterFlow=true;
								if(this.isBlockLiquid(par1Entity.worldObj,x_+1, j, z_))waterFlow=true;
								if(this.isBlockLiquid(par1Entity.worldObj,x_-1, j, z_))waterFlow=true;
								if(this.isBlockLiquid(par1Entity.worldObj,x_, j, z_+1))waterFlow=true;
								if(this.isBlockLiquid(par1Entity.worldObj,x_, j, z_-1))waterFlow=true;
								if(this.isBlockLiquid(par1Entity.worldObj,x_, j+1, z_))waterFlow=true;

							}
							if(waterFlow){
								maxYToPlaceLadder=j;
							}

						}
					}
				}
			}

			//end
			if(waterFlow){
				//System.out.println("water");
				newPathPoint.onLadder=true;
				ForgeDirection dir_=ForgeDirection.UNKNOWN;
				AStarPathPoint p_=previous;
				while(true){
					if(p_.dirFromPrev!=ForgeDirection.UNKNOWN){
						dir_=p_.dirFromPrev;
					}
					if(p_.previous==null){
						break;
					}else{
						p_=p_.previous;
					}
				}
				if(previous.onLadder){
					{
						List<Coord> listTmp=new ArrayList();
						switch(dir_){
						case EAST:
						case WEST:
							listTmp.add(new Coord(x-2,maxYToPlaceLadder,z-1));
							listTmp.add(new Coord(x-2,maxYToPlaceLadder,z));
							listTmp.add(new Coord(x+1,maxYToPlaceLadder,z-1));
							listTmp.add(new Coord(x+1,maxYToPlaceLadder,z));
							break;
						case SOUTH:
						case NORTH:
							listTmp.add(new Coord(x-1,maxYToPlaceLadder,z-2));
							listTmp.add(new Coord(x-1,maxYToPlaceLadder,z+1));
							listTmp.add(new Coord(x,maxYToPlaceLadder,z-2));
							listTmp.add(new Coord(x,maxYToPlaceLadder,z+1));
							break;
						default:
							listTmp.add(new Coord(x-2,maxYToPlaceLadder,z-1));
							listTmp.add(new Coord(x-2,maxYToPlaceLadder,z));
							listTmp.add(new Coord(x+1,maxYToPlaceLadder,z-1));
							listTmp.add(new Coord(x+1,maxYToPlaceLadder,z));
							break;

						}
						List<Coord> coordsToPutBlock=new ArrayList();

						for(Coord c:listTmp){
							if(!this.isBlockRidable(par1Entity.worldObj,par1Entity, c.x, c.y, c.z,this.canSwim,this.entityWidth))n:{
								if(previous.blocksToPut!=null){
									for(Coord c1:previous.blocksToPut){
										if(c1!=null && c1.equals(c))break n;
									}
								}
								coordsToPutBlock.add(c);
							}
						}
						//Putting blocks shouldn't causes suffocation.

						AStarPathPoint p=previous;
						while(true){
							for(int i=0;i<coordsToPutBlock.size();i++){
								Coord c=coordsToPutBlock.get(i);
								if(p.xCoord-c.x<=0&&p.xCoord-c.x>=-1
										&&p.zCoord-c.z<=0&&p.zCoord-c.z>=-1
										&&p.yCoord<c.y&&p.yCoord+this.entityHeight>=c.y){
									coordsToPutBlock.remove(i);
									i--;
								}
							}
							if(p.previous==null){
								break;
							}else{
								p=p.previous;
							}
						}
						for(Coord c:coordsToPutBlock){
							//System.out.println("put(water):"+c);
							newPathPoint.addBlocksToPut(c);
						}
					}
					{
						List<Coord> listTmp=new ArrayList();

						listTmp.add(new Coord(x-1,maxYToPlaceLadder,z-1));
						listTmp.add(new Coord(x-1,maxYToPlaceLadder,z));
						listTmp.add(new Coord(x,maxYToPlaceLadder,z-1));
						listTmp.add(new Coord(x,maxYToPlaceLadder,z));

						List<Coord> coordsToPutLadder=new ArrayList();

						for(Coord c:listTmp){
							if(!Blocks.ladder.canPlaceBlockAt(par1Entity.worldObj, c.x,c.y,c.z))n:{
								if(previous.blocksToPut!=null){
									for(Coord c1:previous.blocksToPut){
										if(c1!=null && c1.equals(c))break n;
									}
								}
								coordsToPutLadder.add(c);
							}
						}
						//Putting blocks shouldn't causes suffocation.

						AStarPathPoint p=previous;
						while(true){
							for(int i=0;i<coordsToPutLadder.size();i++){
								Coord c=coordsToPutLadder.get(i);
								if(p.xCoord-c.x<=0&&p.xCoord-c.x>=-1
										&&p.zCoord-c.z<=0&&p.zCoord-c.z>=-1
										&&p.yCoord<c.y&&p.yCoord+this.entityHeight>=c.y){
									coordsToPutLadder.remove(i);
									i--;
								}
							}
							if(p.previous==null){
								break;
							}else{
								p=p.previous;
							}
						}

						for(Coord c:coordsToPutLadder){
							//System.out.println("put(ladder):"+c);
							newPathPoint.addLaddersToPut(c,dir_);
						}
					}
				}else{
					{
						List<Coord> listTmp=new ArrayList();
						switch(dir_){
						case EAST:
							for(int y_=y;y_<=maxYToPlaceLadder;y_++){
								listTmp.add(new Coord(x-2,y_,z-1));
								listTmp.add(new Coord(x-2,y_,z));
								listTmp.add(new Coord(x+1,y_,z-1));
								listTmp.add(new Coord(x+1,y_,z));
							}
							break;
						case WEST:
							for(int y_=y;y_<=maxYToPlaceLadder;y_++){
								listTmp.add(new Coord(x-2,y_,z-1));
								listTmp.add(new Coord(x-2,y_,z));
								listTmp.add(new Coord(x+1,y_,z-1));
								listTmp.add(new Coord(x+1,y_,z));
							}
							break;
						case SOUTH:
						case NORTH:
							for(int y_=y;y_<=maxYToPlaceLadder;y_++){
								listTmp.add(new Coord(x-1,y_,z-2));
								listTmp.add(new Coord(x-1,y_,z+1));
								listTmp.add(new Coord(x,y_,z-2));
								listTmp.add(new Coord(x,y_,z+1));
							}
							break;
						default:
							for(int y_=y;y_<=maxYToPlaceLadder;y_++){
								listTmp.add(new Coord(x-2,y_,z-1));
								listTmp.add(new Coord(x-2,y_,z));
								listTmp.add(new Coord(x+1,y_,z-1));
								listTmp.add(new Coord(x+1,y_,z));
							}
							break;

						}
						List<Coord> coordsToPutBlock=new ArrayList();

						for(Coord c:listTmp){
							if(!Blocks.ladder.canPlaceBlockAt(par1Entity.worldObj, c.x,c.y,c.z))n:{
								if(previous.blocksToPut!=null){
									for(Coord c1:previous.blocksToPut){
										if(c1!=null && c1.equals(c))break n;
									}
								}
								coordsToPutBlock.add(c);
							}
						}
						//Putting blocks shouldn't causes suffocation.

						AStarPathPoint p=previous;
						while(true){
							for(int i=0;i<coordsToPutBlock.size();i++){
								Coord c=coordsToPutBlock.get(i);
								if(p.xCoord-c.x<=0&&p.xCoord-c.x>=-1
										&&p.zCoord-c.z<=0&&p.zCoord-c.z>=-1
										&&p.yCoord<c.y&&p.yCoord+this.entityHeight>=c.y){
									coordsToPutBlock.remove(i);
									i--;
								}
							}
							if(p.previous==null){
								break;
							}else{
								p=p.previous;
							}
						}
						for(Coord c:coordsToPutBlock){
							//System.out.println("put(water):"+c);
							newPathPoint.addBlocksToPut(c);
						}
					}
					{
						List<Coord> listTmp=new ArrayList();

						for(int y_=y;y_<=maxYToPlaceLadder;y_++){
							listTmp.add(new Coord(x-1,y_,z-1));
							listTmp.add(new Coord(x-1,y_,z));
							listTmp.add(new Coord(x,y_,z-1));
							listTmp.add(new Coord(x,y_,z));
						}

						List<Coord> coordsToPutLadder=new ArrayList();

						for(Coord c:listTmp){
							if(!Blocks.ladder.canPlaceBlockAt(par1Entity.worldObj, c.x,c.y,c.z))n:{
								if(previous.blocksToPut!=null){
									for(Coord c1:previous.blocksToPut){
										if(c1!=null && c1.equals(c))break n;
									}
								}
								coordsToPutLadder.add(c);
							}
						}
						//Putting blocks shouldn't causes suffocation.
						/*
						AStarPathPoint p=previous;
						while(true){
							for(int i=0;i<coordsToPutLadder.size();i++){
								Coord c=coordsToPutLadder.get(i);
								if(p.xCoord-c.x<=0&&p.xCoord-c.x>=-1
										&&p.zCoord-c.z<=0&&p.zCoord-c.z>=-1
										&&p.yCoord<c.y&&p.yCoord+this.entityHeight>=c.y){
									coordsToPutLadder.remove(i);
									i--;
								}
							}
							if(p.previous==null){
								break;
							}else{
								p=p.previous;
							}
						}
						 */
						for(Coord c:coordsToPutLadder){
							//System.out.println("put(ladder):"+c);
							newPathPoint.addLaddersToPut(c,dir_);
						}
					}
				}
			}
			if(newPathPoint.onLadder){
				//System.out.println("Ladder");
			}
			if(isAbove && !newPathPoint.onLadder){
				//System.out.println("Deny ("+x+","+y+","+z+") NoLadder");
				return false;
			}
		}

		newPathPoint.totalRealCost=2+newPathPoint.previous.totalRealCost+this.getBlockCost(par1Entity,newPathPoint,end,size)+this.getEngineerCost(par1Entity,newPathPoint,end);
		if(newPathPoint.totalRealCost>this.maxCost){
			if(logAcceptOrDeny){
				System.out.println("Deny ("+x+","+y+","+z+") TooMuchCost");
			}
			return false;
		}
		//newPathPoint.totalHeuristicCost=newPathPoint.previous.totalHeuristicCost+getHeuristicCost(e,newPathPoint,end);
		newPathPoint.totalCost=newPathPoint.totalRealCost+getHeuristicCost(par1Entity,newPathPoint,end);
		newPathPoint.tickToNext=this.getPlannedTickToNext(par1Entity, previous, newPathPoint);
		// if(this.ai instanceof EntityAIBreakBlock)
		//System.out.println("opened:"+x+","+y+","+z+" as cost:"+newPathPoint.totalCost+" as time:"+newPathPoint.tickToNext);
		int l = AStarPathPoint.makeHash(x, y, z);
		AStarPathPoint a=(AStarPathPoint)this.pointMap.lookup(l);
		if(a==null){
			this.pointMap.addKey(l, newPathPoint);
			this.openedPointList.add(newPathPoint);
		}else if(a.totalCost>newPathPoint.totalCost){
			a=newPathPoint;
			this.openedPointList.add(newPathPoint);
			this.pointMap.addKey(l, newPathPoint);
		}else{
			if(logAcceptOrDeny){
				System.out.println("Deny ("+x+","+y+","+z+") Useless cost:"+newPathPoint.totalCost);
			}
			return false;
		}
		if(logAcceptOrDeny){
			System.out.println("Accept ("+x+","+y+","+z+") cost:"+newPathPoint.totalCost);
		}
		return true;
	}

	public int getEngineerCost(Entity par1Entity,AStarPathPoint p,AStarPathPoint end){
		if(p.numberOfBlocksToBreak==0&&p.numberOfBlocksToPut==0)return 0;
		if(!(par1Entity instanceof EntityRegularArmy))return 0;
		EntityRegularArmy entity=(EntityRegularArmy)par1Entity;
		int cost=20;

		boolean flag=true;
		if(p.blocksToBreak!=null){
			for(int i=0;i<p.numberOfBlocksToBreak;i++){
				Coord c=p.blocksToBreak[i];
				Block id=this.worldMap.getBlock(c.x, c.y, c.z);
				if(id!=Blocks.air){
					flag=false;
					cost+=5+id.getBlockHardness(par1Entity.worldObj, c.x, c.y, c.z)*2;
					if(p.laddersToPut==null){
						if(this.isBlockLiquid(par1Entity.worldObj,c.x, c.y, c.z))cost+=60;
						if(this.isBlockLiquid(par1Entity.worldObj,c.x+1, c.y, c.z))cost+=60;
						if(this.isBlockLiquid(par1Entity.worldObj,c.x-1, c.y, c.z))cost+=60;
						if(this.isBlockLiquid(par1Entity.worldObj,c.x, c.y, c.z+1))cost+=60;
						if(this.isBlockLiquid(par1Entity.worldObj,c.x, c.y, c.z-1))cost+=60;
						if(this.isBlockLiquid(par1Entity.worldObj,c.x, c.y+1, c.z))cost+=60;
					}
				}
			}
		}
		if(p.blocksToPut!=null){
			for(int i=0;i<p.numberOfBlocksToPut;i++){
				Coord c=p.blocksToPut[i];
				//Block id=this.worldMap.getBlock(c.x, c.y, c.z);
				flag=false;
				cost+=4;

			}
		}

		if(p.beforeRequested==0){
			if(p.previous.beforeRequested>16&&flag&&!this.lowerEngineerCost){
				cost+=50;
			}
		}
		return cost;
	}


	public int getBlockCost(Entity par1Entity,AStarPathPoint p,AStarPathPoint end,int size){
		int cost=0;
		if(p.previous.yCoord-p.yCoord>=3)cost+=(p.previous.yCoord-p.yCoord-1)*16;
		if(p.previous.yCoord-p.yCoord<=-1)cost+=2;
		if(size==1){
			Block b=this.worldMap.getBlock(p.xCoord,p.yCoord,p.zCoord);
			//if(!isBlockRidable(e,p.xCoord,p.yCoord-1,p.zCoord))cost+= Block.blocksList[this.worldMap.getBlockId(p.xCoord,p.yCoord-1,p.zCoord)] instanceof BlockFluid? 4:0;
			//if(isBlockRidable(e,p.xCoord,p.yCoord,p.zCoord))cost+=b.getBlockHardness(e.worldObj,p.xCoord,p.yCoord,p.zCoord)/2+3;
			if(b instanceof BlockFluidBase ||b instanceof BlockLiquid)cost+=30;
			int i=0;
			boolean flag=true;
			switch(p.index%8){
			case 0:
				for(;i<15;i++){
					if(this.isBlockRidable(par1Entity.worldObj,par1Entity, p.xCoord+1, p.yCoord-i+1, p.zCoord,this.canSwim,this.entityWidth)){
						break;
					}
				}
				break;
			case 2:
				for(;i<15;i++){
					if(this.isBlockRidable(par1Entity.worldObj,par1Entity, p.xCoord, p.yCoord-i+1, p.zCoord+1,this.canSwim,this.entityWidth)){
						break;
					}
				}
				break;
			case 4:
				for(;i<15;i++){
					if(this.isBlockRidable(par1Entity.worldObj,par1Entity, p.xCoord-1, p.yCoord-i+1, p.zCoord,this.canSwim,this.entityWidth)){
						break;
					}
				}
				break;
			case 6:
				for(;i<15;i++){
					if(this.isBlockRidable(par1Entity.worldObj,par1Entity, p.xCoord, p.yCoord-i+1, p.zCoord-1,this.canSwim,this.entityWidth)){
						break;
					}
				}
				break;
			default:
				flag=false;
			}
			cost+=this.lowerEngineerCost?(i<3?0:i*2):(i<3?0:i*1);
			//if(flag)System.out.println(p.toCoord().toString()+"'s height cost is "+(i<3?0:i*4));
			if(this.settings!=null){
				int[] ids=new int[5*4];
				int num=0;
				for(int y=p.yCoord-1;y<p.yCoord+3;y++){
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord, y, p.zCoord));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord+1, y, p.zCoord));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord, y, p.zCoord+1));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord-1, y, p.zCoord));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord, y, p.zCoord-1));
				}
			}
		}else{
			for(int x=p.xCoord;x>=p.xCoord-1;x--){
				for(int z=p.zCoord;z>=p.zCoord-1;z--){
					Block b=this.worldMap.getBlock(x,p.yCoord,z);
					if(b instanceof BlockFluidBase ||b instanceof BlockLiquid)cost+=30;
					if(settings==null){
						if(b==Blocks.lava||b==Blocks.flowing_lava)cost+=500;
						Block id=this.worldMap.getBlock(x,p.yCoord-1,z);
						if(id==Blocks.cactus){
							cost+=40;
						}else if(id==Blocks.lava||id==Blocks.flowing_lava){
							cost+=500;
						}
					}
				}
			}

			//if(!isBlockRidable(e,p.xCoord,p.yCoord-1,p.zCoord))cost+= Block.blocksList[this.worldMap.getBlockId(p.xCoord,p.yCoord-1,p.zCoord)] instanceof BlockFluid? 4:0;
			//if(isBlockRidable(e,p.xCoord,p.yCoord,p.zCoord))cost+=b.getBlockHardness(e.worldObj,p.xCoord,p.yCoord,p.zCoord)/2+3;
			int i=0;
			boolean flag=true;
			switch(p.index%8){
			case 0:
				for(;i<15;i++){
					if(this.isBlockRidable(par1Entity.worldObj,par1Entity, p.xCoord+1, p.yCoord-i+1, p.zCoord,this.canSwim,this.entityWidth)){
						break;
					}
				}
				break;
			case 2:
				for(;i<15;i++){
					if(this.isBlockRidable(par1Entity.worldObj,par1Entity, p.xCoord, p.yCoord-i+1, p.zCoord+1,this.canSwim,this.entityWidth)){
						break;
					}
				}
				break;
			case 4:
				for(;i<15;i++){
					if(this.isBlockRidable(par1Entity.worldObj,par1Entity, p.xCoord-1, p.yCoord-i+1, p.zCoord,this.canSwim,this.entityWidth)){
						break;
					}
				}
				break;
			case 6:
				for(;i<15;i++){
					if(this.isBlockRidable(par1Entity.worldObj,par1Entity, p.xCoord, p.yCoord-i+1, p.zCoord-1,this.canSwim,this.entityWidth)){
						break;
					}
				}
				break;
			default:
				flag=false;
			}
			cost+=this.lowerEngineerCost?(i<3?0:i*6):(i<3?0:i*3);
			//if(flag)System.out.println(p.toCoord().toString()+"'s height cost is "+(i<3?0:i*4));
			if(this.settings!=null){
				int[] ids=new int[12*4];
				int num=0;
				for(int y=p.yCoord-1;y<p.yCoord+3;y++){
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord, y, p.zCoord));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord-1, y, p.zCoord));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord, y, p.zCoord-1));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord-1, y, p.zCoord-1));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord+1, y, p.zCoord));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord+1, y, p.zCoord-1));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord-2, y, p.zCoord));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord-2, y, p.zCoord-1));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord, y, p.zCoord+1));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord-1, y, p.zCoord+1));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord, y, p.zCoord-2));
					ids[num++]=Block.getIdFromBlock(this.worldMap.getBlock(p.xCoord-1, y, p.zCoord-2));
				}

				cost+=this.settings.getTotalBlocksCost(ids);
			}
		}
		if(this.settings!=null){
			if(p.index%4==0){
				Integer num1=(Integer) this.settings.chunkCost.get((((p.xCoord/16)&0x7ff+(p.xCoord<0?0x800:0))<<20)+(((p.yCoord/16)&0xff)<<12)+(p.zCoord/16)&0x7ff+(p.zCoord<0?0x800:0));
				cost+=(num1==null||num1<0?0:num1*2);

			}
			float num2=0;
			int miniChunkHash=p.makeMiniChunkHash();
			if(this.usedCrowdCost.containsKey(miniChunkHash)){
				num2=this.usedCrowdCost.get(miniChunkHash);
			}else{
				if(this.settings.crowdCost.containsKey(miniChunkHash)){
					num2=this.settings.crowdCost.get(miniChunkHash);
					this.usedCrowdCost.put(miniChunkHash, num2);
				}
			}
			cost+=num2*this.ai.getCrowdCost();
		}
		return cost;
	}

	public int getHeuristicCost(Entity par1Entity,AStarPathPoint p,AStarPathPoint end){
		int d1=p.xCoord-end.xCoord;
		int d2=p.zCoord-end.zCoord;
		int d3=p.yCoord-end.yCoord;
		if(this.canUseEngineer){
			int cost=0;
			if((p.totalCost-p.totalRealCost)>(p.previous.totalCost-p.previous.totalRealCost)){
				cost+=p.numberOfBlocksToPut*4;
				cost+=p.numberOfBlocksToBreak*4;
			}
			return (int) (MathHelper.sqrt_float(d1*d1+d2*d2+d3*d3))*10+cost;
		}else{
			return (int) (MathHelper.sqrt_float(d1*d1+d2*d2+d3*d3))*5;
		}

	}

	public int getPlannedTickToNext(Entity par1Entity,AStarPathPoint prev,AStarPathPoint current){
		int tickPrev=this.baseTickToNext;
		for(int i=prev.yCoord-1;i<prev.yCoord+MathHelper.floor_float(this.entityHeight);i++){
			if(this.worldMap.getBlock(prev.xCoord, i, prev.zCoord).getMaterial().isLiquid()){
				tickPrev*=2;
				break;
			}
		}
		int tickNext=this.baseTickToNext;
		for(int i=current.yCoord-1;i<current.yCoord+MathHelper.floor_float(this.entityHeight);i++){
			if(this.worldMap.getBlock(current.xCoord, i, current.zCoord).getMaterial().isLiquid()){
				tickNext*=2;
				break;
			}
		}
		if(prev.yCoord>current.yCoord){
			tickPrev+=(prev.yCoord-current.yCoord)*8;
		}else if(prev.yCoord<current.yCoord){
			tickPrev+=(current.yCoord-prev.yCoord)*4;
		}
		if(par1Entity instanceof IBreakBlocksMob){
			for(int i=0;i<current.numberOfBlocksToBreak;i++){
				Coord c=current.blocksToBreak[i];
				tickNext+=10+32*((IBreakBlocksMob)par1Entity).getblockStrength(this.worldMap.getBlock(c.x,c.y,c.z), par1Entity.worldObj, c.x,c.y,c.z);

			}
		}
		tickNext+=current.numberOfBlocksToPut*30;


		return tickPrev+tickNext+20;
	}

	public void setSetting(FinderSettings f){
		this.settings=f;
	}

	/**
	 * Returns a new PathEntity for a given start and end point
	 */
	private AStarPathEntity createEntityPath(AStarPathPoint par1AStarPathPoint, AStarPathPoint par2AStarPathPoint,boolean isArrival,int entitySize,Entity entity)
	{
		int i = 1;
		AStarPathPoint AStarPathPoint2;

		for (AStarPathPoint2 = par2AStarPathPoint; AStarPathPoint2.previous != null; AStarPathPoint2 = AStarPathPoint2.previous)
		{
			++i;
		}

		AStarPathPoint[] aAStarPathPoint = new AStarPathPoint[i];
		AStarPathPoint2 = par2AStarPathPoint;
		--i;

		for (aAStarPathPoint[i] = par2AStarPathPoint; AStarPathPoint2.previous != null; aAStarPathPoint[i] = AStarPathPoint2)
		{
			removeUnnecessaryRequests(AStarPathPoint2,entity);
			AStarPathPoint2 = AStarPathPoint2.previous;
			--i;
		}

		return new AStarPathEntity(aAStarPathPoint,this.ai,isArrival,entitySize);
	}

	public void removeUnnecessaryRequests(AStarPathPoint newPathPoint,Entity theEntity){
		if(newPathPoint.onLadder)return;
		boolean canUseEngineer=this.canUseEngineer;
		if(!canUseEngineer)return;
		if(newPathPoint.previous==null || newPathPoint.previous.blocksToPut==null)return;
		int x=newPathPoint.xCoord;
		int y=newPathPoint.yCoord;
		int z=newPathPoint.zCoord;	
		AStarPathPoint previous=newPathPoint.previous;

		Entity follower=theEntity;

		if(this.entitySize==1){
			boolean putBlockUnder=false;

			float maxY=Math.max(newPathPoint.yOffset+((float)newPathPoint.yCoord),
					previous.yOffset+((float)previous.yCoord))+follower.height;
			for(int j=y;j<maxY;j++){

				for(int k=0;k<newPathPoint.previous.blocksToPut.length;k++){
					Coord c=newPathPoint.previous.blocksToPut[k];
					if(c!=null &&c.equals(new Coord(x,j,z))){
						for(int l=k+1;l<newPathPoint.previous.numberOfBlocksToPut;l++){
							newPathPoint.previous.blocksToPut[l-1]=newPathPoint.previous.blocksToPut[l];
							newPathPoint.previous.numberOfBlocksToPut--;
							if(logRemoveUnne){
								System.out.println(c.toString()+"is unnecessary");
							}
						}
					}
				}

			}

		}else{
			boolean putBlockUnder=false;
			boolean ridableFlag=false;
			int offY=newPathPoint.yOffset<0?-1:0;

			//These commands decides which blocks should be broken.
			double underMaxY=AStarPathFinder.getMaxY(follower.worldObj,follower,  x, y-1, z,2);
			//AxisAlignedBB aabbUnder=AStarPathFinder.getAABB(e.worldObj,e, x, y-1, z);


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
						//System.out.println("not passable");

						for(int k=0;k<newPathPoint.previous.blocksToPut.length;k++){
							Coord c=newPathPoint.previous.blocksToPut[k];
							
							if(c!=null && c.equals(new Coord(x_,j,z_))){
								for(int l=k+1;l<newPathPoint.previous.numberOfBlocksToPut;l++){
									newPathPoint.previous.blocksToPut[l-1]=newPathPoint.previous.blocksToPut[l];
									newPathPoint.previous.numberOfBlocksToPut--;
									//System.out.println(c.toString()+"is unnecessary");
								}
							}
						}

					}
				}
			}
			for(int x_=previous.xCoord;x_>=x-1;x_--){
				for(int z_=previous.zCoord;z_>=z-1;z_--){
					for(int j=MathHelper.ceiling_float_int(previous.yCoord+follower.height);j<maxY;j++){
						//System.out.println("breakP:"+new Coord(x_,j,z_));
						for(int k=0;k<newPathPoint.previous.blocksToPut.length;k++){
							Coord c=newPathPoint.previous.blocksToPut[k];
							if(c!=null &&c.equals(new Coord(x_,j,z_))){
								for(int l=k+1;l<newPathPoint.previous.numberOfBlocksToPut;l++){
									newPathPoint.previous.blocksToPut[l-1]=newPathPoint.previous.blocksToPut[l];
									newPathPoint.previous.numberOfBlocksToPut--;
									//System.out.println(c.toString()+"is unnecessary");
								}
							}
						}

					}
				}
			}
		}
	}

	public static boolean isBlockRidable(World w,Entity par1Entity,int x,int y,int z,boolean canSwim,float width){
		Block id=w.getBlock(x, y,z);
		Block b=w.getBlock(x, y+1,z);
		if(par1Entity instanceof EntityLivingBase){
			if(b.isLadder(w, x, y, z, (EntityLivingBase)par1Entity))return true;
		}
		if(id==Blocks.air)return false;
		else{
			if(id.isOpaqueCube())return true;

			if((b instanceof BlockFluidBase || b instanceof BlockLiquid))return true;
			if(isBlockPassable(par1Entity.worldObj,par1Entity, x, y, z))return false;

			AxisAlignedBB aabb=id.getCollisionBoundingBoxFromPool(par1Entity.worldObj, x, y, z);
			//if(aabb==null)return false;

			if(aabb.maxX+width/2<x+1||aabb.minX-width/2>x||aabb.maxZ+width/2<z+1||aabb.minZ-width/2>z)return false;
			return true;
		}
	}

	public static boolean isBlockPassable(World w,Entity par1Entity,int x,int y,int z){
		Block id=w.getBlock(x, y,z);
		if(id==Blocks.air)return true;
		else if(id instanceof BlockFluidBase || id instanceof BlockLiquid)return true;
		else if(id.getCollisionBoundingBoxFromPool(par1Entity.worldObj, x, y, z)==null)return true;
		return false;   
	}

	public static boolean isBlockLiquid(World w,int x,int y,int z){
		Block id=w.getBlock(x, y,z);
		if(id instanceof BlockFluidBase || id instanceof BlockLiquid)return true;
		return false;
	}

	public static double getMaxY(World w,Entity e,int x,int y,int z,int size){
		if(size==1){
			Block id=w.getBlock(x, y, z);
			Block idAbove=w.getBlock(x, y+1, z);
			if((e instanceof EntityLivingBase && idAbove.isLadder(w, x, y+1, z, (EntityLivingBase)e))){
				return y+1;
			}
			if(id==Blocks.air){
				return Double.NaN;
			}
			if(id.isOpaqueCube()){
				return y+1;
			}

			if(id instanceof BlockFluidBase||id instanceof BlockLiquid){
				//System.out.println("point"+newPathPoint.toCoord().toString()+"is fluid");
				return y+0.2;
			}
			if((e instanceof EntityLivingBase && id.isLadder(w, x, y, z, (EntityLivingBase)e))){
				return y+1;
			}
			List<AxisAlignedBB> list=new ArrayList();
			id.addCollisionBoxesToList(w, x, y, z, AxisAlignedBB.getBoundingBox(x, y, z, x+1, y+1, z+1), list, e);
			if(list.isEmpty()){
				return y;
			}
			double maxY=list.get(0).maxY;
			double maxYNow;
			for(int i=1;i<list.size();i++){
				maxYNow=list.get(i).maxY;
				if(maxYNow>maxY){
					maxY=maxYNow;
				}
			}
			//System.out.println("maxY at "+x+","+y+","+z+" is "+maxY);
			return maxY;
		}else{
			double maxHeight=Double.NaN;

			for(int i=0;i<4;i++){
				double height;
				//Coord c=null;
				n:{
					Block id=null;
					switch(i){
					case 0:
						id= w.getBlock(x, y, z);
						//c=new Coord(x, y, z);
						break;
					case 1:
						id= w.getBlock(x-1, y, z);
						//c=new Coord(x-1, y, z);
						break;
					case 2:
						id= w.getBlock(x, y, z-1);
						//c=new Coord(x, y, z-1);
						break;
					case 3:
						id= w.getBlock(x-1, y, z-1);
						//c=new Coord(x-1, y, z-1);
						break;
					}

					if(id==Blocks.air){
						height=Double.NaN;
						break n;
					}
					if(id.isOpaqueCube()){
						height=y+1;
						break n;
					}

					if(id instanceof BlockFluidBase||id instanceof BlockLiquid){
						//System.out.println("point"+newPathPoint.toCoord().toString()+"'s under is fluid");
						height= y+0.3f;
						break n;
					}
					List<AxisAlignedBB> list=new ArrayList();
					id.addCollisionBoxesToList(w, x, y, z, AxisAlignedBB.getBoundingBox(x, y, z, x+1, y+1, z+1), list, e);
					if(list.isEmpty()){
						height=y;
						break n;
					}
					double maxY=list.get(0).maxY;
					double maxYNow;
					for(int j=1;j<list.size();j++){
						maxYNow=list.get(j).maxY;
						if(maxYNow>maxY){
							maxY=maxYNow;
						}
					}
					height=maxY;
					break n;
				}
				//System.out.println("height at "+c+" is "+height);
				if(Double.isNaN(maxHeight) || maxHeight<height){
					maxHeight=height;
				}
			}
			//System.out.println("maxY at "+x+","+y+","+z+" is "+maxY);
			return maxHeight;
		}

	}

	public static double getMinY(World w,Entity e,int x,int y,int z,int size){
		if(size==1){
			Block id=w.getBlock(x, y, z);
			if(id==Blocks.air){
				return Double.NaN;
			}
			if(id.isOpaqueCube()){
				return y;
			}
			List<AxisAlignedBB> list=new ArrayList();
			id.addCollisionBoxesToList(w, x, y, z, AxisAlignedBB.getBoundingBox(x, y, z, x+1, y+1, z+1), list, e);
			if(list.isEmpty()){
				return y-1;
			}
			double minY=list.get(0).minY;
			double minYNow;
			for(int i=1;i<list.size();i++){
				minYNow=list.get(i).minY;
				if(minYNow>minY){
					minY=minYNow;
				}
			}
			//System.out.println("maxY at "+x+","+y+","+z+" is "+maxY);
			return minY;
		}else{
			double maxHeight=Double.NaN;

			for(int i=0;i<4;i++){
				double height;
				n:{
					Block id=null;
					switch(i){
					case 0:
						id= w.getBlock(x, y, z);
						break;
					case 1:
						id= w.getBlock(x-1, y, z);
						break;
					case 2:
						id= w.getBlock(x, y, z-1);
						break;
					case 3:
						id= w.getBlock(x-1, y, z-1);
						break;
					}
					if(id==Blocks.air){
						height=Double.NaN;
						break n;
					}
					if(id.isOpaqueCube()){
						height= y;
						break n;
					}
					List<AxisAlignedBB> list=new ArrayList();
					id.addCollisionBoxesToList(w, x, y, z, AxisAlignedBB.getBoundingBox(x, y, z, x+1, y+1, z+1), list, e);
					if(list.isEmpty()){
						height= y-1;
						break n;
					}
					double minY=list.get(0).minY;
					double minYNow;
					for(int j=1;j<list.size();j++){
						minYNow=list.get(j).minY;
						if(minYNow>minY){
							minY=minYNow;
						}
					}
					height=minY;
					break n;
				}
				if(Double.isNaN(maxHeight)|| maxHeight<height){
					maxHeight=height;
				}
			}
			//System.out.println("maxY at "+x+","+y+","+z+" is "+maxY);
			return maxHeight;
		}
	}

	private AxisAlignedBB getAABB(World w,Entity e,int x,int y,int z){
		Block id=this.worldMap.getBlock(x, y, z);
		if(id==Blocks.air)return null;
		if(id.isOpaqueCube()){
			return AxisAlignedBB.getBoundingBox(x, y, z, x+1, y+1, z+1);
		}
		List<AxisAlignedBB> list=new ArrayList();
		id.addCollisionBoxesToList(w, x, y, z, AxisAlignedBB.getBoundingBox(x, y, z, x+1, y+1, z+1), list, e);
		if(list.isEmpty())return null;
		AxisAlignedBB aabb=list.get(0);
		for(int i=1;i<list.size();i++){
			AxisAlignedBB aabb1=list.get(i);
			if(aabb1.minX<aabb.minX)aabb.minX=aabb1.minX;
			if(aabb1.minY<aabb.minY)aabb.minY=aabb1.minY;
			if(aabb1.minZ<aabb.minZ)aabb.minZ=aabb1.minZ;
			if(aabb1.maxX>aabb.maxX)aabb.maxX=aabb1.maxX;
			if(aabb1.maxY>aabb.maxY)aabb.maxY=aabb1.maxY;
			if(aabb1.maxZ>aabb.maxZ)aabb.maxZ=aabb1.maxZ;
		}

		return aabb;
	}
}
