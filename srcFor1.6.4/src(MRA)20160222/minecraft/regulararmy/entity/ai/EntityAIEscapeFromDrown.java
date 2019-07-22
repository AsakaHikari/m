package regulararmy.entity.ai;

import regulararmy.entity.EntityRegularArmy;
import regulararmy.pathfinding.AStarPathEntity;

public class EntityAIEscapeFromDrown extends EntityRegularAIBase {
	public EntityRegularArmy theEntity;
	public AStarPathEntity pathEntity;
	public short findingFailPenalty=0;

	public EntityAIEscapeFromDrown(EntityRegularArmy e){
		this.theEntity=e;
		this.setMutexBits(3);
	}
	@Override
	public boolean shouldExecute() {
		
		if(theEntity.getAir()>60)return false;
		if(this.findingFailPenalty-->0)return false;
		theEntity.getANavigator().canUseEngineer=0;
		this.pathEntity=theEntity.getANavigator().getPathToXYZ(theEntity.posX+theEntity.worldObj.rand.nextInt(7)-3, theEntity.posY+theEntity.worldObj.rand.nextInt(5)-2, theEntity.posZ+theEntity.worldObj.rand.nextInt(7)-3, 1.4f, this);
		theEntity.getANavigator().canUseEngineer=2;
		if(this.pathEntity==null){
			this.findingFailPenalty=15;
			return false;
		}else{
			return true;
		}
		
	}
	
	@Override
	public void startExecuting() {
		theEntity.getANavigator().setPath(this.pathEntity, this.theEntity.getSpeed());
	}
	@Override
	public boolean continueExecuting(){
		return theEntity.getAir()<=100;
	}

}
