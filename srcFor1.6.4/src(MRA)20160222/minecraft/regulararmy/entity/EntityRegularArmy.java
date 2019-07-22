package regulararmy.entity;

import regulararmy.analysis.DataAnalyzer;
import regulararmy.analysis.DataNode;
import regulararmy.analysis.FinderSettings;
import regulararmy.core.*;
import regulararmy.entity.command.MonsterUnit;
import regulararmy.pathfinding.AStarPathEntity;
import regulararmy.pathfinding.AStarPathNavigate;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public abstract class EntityRegularArmy extends EntityMob{
	public AStarPathEntity pathToEntity;
	public AStarPathNavigate navigator;
	public MonsterUnit unit;
	public double lastDistance=0;
    public double lastDistanceDifference=0;
	public double lastDistanceDifferenceAmount=0;
	public short lastDistanceDifferenceAmountNum=0;


	public EntityRegularArmy(World par1World) {
		super(par1World);
		this.navigator=new AStarPathNavigate(this,par1World);
		this.unit=null;
	}
	
	public EntityRegularArmy(World par1World,MonsterUnit unit) {
		super(par1World);
		this.navigator=new AStarPathNavigate(this,par1World);
		this.unit=unit;
		/*
		if(isDummy){
			for(int i=0;i<this.tasks.taskEntries.size();i++){
				this.tasks.removeTask(((EntityAITaskEntry)this.tasks.taskEntries.get(i)).action);
			}
		}
		*/
	}

	
	@Override
	public void updateAITasks(){
		Vec3 vec3=this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, (double)this.navigator.getPathableYPos(), this.posZ);
		
		if(this.getMoveHelper().isUpdating()){
			
			this.lastDistanceDifference=this.lastDistance-vec3.distanceTo(this.navigator.currentPath.getVectorFromIndex(this,this.navigator.currentPath.getCurrentPathIndex()));
			this.lastDistanceDifferenceAmount+=this.lastDistanceDifference;
			this.lastDistanceDifferenceAmountNum++;
		}
		super.updateAITasks();
        
		this.navigator.onUpdateNavigation();
		
		if(this.navigator.currentPath!=null){
			this.lastDistance=vec3.distanceTo(this.navigator.currentPath.getVectorFromIndex(this,this.navigator.currentPath.getCurrentPathIndex()));
		}
	}
	
	public AStarPathNavigate getANavigator(){
		return this.navigator;
	}
	
	@Override
	public void onDeath(DamageSource par1DamageSource){
		super.onDeath(par1DamageSource);
		
		if(this.pathToEntity!=null){
			this.pathToEntity.disablePath(this.unit.getRequestManager());
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
		if(!this.worldObj.isRemote){
		this.addNode(new DataNode(par2*8,this.getBlockIDsAround(),new int[]{this.getChunkHash()}));
		}
		return super.attackEntityFrom(par1DamageSource, par2);
	}
	
	@Override
	public void onUpdate(){
		if(!this.worldObj.isRemote&&this.ticksExisted%32==0){
			//System.out.println(this.lastDistanceDifferenceAmountNum==0?0:this.lastDistanceDifferenceAmount/this.lastDistanceDifferenceAmountNum);
			this.addNode(new DataNode(-4.0f+(float)(this.lastDistanceDifferenceAmountNum==0?0:this.lastDistanceDifferenceAmount/this.lastDistanceDifferenceAmountNum*40)
					,this.getBlockIDsAround()
					,new int[]{this.getChunkHash()}));
			this.lastDistanceDifferenceAmount=0;
			this.lastDistanceDifferenceAmountNum=0;
		}
		super.onUpdate();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt){
		super.writeEntityToNBT(nbt);
		nbt.setShort("unitId",unit.id);
		nbt.setByte("leaderId", unit.leader.id);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt){
		super.readEntityFromNBT(nbt);
		this.unit=
				MonsterRegularArmyCore.leaders
				[nbt.getByte("leaderId")].
				unitList.
				get
				(nbt.getShort("unitId"));
		this.unit.entityList.add(this);
	}
	
	public FinderSettings getSettings(){
		return this.unit.leader.analyzerManager.getSettings(this);
	}
	
	public void addNode(DataNode n){
		//System.out.println("Node added(score:"+n.result+")");
		if(!this.worldObj.isRemote)
		this.
		unit.
		leader.
		getAnalyzer(this).
		nodes.add(n);
	}
	
	public int[] getBlockIDsAround(){
		int minx=MathHelper.floor_double(this.posX-this.width/2)-1;
		int miny=MathHelper.floor_double(this.posY)-1;
		int minz=MathHelper.floor_double(this.posZ-this.width/2)-1;
		int maxx=MathHelper.ceiling_double_int(this.posX+this.width/2);
		int maxy=MathHelper.ceiling_double_int(this.posY+this.height);
		int maxz=MathHelper.ceiling_double_int(this.posZ+this.width/2);
		int blocksXSide=(maxy-miny-1)*(maxz-minz-1);
		int blocksZSide=(maxy-miny-1)*(maxx-minx-1);
		int[] ids=new int[(blocksXSide+blocksZSide)*2+(maxx-minx-1)*(maxy-miny+1)*(maxz-minz-1)];
		int num=0;
		for(int y=miny+1;y<maxy;y++){
			for(int z=minz+1;z<maxz;z++){
				ids[num++]=this.worldObj.getBlockId(maxx, y, z);
				ids[num++]=this.worldObj.getBlockId(minx, y, z);
				//System.out.println(maxx+","+ y+","+ z+" "+minx+","+ y+","+ z);
			}
			for(int x=minx+1;x<maxx;x++){
				ids[num++]=this.worldObj.getBlockId(x, y, maxz);
				ids[num++]=this.worldObj.getBlockId(x, y, minz);
				//System.out.println(x+","+ y+","+ maxz+" "+x+","+ y+","+ minz);
			}
		}
		for(int y=miny;y<=maxy;y++){
			for(int z=minz+1;z<maxz;z++){
				for(int x=minx+1;x<maxx;x++){
					ids[num++]=this.worldObj.getBlockId(x, y, z);
				}
			}
		}
		return ids;
	}
	
	public int getChunkHash(){
		return (((((int)this.posX)/16)&0xfff)<<20)+(((((int)this.posY)/16)&0xff)<<12)+((((int)this.posZ)/16)&0xfff);
	}
	
	public float getSpeed(){
		return (float) this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
	}
	@Override
	public boolean canDespawn(){
		return false;
	}
}
