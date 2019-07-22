package regulararmy.entity.command;

import java.util.HashMap;
import java.util.Map;

import regulararmy.analysis.DataAnalyzer;
import regulararmy.analysis.FinderSettings;
import regulararmy.core.MonsterRegularArmyCore;
import regulararmy.entity.EntityRegularArmy;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;

public class TestLearningManager extends LearningManagerBase {
	public DataAnalyzer[] analyzers;
	public FinderSettings[] settings;
	public Map<Class,Integer> entityNumMap=new HashMap();
	public TestLearningManager(RegularArmyLeader l){
		this.leader=l;
		this.analyzers=new DataAnalyzer[MonsterRegularArmyCore.entityList.size()];
		this.settings=new FinderSettings[MonsterRegularArmyCore.entityList.size()];
		for(int i=0;i<MonsterRegularArmyCore.entityList.size();i++){
			this.analyzers[i]=new DataAnalyzer();
			this.settings[i]=new FinderSettings();
			this.entityNumMap.put(MonsterRegularArmyCore.entityList.get(i), i);
		}
		
	}
	
	public TestLearningManager(RegularArmyLeader l,NBTTagCompound nbt){
		this.leader=l;
		this.analyzers=new DataAnalyzer[MonsterRegularArmyCore.entityList.size()];
		this.settings=new FinderSettings[MonsterRegularArmyCore.entityList.size()];
		for(int i=0;i<MonsterRegularArmyCore.entityList.size();i++){
			this.entityNumMap.put(MonsterRegularArmyCore.entityList.get(i), i);
		}
		this.readFromNBT(nbt);
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onUpdate() {
		for(int i=0;i<this.analyzers.length;i++){
			System.out.println(i);
			Map<Integer,Integer>[] map=this.analyzers[i].analyze();
			if(map.length==0)continue;
			this.settings[i].setblockCostsFromMap(map[0]);
			this.settings[i].chunkCost=map[1];
			this.analyzers[i].nodes.clear();
		}
		this.leader.endPhase();
	}

	@Override
	public void onEnd() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
		for(int i=0;i<MonsterRegularArmyCore.entityList.size();i++){
			String a="analyzer"+EntityList.classToStringMapping.get((MonsterRegularArmyCore.entityList.get(i)));
			String s="setting"+EntityList.classToStringMapping.get((MonsterRegularArmyCore.entityList.get(i)));
			if(!nbt.hasKey(a)){
				this.analyzers[i]=new DataAnalyzer();
				this.settings[i]=new FinderSettings();
				continue;
			}
			this.analyzers[i]=new DataAnalyzer(nbt.getCompoundTag(a));
		
			this.settings[i]=new FinderSettings(nbt.getCompoundTag(s));
		}
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		for(int i=0;i<MonsterRegularArmyCore.entityList.size();i++){
			nbt.setCompoundTag("analyzer"+EntityList.classToStringMapping.get((MonsterRegularArmyCore.entityList.get(i))), this.analyzers[i].writeToNBT(new NBTTagCompound()));
			nbt.setCompoundTag("setting"+EntityList.classToStringMapping.get((MonsterRegularArmyCore.entityList.get(i))), this.settings[i].writeToNBT(new NBTTagCompound()));
		}
		return nbt;
	}

	@Override
	public DataAnalyzer getAnalyzer(EntityRegularArmy e) {
		return this.analyzers[this.entityNumMap.get(e.getClass())];
	}

	@Override
	public FinderSettings getSettings(EntityRegularArmy e) {
		return this.settings[this.entityNumMap.get(e.getClass())];
	}

}
