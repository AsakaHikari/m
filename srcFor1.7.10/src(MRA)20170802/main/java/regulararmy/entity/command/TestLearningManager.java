package regulararmy.entity.command;

import java.util.HashMap;
import java.util.Map;

import regulararmy.analysis.DataAnalyzer;
import regulararmy.analysis.DataAnalyzerOneToOne;
import regulararmy.analysis.FinderSettings;
import regulararmy.core.MonsterRegularArmyCore;
import regulararmy.entity.EntityRegularArmy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;

public class TestLearningManager extends LearningManagerBase {
	public DataAnalyzer analyzer;
	public FinderSettings setting;
	//public DataAnalyzerOneToOne distanceAnalyzer;
	public static boolean learningLog=false;
	public TestLearningManager(RegularArmyLeader l){
		this.leader=l;
		this.analyzer=new DataAnalyzer();
		this.setting=new FinderSettings();
		//this.distanceAnalyzer=new DataAnalyzerOneToOne();
	}
	
	public TestLearningManager(RegularArmyLeader l,NBTTagCompound nbt){
		this.leader=l;
		this.analyzer=new DataAnalyzer();
		this.setting=new FinderSettings();
		//this.distanceAnalyzer=new DataAnalyzerOneToOne();
		this.readFromNBT(nbt);
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onUpdate() {
		Map<Integer,Integer>[] map=this.analyzer.analyze_average();
		//float[] array=this.distanceAnalyzer.analyze_weightedAverage();
		if(map.length!=0){
			
			this.setting.setblockCostsFromMap(map[0]);
			this.setting.chunkCost=map[1];
			this.setting.dangerousSupporter=map[2];
			
			if(MonsterRegularArmyCore.logEntity){
				for(Map.Entry<Integer,Integer> e:map[2].entrySet()){
					String theName=EntityList.getStringFromID(e.getKey());
					if(theName==null && e.getKey()<0){
						try{
							theName=MonsterRegularArmyCore.entityIDList.get(-e.getKey()-1);
						}catch(Exception exc){
							System.out.println("Unknown id:"+e.getKey()+";"+e.getValue());
							continue;
						}
					}
					if(theName!=null){
						System.out.println(theName+";"+e.getValue());
					}else{
						System.out.println("Unknown id:"+e.getKey()+";"+e.getValue());
					}

				}
			}
			if(MonsterRegularArmyCore.logRegion){
				for(Map.Entry<Integer,Integer> e:map[1].entrySet()){
					int hash=e.getKey();
					int rawx=hash>>20;
					int rawy=hash>>12 & 0xff;
					int rawz=hash & 0xfff;
					int x=(rawx&0x800)==0?16*(rawx & 0x7ff)+8 : -16*(~rawx & 0x7ff)-8;
					int z=(rawz&0x800)==0?16*(rawz & 0x7ff)+8 : -16*(~rawz & 0x7ff)-8;
					int y=rawy*16+8;
					System.out.println("Cost around("+x+","+y+","+z+");"+e.getValue());
				}
			}

			/*
			for(Map.Entry<Integer, Integer> entry:map[2].entrySet()){
				Class<Entity> cls=EntityList.getClassFromID(entry.getKey());
				if(entry.getValue()>8&&cls!=null){
					if(!this.setting.dangerousSupporter.contains(cls)){
						this.setting.dangerousSupporter.add(cls);
						if(learningLog){
							System.out.println(cls.getSimpleName()+"is a supporter");
						}
					}
				}
			}
			*/
		}
			
			/*
			for(Map.Entry<Integer, Integer> entry:map1[0].entrySet()){
				Class<Entity> cls=EntityList.getClassFromID(entry.getKey());
				if(entry.getValue()>20&&cls!=null){
					if(!this.setting.dangerousAttacker.contains(cls)){
						this.setting.dangerousAttacker.add(cls);
						if(learningLog){
							System.out.println(cls.getSimpleName()+"is an attacker");
						}
					}
				}
			}*/
		/*
		if(array.length!=0){
			if(learningLog){
				System.out.println("distance: "+array[0]);
			}
			this.leader.setFightingDistance(array[0]);
		}
		*/
		this.leader.endPhase();
	}

	@Override
	public void onEnd() {

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
			String a="analyzer";
			String s="setting";
			String attacker="attacker";
			String dis="distanceAnalyzer";
			if(!nbt.hasKey(a)){
				this.analyzer=new DataAnalyzer();
				this.setting=new FinderSettings();
			}else{
				this.analyzer=new DataAnalyzer(nbt.getCompoundTag(a));
				
				this.setting=new FinderSettings(nbt.getCompoundTag(s));
				
				//this.distanceAnalyzer=new DataAnalyzerOneToOne(nbt.getCompoundTag(dis));
			}
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
			nbt.setTag("analyzer", this.analyzer.writeToNBT(new NBTTagCompound()));
			nbt.setTag("setting", this.setting.writeToNBT(new NBTTagCompound()));
			//nbt.setTag("distanceAnalyzer", this.distanceAnalyzer.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	public DataAnalyzer getAnalyzer(EntityRegularArmy e) {
		return this.analyzer;
	}

	@Override
	public FinderSettings getSettings(EntityRegularArmy e) {
		return this.setting;
	}

	/*
	public DataAnalyzerOneToOne getDistanceAnalyzer(EntityRegularArmy e){
		return this.distanceAnalyzer;
	}
*/
}
