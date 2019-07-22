package regulararmy.core;

public class Coord {
	public int x;
	public int y;
	public int z;
	public Coord(int x,int y,int z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public void set(int x,int y,int z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public float getSquareDistance(){
		return x*x+y*y+z*z;
	}
	
	public Coord getRelativeCoord(Coord c){
		return new Coord(x-c.x,y-c.y,z-c.z);
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Coord){
			return ((Coord) o).x==this.x&&((Coord) o).y==this.y&&((Coord) o).z==this.z;
		}
		return false;
	}
	
	@Override
	public String toString(){
		return x+","+y+","+z;
	}
}
