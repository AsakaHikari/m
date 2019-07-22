package regulararmy.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import regulararmy.analysis.DataAnalyzer;
import regulararmy.analysis.DataAnalyzerOneToOne;
import regulararmy.analysis.FinderSettings;
import regulararmy.core.*;
import regulararmy.entity.ai.EntityAIFollowEngineer;
import regulararmy.entity.ai.EntityMoveHelperEx;
import regulararmy.entity.command.MonsterUnit;
import regulararmy.entity.command.RegularArmyLeader;
import regulararmy.pathfinding.AStarPathEntity;
import regulararmy.pathfinding.AStarPathNavigate;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public abstract class EntityRegularArmy extends EntityMob{

	public MRAEntityData data;
	public AStarPathEntity pathToEntity;
	public AStarPathNavigate navigator;
	public MonsterUnit unit;
	public double lastDistance=0;
	public double lastDistanceDifference=0;
	public double lastDistanceDifferenceAmount=0;
	public short lastDistanceDifferenceAmountNum=0;
	public boolean doRideHorses=true;
	public Field FmoveHelper;

	public EntityAIFollowEngineer follow=null;
	
    public HorseSelector selector=new HorseSelector();

	public EntityRegularArmy(World par1World) {
		super(par1World);
		this.navigator=new AStarPathNavigate(this,par1World);
		this.unit=null;
		this.data=MRAEntityData.classToData.get(this.getClass());
		try {
			this.FmoveHelper=ReflectionHelper.findField(EntityLiving.class,ObfuscationReflectionHelper.remapFieldNames(EntityLiving.class.getName(),"moveHelper","field_70765_h"));
			this.FmoveHelper.set(this, new EntityMoveHelperEx(this,30f));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public EntityRegularArmy(World par1World,MonsterUnit unit) {
		super(par1World);
		this.navigator=new AStarPathNavigate(this,par1World);
		this.unit=unit;
		try {
			this.FmoveHelper=EntityLiving.class.getDeclaredField("moveHelper");
			this.FmoveHelper.setAccessible(true);
			this.FmoveHelper.set(this, new EntityMoveHelperEx(this,30f));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateAITasks(){
		Vec3 vec3=Vec3.createVectorHelper(this.posX, (double)this.navigator.getPathableYPos(), this.posZ);

		if(this.getMoveHelper().isUpdating()&&this.navigator.currentPath!=null){

			this.lastDistanceDifference=this.lastDistance-vec3.distanceTo(this.navigator.currentPath.getVectorFromIndex(this,this.navigator.currentPath.getCurrentPathIndex()));
			this.lastDistanceDifferenceAmount+=this.lastDistanceDifference;
			this.lastDistanceDifferenceAmountNum++;

		}
		super.updateAITasks();

		this.navigator.onUpdateNavigation();

		if(this.navigator.currentPath!=null&&this.navigator.currentPath.getCurrentPathLength()>this.navigator.currentPath.getCurrentPathIndex()){
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
			this.pathToEntity.disablePath(this.unit.leader);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
		if(!this.worldObj.isRemote){
			this.addNode(new DataAnalyzer.DataNode
					(par2*100,this.getBlockIDsAround(),new int[]{this.getChunkHash()},this.getEntityIDArrayNear(par1DamageSource.getSourceOfDamage())));
			if(par1DamageSource.getSourceOfDamage()!=null){
				
				EntityLivingBase attacker = null;
				if(par1DamageSource.getSourceOfDamage() instanceof EntityLivingBase){
					attacker=(EntityLivingBase) par1DamageSource.getSourceOfDamage();
				}else if(par1DamageSource.getEntity()!=null&&par1DamageSource.getEntity() instanceof EntityLivingBase){
					attacker=(EntityLivingBase) par1DamageSource.getEntity();
				}

				if(attacker!=null){
					double rx=this.posX-attacker.posX;
					double ry=this.posY-attacker.posY;
					double rz=this.posZ-attacker.posZ;
					//this.addDistanceNode(new DataAnalyzerOneToOne.DataNode(MathHelper.sqrt_double(rx*rx+ry*ry+rz*rz), new int[]{(int) par2+1}));
				}
			}
		}

		return super.attackEntityFrom(par1DamageSource, par2);
	}

	@Override
	public void onUpdate(){
		if(this.getAttackTarget()==null||this.getAttackTarget().isDead||!this.getAttackTarget().isEntityAlive()){
			this.setAttackTarget(null);
		}
		if(!this.worldObj.isRemote){
			if(this.ticksExisted%32==0){
				//System.out.println(-2.0f+(float)(this.lastDistanceDifferenceAmountNum==0?-8.0f:-(this.lastDistanceDifferenceAmount/this.lastDistanceDifferenceAmountNum)*80));
				//System.out.println(this.lastDistanceDifferenceAmountNum==0?0:this.lastDistanceDifferenceAmount/this.lastDistanceDifferenceAmountNum);
				this.addNode(new DataAnalyzer.DataNode(-8.0f+(float)(this.lastDistanceDifferenceAmountNum==0?-8.0f:-(this.lastDistanceDifferenceAmount/this.lastDistanceDifferenceAmountNum)*80)
						,this.getBlockIDsAround()
						,new int[]{this.getChunkHash()}
				,this.getEntityIDArrayNear(null)));
				this.lastDistanceDifferenceAmount=0;
				this.lastDistanceDifferenceAmountNum=0;
			}
			if(this.doRideHorses){
				List<Entity> horselist= this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.5, 0.5, 0.5),this.selector );
				if(!horselist.isEmpty()){
					this.mountEntity(horselist.get(0));
					
				}
			}
		}
		super.onUpdate();
		boolean flag = this.isSneaking() && this.isOnLadder();

		if (flag && this.motionY < 0.0D)
		{
			this.motionY = 0.0D;
		}

	}
	/*
	@Override
	public void setMoveForward(float p_70657_1_)
    {
		double relativeX=this.posX-this.unit.leader.x-0.5;
		double relativeY=this.posY-this.unit.leader.y-0.5;
		double relativeZ=this.posZ-this.unit.leader.z-0.5;
		double sqdis=relativeX*relativeX+relativeY*relativeY+relativeZ*relativeZ;
		if(sqdis<4){
			double relrot=(this.rotationYawHead-Math.atan2(relativeZ, relativeX)*180/Math.PI+630)%360;
			//System.out.println("rot:"+relrot);
			if(relrot>70 && relrot<290){
				super.setMoveForward(0);
				//System.out.println("zero2");
				return;
			}else{
				super.setMoveForward(p_70657_1_);
				return;
				
				
			}
		}
		super.setMoveForward(p_70657_1_);
		return;
    }
*/
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt){
		super.writeEntityToNBT(nbt);
		nbt.setShort("unitId",unit.getID());
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

	public void addNode(DataAnalyzer.DataNode n){
		//System.out.println("Node added(score:"+n.result+")");
		if(!this.worldObj.isRemote)this.unit.leader.getAnalyzer(this).nodes.add(n);
	}
/*
	public void addDistanceNode(DataAnalyzerOneToOne.DataNode n){
		if(!this.worldObj.isRemote)this.unit.leader.getDistanceAnalyzer(this).nodes.add(n);
	}
*/
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
				
				int newid1=Block.getIdFromBlock(this.worldObj.getBlock(maxx, y, z));
				for(int i=0;i<ids.length;i++){
					if(ids[i]==newid1){
						break;
					}
					if(ids[i]==0){
						ids[i]=newid1;
						break;
					}
				}
				int newid2=Block.getIdFromBlock(this.worldObj.getBlock(minx, y, z));
				for(int i=0;i<ids.length;i++){
					if(ids[i]==newid2){
						break;
					}
					if(ids[i]==0){
						ids[i]=newid2;
						break;
					}
				}
				
				//System.out.println(maxx+","+ y+","+ z+" "+minx+","+ y+","+ z);
			}
			for(int x=minx+1;x<maxx;x++){
				int newid1=Block.getIdFromBlock(this.worldObj.getBlock(x, y, maxz));
				for(int i=0;i<ids.length;i++){
					if(ids[i]==newid1){
						break;
					}
					if(ids[i]==0){
						ids[i]=newid1;
						break;
					}
				}
				int newid2=Block.getIdFromBlock(this.worldObj.getBlock(x, y, minz));
				for(int i=0;i<ids.length;i++){
					if(ids[i]==newid2){
						break;
					}
					if(ids[i]==0){
						ids[i]=newid2;
						break;
					}
				}
				//System.out.println(x+","+ y+","+ maxz+" "+x+","+ y+","+ minz);
			}
		}
		for(int y=miny;y<=maxy;y++){
			for(int z=minz+1;z<maxz;z++){
				for(int x=minx+1;x<maxx;x++){
					int newid1=Block.getIdFromBlock(this.worldObj.getBlock(x, y, z));
					for(int i=0;i<ids.length;i++){
						if(ids[i]==newid1){
							break;
						}
						if(ids[i]==0){
							ids[i]=newid1;
							break;
						}
					}
				}
			}
		}
		return ids;
	}

	public int getChunkHash(){
		return (((((int)this.posX)/16)&0x7ff+(this.posX<0?0x800:0))<<20)+(((((int)this.posY)/16)&0xff)<<12)+((((int)this.posZ)/16)&0x7ff+(this.posZ<0?0x800:0));
	}

	public int[] getEntityIDArrayNear(Entity e){
		List<Entity> list=this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBox(this.posX,this.posY,this.posZ,this.posX,this.posY,this.posZ).expand(2d, 2d, 2d));
		List<Integer> array=new ArrayList();
		if(e!=null){
			int id=getCustomEntitySharedID(e);
			array.add(id);
		}
		for(int i=0;i<list.size();i++){
			int id=getCustomEntitySharedID(list.get(i));
			array.add(id);
		}
		if(array.size()==0){
			return new int[0];
		}
		Integer[] integerArray=(Integer[])array.toArray(new Integer[0]);
		int[] intArray=new int[integerArray.length];
		for(int i=0;i<integerArray.length;i++){
			intArray[i]=integerArray[i];
		}
		return intArray;
	}
	
	public static int getCustomEntitySharedID(Entity e){
		int id=EntityList.getEntityID(e);
		if(id==0){
			String theName;
			if(e instanceof EntityPlayer){
				theName="Player;"+((EntityPlayer)e).getDisplayName();
			}else{
				theName=e.getClass().getName();
			}
			id=getCustomEntitySharedIDFromName(theName);
		}
		return id;
	}
	
	public static int getCustomEntitySharedIDFromName(String theName){
		for(int i1=0;i1<MonsterRegularArmyCore.entityIDList.size();i1++){
			if(MonsterRegularArmyCore.entityIDList.get(i1).equals(theName)){
				return i1-1;
			}
		}
		MonsterRegularArmyCore.entityIDList.add(theName);
		System.out.println(theName+" idAdd "+(-MonsterRegularArmyCore.entityIDList.size()));
		return -MonsterRegularArmyCore.entityIDList.size();
			
	}

	public float getSpeed(){
		return (float) this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
	}
	@Override
	public boolean canDespawn(){
		return false;
	}

	public MRAEntityData getDataOfSpecies(){
		return this.data;
	}

	public void setEquipmentsFromTier(int tier){
		this.addRandomArmorFromTier( tier);
		this.enchantEquipmentFromTier( tier);
		this.setHorsesFromTier(tier);
	}

	public void setHorsesFromTier(int tier){
		if(this.unit.ridingEntity!=0 || tier<6 ||!this.doRideHorses)return;
		int rand=this.worldObj.rand.nextInt(40+tier);
		if(rand>40){
			this.unit.setRidingEntity(1);
		}
	}

	public void addRandomArmorFromTier(int tier){
		if (this.worldObj.rand.nextInt(32)-8 >tier)return;
		int i = this.worldObj.rand.nextInt(2);
		float f = this.worldObj.difficultySetting == EnumDifficulty.HARD ? 0.1F : 0.25F;

		if (this.worldObj.rand.nextInt(64) < tier)
		{
			++i;
		}

		if (this.worldObj.rand.nextInt(64) < tier)
		{
			++i;
		}

		if (this.worldObj.rand.nextInt(64) < tier)
		{
			++i;
		}

		for(int j=0;j<5;j++){

			ItemStack itemstack = this.getEquipmentInSlot(j);
			if (itemstack == null)
			{
				ItemStack weapon=this.getRandomEquip(j,(byte) i);
				if(weapon!=null){
					this.setCurrentItemOrArmor(j , weapon);
				}
			}
		}

	}

	public void enchantEquipmentFromTier(int tier){

		if (this.getHeldItem() != null &&(tier>30||this.worldObj.rand.nextInt(5-tier/16)==0))
		{
			EnchantmentHelper.addRandomEnchantment(this.worldObj.rand, this.getHeldItem(), 5+this.worldObj.rand.nextInt(tier/2+1));
		}

		for (int i = 0; i < 4; ++i)
		{
			ItemStack itemstack = this.func_130225_q(i);

			if (itemstack != null &&(tier>30||this.worldObj.rand.nextInt(5-tier/16)==0))
			{
				EnchantmentHelper.addRandomEnchantment(this.worldObj.rand, itemstack,5+this.worldObj.rand.nextInt(tier/2+1));
			}
		}
	}

	public ItemStack getRandomEquip(int slot,int tier){
		List<ItemStack> canditateItems=new ArrayList();
		Item[] itemArray=null;
		int[] damageArray=null;
		int[] tierArray=null;
		switch(slot){
		case 0:
			itemArray=MonsterRegularArmyCore.weapons;
			damageArray=MonsterRegularArmyCore.weaponsDamage;
			tierArray=MonsterRegularArmyCore.weaponsTier;
			break;
		case 1:
			itemArray=MonsterRegularArmyCore.boots;
			damageArray=MonsterRegularArmyCore.bootsDamage;
			tierArray=MonsterRegularArmyCore.bootsTier;
			break;
		case 2:
			itemArray=MonsterRegularArmyCore.legs;
			damageArray=MonsterRegularArmyCore.legsDamage;
			tierArray=MonsterRegularArmyCore.legsTier;
			break;
		case 3:
			itemArray=MonsterRegularArmyCore.chests;
			damageArray=MonsterRegularArmyCore.chestsDamage;
			tierArray=MonsterRegularArmyCore.chestsTier;
			break;
		case 4:
			itemArray=MonsterRegularArmyCore.helms;
			damageArray=MonsterRegularArmyCore.helmsDamage;
			tierArray=MonsterRegularArmyCore.helmsTier;
			break;
		}
		for(int i=0;i<itemArray.length;i++){
			if(tierArray[i]==tier&&itemArray[i]!=null){
				canditateItems.add(new ItemStack(itemArray[i],1,damageArray[i]));
			}
		}
		if(canditateItems.size()==0){
			return null;
		}else{
			int num=this.worldObj.rand.nextInt(canditateItems.size());
			return canditateItems.get(num);
		}
	}

	@Override
	public double getYOffset()
	{
		return super.getYOffset()-0.4D;
	}


	public Entity getBottomEntity(){
		Entity e=this;
		while(e.ridingEntity!=null){
			e=e.ridingEntity;
		}
		return e;
	}

	public EntityMoveHelperEx getMoveHelperEx(){
		try {
			return (EntityMoveHelperEx) this.FmoveHelper.get(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setTurnLimitPerTick(float limitAngle){
		this.getMoveHelperEx().angleMovementLimit=limitAngle;
	}

	@Override
	protected void onDeathUpdate()
	{
		++this.deathTime;

		if (this.deathTime == 20)
		{
			int i;

			if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && this.func_146066_aG() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot"))
			{
				i = this.getExperiencePoints(this.attackingPlayer);

				while (i > 0)
				{
					int j = EntityXPOrb.getXPSplit(i);
					i -= j;
					this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.attackingPlayer.worldObj, this.attackingPlayer.posX, this.attackingPlayer.posY, this.attackingPlayer.posZ, j));
				}
			}

			this.setDead();

			for (i = 0; i < 20; ++i)
			{
				double d2 = this.rand.nextGaussian() * 0.02D;
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1);
			}
		}
	}

	@Override
	public EntityItem entityDropItem(ItemStack itemStack, float p_70099_2_)
	{
		if(!MonsterRegularArmyCore.doDropItem)return null;
		if (itemStack.stackSize != 0 && itemStack.getItem() != null)
		{
			EntityPlayer lastAttacker=this.attackingPlayer;
			EntityItem entityitem;
			if(lastAttacker!=null&&!lastAttacker.isDead){
				entityitem = new EntityItem(lastAttacker.worldObj, lastAttacker.posX, lastAttacker.posY + (double)p_70099_2_, lastAttacker.posZ, itemStack);
			}else{
				entityitem = new EntityItem(this.worldObj, this.posX, this.posY + (double)p_70099_2_, this.posZ, itemStack);
			}
			entityitem.delayBeforeCanPickup = 1;
			if (captureDrops)
			{
				capturedDrops.add(entityitem);
			}
			else
			{
				this.worldObj.spawnEntityInWorld(entityitem);
			}
			return entityitem;
		}
		else
		{
			return null;
		}
	}


    public static class HorseSelector implements IEntitySelector{
		@Override
		public boolean isEntityApplicable(Entity p_82704_1_) {
			if(p_82704_1_ instanceof EntityHorse){
				EntityHorse horse=((EntityHorse)p_82704_1_);
				return horse.riddenByEntity==null && horse.func_152119_ch().equals(new UUID(0l,0l).toString());
			}
			return false;
		}
    	
    }
}
