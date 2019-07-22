package regulararmy.pathfinding;

import regulararmy.core.Coord;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;

public class AStarPathPoint
{
    /** The x coordinate of this point */
    public final int xCoord;

    /** The y coordinate of this point */
    public final int yCoord;

    /** The z coordinate of this point */
    public final int zCoord;

    /** A hash of the coordinates used to identify this point */
    private final int hash;

    /** The index of this point in its assigned path */
    public int index = -1;

    /** The cost along the path to this point */
    public int totalCost;
    
    /** The totalCost,excluded heuristic cost*/
    public int totalRealCost;
    
    public int totalHeuristicCost;
    
    /** Engineers requested before [beforeRequested] blocks.*/
    public int beforeRequested=20;

    /** The linear distance to the next point */
    public float distanceToNext;

    /** The distance to the target */
    public float distanceToTarget;
    
    public int tickToNext=30;

    /** The point preceding this in its assigned path */
    public AStarPathPoint previous;

    /** Indicates this is on head */
    public boolean isHead;
    
    public Coord[] blocksToBreak=new Coord[16];
    public Coord[] blocksToPut=new Coord[16];
    
    public int numberOfBlocksToBreak;
    public int numberOfBlocksToPut;
    
    /**If a block under this point is opaque, the value is 0.0 . ex)slab:-0.5 dirtBlock:0.0 fence:+0.5*/
    public float yOffset=0f;

    public AStarPathPoint(int par1, int par2, int par3)
    {
        this.xCoord = par1;
        this.yCoord = par2;
        this.zCoord = par3;
        this.hash = makeHash(par1, par2, par3);
    }

    public static int makeHash(int par0, int par1, int par2)
    {
        return par1 & 255 | (par0 & 32767) << 8 | (par2 & 32767) << 24 | (par0 < 0 ? Integer.MIN_VALUE : 0) | (par2 < 0 ? 32768 : 0);
    }

    /**
     * Returns the linear distance to another path point
     */
    public float distanceTo(AStarPathPoint par1PathPoint)
    {
        float f = (float)(par1PathPoint.xCoord - this.xCoord);
        float f1 = (float)(par1PathPoint.yCoord - this.yCoord);
        float f2 = (float)(par1PathPoint.zCoord - this.zCoord);
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }

    public float func_75832_b(AStarPathPoint par1PathPoint)
    {
        float f = (float)(par1PathPoint.xCoord - this.xCoord);
        float f1 = (float)(par1PathPoint.yCoord - this.yCoord);
        float f2 = (float)(par1PathPoint.zCoord - this.zCoord);
        return f * f + f1 * f1 + f2 * f2;
    }

    public boolean equals(Object par1Obj)
    {
        if (!(par1Obj instanceof AStarPathPoint))
        {
            return false;
        }
        else
        {
            AStarPathPoint pathpoint = (AStarPathPoint)par1Obj;
            return this.hash == pathpoint.hash && this.xCoord == pathpoint.xCoord && this.yCoord == pathpoint.yCoord && this.zCoord == pathpoint.zCoord;
        }
    }

    public int hashCode()
    {
        return this.hash;
    }

    /**
     * Returns true if this point has already been assigned to a path
     */
    public boolean isAssigned()
    {
        return this.index >= 0;
    }
    
    public String toString()
    {
        return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
    }
    
    public void addBlocksToBreak(Coord c){
    	this.blocksToBreak[this.numberOfBlocksToBreak++]=c;
    	this.beforeRequested=0;
    }
    
    public void addBlocksToPut(Coord c){
    	this.blocksToPut[this.numberOfBlocksToPut++]=c;
    	this.beforeRequested=0;
    }
    
    public Coord toCoord(){
    	return new Coord(this.xCoord,this.yCoord,this.zCoord);
    }
}
