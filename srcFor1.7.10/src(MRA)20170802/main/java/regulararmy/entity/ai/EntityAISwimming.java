package regulararmy.entity.ai;

import regulararmy.entity.EntityRegularArmy;
import regulararmy.pathfinding.AStarPathPoint;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAISwimming extends EntityAIBase
{
    private EntityRegularArmy theEntity;

    public EntityAISwimming(EntityRegularArmy par1EntityLiving)
    {
        this.theEntity = par1EntityLiving;
        this.setMutexBits(4);
        par1EntityLiving.getANavigator().setCanSwim(true);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if((this.theEntity.isInWater() || this.theEntity.handleLavaMovement())&&!this.theEntity.getANavigator().noPath()){
        	AStarPathPoint point=this.theEntity.getANavigator().getCurrentPathPoint();
        	if(point==null)return false;
        	return (point.yCoord+point.yOffset>this.theEntity.posY);
        }
        return false;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (this.theEntity.getRNG().nextFloat() < 0.8F)
        {
            this.theEntity.getJumpHelper().setJumping();
        }
    }
}
