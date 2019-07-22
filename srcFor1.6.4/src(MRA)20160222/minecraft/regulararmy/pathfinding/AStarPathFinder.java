package regulararmy.pathfinding;

import java.util.ArrayList;
import java.util.List;

import regulararmy.analysis.FinderSettings;
import regulararmy.core.Coord;
import regulararmy.entity.EntityEngineer;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.entity.ai.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.pathfinding.*;

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
    private boolean isPathingInWater;

    /** tells the FathFinder to not stop pathing underwater */
    private boolean canEntityDrown;
    
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

    public AStarPathFinder(IBlockAccess par1IBlockAccess, boolean par2, boolean par3, boolean par4, boolean par5,boolean lowerEngineerCost,float par6,IPathFindRequester ai)
    {
        this.worldMap = par1IBlockAccess;
        this.isWoddenDoorAllowed = par2;
        this.isMovementBlockAllowed = par3;
        this.isPathingInWater = par4;
        this.canEntityDrown = par5;
        this.jumpheight=par6;
        this.lowerEngineerCost=lowerEngineerCost;
        
        this.ai=ai;
    }

    /**
     * Creates a path from one entity to another within a minimum distance
     */
    public AStarPathEntity createEntityPathTo(EntityRegularArmy par1Entity, Entity par2Entity, float par3,float par4)
    {
        return this.createEntityPathTo(par1Entity, par2Entity.posX, par2Entity.boundingBox.minY, par2Entity.posZ, par3,par4);
    }

    /**
     * Creates a path from an entity to a specified location within a minimum distance
     */
    public AStarPathEntity createEntityPathTo(EntityRegularArmy par1Entity, int par2, int par3, int par4, float par5,float par6)
    {
        return this.createEntityPathTo(par1Entity, (double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), par5,par6);
    }

    /**
     * Internal implementation of creating a path from an entity to a point
     */
    private AStarPathEntity createEntityPathTo(EntityRegularArmy par1Entity, double par2, double par4, double par6, float par8,float par9)
    {
    	if(!this.canUseEngineer&&!this.isBlockPassable(par1Entity,  MathHelper.floor_double(par2 - (double)(par1Entity.width / 2.0F)), MathHelper.floor_double(par4), MathHelper.floor_double(par6 - (double)(par1Entity.width / 2.0F)))){
    		return null;
    	}
    	if(!this.canUseEngineer&&!this.isBlockRidable(par1Entity,  MathHelper.floor_double(par2 - (double)(par1Entity.width / 2.0F)), MathHelper.floor_double(par4)-1, MathHelper.floor_double(par6 - (double)(par1Entity.width / 2.0F)))){
    		return null;
    	}
    	this.openedPointList=new ArrayList((int) (par8*32));
        this.pointMap.clearMap();
        boolean flag = this.isPathingInWater;
        int i = MathHelper.floor_double(par1Entity.boundingBox.minY + 0.5D);

        if (this.canEntityDrown && par1Entity.isInWater())
        {
            i = (int)par1Entity.boundingBox.minY;

            for (int j = this.worldMap.getBlockId(MathHelper.floor_double(par1Entity.posX), i, MathHelper.floor_double(par1Entity.posZ)); j == Block.waterMoving.blockID || j == Block.waterStill.blockID; j = this.worldMap.getBlockId(MathHelper.floor_double(par1Entity.posX), i, MathHelper.floor_double(par1Entity.posZ)))
            {
                ++i;
            }

            flag = this.isPathingInWater;
            this.isPathingInWater = false;
        }
        else
        {
            i = MathHelper.floor_double(par1Entity.boundingBox.minY + 0.5D);
        }
        System.out.println("from"+par1Entity.posX+","+par1Entity.posY+","+par1Entity.posZ);
        System.out.println("for"+par2+","+par4+","+par6);
        AStarPathPoint AStarPathPoint = new AStarPathPoint(MathHelper.floor_double(par1Entity.boundingBox.minX), i, MathHelper.floor_double(par1Entity.boundingBox.minZ));
        AStarPathPoint AStarPathPoint1 = new AStarPathPoint(MathHelper.floor_double(par2 - (double)(par1Entity.width / 2.0F)), MathHelper.floor_double(par4), MathHelper.floor_double(par6 - (double)(par1Entity.width / 2.0F)));
        AStarPathPoint AStarPathPoint2 = new AStarPathPoint(MathHelper.floor_float(par1Entity.width + 1.0F), MathHelper.floor_float(par1Entity.height + 1.0F), MathHelper.floor_float(par1Entity.width + 1.0F));
        AStarPathEntity pathentity = this.addToPath(par1Entity, AStarPathPoint, AStarPathPoint1, AStarPathPoint2, par8,par9);
        this.isPathingInWater = flag;
        /*
        boolean flag1=false;
        for(int j=0;j<pathentity.points.length;j++){
        	if(pathentity.points[j].blocksToBreak.length!=0){
        		flag1=true;
        		break;
        	}
        }
        */
        
        if(pathentity!=null&&!(par1Entity instanceof EntityEngineer)){
        	for(int i1=0;i1<pathentity.getCurrentPathLength();i1++){
        		System.out.println("¨"+pathentity.getPathPointFromIndex(i1).xCoord+","+pathentity.getPathPointFromIndex(i1).yCoord+","+pathentity.getPathPointFromIndex(i1).zCoord);
        	}
        	System.out.println(pathentity.getTotalCost());
        }
        
        return pathentity;
    }

    /**
     * Adds a path from start to end and returns the whole path (args: unused, start, end, unused, maxDistance)
     */
    private AStarPathEntity addToPath(EntityRegularArmy par1Entity, AStarPathPoint startPathPoint, AStarPathPoint endPathPoint, AStarPathPoint par4AStarPathPoint, float par5,float par6)
    {
        startPathPoint.totalCost = 0;
        startPathPoint.distanceToNext = startPathPoint.func_75832_b(endPathPoint);
        startPathPoint.distanceToTarget = startPathPoint.distanceToNext;
        AStarPathPoint headPathPoint=startPathPoint;
        AStarPathPoint nearestPathPoint=startPathPoint;
        this.openedPointList.add(headPathPoint);
        headPathPoint.index=0;
        headPathPoint.isHead=true;
        AxisAlignedBB aabbUnder=this.getAABB(par1Entity.worldObj, startPathPoint.xCoord,startPathPoint.yCoord-1,startPathPoint.zCoord);
        if(Block.blocksList[this.worldMap.getBlockId(startPathPoint.xCoord,startPathPoint.yCoord,startPathPoint.zCoord)] instanceof BlockFluid){
        	//System.out.println("point"+newPathPoint.toCoord().toString()+"is fluid");
        	startPathPoint.yOffset=0.3f;
        }else if(Block.blocksList[this.worldMap.getBlockId(startPathPoint.xCoord,startPathPoint.yCoord-1,startPathPoint.zCoord)] instanceof BlockFluid){
        	//System.out.println("point"+newPathPoint.toCoord().toString()+"'s under is fluid");
        	startPathPoint.yOffset=-0.7f;
        }else if(aabbUnder==null){
        	//System.out.println("point"+newPathPoint.toCoord().toString()+"is air");
        	startPathPoint.yOffset=-1;
        
        }else{
        	//System.out.println("point"+newPathPoint.toCoord().toString()+"is solid("+(aabbUnder.maxY-(double)y)+")");
        	startPathPoint.yOffset=(float)aabbUnder.maxY-(float)startPathPoint.yCoord;
        }

        for(int i=0;i<par5*8;i++)
        {
            if (headPathPoint.equals(endPathPoint))
            {
                return this.createEntityPath(startPathPoint, headPathPoint);
            }
            {
            	float f = (float)(endPathPoint.xCoord - headPathPoint.xCoord);
                float f1 = (float)(endPathPoint.yCoord - headPathPoint.yCoord);
                float f2 = (float)(endPathPoint.zCoord - headPathPoint.zCoord);
                if(f * f + f1 * f1 + f2 * f2<par6*par6||i>this.maxLength){
                	return this.createEntityPath(startPathPoint, headPathPoint);
                }
            }
            if(this.canUseEngineer&&headPathPoint.func_75832_b(endPathPoint)>startPathPoint.distanceToTarget+100){
            	this.openedPointList.remove(headPathPoint);
            }else{
            	this.openPathAround(par1Entity, headPathPoint, par4AStarPathPoint, endPathPoint, par5);
            }
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

            return this.createEntityPath(startPathPoint, nearestPathPoint);
    }

    /**
     * populates pathOptions with available points and returns the number of options found (args: unused1, currentPoint,
     * unused2, targetPoint, maxDistance)
     */
    private void openPathAround(EntityRegularArmy par1Entity, AStarPathPoint currentPoint, AStarPathPoint par3AStarPathPoint, AStarPathPoint endPoint, float par5)
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
    	this.openPointSide(par1Entity, currentPoint, endPoint, currentPoint.xCoord, MathHelper.ceiling_double_int(maxheight), currentPoint.zCoord, (float) maxheight);
    	this.openPointSide(par1Entity, currentPoint, endPoint, currentPoint.xCoord+1, MathHelper.ceiling_double_int(maxheight), currentPoint.zCoord, (float) maxheight);
    	this.openPointSide(par1Entity, currentPoint, endPoint, currentPoint.xCoord, MathHelper.ceiling_double_int(maxheight), currentPoint.zCoord+1, (float) maxheight);
    	this.openPointSide(par1Entity, currentPoint, endPoint, currentPoint.xCoord-1, MathHelper.ceiling_double_int(maxheight), currentPoint.zCoord, (float) maxheight);
    	this.openPointSide(par1Entity, currentPoint, endPoint, currentPoint.xCoord, MathHelper.ceiling_double_int(maxheight), currentPoint.zCoord-1, (float) maxheight);
    	//this.openPointDiagonal(par1Entity, currentPoint, endPoint, currentPoint.xCoord+1,MathHelper.floor_float(maxheight), currentPoint.zCoord+1, maxheight);
    	//this.openPointDiagonal(par1Entity, currentPoint, endPoint, currentPoint.xCoord+1,MathHelper.floor_float(maxheight), currentPoint.zCoord-1, maxheight);
    	//this.openPointDiagonal(par1Entity, currentPoint, endPoint, currentPoint.xCoord-1,MathHelper.floor_float(maxheight), currentPoint.zCoord-1, maxheight);
    	//this.openPointDiagonal(par1Entity, currentPoint, endPoint, currentPoint.xCoord-1,MathHelper.floor_float(maxheight), currentPoint.zCoord+1, maxheight);
    	this.openedPointList.remove(currentPoint);
    }

    
    private void openPointSide(EntityRegularArmy par1Entity,AStarPathPoint currentPoint,AStarPathPoint endPoint,int x,int y,int z,float maxheight){
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
    	for(y=y;y>=1;y--){
    		AxisAlignedBB aabb=this.getAABB(par1Entity.worldObj, x, y-1, z);
    		if((aabb==null?y:aabb.maxY)<=maxheight){
    			openPoint(par1Entity,currentPoint,endPoint,x,y,z);
    		}
    		if(y<=currentPoint.yCoord-1&&isBlockRidable(par1Entity,x,y,z))break;
    	}
    }
    
    
    
    public void openPointDiagonal(EntityRegularArmy par1Entity,AStarPathPoint currentPoint,AStarPathPoint endPoint,int x,int y,int z,float maxheight){
    	while(y>=1){
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
   					if(isBlockRidable(par1Entity,currentPoint.xCoord,j,z)
   							&&Block.blocksList[this.worldMap.getBlockId(currentPoint.xCoord, j, z)].getCollisionBoundingBoxFromPool(par1Entity.worldObj, currentPoint.xCoord, j, z).minY
   							<y+par1Entity.height){
   						flag=false;
   						break;
   					}
   					if(isBlockRidable(par1Entity,x,j,currentPoint.zCoord)
   							&&Block.blocksList[this.worldMap.getBlockId(x, j, currentPoint.zCoord)].getCollisionBoundingBoxFromPool(par1Entity.worldObj, x, j, currentPoint.zCoord).minY
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
    	}
    }

    /**
     * Returns a mapped point or creates and adds one
     */
    private boolean openPoint(EntityRegularArmy e,AStarPathPoint previous,AStarPathPoint end,int x, int y, int z)
    {
        int l = AStarPathPoint.makeHash(x, y, z);
        AStarPathPoint newPathPoint = null;
        
        //System.out.println("opened:"+x+","+y+","+z);
        
        newPathPoint = new AStarPathPoint(x, y, z);
        
        if(this.unusablePoints.contains(newPathPoint.toCoord()))return false;
        
        if(newPathPoint.isAssigned())return false;
        newPathPoint.previous=previous;
        newPathPoint.index=previous.index+1;
        newPathPoint.beforeRequested=previous.beforeRequested+1;
        
        boolean putBlockUnder=false;
        if(!isBlockRidable(e,x,y-1,z)){
        	//System.out.println("not ridable");
        	if(!this.canUseEngineer)return false;
        	
        	//Putting blocks shouldn't causes suffocation.
        	if(e.width<=1.0f){
        		AStarPathPoint p=previous;
        		while(true){
        			if(p.xCoord==x&&p.zCoord==z&&p.yCoord<=y&&p.yCoord+e.height>=y-1){
        				return false;
        			}
        			if(p.previous==null){
        				break;
        			}else{
        				p=p.previous;
        			}
        		}
        	}else{
        		AStarPathPoint p=previous;
        		while(true){
        			if(p.xCoord-e.width<x&&x<p.xCoord+e.width&&p.zCoord-e.width<z&&z<p.zCoord+e.width&&p.yCoord<=y&&p.yCoord+e.height>=y-1){
        				return false;
        			}
        			if(p.previous==null){
        				break;
        			}else{
        				p=p.previous;
        			}
        		}
        	}
        	//end
        	newPathPoint.addBlocksToPut(new Coord(x,y-1,z));
        	putBlockUnder=true;
        }
        //These commands decides which blocks should be broken.
        AxisAlignedBB aabbUnder=this.getAABB(e.worldObj, x, y-1, z);
        
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
        }else if(Block.blocksList[this.worldMap.getBlockId(x,y,z)] instanceof BlockFluid){
        	//System.out.println("point"+newPathPoint.toCoord().toString()+"is fluid");
        	newPathPoint.yOffset=0.3f;
        }else if(Block.blocksList[this.worldMap.getBlockId(x,y-1,z)] instanceof BlockFluid){
        	//System.out.println("point"+newPathPoint.toCoord().toString()+"'s under is fluid");
        	newPathPoint.yOffset=-0.7f;
        }else if(aabbUnder==null){
        	//System.out.println("point"+newPathPoint.toCoord().toString()+"is air");
        	newPathPoint.yOffset=-1;
        
        }else{
        	//System.out.println("point"+newPathPoint.toCoord().toString()+"is solid("+(aabbUnder.maxY-(double)y)+")");
        	newPathPoint.yOffset=(float)aabbUnder.maxY-(float)y;
        }
        
        float maxY=Math.max(newPathPoint.yOffset+((float)newPathPoint.yCoord),
        		previous.yOffset+((float)previous.yCoord))+e.height;
        	for(int j=y;j<maxY;j++){
				if(!this.isBlockPassable(e,x, j, z)
						&&this.getAABB(e.worldObj, x, j, z).minY<maxY
						){
					//System.out.println("not passable");
					if(!this.canUseEngineer)return false;
		        		newPathPoint.addBlocksToBreak(new Coord(x,j,z));
				}
			}
        
        	for(int j=MathHelper.ceiling_float_int(previous.yCoord+e.height);j<maxY;j++){
				if(!this.isBlockPassable(e,previous.xCoord, j, previous.zCoord)
						&&this.getAABB(e.worldObj,previous.xCoord, j, previous.zCoord).minY<maxY
						){
					if(!this.canUseEngineer)return false;
		        		newPathPoint.addBlocksToBreak(new Coord(previous.xCoord,j,previous.zCoord));
				}
			}
        //end
        	
        	
       
        newPathPoint.totalRealCost=10+newPathPoint.previous.totalRealCost+this.getBlockCost(e,newPathPoint,end)+this.getEngineerCost(e,newPathPoint,end);
        if(newPathPoint.totalRealCost>this.maxCost)return false;
        //newPathPoint.totalHeuristicCost=newPathPoint.previous.totalHeuristicCost+getHeuristicCost(e,newPathPoint,end);
        newPathPoint.totalCost=newPathPoint.totalRealCost+getHeuristicCost(e,newPathPoint,end);
        newPathPoint.tickToNext=this.getPlannedTickToNext(e, previous, newPathPoint);
       // if(this.ai instanceof EntityAIBreakBlock)
      //System.out.println("opened:"+x+","+y+","+z+" as cost:"+newPathPoint.totalCost+" as time:"+newPathPoint.tickToNext);
        AStarPathPoint a=(AStarPathPoint)this.pointMap.lookup(l);
        if(a==null){
        	this.pointMap.addKey(l, newPathPoint);
        	this.openedPointList.add(newPathPoint);
        }else if(a.totalCost>newPathPoint.totalCost){
        	a=newPathPoint;
        	this.openedPointList.add(newPathPoint);
        }
        return true;
    }
    
    public int getEngineerCost(EntityRegularArmy e,AStarPathPoint p,AStarPathPoint end){
    	if(p.numberOfBlocksToBreak==0&&p.numberOfBlocksToPut==0)return 0;
    	int cost=0;
    	
    	boolean flag=true;
    	for(int i=0;i<p.numberOfBlocksToBreak;i++){
    		Coord c=p.blocksToBreak[i];
    		int id=this.worldMap.getBlockId(c.x, c.y, c.z);
    		if(id!=0){
    			if(e.unit.getRequestManager().getEqual(c)!=null){
    				flag=false;
    				cost+=15+Block.blocksList[id].getBlockHardness(e.worldObj, c.x, c.y, c.z)*2;
    			}else{
    				cost+=25+Block.blocksList[id].getBlockHardness(e.worldObj, c.x, c.y, c.z)*4;
    			}
    		}
    	}
    	for(int i=0;i<p.numberOfBlocksToPut;i++){
    		Coord c=p.blocksToPut[i];
    		int id=this.worldMap.getBlockId(c.x, c.y, c.z);
    		if(e.unit.getRequestManager().getEqual(c)!=null){
    			flag=false;
    			cost+=8;
    		}else{
    			cost+=20;
    		}
    	}
    	if(this.lowerEngineerCost){
    		cost/=2;
    	}
    	if(p.beforeRequested==0){
    		if(p.previous.beforeRequested>16&&flag&&!this.lowerEngineerCost){
    			cost+=50;
    		}
    	}
    	return cost;
    }
    
    public int getBlockCost(EntityRegularArmy e,AStarPathPoint p,AStarPathPoint end){
    	int cost=0;
    	
    	
    	if(p.previous.yCoord-p.yCoord>=3)cost+=(p.previous.yCoord-p.yCoord)*16-20;
    	Block b=Block.blocksList[this.worldMap.getBlockId(p.xCoord,p.yCoord,p.zCoord)];
    	//if(!isBlockRidable(e,p.xCoord,p.yCoord-1,p.zCoord))cost+= Block.blocksList[this.worldMap.getBlockId(p.xCoord,p.yCoord-1,p.zCoord)] instanceof BlockFluid? 4:0;
    	//if(isBlockRidable(e,p.xCoord,p.yCoord,p.zCoord))cost+=b.getBlockHardness(e.worldObj,p.xCoord,p.yCoord,p.zCoord)/2+3;
    	if(b instanceof BlockFluid)cost+=30;
    	int i=0;
    	switch(p.index%8){
    	case 0:
    		for(;i<15;i++){
    			if(this.isBlockRidable(e, p.xCoord+1, p.yCoord-i+1, p.zCoord)){
    				break;
    			}
    		}
    		break;
    	case 2:
    		for(;i<15;i++){
    			if(this.isBlockRidable(e, p.xCoord, p.yCoord-i+1, p.zCoord+1)){
    				break;
    			}
    		}
    		break;
    	case 4:
    		for(;i<15;i++){
    			if(this.isBlockRidable(e, p.xCoord-1, p.yCoord-i+1, p.zCoord)){
    				break;
    			}
    		}
    		break;
    	case 6:
    		for(;i<15;i++){
    			if(this.isBlockRidable(e, p.xCoord, p.yCoord-i+1, p.zCoord-1)){
    				break;
    			}
    		}
    		break;
    	}
    	cost+=i<3?0:i*4;
    	//System.out.println(p.toCoord().toString()+"'s height cost is "+(i<3?0:i*4));
    	if(this.settings!=null){
    		int[] ids=new int[5*4];
    		int num=0;
    		for(int y=p.yCoord-1;y<p.yCoord+3;y++){
    			ids[num++]=this.worldMap.getBlockId(p.xCoord, y, p.zCoord);
    			ids[num++]=this.worldMap.getBlockId(p.xCoord+1, y, p.zCoord);
    			ids[num++]=this.worldMap.getBlockId(p.xCoord, y, p.zCoord+1);
    			ids[num++]=this.worldMap.getBlockId(p.xCoord-1, y, p.zCoord);
    			ids[num++]=this.worldMap.getBlockId(p.xCoord, y, p.zCoord-1);
    		}
    	
    		cost+=this.settings.getTotalBlocksCost(ids);
    	}else{
    	
    		if(b==Block.lavaStill||b==Block.lavaMoving)cost+=500;
    		int id=this.worldMap.getBlockId(p.xCoord,p.yCoord-1,p.zCoord);
    		if(id==Block.cactus.blockID){
    			cost+=40;
    		}else if(id==Block.lavaMoving.blockID||id==Block.lavaStill.blockID){
    			cost+=500;
    		}
    	
    	}
    	return cost;
    }
    
    public int getHeuristicCost(EntityRegularArmy e,AStarPathPoint p,AStarPathPoint end){
    	int d1=p.xCoord-end.xCoord;
    	int d2=p.zCoord-end.zCoord;
    	int d3=p.yCoord-end.yCoord;
    	int chunkCost=0;
    	if(p.index%8==0){
    		Integer num=(Integer) this.settings.chunkCost.get((((p.xCoord/16)&0xfff)<<20)+(((p.yCoord/16)&0xff)<<12)+(p.zCoord/16)&0xfff);
    		chunkCost=num==null?0:num;
    	}
    	return (int) ((MathHelper.sqrt_float(d1*d1+d2*d2)+Math.abs(d3))*10+
    			(MathHelper.abs_int(p.yCoord-end.yCoord)>MathHelper.abs_int(p.previous.yCoord-end.yCoord)?10:0))+chunkCost;
    }

    public int getPlannedTickToNext(EntityRegularArmy e,AStarPathPoint prev,AStarPathPoint current){
    	int tickPrev=(int) (0.5/((EntityLivingBase) e).getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
    	for(int i=prev.yCoord-1;i<prev.yCoord+MathHelper.floor_float(e.height);i++){
    		if(this.worldMap.getBlockMaterial(prev.xCoord, i, prev.zCoord).isLiquid()){
    			tickPrev*=2;
    			break;
    		}
    	}
    	int tickNext=(int) (0.5/((EntityLivingBase) e).getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
    	for(int i=current.yCoord-1;i<current.yCoord+MathHelper.floor_float(e.height);i++){
    		if(this.worldMap.getBlockMaterial(current.xCoord, i, current.zCoord).isLiquid()){
    			tickNext*=2;
    			break;
    		}
    	}
    	if(prev.yCoord>current.yCoord){
    		tickPrev+=(prev.yCoord-current.yCoord)*8;
    	}else if(prev.yCoord<current.yCoord){
    		tickPrev+=(current.yCoord-prev.yCoord)*4;
    	}
    	/*
    	for(int i=0;i<current.numberOfBlocksToBreak;i++){
    		Coord c=current.blocksToBreak[i];
    		if(this.ai instanceof EntityAIBreakBlock){
    			tickNext+=((IBreakBlocksMob)e).getblockStrength(Block.blocksList[this.worldMap.getBlockId(c.x,c.y,c.z)], e.worldObj, c.x,c.y,c.z);
    		}else{
    			tickNext+=200+Block.blocksList[this.worldMap.getBlockId(c.x,c.y,c.z)].getBlockHardness(e.worldObj, c.x,c.y,c.z)*10;
    		}
    	}
    	if(this.ai instanceof EntityAIBreakBlock){
    		tickNext+=current.numberOfBlocksToPut*4;
    	}else if(current.numberOfBlocksToPut!=0){
    		tickNext+=200;
    	}
    	*/
    	return tickPrev+tickNext+50;
    }
    
    public void setSetting(FinderSettings f){
    	this.settings=f;
    }

    /**
     * Returns a new PathEntity for a given start and end point
     */
    private AStarPathEntity createEntityPath(AStarPathPoint par1AStarPathPoint, AStarPathPoint par2AStarPathPoint)
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
            AStarPathPoint2 = AStarPathPoint2.previous;
            --i;
        }

        return new AStarPathEntity(aAStarPathPoint,this.ai);
    }
    
   private boolean isBlockRidable(EntityRegularArmy e,int x,int y,int z){
	   int id=this.worldMap.getBlockId(x, y,z);
	   if(id==0)return false;
	   else{
		   if(Block.blocksList[id].isOpaqueCube())return true;
		   
		   if(Block.blocksList[this.worldMap.getBlockId(x, y+1,z)] instanceof BlockFluid)return true;
		   if(this.isBlockPassable(e, x, y, z))return false;
		   
		   AxisAlignedBB aabb=Block.blocksList[id].getCollisionBoundingBoxFromPool(e.worldObj, x, y, z);
		   //if(aabb==null)return false;
		   
		   if(aabb.maxX+e.width/2<x+1||aabb.minX-e.width/2>x||aabb.maxZ+e.width/2<z+1||aabb.minZ-e.width/2>z)return false;
		   return true;
	   }
   }
   
   private boolean isBlockPassable(EntityRegularArmy e,int x,int y,int z){
	   int id=this.worldMap.getBlockId(x, y,z);
	   if(id==0)return true;
	   else if( Block.blocksList[id] instanceof BlockFluid)return true;
	   else if(Block.blocksList[id].getCollisionBoundingBoxFromPool(e.worldObj, x, y, z)==null)return true;
	return false;   
   }
   
   private AxisAlignedBB getAABB(World w,int x,int y,int z){
	   int id=this.worldMap.getBlockId(x, y, z);
	   if(id==0)return null;
	   return Block.blocksList[id].getCollisionBoundingBoxFromPool(w, x, y, z);
   }
}
