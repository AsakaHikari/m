package regulararmy.entity.ai;

import java.util.ArrayList;
import java.util.List;

import regulararmy.core.Coord;
import regulararmy.entity.command.RegularArmyLeader;
import regulararmy.entity.command.RequestManager;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.util.ForgeDirection;

public class EngineerRequest {
	public Coord coord;
	public RequestType isSet;
	public int number;
	public boolean hasApproved;
	public boolean isEnable;
	public RequestManager manager;
	public Coord waitingPoint;
	public ForgeDirection dir;
	
	public EngineerRequest(Coord c,Coord waitingPoint,RequestType isSet,int number,RequestManager manager){
		this.coord=c;
		this.waitingPoint=waitingPoint;
		this.isSet=isSet;
		this.number=number;
		this.manager=manager;
	}
	
	public EngineerRequest(Coord c,RequestType isSet){
		this.coord=c;
		this.isSet=isSet;
	}
	
	public double getSquareDistance(double posX,double posY,double posZ){
		double i=this.coord.x+0.5-posX;
		double j=this.coord.y+0.5-posY;
		double k=this.coord.z+0.5-posZ;
		return i*i+j*j+k*k;
	}
	
	public double getSquareDistance(Coord c){
		return getSquareDistance(c.x+0.5,c.y+0.5,c.z+0.5);
	}
	
	public void approve(){
		this.hasApproved=true;
	}
	
	public void fulfill(){
		manager.requested.remove(this);
		this.isEnable=false;
	}
	
	@Override
	public String toString(){
		String s="";
		switch(this.isSet){
		case PUT_BLOCK:
			s="PutBlock";
			break;
		case BREAK:
			s="Break";
			break;
		case PUT_LADDER:
			s="PutLadder";
			break;
		default:
			break;
		}
		return s+" at "+this.coord.toString();
	}
	
	public static enum RequestType{
		PUT_BLOCK,PUT_LADDER,BREAK
	}
}
