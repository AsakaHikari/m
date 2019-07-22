package regulararmy.entity.command;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import regulararmy.entity.*;
import regulararmy.entity.ai.AIAnimalMountedByEntity;

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
			EntityRegularArmy e1=new EntityEngineer(this.leader.theWorld);
			MonsterUnit u=this.leader.addUnit(e);
		
			e.setPosition(this.leader.x-20, this.leader.y-20, this.leader.z+10);
			e1.setPosition(this.leader.x-30, this.leader.y, this.leader.z-30);
			
			u.spawnAll();
		}
		if(this.leader.age==400){
			this.leader.endPhase();
		}
		if(this.leader.age==500&&!this.leader.theWorld.isRemote){
			
			EntityRegularArmy  e=new EntityCannon(this.leader.theWorld);
			EntityRegularArmy e1=new EntityEngineer(this.leader.theWorld);
			EntityRegularArmy  e2=new EntityFastZombie(this.leader.theWorld);
			MonsterUnit u=this.leader.addUnit(e2);
			
			e.setPosition(this.leader.x+30, this.leader.y, this.leader.z+20);
			e1.setPosition(this.leader.x+30, this.leader.y, this.leader.z+0);
			e2.setPosition(this.leader.x+30, this.leader.y, this.leader.z+0);
			//e1.setPosition(this.leader.x+10, this.leader.y+5, this.leader.z+10);
			//u.spawnAll();
			
			
		}
	}

	@Override
	public void onEnd() {
		
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		
	}

}
