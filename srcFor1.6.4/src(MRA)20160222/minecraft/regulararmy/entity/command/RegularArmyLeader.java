package regulararmy.entity.command;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import regulararmy.analysis.DataAnalyzer;
import regulararmy.core.Coord;
import regulararmy.entity.*;
import regulararmy.entity.ai.EngineerRequest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class RegularArmyLeader {
	//public List<Coord> notToBreak=new ArrayList();
	//public List<Coord> notToSet=new ArrayList();
	public List<MonsterUnit> unitList=new ArrayList();
	public World theWorld;
	public int x,y,z;
	public SpawnManagerBase spawnManager;
	public LearningManagerBase analyzerManager;
	public int age;
	/**you shouldn't change this value.*/
	public byte id;
	public boolean isAnalyzing=false;
	public short wave=1;
	public int hp=10000;
	public int maxWave=40;
	
	public RegularArmyLeader(World w,int x,int y,int z,byte id){
		this.theWorld=w;
		this.x=x;
		this.y=y;
		this.z=z;
		this.id=id;
		this.spawnManager=new FaintSpawnManager(this);
		this.analyzerManager=new TestLearningManager(this);
		
	}
	
	public RegularArmyLeader(World w,NBTTagCompound nbt,byte id){
		this.theWorld=w;
		this.id=id;
		this.spawnManager=new FaintSpawnManager(this);
		this.analyzerManager=new TestLearningManager(this,nbt);
		
		this.readFromNBT(nbt);
	}
	
	
	public DataAnalyzer getAnalyzer(EntityRegularArmy e){
		return this.analyzerManager.getAnalyzer(e);
	}
	

	public void onStart(){
		 this.spawnManager.onStart();
	}
	
		 public void onUpdate() {
			 if(theWorld.isRemote)return;
			 if(this.hp<1){
				 if(this.isAnalyzing){
					 this.analyzerManager.onEnd();
				 }else{
					 this.spawnManager.onEnd();
				 }
				 for(int i=0;i<this.unitList.size();i++){
						 for(int j=0;j<this.unitList.get(i).entityList.size();j++){
							 this.unitList.get(i).entityList.get(j).setDead();
						 }
						 
				 }
			 }
			 if(this.isAnalyzing){
				 this.analyzerManager.onUpdate();
			 }else{
				 this.spawnManager.onUpdate();
			 }
		  age++;
		 }

		 public MonsterUnit addUnit(EntityRegularArmy... entity){
			 MonsterUnit unit=new MonsterUnit(this,(short) this.unitList.size(),this.wave,entity);
			 this.unitList.add(unit);
			 for(EntityRegularArmy i:entity){
				 i.unit=unit;
			 }
			 return unit;
		 }
		 
		 public void endPhase(){
			 if(this.isAnalyzing){
				 this.analyzerManager.onEnd();
				 this.isAnalyzing=false;
				 this.wave++;
				 for(int i=0;i<this.unitList.size();i++){
					 if(this.unitList.get(i).wave<this.wave-1){
						 for(int j=0;j<this.unitList.get(i).entityList.size();j++){
							 this.unitList.get(i).entityList.get(j).setDead();
						 }
					 }
						 
				 }
				 this.spawnManager.onStart();
			 }else{
				 this.spawnManager.onEnd();
				 this.isAnalyzing=true;
				 this.analyzerManager.onStart();
			 }
		 }
		 
		 public NBTTagCompound writeToNBT(NBTTagCompound nbt){
			 nbt.setInteger("x", x);
			 nbt.setInteger("y", y);
			 nbt.setInteger("z", z);
			 nbt.setInteger("age", age);
			 for(int i=0;i<unitList.size();i++){
				 nbt.setCompoundTag("unit"+i, unitList.get(i).writeToNBT(new NBTTagCompound()));
			 }
			 this.spawnManager.writeToNBT(nbt);
			 this.analyzerManager.writeToNBT(nbt);
			 return nbt;
		 }
		 
		 public RegularArmyLeader readFromNBT(NBTTagCompound nbt){
			 this.x=nbt.getInteger("x");
			 this.y=nbt.getInteger("y");
			 this.z=nbt.getInteger("z");
			 this.age=nbt.getInteger("age");
			 for(int i=0;i<25565;i++){
				 if(nbt.hasKey("unit"+i)){
					 this.unitList.add(new MonsterUnit(nbt.getCompoundTag("unit"+i), this, (short)i));
				 }else{
					 break;
				 }
			 }
			 this.spawnManager.readFromNBT(nbt);
			 return this;
		 }

		public List<EntityRegularArmy> getEntityList() {
			List entityList=new ArrayList();
			for(int i=0;i<this.unitList.size();i++){
				entityList.addAll(this.unitList.get(i).entityList);
			}
			return entityList;
		}

		public World getTheWorld() {
			return theWorld;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getZ() {
			return z;
		}

}
