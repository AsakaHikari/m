package regulararmy.entity.command;

import net.minecraft.nbt.NBTTagCompound;
import regulararmy.entity.EntityEngineer;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.entity.EntitySniperSkeleton;

public class TestSpawnManager extends SpawnManagerBase {

	public TestSpawnManager(RegularArmyLeader leader) {
		super(leader);
	}

	@Override
	public void onStart() {
		
	}

	@Override
	public void onUpdate() {
		if(this.leader.age==0&&!this.leader.theWorld.isRemote){
			
			EntityRegularArmy  e=new EntitySniperSkeleton(this.leader.theWorld);
			//EntityRegularArmy e1=new EntityEngineer(this.leader.theWorld);
			MonsterUnit u=this.leader.addUnit(e);
			
			e.setPosition(this.leader.x+20, this.leader.y+5, this.leader.z+0);
			//e1.setPosition(this.leader.x+10, this.leader.y+5, this.leader.z+10);
			u.spawnAll();
		}
		if(this.leader.age==200){
			this.leader.endPhase();
		}
		if(this.leader.age==250&&!this.leader.theWorld.isRemote){
			
			EntityRegularArmy  e=new EntitySniperSkeleton(this.leader.theWorld);
			EntityRegularArmy e1=new EntityEngineer(this.leader.theWorld);
			EntityRegularArmy  e2=new EntitySniperSkeleton(this.leader.theWorld);
			MonsterUnit u=this.leader.addUnit(e,e1,e2);
			
			e.setPosition(this.leader.x+50, this.leader.y-5, this.leader.z+0);
			e1.setPosition(this.leader.x+50, this.leader.y-5, this.leader.z+0);
			e2.setPosition(this.leader.x+50, this.leader.y-5, this.leader.z+0);
			//e1.setPosition(this.leader.x+10, this.leader.y+5, this.leader.z+10);
			u.spawnAll();
			
		}
	}

	@Override
	public void onEnd() {
		
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
