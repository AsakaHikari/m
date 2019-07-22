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
import net.minecraft.world.World;

public class EntityAIAttackOnCollide extends EntityRegularAIBase implements IPathFindRequester
{
    public World worldObj;
    public EntityRegularArmy attacker;

    /**
     * An amount of decrementing ticks that allows the entity to attack once the tick reaches 0.
     */
    public int attackTick;

    /** The speed with which the mob will approach the target */
    public double speedTowardsTarget;

    /**
     * When true, the mob will continue chasing its target, even if it can't find a path to them right now.
     */
    public boolean longMemory;

    /** The PathEntity of our entity. */
    public AStarPathEntity entityPathEntity;
    public int field_75445_i;

    public int failedPathFindingPenalty;
    
    public float angleMovementLimitPerTick=30f;
    public IEntitySelector selector;

    public EntityAIAttackOnCollide(EntityRegularArmy par1EntityCreature, double par2, boolean par4)
    {
        this.attacker = par1EntityCreature;
        this.worldObj = par1EntityCreature.worldObj;
        this.speedTowardsTarget = par2;
        this.longMemory = par4;
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
        this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, this.angleMovementLimitPerTick,this.angleMovementLimitPerTick);

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
        if(!isLaggy()){
        	this.attackTick = Math.max(this.attackTick - 1, 0);
        	double d0 = (double)(this.attacker.width * 2.0F * this.attacker.width * 2.0F + entitylivingbase.width*entitylivingbase.width);
        	AxisAlignedBB roughArea=AxisAlignedBB.getBoundingBox(this.attacker.posX-this.attacker.width, this.attacker.posY-this.attacker.height, this.attacker.posZ-this.attacker.width,
        			this.attacker.posX+this.attacker.width, this.attacker.posY+this.attacker.height, this.attacker.posZ+this.attacker.width);
        	List<Entity> list = this.attacker.worldObj.getEntitiesWithinAABBExcludingEntity(attacker, roughArea,this.selector);
        	for(Entity e:list){
        		if (this.attacker.getDistanceSq(e.posX, e.posY, e.posZ) <= d0)
        		{
        			if (this.attackTick <= 0)
        			{
        				this.attackTick = 20;

        				if (this.attacker.getHeldItem() != null)
        				{
        					this.attacker.swingItem();
        				}

        				this.attacker.attackEntityAsMob(e);
        			}
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
