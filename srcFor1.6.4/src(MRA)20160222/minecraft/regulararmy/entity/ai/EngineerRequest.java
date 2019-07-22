package regulararmy.entity.ai;

import java.util.ArrayList;
import java.util.List;

import regulararmy.core.Coord;
import regulararmy.entity.command.RegularArmyLeader;
import regulararmy.entity.command.RequestManager;
import net.minecraft.entity.EntityLiving;

public class EngineerRequest {
	public Coord coord;
	public boolean isSet;
	public int number;
	public boolean hasApproved;
	public boolean isEnable;
	public RequestManager manager;
	public Coord waitingPoint;
	
	public EngineerRequest(Coord c,Coord waitingPoint,boolean isSet,int number,RequestManager manager){
		this.coord=c;
		this.waitingPoint=waitingPoint;
		this.isSet=isSet;
		this.number=number;
		this.manager=manager;
	}
	
	public EngineerRequest(Coord c,boolean isSet){
		this.coord=c;
		this.isSet=isSet;
	}
	
	public int getSquareDistance(int x,int y,int z){
		int i=this.coord.x-x;
		int j=this.coord.y-y;
		int k=this.coord.z-z;
		return i*i+j*j+k*k;
	}
	
	public int getSquareDistance(Coord c){
		return getSquareDistance(c.x,c.y,c.z);
	}
	
	public void approve(){
		this.hasApproved=true;
	}
	
	public void fulfill(){
		manager.requested.remove(this);
		this.isEnable=false;
	}
}
