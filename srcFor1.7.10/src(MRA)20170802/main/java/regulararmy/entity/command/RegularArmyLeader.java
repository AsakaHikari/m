package regulararmy.entity.command;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import regulararmy.analysis.DataAnalyzer;
import regulararmy.analysis.DataAnalyzerOneToOne;
import regulararmy.core.Coord;
import regulararmy.core.MonsterRegularArmyCore;
import regulararmy.entity.*;
import regulararmy.entity.ai.EngineerRequest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class RegularArmyLeader {
	//public List<Coord> notToBreak=new ArrayList();
	//public List<Coord> notToSet=new ArrayList();
	public List<MonsterUnit> unitList=new ArrayList();
	public World theWorld;
	public int x,y,z;
	public SpawnManagerBase spawnManager;
	public TestLearningManager analyzerManager;
	public int age;
	public float fightingDistance=1;

	/**you shouldn't change this value.*/
	public byte id;
	public boolean isAnalyzing=false;
	public int wave=1;
	public int hp=10000;
	public int lastHp=10000;
	public int maxWave=40;
	public int maxHP=10000;

	public RegularArmyLeader(World w,int x,int y,int z,byte id){
		this.theWorld=w;
		this.x=x;
		this.y=y;
		this.z=z;
		this.id=id;
		this.maxWave=MonsterRegularArmyCore.maxWave;
		this.maxHP=MonsterRegularArmyCore.maxHP;
		this.spawnManager=new FaintSpawnManager(this);
		//this.spawnManager=new TestSpawnManager(this);
		this.analyzerManager=new TestLearningManager(this);

	}

	public RegularArmyLeader(World w,NBTTagCompound nbt,byte id){
		this.theWorld=w;
		this.id=id;
		this.maxWave=MonsterRegularArmyCore.maxWave;
		this.maxHP=MonsterRegularArmyCore.maxHP;
		this.spawnManager=new FaintSpawnManager(this);
		//this.spawnManager=new TestSpawnManager(this);
		this.analyzerManager=new TestLearningManager(this,nbt);

		this.readFromNBT(nbt);
	}


	public DataAnalyzer getAnalyzer(EntityRegularArmy e){
		return this.analyzerManager.getAnalyzer(e);
	}
/*
	public DataAnalyzerOneToOne getDistanceAnalyzer(EntityRegularArmy e){
		return this.analyzerManager.getDistanceAnalyzer(e);
	}
*/
	public void onStart(){
		this.spawnManager.onStart();
		this.hp=this.maxHP;
		this.lastHp=this.maxHP;
		this.wave=MonsterRegularArmyCore.firstWave;
	}

	public void onUpdate() {
		if(theWorld.isRemote)return;
		List<Entity> entityList=this.theWorld.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(x+0.5-2, y+0.5-2, z+0.5-2,x+0.5+2, y+0.5+2, z+0.5+2));
		for(Entity e:entityList){
			if(e instanceof EntityRegularArmy){
				this.hp-=3;
			}
		}
		if(this.hp*10<this.maxHP*3&&this.lastHp*10>=this.maxHP*3){
			List<EntityPlayer> players=this.theWorld.playerEntities;
			for(int i=0;i<players.size();i++){
				players.get(i).addChatMessage(new ChatComponentText("30% HP left"));
			}
		}
		if(this.hp*10<this.maxHP*7&&this.lastHp*10>=this.maxHP*7){
			List<EntityPlayer> players=this.theWorld.playerEntities;
			for(int i=0;i<players.size();i++){
				players.get(i).addChatMessage(new ChatComponentText("70% HP left"));
			}
		}
		if(this.age%4096==4095){
			List<EntityPlayer> players=this.theWorld.playerEntities;
			for(int i=0;i<players.size();i++){
				players.get(i).addChatMessage(new ChatComponentText(this.hp*100/this.maxHP+"% HP left"));
			}
		}
		if(this.wave==this.maxWave&&this.getEntityList().isEmpty()){
			List<EntityPlayer> players=this.theWorld.playerEntities;
			for(int i=0;i<players.size();i++){
				players.get(i).addChatMessage(new ChatComponentText("Oh yeah. You win!"));
			}
			this.onEnd(false);
			return;
		}
		this.lastHp=this.hp;
		if(this.hp<1){
			List<EntityPlayer> players=this.theWorld.playerEntities;
			for(int i=0;i<players.size();i++){
				players.get(i).addChatMessage(new ChatComponentText("You lose. At wave "+this.wave));
			}
			this.onEnd(true);
		}
		if(this.age%40==0 && this.age>200){
			for(int i=0;i<MonsterRegularArmyCore.results.length;i++){
				if(((this.age/60)%MonsterRegularArmyCore.resultsTier[i])==0){
					if(this.theWorld.rand.nextInt(50)<this.wave){
						EntityItem entityItem=new EntityItem(this.theWorld,this.x+0.5,this.y+1.5,this.z+0.5,new ItemStack(MonsterRegularArmyCore.results[i],1,MonsterRegularArmyCore.resultsDamage[i]));
						this.theWorld.spawnEntityInWorld(entityItem);
					}
				}
			}
		}
		if(this.isAnalyzing){
			this.analyzerManager.onUpdate();
		}else{
			this.spawnManager.onUpdate();
		}
		for(int i=0;i<this.unitList.size();i++){
			this.unitList.get(i).onUpdate();
		}
		age++;
	}

	public void onEnd(boolean doBreak){
		if(this.isAnalyzing){
			this.analyzerManager.onEnd();
		}else{
			this.spawnManager.onEnd();
		}
		for(int i=0;i<this.unitList.size();i++){
			MonsterUnit unit=this.unitList.get(i);
			for(int j=0;j<unit.entityList.size();j++){
				EntityRegularArmy entity=unit.entityList.get(j);
				if(unit.ridingEntity!=0&&entity.ridingEntity!=null){
					entity.ridingEntity.setDead();
				}
				entity.setDead();
			}

		}
		if(doBreak){
			this.theWorld.func_147480_a(this.x,this.y,this.z,false);
		}else{
			this.theWorld.setBlockMetadataWithNotify(this.x,this.y,this.z, 0, 3);
		}
		MonsterRegularArmyCore.leaders[this.id]=null;
	}

	public MonsterUnit addUnit(EntityRegularArmy... entity){
		MonsterUnit unit=new MonsterUnit(this,this.wave,entity);
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

			if(this.wave>this.maxWave){
				List<EntityPlayer> players=this.theWorld.playerEntities;
				for(int i=0;i<players.size();i++){
					players.get(i).addChatMessage(new ChatComponentText("Oh yeah. You win!"));
				}
				this.onEnd(false);
				return;
			}
			List<EntityPlayer> players=this.theWorld.playerEntities;
			for(int i=0;i<players.size();i++){
				players.get(i).addChatMessage(new ChatComponentText("Wave "+this.wave));
			}
			for(int i=0;i<this.unitList.size();i++){
				if(this.unitList.get(i).wave<=this.wave-(MonsterRegularArmyCore.waveMobVanish)){
					MonsterUnit unit=this.unitList.get(i);
					for(int j=0;j<unit.entityList.size();j++){
						EntityRegularArmy entity=unit.entityList.get(j);
						if(unit.ridingEntity!=0&&entity.ridingEntity!=null){
							entity.ridingEntity.setDead();
						}
						entity.setDead();
					}
					this.unitList.remove(i);
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
		nbt.setInteger("wave", wave);
		nbt.setInteger("hp", this.hp);
		nbt.setFloat("fightingDistance", this.fightingDistance);
		for(int i=0;i<unitList.size();i++){
			nbt.setTag("unit"+i, unitList.get(i).writeToNBT(new NBTTagCompound()));
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
		this.wave=nbt.getInteger("wave");
		this.hp=nbt.getInteger("hp");
		this.fightingDistance=nbt.getFloat("fightingDistance");
		for(int i=0;i<25565;i++){
			if(nbt.hasKey("unit"+i)){
				this.unitList.add(new MonsterUnit(nbt.getCompoundTag("unit"+i), this));
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
	public float getFightingDistance() {
		return fightingDistance;
	}

	public void setFightingDistance(float fightingDistance) {
		this.fightingDistance = fightingDistance;
	}
	
}
