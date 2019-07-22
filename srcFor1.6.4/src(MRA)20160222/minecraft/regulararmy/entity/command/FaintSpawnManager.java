package regulararmy.entity.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import regulararmy.core.MonsterRegularArmyCore;
import regulararmy.entity.EntityEngineer;
import regulararmy.entity.EntityFastZombie;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.entity.EntityScouter;
import regulararmy.entity.EntitySniperSkeleton;
import regulararmy.pathfinding.AStarPathEntity;
import regulararmy.pathfinding.AStarPathFinder;
import regulararmy.pathfinding.AStarPathPoint;
import regulararmy.pathfinding.IPathFindRequester;

public class FaintSpawnManager extends SpawnManagerBase implements IPathFindRequester {
	public int maxScore=Integer.MIN_VALUE;
	public int secondScore=Integer.MIN_VALUE;
	public int limitCost=Integer.MAX_VALUE;
	public double spawnX;
	public double spawnY;
	public double spawnZ;
	public double spawnX2;
	public double spawnY2;
	public double spawnZ2;
	public int spawnDistance=60;
	public double searchingX=this.leader.x+((double)this.spawnDistance);
	public int searchingY=0;
	public double searchingZ=this.leader.z;
	public float searchingAngle;
	
	public int searchProg=0;

	public FaintSpawnManager(RegularArmyLeader leader) {
		super(leader);
	}

	@Override
	public int getTacticsCost(Entity entity, AStarPathPoint start,
			AStarPathPoint current, AStarPathPoint end) {
		return 0;
	}

	@Override
	public boolean isEngineer() {
		return false;
	}

	@Override
	public void onStart() {
		List<EntityPlayer> players=this.leader.theWorld.playerEntities;
		for(int i=0;i<players.size();i++){
			players.get(i).addChatMessage("Start searching spawn point");
		}
	}

