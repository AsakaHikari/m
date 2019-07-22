package regulararmy.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

public class DataAnalyzer {
	public List<DataNode> nodes=new ArrayList(100);
	
	public DataAnalyzer(){
		
	}
	
	public DataAnalyzer(NBTTagCompound nbt){
		this.readFromNBT(nbt);
	}
	
	public Map<Integer,Integer>[] analyze(){
		if(nodes.isEmpty())return new Map[0];
		Map<Integer,Integer>[] map=new Map[nodes.get(0).conditions.length];
		System.out.println(map.length);
		for(int i=0;i<map.length;i++){
			map[i]=new HashMap(30);
			Map<Integer,List<Float>> resultsMap=new HashMap(30);
			for(int j=0;j<nodes.size();j++){
				DataNode node=this.nodes.get(j);
				for(int k=0;k<node.conditions[i].length;k++){
				List<Float> list=resultsMap.get(node.conditions[i][k]);
					if(list==null){
						list=new ArrayList(60);
						resultsMap.put(node.conditions[i][k],list);
					}
					list.add(node.result);
				}
				
			}
			Integer[] array=resultsMap.keySet().toArray(new Integer[0]);
			for(int j=0;j<resultsMap.size();j++){
				Integer key=array[j];
				List list=resultsMap.get(key);
				float value=0;
				for(int k=0;k<list.size();k++){
					value+=(Float)list.get(k);
				}
				value/=list.size();
				map[i].put(key,(int)value);
				System.out.println("result:"+key+","+value);
			}
		}
		return map;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		DataNode[] nodeArray=this.nodes.toArray(new DataNode[0]);
		for(int i=0;i<nodeArray.length;i++){
			nbt.setCompoundTag("node"+i,nodeArray[i].writeToNBT(new NBTTagCompound()));
		}
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound nbt){
		for(int i=0;true;i++){
			if(!nbt.hasKey("node"+i))break;
			DataNode node=new DataNode(nbt.getCompoundTag("node"+i));
			this.nodes.add(node);
		}
	}
}
