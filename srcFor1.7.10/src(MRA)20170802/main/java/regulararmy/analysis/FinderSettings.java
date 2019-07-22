package regulararmy.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import regulararmy.core.MonsterRegularArmyCore;
import regulararmy.entity.EntityRegularArmy;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.nbt.NBTTagCompound;

public class FinderSettings {
	public int[] blocksCost=new int[4096];
	public Map<Integer,Integer> dangerousSupporter=new HashMap();
	public Map<Integer,Integer> chunkCost=new HashMap();
	public Map<Integer,Float> crowdCost=new HashMap();
	
	public FinderSettings(){
		
	}
	
	public FinderSettings(NBTTagCompound nbt){
		this.readFromNBT(nbt);
	}
	
	public void setblockCostsFromMap(Map<Integer,Integer> map){
		Map.Entry<Integer,Integer>[] entries=map.entrySet().toArray(new Map.Entry[0]);
		int value=0;
		for(int i=0;i<entries.length;i++){
			value+=entries[i].getValue();
		}
		value/=entries.length;

		for(int i=0;i<entries.length;i++){
			this.blocksCost[entries[i].getKey()]=entries[i].getValue()>value?entries[i].getValue()-value:0;
			if(MonsterRegularArmyCore.logBlock){
				System.out.println(Block.getBlockById(entries[i].getKey()).getUnlocalizedName()+"'s cost: "+this.blocksCost[entries[i].getKey()]);

			}
		}
	}
	
	public int getTotalBlocksCost(int[] ids){
		int cost=0;
		int[] idAppeared=new int[ids.length];
		for(int i=0;i<ids.length;i++){
			if(ids[i]==0)continue;
			for(int j=0;j<idAppeared.length;j++){
				if(idAppeared[j]==ids[i]){
					break;
				}
				if(idAppeared[j]==0){
					cost+=this.blocksCost[ids[i]]<0?0:this.blocksCost[ids[i]];
					idAppeared[j]=ids[i];
				}
			}
			//System.out.println("id:"+ids[i]+"'s cost is "+this.blocksCost[ids[i]]);
		}
		return cost;
	}
	
	public void addCrowdCost(Map<Integer,Float> map){
		for(Entry<Integer,Float> entry:map.entrySet()){
			if(this.crowdCost.containsKey(entry.getKey())){
				this.crowdCost.put(entry.getKey(), this.crowdCost.get(entry.getKey())+entry.getValue());
				//System.out.println("chunk:"+entry.getKey()+"'s new cost is"+this.crowdCost.get(entry.getKey()));
			}else{
				this.crowdCost.put(entry.getKey(), entry.getValue());
				//System.out.println("chunk:"+entry.getKey()+"'s cost is"+this.crowdCost.get(entry.getKey()));
			}
		}
	}
	
	public void removeCrowdCost(Map<Integer,Float> map){
		for(Entry<Integer,Float> entry:map.entrySet()){
			if(this.crowdCost.containsKey(entry.getKey())){
				float value= this.crowdCost.get(entry.getKey())-entry.getValue();
				if(value<0.1f){
					this.crowdCost.remove(entry.getKey());
				}else{
					this.crowdCost.put(entry.getKey(),value);
				}
			}
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		nbt.setIntArray("blocksCost", this.blocksCost);
		Integer[] supporterNameArray=this.dangerousSupporter.keySet().toArray(new Integer[0]);
		Integer[] supporterCostArray=this.dangerousSupporter.values().toArray(new Integer[0]);
		
		for(int i=0;i<supporterNameArray.length;i++){
			/*
			String theName=EntityList.getStringFromID(supporterNameArray[i]);
			if(supporterNameArray[i]==0){
				theName="null";
			}else if(theName==null){
				theName=MonsterRegularArmyCore.entityIDList.get(-supporterNameArray[i]-1);
			}*/
			nbt.setInteger("supporterName"+i, supporterNameArray[i]);
			nbt.setInteger("supporterCost"+i,supporterCostArray[i]);
		}
		Integer[] chunkHashArray=this.chunkCost.keySet().toArray(new Integer[0]);
		Integer[] chunkCostArray=this.chunkCost.values().toArray(new Integer[0]);
		for(int i=0;i<chunkHashArray.length;i++){
			nbt.setInteger("chunkHash"+i, chunkHashArray[i]);
			nbt.setInteger("chunkCost"+i, chunkCostArray[i]);
		}
		
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound nbt){
		this.blocksCost=nbt.getIntArray("blocksCost");
		/*
		Map stringToIDMapping=null;
		try {
			stringToIDMapping = (Map) ReflectionHelper.findField(EntityList.class,
					ObfuscationReflectionHelper.remapFieldNames(EntityList.class.getName(),"stringToIDMapping","field_75622_f")).get(new HashMap());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		 */
		//if(stringToIDMapping!=null){

		for(int i=0;nbt.hasKey("supporterName"+i);i++){
			Integer id=nbt.getInteger("supporterName"+i);
			/*
				Integer id=(Integer) stringToIDMapping.get(theName);
				if(id==null ||id==0  ){
					id=EntityRegularArmy.getCustomEntitySharedIDFromName(theName);
				}*/
			this.dangerousSupporter.put(id, nbt.getInteger("supporterCost"+i));
		}
		//}
		for(int i=0;nbt.hasKey("chunkHash"+i);i++){
			this.chunkCost.put(nbt.getInteger("chunkHash"+i), nbt.getInteger("chunkCost"+i));
		}
	}
}
