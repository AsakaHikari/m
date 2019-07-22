package regulararmy.entity.command;

import java.util.ArrayList;
import java.util.List;

import regulararmy.entity.EntityRegularArmy;
import regulararmy.entity.ai.IBreakBlocksMob;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MonsterUnit {
	public List<EntityRegularArmy> entityList=new ArrayList();
	public int age;
	public RegularArmyLeader leader;
	/**you shouldn't change this value.*/
	public short id;
	public short wave;
	public RequestManager requestManager;
	public boolean canUseEngineer=false;
	
	public MonsterUnit(RegularArmyLeader leader,short id,short wave,EntityRegularArmy... entity){
		this.leader=leader;
		for(int i=0;i<entity.length;i++){
			this.entityList.add(entity[i]);
			if(!this.canUseEngineer && entity[i] instanceof IBreakBlocksMob) this.canUseEngineer=true;
		}
		this.id=id;
		this.requestManager=new RequestManager();
		this.wave=wave;
	}
	
	public MonsterUnit(NBTTagCompound nbt,RegularArmyLeader l,short id){
		this.readFromNBT(nbt);
		this.leader=l;
		this.id=id;
		this.requestManager=new RequestManager();
	}
	
	public void onUpdate(){
		boolean flag=true;
		for(int i=0;i<this.entityList.size();i++){
			if(entityList.get(i).isDead){
				
				entityList.remove(i);
			}else if(entityList.get(i) instanceof IBreakBlocksMob){
				flag=false;
				
			}
		}
		if(flag) this.canUseEngineer=false;
	}
	
	public void spawnAll(){
		for(int i=0;i<this.entityList.size();i++){
			this.getTheWorld().spawnEntityInWorld(entityList.get(i));
			entityList.get(i).onSpawnWithEgg(null);
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		if(nbt==null)nbt=new NBTTagCompound();
		nbt.setInteger("age", age);
		nbt.setShort("wave",wave);
		nbt.setBoolean("canUseEngineer", this.canUseEngineer);
		return nbt;
	}
	
	public MonsterUnit readFromNBT(NBTTagCompound nbt){
		if(nbt==null)return null;
		this.age=nbt.getInteger("age");
		this.wave=nbt.getShort("wave");
		this.canUseEngineer=nbt.getBoolean("canUseEngineer");
		return this;
	}
	
	public List<EntityRegularArmy> getEntityList() {
		return entityList;
	}
	public World getTheWorld() {
		return leader.getTheWorld();
	}
	public int getAge() {
		return age;
	}
	public RequestManager getRequestManager() {
		return this.requestManager;
	}
	
	
}
