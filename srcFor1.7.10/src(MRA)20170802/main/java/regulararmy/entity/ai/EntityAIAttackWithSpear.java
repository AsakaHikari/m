package regulararmy.entity.ai;

import java.util.List;

import regulararmy.core.*;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.pathfinding.AStarPathEntity;
import regulararmy.pathfinding.AStarPathPoint;
import regulararmy.pathfinding.IPathFindRequester;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIAttackWithSpear extends EntityRegularAIBase implements IPathFindRequester
{
	public World worldObj;
	public EntityRegularArmy attacker;
    
	public float hitboxWidth;
	public float hitboxHeight;
	public float hitboxDepth;
	public Vec3 centreOfHitbox;
    /**
     * An amount of decrementing ticks that allows the entity to attack once the tick reaches 0.
     */
    public  int attackTick;

    /** The speed with which the mob will approach the target */
    public double speedTowardsTarget;

    /**
     * When true, the mob will continue chasing its target, even if it can't find a path to them right now.
     */
    public  boolean longMemory;

    /** The PathEntity of our entity. */
    public  AStarPathEntity entityPathEntity;
    private int field_75445_i;

    private int failedPathFindingPenalty;
    
    public IEntitySelector selector;
    
    public EntityAIAttackWithSpear(EntityRegularArmy attacker,
			float hitboxWidth, float hitboxHeight, float hitboxDepth,Vec3 centreOfHitbox,
			double speedTowardsTarget, boolean longMemory) {
		super();
		this.attacker = attacker;
		this.worldObj = attacker.worldObj;
		this.hitboxWidth=hitboxWidth;
		this.hitboxHeight=hitboxHeight;
		this.hitboxDepth=hitboxDepth;
		this.centreOfHitbox=centreOfHitbox;
		this.speedTowardsTarget = speedTowardsTarget;
		this.longMemory = longMemory;
		this.setMutexBits(3);
		this.selector=new EntityAILearnedTarget.EnemySelector(true, attacker);
	}

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        
        else
        {
            if (-- this.field_75445_i <= 0)
            {
            	
                this.entityPathEntity = this.attacker.getANavigator().getPathToEntityLiving(entitylivingbase,MathHelper.sqrt_double((double)(this.attacker.width *this.attacker.width)-0.2),this);
                this.field_75445_i = 4 + this.attacker.getRNG().nextInt(7);
                return this.entityPathEntity != null;
            }
            else
            {
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        return ((entitylivingbase==null||(entitylivingbase!=null&&!entitylivingbase.isEntityAlive())) ? false : (!this.longMemory ? !this.attacker.getANavigator().noPath() : this.attacker.isWithinHomeDistance(MathHelper.floor_double(entitylivingbase.posX), MathHelper.floor_double(entitylivingbase.posY), MathHelper.floor_double(entitylivingbase.posZ))));
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.attacker.getANavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
        this.field_75445_i = 0;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.attacker.getANavigator().clearAStarPathEntity();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if(entitylivingbase==null){
        	return;
        }
        this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);

        if ((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) && --this.field_75445_i <= 0)
        {
            this.field_75445_i = failedPathFindingPenalty + 4 + this.attacker.getRNG().nextInt(7);
            this.attacker.getANavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget,this.attacker.width*this.attacker.width,this);
            if (this.attacker.getANavigator().getPath() != null)
            {
                AStarPathPoint finalPathPoint = this.attacker.getANavigator().getPath().getFinalPathPoint();
                if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord) < 1)
                {
                    failedPathFindingPenalty = 0;
                }
                else
                {
                    failedPathFindingPenalty += 10;
                }
            }
            else
            {
                failedPathFindingPenalty += 10;
            }
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);

        if (!isLaggy() && this.attackTick <= 0)
        {
        	double d=this.hitboxWidth+this.hitboxDepth;
        	Vec3 center=Vec3.createVectorHelper(this.attacker.posX, this.attacker.posY, this.attacker.posZ);
        	Vec3 rotated=Vec3.createVectorHelper(this.centreOfHitbox.xCoord, this.centreOfHitbox.yCoord, this.centreOfHitbox.zCoord);
        	rotated.rotateAroundY((float)-Math.toRadians(this.attacker.renderYawOffset));
        	center.addVector(rotated.xCoord, rotated.yCoord, rotated.zCoord);
        	AxisAlignedBB roughArea=AxisAlignedBB.getBoundingBox(center.xCoord-d, center.yCoord-this.hitboxHeight, center.zCoord-d,
        			center.xCoord+d, center.yCoord+this.hitboxHeight, center.zCoord+d);
        	//System.out.println("yaw:"+this.attacker.renderYawOffset);
        	List<Entity> list = this.attacker.worldObj.getEntitiesWithinAABBExcludingEntity(attacker, roughArea,this.selector);
        	for(Entity e:list){
        		if(new OrientedBB(this.centreOfHitbox.addVector(this.attacker.posX, this.attacker.posY, this.attacker.posZ),
        				center,this.hitboxWidth,this.hitboxHeight,this.hitboxDepth,0,(float)-Math.toRadians(this.attacker.renderYawOffset),0)
        		.isCollidingWith(new OrientedBB(AxisAlignedBB.getBoundingBox(e.posX-e.width/2,
        				e.posY-e.height/2,e.posZ-e.width/2,
        				e.posX+e.width/2,e.posY+e.height/2,e.posZ+e.width/2)))){
        			if(this.attacker.getEntitySenses().canSee(e)){
        				this.attackTick = 30;
        				this.attacker.swingItem();
        				this.attacker.attackEntityAsMob(e);
        				break;
        			}
        			//System.out.println("hit");
        		}
        	}
        	
        }
    }

	@Override
	public int getTacticsCost(Entity entity, AStarPathPoint start,
			AStarPathPoint current, AStarPathPoint end) {
		return 0;
	}
	
	@Override
	public boolean isEngineer(){
		return false;
	}

	@Override
	public float getJumpHeight() {
		return this.attacker.data.jumpHeight;
	}

	@Override
	public float getCrowdCost() {
		return this.attacker.data.crowdCostPerBlock;
	}

	@Override
	public float getFightRange() {
		return this.attacker.data.fightRange;
	}
}
