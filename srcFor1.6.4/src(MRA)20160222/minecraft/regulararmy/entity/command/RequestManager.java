package regulararmy.entity.command;

import java.util.ArrayList;
import java.util.List;

import regulararmy.core.Coord;
import regulararmy.entity.ai.EngineerRequest;
import net.minecraft.entity.EntityLiving;

public class RequestManager {
public List<EngineerRequest> requested=new ArrayList();

public void request(Coord c,boolean isSet,Coord waitingPoint){
	
	for(int i=0;i<requested.size();i++){
		EngineerRequest e=requested.get(i);
		if(e.coord.equals(c)){
			e.number++;
			//System.out.println("requested on:"+c.x+","+c.y+","+c.z+" as "+(isSet?"set":"break")+" for "+e.number+" mobs");
			return;
		}
	}
	//System.out.println("requested on:"+c.x+","+c.y+","+c.z+" as "+(isSet?"set":"break")+" for 1 mob");
	EngineerRequest newe=new EngineerRequest(c,waitingPoint,isSet,1,this);
	requested.add(newe);
	newe.isEnable=true;
	
}

public void delete(Coord c){
	for(int i=0;i<requested.size();i++){
		EngineerRequest e=requested.get(i);
		if(e.coord.equals(c)){
			if(e.number<=1){
				requested.remove(e);
				e.isEnable=false;
			}else{
				e.number--;
			}
			return;
		}
	}
}

public EngineerRequest getNearest(EntityLiving e){
	return getNearest((int)e.posX,(int)e.posY,(int)e.posZ);
}

public EngineerRequest getNearest(int x,int y,int z){
	if(requested.size()==0)return null;
	EngineerRequest e=requested.get(0);
	int n=e.getSquareDistance(x, y, z);
	for(int i=1;i<requested.size();i++){
		if(requested.get(i).getSquareDistance(x, y, z)<n)e=requested.get(i);
	}
	return e;
}

public EngineerRequest getNearest(Coord c){
	return getNearest(c.x,c.y,c.z);
}
public EngineerRequest getEqual(Coord c){
	for(int i=0;i<requested.size();i++){
		EngineerRequest e=requested.get(i);
		if(e.coord.equals(c)){
			return e;
		}
	}
	return null;
}

public boolean isThereNotApproved(){
	for(int i=0;i<requested.size();i++){
		if(!requested.get(i).hasApproved)return true;
	}
	return false;
}

}
