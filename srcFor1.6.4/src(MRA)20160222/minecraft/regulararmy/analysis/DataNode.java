package regulararmy.analysis;

import net.minecraft.nbt.NBTTagCompound;

public class DataNode {
	public int[][] conditions;
	
	public float result;
	public DataNode(float result,int[]...conditions){
		this.result=result;
		this.conditions=conditions;
	}
	
	public DataNode(NBTTagCompound nbt){
		readFromNBT(nbt);
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		for(int i=0;i<conditions.length;i++){
			nbt.setIntArray("cond"+i, conditions[i]);
		}
		nbt.setFloat("result", this.result);
		return nbt;
	}
	
	public void readFromNBT(NBTTagCompound nbt){
		int i=0;
		while(true){
			if(!nbt.hasKey("cond"+i))break;
			i++;
		}
		this.conditions=new int[i][];
		for(int j=0;j<i;j++){
			this.conditions[j]=nbt.getIntArray("cond"+j);
		}
		this.result=nbt.getFloat("result");
	}
}
