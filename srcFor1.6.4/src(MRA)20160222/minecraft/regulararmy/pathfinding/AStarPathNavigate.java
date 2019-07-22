package regulararmy.pathfinding;

import java.util.ArrayList;
import java.util.List;

import regulararmy.core.Coord;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.entity.ai.EngineerRequest;
import regulararmy.entity.ai.EntityRegularAIBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public class AStarPathNavigate
{
    private EntityRegularArmy theEntity;
    private World worldObj;

    /** The AStarPathEntity being followed. */
    public AStarPathEntity currentPath;
    private double speed;

    /**
     * The number of blocks (extra) +/- in each axis that get pulled out as cache for the pathfinder's search space
     */
    private AttributeInstance pathSearchRange;
    private boolean noSunPathfind;

    /** Time, in number of ticks, following the current path */
    private int totalTicks;

    /**
     * The time when the last position check was done (to detect successful movement)
     */
    private int ticksAtLastPos;

    /**
     * Coordinates of the entity's position last time a check was done (part of monitoring getting 'stuck')
     */
    private Vec3 lastPosCheck = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);

    /**
     * Specifically, if a wooden door block is even considered to be passable by the pathfinder
     */
    private boolean canPassOpenWoodenDoors = true;

    /** If door blocks are considered passable even when closed */
    private boolean canPassClosedWoodenDoors;

    /** If water blocks are avoided (at least by the pathfinder) */
    private boolean avoidsWater;

    /**
     * If the entity can swim. Swimming AI enables this and the pathfinder will also cause the entity to swim straight
     * upwards when underwater
     */
    private boolean canSwim;
    
    /**
     * 0:Can't use engineer
     * 1:Can use engineer
     * 2:Follow to unit's setting*/
    public byte canUseEngineer=2;
    
    public List<Coord> unuseablepoints=new ArrayList();
    
    public int maxCost=Integer.MAX_VALUE;
    
    
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
    	if(!this.canNavigate())return null;
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
        		((IPathFindRequester)ai).isEngineer(),1.5f,(IPathFindRequester)ai);
        finder.setSetting(this.theEntity.getSettings());
        finder.canUseEngineer=finder.canUseEngineer=this.canUseEngineer==2?this.theEntity.unit.canUseEngineer:this.canUseEngineer==1?true:false;
        finder.unusablePoints=this.unuseablepoints;
        finder.maxCost=this.maxCost;
        AStarPathEntity pathentity = finder.createEntityPathTo(this.theEntity,MathHelper.floor_double(par1), (int)par3, MathHelper.floor_double(par5), this.getPathSearchRange(),par6);
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
    	if(!this.canNavigate())return null;
    	this.worldObj.theProfiler.startSection("pathfind");
        int i = MathHelper.floor_double(this.theEntity.posX);
        int j = MathHelper.floor_double(this.theEntity.posY + 1.0D);
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
        		((IPathFindRequester)ai).isEngineer(),1.5f,(IPathFindRequester)ai);
        finder.setSetting(this.theEntity.getSettings());
        finder.canUseEngineer=this.canUseEngineer==2?this.theEntity.unit.canUseEngineer:this.canUseEngineer==1?true:false;
        finder.unusablePoints=this.unuseablepoints;
        finder.maxCost=this.maxCost;
        AStarPathEntity pathentity = finder.createEntityPathTo(this.theEntity, par1Entity, 64,par1);
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
            		this.currentPath.disablePath(this.theEntity.unit.getRequestManager());
                this.currentPath = par1AStarPathEntity;
                this.currentPath.enablePath(this.theEntity.unit.getRequestManager());
            }

            if (this.noSunPathfind)
            {
                this.removeSunnyPath();
            }

            if (this.currentPath.getCurrentPathLength() == 0)
            {
                return false;
            }
            else
            {
                this.speed = par2;
                Vec3 vec3 = this.getEntityPosition();
                this.ticksAtLastPos = this.totalTicks;
                this.lastPosCheck.xCoord = vec3.xCoord;
                this.lastPosCheck.yCoord = vec3.yCoord;
                this.lastPosCheck.zCoord = vec3.zCoord;
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

            if (!this.noPath()&&!this.shouldWait())
            {
                Vec3 vec3 = this.currentPath.getPosition(this.theEntity);

                if (vec3 != null)
                {
                    this.theEntity.getMoveHelper().setMoveTo(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.speed);
                }
            }
        }
    }

    private void pathFollow()
    {
        Vec3 vec3 = this.getEntityPosition();
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

        float f = this.theEntity.width * this.theEntity.width;
        int k;

        for (k = this.currentPath.getCurrentPathIndex(); k < i; ++k)
        {
        	//System.out.println("distance:"+vec3.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, k))+"at "+this.currentPath.getVectorFromIndex(this.theEntity, k));
            if (vec3.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, k)) < (double)f)
            {
            	//System.out.println("nearby enough");
                this.currentPath.setCurrentPathIndex(k + 1);
                this.ticksAtLastPos=this.totalTicks;
            }
        }

        k = MathHelper.ceiling_float_int(this.theEntity.width);
        int l = (int)this.theEntity.height + 1;
        int i1 = k;

        for (int j1 = i - 1; j1 >= this.currentPath.getCurrentPathIndex(); --j1)
        {
            if (this.currentPath.getPathPointFromIndex(this.currentPath.getCurrentPathIndex()).numberOfBlocksToBreak==0&&this.currentPath.getPathPointFromIndex(this.currentPath.getCurrentPathIndex()).numberOfBlocksToPut==0&&this.isDirectPathBetweenPoints(vec3, this.currentPath.getVectorFromIndex(this.theEntity, j1), k, l, i1))
            {
                this.currentPath.setCurrentPathIndex(j1);
                break;
            }
        }

        if(this.currentPath.getCurrentPathLength()<=this.currentPath.getCurrentPathIndex()){
        	this.clearAStarPathEntity();
        	//System.out.println("path over");
        	return;
        }
        
        if (this.totalTicks - this.ticksAtLastPos > this.currentPath.getPathPointFromIndex(this.currentPath.getCurrentPathIndex()).tickToNext*3)
        {
            this.clearAStarPathEntity();
            System.out.println("time out");
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
    		this.currentPath.disablePath(this.theEntity.unit.getRequestManager());
        this.currentPath = null;
    }

    private Vec3 getEntityPosition()
    {
        return this.worldObj.getWorldVec3Pool().getVecFromPool(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ);
    }

    /**
     * Gets the safe pathing Y position for the entity depending on if it can path swim or not
     */
    public int getPathableYPos()
    {
        if (this.theEntity.isInWater() && this.canSwim)
        {
            int i = (int)this.theEntity.boundingBox.minY;
            int j = this.worldObj.getBlockId(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ));
            int k = 0;

            do
            {
                if (j != Block.waterMoving.blockID && j != Block.waterStill.blockID)
                {
                    return i;
                }

                ++i;
                j = this.worldObj.getBlockId(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ));
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
    private boolean canNavigate()
    {
        return this.theEntity.onGround || this.canSwim && this.isInFluid();
    }

    /**
     * Returns true if the entity is in water or lava, false otherwise
     */
    private boolean isInFluid()
    {
        return this.theEntity.isInWater() || this.theEntity.handleLavaMovement();
    }
    
    private boolean shouldWait(){
    	
    	for(int i=0;i<this.getCurrentPathPoint().numberOfBlocksToBreak;i++){
    		EngineerRequest e=this.theEntity.unit.getRequestManager().getEqual(this.getCurrentPathPoint().blocksToBreak[i]);
    		if(e!=null&&e.isEnable)return true;
    	}
    	for(int i=0;i<this.getCurrentPathPoint().numberOfBlocksToPut;i++){
    		EngineerRequest e=this.theEntity.unit.getRequestManager().getEqual(this.getCurrentPathPoint().blocksToPut[i]);
    		if(e!=null&&e.isEnable)return true;
    	}
    	
    	return false;
    }
    
    private AStarPathPoint getCurrentPathPoint(){
    	return this.currentPath.getPathPointFromIndex(this.currentPath.getCurrentPathIndex());
    }

    /**
     * Trims path data from the end to the first sun covered block
     */
    private void removeSunnyPath()
    {
        if (!this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.theEntity.posX), (int)(this.theEntity.boundingBox.minY + 0.5D), MathHelper.floor_double(this.theEntity.posZ)))
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

    /**
     * Returns true when an entity of specified size could safely walk in a straight line between the two points. Args:
     * pos1, pos2, entityXSize, entityYSize, entityZSize
     */
    private boolean isDirectPathBetweenPoints(Vec3 par1Vec3, Vec3 par2Vec3, int par3, int par4, int par5)
    {
        int x1_int = MathHelper.floor_double(par1Vec3.xCoord);
        int z1_int = MathHelper.floor_double(par1Vec3.zCoord);
        double relativeX = par2Vec3.xCoord - par1Vec3.xCoord;
        double relativeZ = par2Vec3.zCoord - par1Vec3.zCoord;
        double squareDistance = relativeX * relativeX + relativeZ * relativeZ;

        if (squareDistance < 1.0E-8D)
        {
            return false;
        }
        else
        {
            double d3 = 1.0D / MathHelper.sqrt_double(squareDistance);
            relativeX *= d3;
            relativeZ *= d3;
            par3 += 2;
            par5 += 2;

            if (!this.isSafeToStandAt(x1_int, (int)par1Vec3.yCoord, z1_int, par3, par4, par5, par1Vec3, relativeX, relativeZ))
            {
                return false;
            }
            else
            {
                par3 -= 2;
                par5 -= 2;
                double d4 = 1.0D / Math.abs(relativeX);
                double d5 = 1.0D / Math.abs(relativeZ);
                double d6 = (double)(x1_int * 1) - par1Vec3.xCoord;
                double d7 = (double)(z1_int * 1) - par1Vec3.zCoord;

                if (relativeX >= 0.0D)
                {
                    ++d6;
                }

                if (relativeZ >= 0.0D)
                {
                    ++d7;
                }

                d6 /= relativeX;
                d7 /= relativeZ;
                int j1 = relativeX < 0.0D ? -1 : 1;
                int k1 = relativeZ < 0.0D ? -1 : 1;
                int l1 = MathHelper.floor_double(par2Vec3.xCoord);
                int i2 = MathHelper.floor_double(par2Vec3.zCoord);
                int j2 = l1 - x1_int;
                int k2 = i2 - z1_int;

                do
                {
                    if (j2 * j1 <= 0 && k2 * k1 <= 0)
                    {
                        return true;
                    }

                    if (d6 < d7)
                    {
                        d6 += d4;
                        x1_int += j1;
                        j2 = l1 - x1_int;
                    }
                    else
                    {
                        d7 += d5;
                        z1_int += k1;
                        k2 = i2 - z1_int;
                    }
                }
                while (this.isSafeToStandAt(x1_int, (int)par1Vec3.yCoord, z1_int, par3, par4, par5, par1Vec3, relativeX, relativeZ));

                return false;
            }
        }
    }

    /**
     * Returns true when an entity could stand at a position, including solid blocks under the entire entity. Args:
     * xOffset, yOffset, zOffset, entityXSize, entityYSize, entityZSize, originPosition, vecX, vecZ
     */
    private boolean isSafeToStandAt(int par1, int par2, int par3, int par4, int par5, int par6, Vec3 par7Vec3, double par8, double par10)
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
                        int k2 = this.worldObj.getBlockId(i2, par2 - 1, j2);

                        if (k2 <= 0)
                        {
                            return false;
                        }

                        Material material = Block.blocksList[k2].blockMaterial;

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
    private boolean isPositionClear(int par1, int par2, int par3, int par4, int par5, int par6, Vec3 par7Vec3, double par8, double par10)
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
                        int j2 = this.worldObj.getBlockId(k1, l1, i2);

                        if (j2 > 0 && !Block.blocksList[j2].getBlocksMovement(this.worldObj, k1, l1, i2))
                        {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
    
}