	@Override
	public void onUpdate() {
		if(this.leader.theWorld.isRemote)return;
		if(this.searchProg<24){
			if(this.leader.age%10!=0)return;
			EntityRegularArmy e=new EntityScouter(this.leader.theWorld);
			 
			this.leader.addUnit(e);
			AStarPathFinder finder=new AStarPathFinder(this.leader.theWorld,true,false,false,true,false,1.2f,this);
			finder.setSetting(e.getSettings());
			boolean flag1=true;
			while(searchingY<this.leader.theWorld.getActualHeight()-e.height-1){
				searchingY++;
				if(!isBlockRidable(e,(int)searchingX,searchingY,(int)searchingZ))continue;
				boolean flag=false;
				for(int m=searchingY+1;m<searchingY+e.height;m++){
					if(!isBlockPassable(e,(int)searchingX,m,(int)searchingZ)){
						flag=true;
						break;
					}
				}
				if(flag)continue;
				e.setPosition(searchingX, searchingY+1, searchingZ);
				int score=this.leader.theWorld.rand.nextInt(500);
				int cost=0;
				for(int i=0;i<5;i++){
					finder.maxCost=this.limitCost-cost;
					AStarPathEntity path=finder.createEntityPathTo(e, this.leader.x, this.leader.y, this.leader.z, 50.0f, 1.0f);
					if(path==null){
						score=Integer.MIN_VALUE;
						break;
					}
					cost+=path.getTotalCost();
					score+=getPathScore(path,searchingX,searchingZ);
					if(score<this.maxScore)break;
					for(int i1=5;i1<path.points.length-5;i1++){
						finder.unusablePoints.add(path.getPathPointFromIndex(i1).toCoord());
					}
				}
				if(this.maxScore<score){
					this.secondScore=this.maxScore;
					this.spawnX2=this.spawnX;
					this.spawnY2=this.spawnY;
					this.spawnZ2=this.spawnZ;
					this.limitCost=cost;
					this.maxScore=score;
					this.spawnX=searchingX;
					this.spawnY=searchingY+1;
					this.spawnZ=searchingZ;
					
				}
				flag1=false;
				break;
				
			}
			if(flag1){
				this.searchProg++;
				this.searchingAngle=(((float)this.searchProg)+leader.theWorld.rand.nextFloat())/12.0f*(float)Math.PI;
				this.searchingX=this.leader.x+(this.spawnDistance)*MathHelper.cos(this.searchingAngle);
				this.searchingZ=this.leader.z+(this.spawnDistance)*MathHelper.sin(this.searchingAngle);
				this.searchingY=0;
				List<EntityPlayer> players=this.leader.theWorld.playerEntities;
				for(int i=0;i<players.size();i++){
					players.get(i).addChatMessage(4*this.searchProg+"% over");
				}
				
			}
		}else if(this.searchProg==24){
			System.out.println("maxScore="+this.maxScore+" at "+this.spawnX+","+this.spawnY+","+this.spawnZ);
			System.out.println("secondScore="+this.secondScore+" at "+this.spawnX2+","+this.spawnY2+","+this.spawnZ2);
			this.searchProg++;
			List<EntityPlayer> players=this.leader.theWorld.playerEntities;
			for(int i=0;i<players.size();i++){
				players.get(i).addChatMessage("End searching");
			}
			/*
		}else if(this.searchProg==25){
			EntityRegularArmy e1,e2,e3;
			e1=new EntityFastZombie(this.leader.theWorld);
			e2=new EntityFastZombie(this.leader.theWorld);
			e3=new EntityEngineer(this.leader.theWorld);
			e1.setPosition(spawnX, spawnY, spawnZ);
			e2.setPosition(spawnX, spawnY, spawnZ);
			e3.setPosition(spawnX, spawnY, spawnZ);
			MonsterUnit u=this.leader.addUnit(e1,e2,e3);
			u.spawnAll();
			this.searchProg++;
		}else if(this.searchProg==26){
			EntityRegularArmy e1,e2,e3;
			e1=new EntitySniperSkeleton(this.leader.theWorld);
			e2=new EntitySniperSkeleton(this.leader.theWorld);
			e3=new EntityEngineer(this.leader.theWorld);
			e1.setPosition(spawnX, spawnY, spawnZ);
			e2.setPosition(spawnX, spawnY, spawnZ);
			e3.setPosition(spawnX, spawnY, spawnZ);
			MonsterUnit u=this.leader.addUnit(e1,e2,e3);
			u.spawnAll();
			this.searchProg++;
		*/
		}else if(this.searchProg>24&&this.leader.age%200==0){
			this.makeRandomUnit(spawnX,spawnY,spawnZ,(byte) (5+this.leader.wave*2)).spawnAll();
			this.searchProg++;
		}else if(this.searchProg>30+this.leader.wave){
			this.searchProg++;
			if(this.searchProg==1000){
				if(this.leader.wave%4==0){
					this.searchProg=0;
				}else{
					this.searchProg=25;
				}
				this.leader.endPhase();
			}
		}
	}

	@Override
	public void onEnd() {
		// TODO 自動生成されたメソッド・スタブ

	}
	
	public MonsterUnit makeRandomUnit(double x,double y,double z,byte tier){
		List<EntityRegularArmy> list=new ArrayList(2+tier/8);
		int type=this.leader.theWorld.rand.nextInt(3);
		for(int i=0;i<2+tier/8;i++){
			EntityRegularArmy e=new EntityFastZombie(this.leader.theWorld);
			switch(type){
			case 0:
				e=new EntitySniperSkeleton(this.leader.theWorld);
				break;
			case 1:
				e=new EntityFastZombie(this.leader.theWorld);
				break;
			
			}
			this.addRandomArmorFromTier(e, tier);
			this.enchantEquipmentFromTier(e, tier);
			e.setPosition(x, y, z);
			list.add(e);
		}
		if(this.leader.theWorld.rand.nextBoolean()){
			EntityRegularArmy e=new EntityEngineer(this.leader.theWorld);
			this.addRandomArmorFromTier(e, tier);
			this.enchantEquipmentFromTier(e, tier);
			e.setPosition(x, y, z);
			list.add(e);
		}
		return this.leader.addUnit(list.toArray(new EntityRegularArmy[0]));
	}
	
