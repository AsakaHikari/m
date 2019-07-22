package regulararmy.pathfinding;

import regulararmy.entity.ai.EngineerRequest;
import regulararmy.entity.ai.EntityAIBreakBlock;
import regulararmy.entity.ai.EntityRegularAIBase;
import regulararmy.entity.command.RequestManager;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Vec3;

public class AStarPathEntity
{
    /** The actual points in the path */
    public final AStarPathPoint[] points;

    /** PathEntity Array Index the Entity is currently targeting */
    private int currentPathIndex;

    /** The total length of the path */
    private int pathLength;
    
    private IPathFindRequester ai;
    
    public AStarPathEntity(AStarPathPoint[] par1ArrayOfAStarPathPoint,IPathFindRequester ai2)
    {
        this.points = par1ArrayOfAStarPathPoint;
        this.pathLength = par1ArrayOfAStarPathPoint.length;
        this.ai=ai2;
    }

    /**
     * Directs this path to the next point in its array
     */
    public void incrementPathIndex()
    {
        ++this.currentPathIndex;
    }

    /**
     * Returns true if this path has reached the end
     */
    public boolean isFinished()
    {
        return this.currentPathIndex >= this.pathLength;
    }

    /**
     * returns the last AStarPathPoint of the Array
     */
    public AStarPathPoint getFinalPathPoint()
    {
        return this.pathLength > 0 ? this.points[this.pathLength - 1] : null;
    }

    /**
     * return the AStarPathPoint located at the specified PathIndex, usually the current one
     */
    public AStarPathPoint getPathPointFromIndex(int par1)
    {
        return this.points[par1];
    }

    public int getCurrentPathLength()
    {
        return this.pathLength;
    }

    public void setCurrentPathLength(int par1)
    {
        this.pathLength = par1;
    }

    public int getCurrentPathIndex()
    {
        return this.currentPathIndex;
    }

    public void setCurrentPathIndex(int par1)
    {
        this.currentPathIndex = par1;
    }

    /**
     * Gets the vector of the AStarPathPoint associated with the given index.
     */
    public Vec3 getVectorFromIndex(Entity par1Entity, int par2)
    {
        double d0 = (double)this.points[par2].xCoord + 0.5;
        double d1 = (double)this.points[par2].yCoord + this.points[par2].yOffset;
        double d2 = (double)this.points[par2].zCoord + 0.5;
        return par1Entity.worldObj.getWorldVec3Pool().getVecFromPool(d0, d1, d2);
    }

    /**
     * returns the current PathEntity target node as Vec3D
     */
    public Vec3 getPosition(Entity par1Entity)
    {
        return this.getVectorFromIndex(par1Entity, this.currentPathIndex);
    }

    /**
     * Returns true if the EntityPath are the same. Non instance related equals.
     */
    public boolean isSamePath(AStarPathEntity par1PathEntity)
    {
        if (par1PathEntity == null)
        {
            return false;
        }
        else if (par1PathEntity.points.length != this.points.length)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < this.points.length; ++i)
            {
                if (this.points[i].xCoord != par1PathEntity.points[i].xCoord || this.points[i].yCoord != par1PathEntity.points[i].yCoord || this.points[i].zCoord != par1PathEntity.points[i].zCoord)
                {
                    return false;
                }
            }

            return true;
        }
    }
    
    public int getTotalCost(){
    	return this.points[pathLength-1].totalRealCost;
    }

    /**
     * Returns true if the final AStarPathPoint in the PathEntity is equal to Vec3D coords.
     */
    public boolean isDestinationSame(Vec3 par1Vec3)
    {
        AStarPathPoint AStarPathPoint = this.getFinalPathPoint();
        return AStarPathPoint == null ? false : AStarPathPoint.xCoord == (int)par1Vec3.xCoord && AStarPathPoint.zCoord == (int)par1Vec3.zCoord;
    }
    
    public void enablePath(RequestManager manager){
    	for(int i=0;i<this.pathLength;i++){
    		
    	}
    	if(ai.isEngineer()){
    		for(int i=0;i<this.pathLength;i++){
    			for(int j=0;j<this.points[i].numberOfBlocksToBreak;j++){
    				((EntityAIBreakBlock) ai).addMyTarget(new EngineerRequest(this.points[i].blocksToBreak[j], false));
    			}
    			for(int j=0;j<this.points[i].numberOfBlocksToPut;j++){
    				((EntityAIBreakBlock) ai).addMyTarget(new EngineerRequest(this.points[i].blocksToPut[j], true));
    			}
    		}
    	}else{
    		for(int i=0;i<this.pathLength;i++){
    			for(int j=0;j<this.points[i].numberOfBlocksToBreak;j++){
    				manager.request(this.points[i].blocksToBreak[j], false,this.points[i==0?0:i-1].toCoord());
    			}
    			for(int j=0;j<this.points[i].numberOfBlocksToPut;j++){
    				manager.request(this.points[i].blocksToPut[j], true,this.points[i==0?0:i-1].toCoord());
    			}
    		}
    	}
    }
    
    public void disablePath(RequestManager manager){
    	for(int i=0;i<this.pathLength;i++){
    		for(int j=0;j<this.points[i].numberOfBlocksToBreak;j++){
    			manager.delete(this.points[i].blocksToBreak[j]);
    		}
    		for(int j=0;j<this.points[i].numberOfBlocksToPut;j++){
    			manager.delete(this.points[i].blocksToPut[j]);
    		}
    	}
    }
}
