package regulararmy.pathfinding;

import java.util.ArrayList;
import java.util.List;

import regulararmy.core.Coord;
import regulararmy.entity.ai.EngineerRequest;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

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
    
    /** Indicates whether this is on a ladder*/
    public boolean onLadder;
    
    public ForgeDirection dirFromPrev=ForgeDirection.UNKNOWN;
    
    public boolean isNearBase;
    
    /** They are only for path finding*/
    public Coord[] blocksToBreak;
    public Coord[] blocksToPut;
    public Coord[] laddersToPut;
    public ForgeDirection[] ladderDirection;
    public int numberOfBlocksToBreak;
    public int numberOfBlocksToPut;
    public int numberOfLaddersToPut;
    
    /** Coords where the mob use ladder*/
    public List<Coord> coordsLadder;
    
    public List<EngineerRequest> requestsTemp;
    
    public List<EngineerRequest> requests; 
    
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
    	if(this.blocksToBreak==null){
    		this.blocksToBreak=new Coord[16];
    	}else if(this.blocksToBreak[this.blocksToBreak.length-1]!=null){
    		Coord[] old=this.blocksToBreak;
    		this.blocksToBreak=new Coord[old.length*2];
    		System.arraycopy(old, 0, this.blocksToBreak, 0, old.length);
    	}
    	this.blocksToBreak[this.numberOfBlocksToBreak++]=c;
    	this.beforeRequested=0;
    }
    
    public void addBlocksToPut(Coord c){
    	if(this.blocksToPut==null){
    		this.blocksToPut=new Coord[16];
    	}else if(this.blocksToPut[this.blocksToPut.length-1]!=null){
    		Coord[] old=this.blocksToPut;
    		this.blocksToPut=new Coord[old.length*2];
    		System.arraycopy(old, 0, this.blocksToPut, 0, old.length);
    	}
    	this.blocksToPut[this.numberOfBlocksToPut++]=c;
    	this.beforeRequested=0;
    }
    
    public void addLaddersToPut(Coord c,ForgeDirection dir){
    	if(this.laddersToPut==null){
    		this.laddersToPut=new Coord[16];
    		this.ladderDirection=new ForgeDirection[16];
    	}else if(this.laddersToPut[this.laddersToPut.length-1]!=null){
    		Coord[] old=this.laddersToPut;
    		this.laddersToPut=new Coord[old.length*2];
    		System.arraycopy(old, 0, this.laddersToPut, 0, old.length);
    		ForgeDirection[] old_=this.ladderDirection;
    		this.ladderDirection=new ForgeDirection[old_.length*2];
    		System.arraycopy(old_, 0, this.ladderDirection, 0, old_.length);
    	}
    	this.laddersToPut[this.numberOfLaddersToPut]=c;
    	this.ladderDirection[this.numberOfLaddersToPut++]=dir;
    	this.beforeRequested=0;
    }
    
    public void addCoordsLadder(Coord c){
    	if(this.coordsLadder==null){
    		this.coordsLadder=new ArrayList();
    	}
    	this.coordsLadder.add(c);
    }
    
    public Coord toCoord(){
    	return new Coord(this.xCoord,this.yCoord,this.zCoord);
    }
    
    public int getHeuristicCost(){
    	return this.totalCost-this.totalRealCost;
    }
    
    public int makeMiniChunkHash(){
    	return (((((int)this.xCoord)/4)&0x7ff+(this.xCoord<0?0x800:0))<<20)+(((((int)this.yCoord)/16)&0xff)<<12)+((((int)this.zCoord)/4)&0x7ff+(this.zCoord<0?0x800:0));
    }
}