	public void addRandomArmorFromTier(EntityRegularArmy e,byte tier){
		if (this.leader.theWorld.rand.nextInt(64)-16 >tier)return;
		int i = this.leader.theWorld.rand.nextInt(2);
        float f = this.leader.theWorld.difficultySetting == 3 ? 0.1F : 0.25F;

        if (this.leader.theWorld.rand.nextInt(128) < tier)
        {
            ++i;
        }

        if (this.leader.theWorld.rand.nextInt(128) < tier)
        {
            ++i;
        }

        if (this.leader.theWorld.rand.nextInt(128) < tier)
        {
            ++i;
        }

        for (int j = 4; j >= 0; --j)
        {
            ItemStack itemstack = e.func_130225_q(j-1);

            if (j < 3 && this.leader.theWorld.rand.nextFloat() < f)
            {
                break;
            }

            if (itemstack == null)
            {
                Item item = e.getArmorItemForSlot(j , i);

                if (item != null)
                {
                    e.setCurrentItemOrArmor(j , new ItemStack(item));
                }
            }
        }
	}
	
	public void enchantEquipmentFromTier(EntityRegularArmy e,byte tier){

        if (e.getHeldItem() != null &&this.leader.theWorld.rand.nextInt(5-tier/32)==0)
        {
            EnchantmentHelper.addRandomEnchantment(this.leader.theWorld.rand, e.getHeldItem(), 5+this.leader.theWorld.rand.nextInt(tier/4));
        }

        for (int i = 0; i < 4; ++i)
        {
            ItemStack itemstack = e.func_130225_q(i);

            if (itemstack != null &&this.leader.theWorld.rand.nextInt(5-tier/32)==0)
            {
                EnchantmentHelper.addRandomEnchantment(this.leader.theWorld.rand, itemstack,5+this.leader.theWorld.rand.nextInt(tier/4));
            }
        }
	}
	
	public double getPathScore(AStarPathEntity path,double x,double z){
		double relativex=x-path.getFinalPathPoint().xCoord;
		double relativez=z-path.getFinalPathPoint().zCoord;
		return -(path.getTotalCost()*path.getTotalCost()/(relativex*relativex+relativez*relativez));
	}

	   public boolean isBlockRidable(EntityRegularArmy e,int x,int y,int z){
		   int id=this.leader.theWorld.getBlockId(x, y,z);
		   if(id==0)return false;
		   else{
			   if(Block.blocksList[id].isOpaqueCube())return true;
			   AxisAlignedBB aabb=Block.blocksList[id].getCollisionBoundingBoxFromPool(e.worldObj, x, y, z);
			   if(aabb==null)return false;
			   if(aabb.maxX+e.width/2<x+1||aabb.minX-e.width/2>x||aabb.maxZ+e.width/2<z+1||aabb.minZ-e.width/2>z)return false;
			   return true;
		   }
	   }
	   
	   public boolean isBlockPassable(EntityRegularArmy e,int x,int y,int z){
		   int id=this.leader.theWorld.getBlockId(x, y,z);
		   if(id==0)return true;
		   else if( Block.blocksList[id] instanceof BlockFluid)return true;
		   else if(Block.blocksList[id].getCollisionBoundingBoxFromPool(e.worldObj, x, y, z)==null)return true;
		return false;   
	   }

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		 this.spawnX=nbt.getDouble("spawnX");
		 this.spawnY=nbt.getDouble("spawnY");
		 this.spawnZ=nbt.getDouble("spawnZ");
		 this.spawnX2=nbt.getDouble("spawnX2");
		 this.spawnY2=nbt.getDouble("spawnY2");
		 this.spawnZ2=nbt.getDouble("spawnZ2");
		this.searchProg=nbt.getInteger("searchProg");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setDouble("spawnX", this.spawnX);
		nbt.setDouble("spawnY", this.spawnY);
		nbt.setDouble("spawnZ", this.spawnZ);
		nbt.setDouble("spawnX2", this.spawnX2);
		nbt.setDouble("spawnY2", this.spawnY2);
		nbt.setDouble("spawnZ2", this.spawnZ2);
		if(this.searchProg<24){
			nbt.setInteger("searchProg", 0);
		}else{
			nbt.setInteger("searchProg", this.searchProg);
		}
	}
}
