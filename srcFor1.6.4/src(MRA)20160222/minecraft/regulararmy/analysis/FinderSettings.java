package regulararmy.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;

public class FinderSettings {
	public int[] blocksCost=new int[4096];
	public Map entityDanger=new HashMap<String,Float>();
	public Map chunkCost=new HashMap<Integer,Integer>();
	
	public FinderSettings(){
		
	}
	
	public FinderSettings(NBTTagCompound nbt){
		this.readFromNBT(nbt);
	}
	
	public void setblockCostsFromMap(Map<Integer,Integer> map){
		Map.Entry<Integer,Integer>[] entries=map.entrySet().toArray(new Map.Entry[0]);
		for(int i=0;i<entries.length;i++){
			this.blocksCost[entries[i].getKey()]=entries[i].getValue();
		}
	}
	
	public int getTotalBlocksCost(int[] ids){
		int cost=0;
		for(int i=0;i<ids.length;i++){
			if(ids[i]==0)continue;
			cost+=this.blocksCost[ids[i]]<0?0:this.blocksCost[ids[i]];
			//System.out.println("id:"+ids[i]+"'s cost is "+this.blocksCost[ids[i]]);
		}
		return cost;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		nbt.setIntArray("blocksCost", this.blocksCost);
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound nbt){
		this.blocksCost=nbt.getIntArray("blocksCost");
	}
}
