package regulararmy.entity.ai;

import regulararmy.entity.EntityEngineer;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.pathfinding.AStarPathEntity;
import regulararmy.pathfinding.AStarPathPoint;
import regulararmy.pathfinding.IPathFindRequester;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class EntityAIForwardBase extends EntityRegularAIBase implements IPathFindRequester{
	public EntityRegularArmy entity;
	public AStarPathEntity entityPathEntity;
	public float speedTowardTarget;
	
	public EntityAIForwardBase(EntityRegularArmy e,float s){
		this.entity=e;
		this.speedTowardTarget=s;
		setMutexBits(3);
	}
	
	@Override
	public int getTacticsCost(Entity entity, AStarPathPoint start,
			AStarPathPoint current, AStarPathPoint end) {
		return 0;
	}

	@Override
	public boolean isEngineer() {
		return this.entity instanceof EntityEngineer;
	}

	@Override
	public boolean shouldExecute() {
		if(this.entity.getAttackTarget()==null){
			this.entityPathEntity=this.entity.getANavigator().getPathToXYZ
					(this.entity.unit.leader.x,this.entity.unit.leader.y+1,this.entity.unit.leader.z,1.2f,this);
			return this.entityPathEntity!=null;
		}
		return false;
	}
	
	@Override
	public void updateTask(){
		
		if()
	}
	
    public void startExecuting()
    {
        this.entity.getANavigator().setPath(this.entityPathEntity, this.speedTowardTarget);
    }
    
    public boolean continueExecuting(){
    	return !(this.entity.getANavigator().getPath()==null);
    }

}
