package regulararmy.entity.command;

import java.util.ArrayList;
import java.util.List;

import regulararmy.core.Coord;
import regulararmy.entity.ai.EngineerRequest;
import regulararmy.entity.ai.EngineerRequest.RequestType;
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
	EngineerRequest newe=new EngineerRequest(c,waitingPoint,RequestType.BREAK,1,this);
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
	return getNearest(e.posX,e.posY,e.posZ);
}

public EngineerRequest getNearest(double posX,double posY,double posZ){
	if(requested.size()==0)return null;
	EngineerRequest e=requested.get(0);
	double n=e.getSquareDistance(posX, posY, posZ);
	for(int i=1;i<requested.size();i++){
		if(requested.get(i).getSquareDistance(posX, posY, posZ)<n)e=requested.get(i);
	}
	return e;
}

public EngineerRequest getNearest(Coord c){
	return getNearest(c.x+0.5,c.y+0.5,c.z+0.5);
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
