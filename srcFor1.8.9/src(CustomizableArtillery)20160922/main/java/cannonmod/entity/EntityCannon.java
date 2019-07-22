package cannonmod.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cannonmod.core.CannonCore;
import cannonmod.core.GuiHandler;
import cannonmod.core.MessageCannonGui;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeModContainer;

public class EntityCannon extends EntityLivingBase implements IInventory{
	
	public ItemStack[] inventory;

	public Vec3 lastEnemyPos;
	public double speedMultiplier=2.0;
	public float turningSpeed=5;
	public double playerDistanceFromCentre=1.0;
	public int lengthOfBarrel=20;
	public double rotationOffsetX=0;
	public double rotationOffsetZ=0;
	public double turretTurning=4;
	public String customName;
	public int fuelLeft=0;
	public int currentItemBurnTime=200;
	public boolean reflectedDamage;
	public int ammoSlot=9;
	public int gunpowderSlot=9;

	public int calibre=20;
	public double bulletSpeed=0.4;
	public double precision=0.1;
	public int reload=20;
	public int engine=0;
	public int motor=0;
	public int armor=1;
	public int reloadtime=0;
	public double size=1;
	public int design=0;
	
	public ModelCannon model;

	public EntityCannon(World par1World) {
		super(par1World);
		this.stepHeight=1.1f;
		//this.setTurnLimitPerTick(1f);
		this.setSize(1.5f, 2f);
		this.inventory=new ItemStack[19];

	}
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(CannonCore.cannonHealth);
    }
	
	@Override
	public void entityInit(){
		super.entityInit();
		this.dataWatcher.addObject(20, new Integer(this.calibre));
		this.dataWatcher.addObject(21, new Integer(this.lengthOfBarrel));
		this.dataWatcher.addObject(22, new Integer(this.motor));
		this.dataWatcher.addObject(23, new Integer(this.engine));
		this.dataWatcher.addObject(24, new Integer(this.armor));
		this.dataWatcher.addObject(25,new Integer(this.reloadtime));
		this.dataWatcher.addObject(26,new Integer(this.design));
	}
	
	public void updateDataWatcher(){
		this.dataWatcher.updateObject(20, new Integer(this.calibre));
		this.dataWatcher.updateObject(21, new Integer(this.lengthOfBarrel));
		this.dataWatcher.updateObject(22, new Integer(this.motor));
		this.dataWatcher.updateObject(23, new Integer(this.engine));
		this.dataWatcher.updateObject(24, new Integer(this.armor));
		this.dataWatcher.updateObject(26,new Integer(this.design));
	}
	
	public void setStats(){
		if(this.calibre*this.lengthOfBarrel==0)return;
		this.speedMultiplier=CannonCore.cannonSpeed*(2d+engine*2d)/(this.calibre*this.lengthOfBarrel+300d+30d*this.armor);
		this.turningSpeed=400f*(2f+engine*2f)/(this.calibre*this.lengthOfBarrel+300f+30f*this.armor);
		this.reload=this.calibre*this.calibre/2;
		this.bulletSpeed=CannonCore.bulletSpeed*Math.sqrt(this.lengthOfBarrel)*1.2;
		this.precision=1d/(Math.pow(Math.E, ((double)this.lengthOfBarrel-(double)this.calibre)/50d)+1d)*15d;
		this.size=(2*(double)this.calibre+(double)this.lengthOfBarrel/2+1)/40d;
		this.setSize((float)this.calibre/5+0.5f, ((float)this.calibre/5+(float)this.lengthOfBarrel/20+2)/4+(float)this.calibre/10);
		this.turretTurning=300d*(2d+this.motor*2d)/(this.calibre*this.lengthOfBarrel+200d);
		//System.out.println("turretSpeed:"+this.turretTurning);
	}
	
	@Override
	public void onEntityUpdate(){
		super.onEntityUpdate();
		this.reflectedDamage=false;
		if(this.worldObj.isRemote){
			this.reloadtime=this.dataWatcher.getWatchableObjectInt(25);
		}
		if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase)
		{
			this.dataWatcher.updateObject(25, new Integer(this.reloadtime));
			EntityLivingBase entitylivingbase = (EntityLivingBase)this.riddenByEntity;
			float f = this.rotationYaw;
			if((this.engine==0 || this.fuelLeft>0) && this.onGround){
				
				this.motionX += -MathHelper.sin((f * (float)Math.PI / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.moveForward * 0.05000000074505806D;
				this.motionZ += MathHelper.cos((f * (float)Math.PI / 180.0F)) * this.speedMultiplier * (double)entitylivingbase.moveForward * 0.05000000074505806D;
				this.rotationYaw-=entitylivingbase.moveStrafing*this.turningSpeed;
			}
			if(this.engine>0 && 
					(MathHelper.abs(entitylivingbase.moveForward)>0.001 || MathHelper.abs(entitylivingbase.moveStrafing)>0.001)){
				this.fuelLeft--;
				if(this.fuelLeft>0){
					if(MathHelper.abs(entitylivingbase.moveForward)<=0.001){
						float speed=0.5f+(float)this.speedMultiplier*(MathHelper.abs(entitylivingbase.moveStrafing)/2)/6f;
						if(this.ticksExisted%(int)(5/speed)==0){
							//System.out.println("speed:"+this.speedMultiplier*entitylivingbase.moveStrafing);
							this.playSound("simplecannon:simplecannon.run",1 , speed);
						}
					}else{
						float speed=0.5f+(float)this.speedMultiplier*(MathHelper.abs(entitylivingbase.moveForward))/6f;
						if(this.ticksExisted%(int)(5/speed)==0){
							//System.out.println("speed:"+this.speedMultiplier*entitylivingbase.moveForward);
							this.playSound("simplecannon:simplecannon.run",1 , speed);
						}
					}
				}

				if(this.fuelLeft<0){
					ItemStack stack=this.getStackInSlot(this.inventory.length-1);
					if(stack!=null && TileEntityFurnace.isItemFuel(stack)){
						this.fuelLeft+=TileEntityFurnace.getItemBurnTime(stack)/2;
						this.currentItemBurnTime=TileEntityFurnace.getItemBurnTime(stack)/2;
						stack.stackSize--;
						if(stack.stackSize==0){
							this.setInventorySlotContents(this.inventory.length-1, stack.getItem().getContainerItem(stack));
						}
					}
				}
				
			}
			
			if(this.rotationYawHead-entitylivingbase.rotationYawHead>180){
				this.rotationYawHead-=360;
			}else if(this.rotationYawHead-entitylivingbase.rotationYawHead<-180){
				this.rotationYawHead+=360;
			}
			//System.out.println("yawhead="+entitylivingbase.rotationYawHead+" pitch="+entitylivingbase.rotationPitch);
			if(this.rotationYawHead-entitylivingbase.rotationYawHead>this.turretTurning){
				this.rotationYawHead-=this.turretTurning;
			}else if(this.rotationYawHead-entitylivingbase.rotationYawHead<-this.turretTurning){
				this.rotationYawHead+=this.turretTurning;
			}else{
				this.rotationYawHead=entitylivingbase.rotationYawHead;
			}
			
			if(this.rotationPitch-entitylivingbase.rotationPitch>180){
				this.rotationPitch-=360;
			}else if(this.rotationPitch-entitylivingbase.rotationPitch<-180){
				this.rotationPitch+=360;
			}
			
			if(this.rotationPitch-entitylivingbase.rotationPitch>this.turretTurning){
				this.rotationPitch-=this.turretTurning;
			}else if(this.rotationPitch-entitylivingbase.rotationPitch<-this.turretTurning){
				this.rotationPitch+=this.turretTurning;
			}else{
				this.rotationPitch=entitylivingbase.rotationPitch;
			}
			
			//System.out.println("yawhead="+this.rotationYawHead+" pitch="+this.rotationPitch);
			
			if(this.reload>this.reloadtime){
				this.reloadtime++;
				float speed=0.7f-((float)this.calibre/100);
				if(this.reload==this.reloadtime+(int)(6/speed)){
					this.playSound("random.door_close", 1.0f, speed);
				}
			}
			
		}
		
		/*
		if(this.dataWatcher.getWatchableObjectByte(14)==1){
			if(this.worldObj.isRemote){
				double xVel=-MathHelper.sin(this.rotationYaw*(float)Math.PI/180f)*0.8;
				double yVel=MathHelper.sin((float) (this.rotationPitch/180.0f*Math.PI))*0.8;
				double zVel=MathHelper.cos(this.rotationYaw*(float)Math.PI/180f)*0.8;
				for(int i=0;i<30;i++){
					this.worldObj.spawnParticle("largesmoke", this.posX,this.posY+0.5,this.posZ,
							xVel+this.rand.nextGaussian()*0.3-0.15,
							yVel+this.rand.nextGaussian()*0.3-0.15,
							zVel+this.rand.nextGaussian()*0.3-0.15);
				}
				for(int i=0;i<60;i++){
					this.worldObj.spawnParticle("flame", this.posX,this.posY+0.5,this.posZ,
							xVel+this.rand.nextGaussian()*0.3-0.15,
							yVel+this.rand.nextGaussian()*0.3-0.15,
							zVel+this.rand.nextGaussian()*0.3-0.15);
				}

			}else{
				this.dataWatcher.updateObject(14, (byte)0);
			}
		}
		 */
		this.updateInventory();
	}
	
	@SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int p_145955_1_)
    {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 200;
        }

        return this.fuelLeft * p_145955_1_ / this.currentItemBurnTime;
    }
	
	@SideOnly(Side.CLIENT)
    public int getReloadTimeProceedingScaled(int p_145955_1_)
    {
        if (this.reload == 0)
        {
            this.reload = 10;
        }

        return this.reloadtime * p_145955_1_ / this.reload;
    }
	
	public boolean isReloaded(){
		return this.reload==this.reloadtime;
	}

	@Override
	public void updateRiderPosition(){
		this.riddenByEntity.setPosition(this.posX+this.playerDistanceFromCentre*MathHelper.sin(this.rotationYawHead*(float)Math.PI/180)
				+this.rotationOffsetX*MathHelper.sin(this.rotationYaw*(float)Math.PI/180),
				this.posY+this.getMountedYOffset(),
				this.posZ-this.playerDistanceFromCentre*MathHelper.cos(this.rotationYawHead*(float)Math.PI/180)
				-this.rotationOffsetZ*MathHelper.cos(this.rotationYaw*(float)Math.PI/180));

	}
	
	@Override
	public double getMountedYOffset(){
		return this.height-0.5;
	}
	
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_){
		boolean flag=super.attackEntityFrom(p_70097_1_, p_70097_2_);
		if(this.reflectedDamage){
			flag=false;
		}
		return flag;
	}

	@Override
	protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_)
	{
		if(!p_70665_1_.isFireDamage()){ 
			Entity source= p_70665_1_.getSourceOfDamage();
			
			if(source!=null){
				
				double width=source.width<0.2?0.2:source.width;
				double height=source.height<0.2?0.2:source.height;
				double volume=Math.sqrt(width*width*height);
				
				if (source instanceof EntityLivingBase){

					ItemStack tool=((EntityLivingBase)source).getEquipmentInSlot(0);
					if(tool!=null && tool.getItem().getHarvestLevel(tool, "pickaxe")>=1){
						super.damageEntity(p_70665_1_, p_70665_2_);
						return;
					}
				}
				double prob=(p_70665_2_>4.0 ? 1.0 : p_70665_2_/4) * volume*this.size/this.armor/2*(1+Math.sqrt(source.motionX*source.motionX+source.motionY*source.motionY+source.motionZ*source.motionZ));
				//System.out.println("prob:"+prob+" armor:"+this.armor+" size:"+this.size);
				if(this.worldObj.rand.nextDouble()<prob){
					super.damageEntity(p_70665_1_, p_70665_2_);
					return;

				}else{
					this.reflectDamager(source, prob);
					return;
				}

			}else{
				double prob=(p_70665_2_>4.0 ? 1.0 : p_70665_2_/4) * this.size/this.armor/2;
				//System.out.println("prob:"+prob+" armor:"+this.armor+" size:"+this.size);
				if(this.worldObj.rand.nextDouble()<prob){
					super.damageEntity(p_70665_1_, p_70665_2_);
					return;

				}else{
					this.playSound("random.anvil_land", 0.5f+(float)prob, 0.5f+(float)prob);
					this.reflectedDamage=true;
					return;
				}
			}
		}else{
			super.damageEntity(p_70665_1_, p_70665_2_);
		}
	}
	
	public void reflectDamager(Entity source,double probability){
		source.motionX=-source.motionX/2;
		source.motionY=-source.motionY/2;
		source.motionZ=-source.motionZ/2;
		this.playSound("random.anvil_land", 0.5f+(float)probability, 0.5f+(float)probability);
		this.reflectedDamage=true;
		return;
	}

	protected void dropFewItems(boolean par1, int par2)
	{
		EntityTNTPrimed tnt=new EntityTNTPrimed(this.worldObj,this.posX,this.posY,this.posZ,this.func_94060_bK());
		tnt.fuse=0;
		this.worldObj.spawnEntityInWorld(tnt);
		tnt.onUpdate();
		
		List<ItemStack> dropList=new ArrayList();
		
		dropList.add(new ItemStack(CannonCore.itemBarrel,1,this.calibre+(this.lengthOfBarrel<<8)));
		dropList.add(new ItemStack(CannonCore.itemCarriage,1,this.motor));
		dropList.add(new ItemStack(CannonCore.itemTrack,1));
		dropList.add(new ItemStack(CannonCore.itemTrack,1));
		if(this.engine>0){
			dropList.add(new ItemStack(CannonCore.itemEngine,1,this.engine-1));
		}
		for(int i=0;i<this.armor-1;i++){
			dropList.add(new ItemStack(CannonCore.itemArmor,1));
		}
		
		for(ItemStack stack:dropList){
			if(this.worldObj.rand.nextDouble()<0.7){
				this.entityDropItem(stack, 1);
			}
		}
		
		for(int i=0;i<this.getSizeInventory();i++){
			if(this.getStackInSlot(i)!=null){
				this.entityDropItem(this.getStackInSlot(i), 1);
			}
		}
		
	}

	public void updateInventory(){
		for(int i=0;i<this.ammoSlot-1;i++){
			if(this.inventory[i]==null && this.inventory[i+1]!=null){
				this.setInventorySlotContents(i, this.inventory[i+1]);
				this.setSlotNull(i+1);
			}
		}
		for(int i=this.ammoSlot;i<this.ammoSlot+this.gunpowderSlot-1;i++){
			if(this.inventory[i]==null && this.inventory[i+1]!=null){
				this.setInventorySlotContents(i, this.inventory[i+1]);
				this.setSlotNull(i+1);
			}
		}
	}
	@Override
	public ItemStack getHeldItem() {
		return null;
	}
	@Override
	public ItemStack getEquipmentInSlot(int p_71124_1_) {
		return null;
	}
	@Override
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {

	}
	
	/**
	 * First layer of player interaction
	 */
	public boolean interactFirst(EntityPlayer p_130002_1_)
	{
		if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != p_130002_1_)
		{
			return true;
		}
		else
		{
			if (!this.worldObj.isRemote)
			{
				p_130002_1_.mountEntity(this);
			}

			return true;
		}
	}

	public void setSlotNull(int num) {
		if(num>this.getSizeInventory())return;
		this.inventory[num]=null;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int num) {
		if(num>this.getSizeInventory())return null;
		return this.inventory[num];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
	 * new stack.
	 */
	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		ItemStack stackInSlot=this.getStackInSlot(p_70298_1_);
		if (stackInSlot != null)
		{
			ItemStack itemstack;

			if (stackInSlot.stackSize <= p_70298_2_)
			{

				this.setSlotNull(p_70298_1_);
				return stackInSlot;
			}
			else
			{
				itemstack = stackInSlot.splitStack(p_70298_2_);

				if (stackInSlot.stackSize == 0)
				{
					this.setSlotNull(p_70298_1_);
				}

				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int num, ItemStack stack) {
		if(num>this.getSizeInventory())return;
		this.inventory[num]=stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {

	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return this.isDead ? false : p_70300_1_.getDistanceSqToEntity(this) <= 64.0D;
	}

	@Override
	public boolean isItemValidForSlot(int num, ItemStack stack) {
		if(num>this.getSizeInventory())return false;
		if(num<this.ammoSlot){
			return true;
		}else if(num<this.ammoSlot+this.gunpowderSlot){
			return stack.getItem()==Items.gunpowder;
		}else{
			return TileEntityFurnace.isItemFuel(stack);
		}
	}

	public void openGUI(){

		if(this.riddenByEntity instanceof EntityPlayer){

			//((EntityPlayer)this.riddenByEntity).openGui(CannonCore.instance, GuiHandler.MOD_TILE_ENTITY_GUI, this.worldObj, this.getEntityId(), 0, 0);
			CannonCore.INSTANCE.sendToServer(new MessageCannonGui(this.getEntityId(),(byte)0));
		}
	}

	public void openFire(){
		if(this.worldObj.isRemote){
			CannonCore.INSTANCE.sendToServer(new MessageCannonGui(this.getEntityId(),(byte)1));
			return;
		}
		if(!this.isReloaded()){
			return;
		}
		
		if(this.getStackInSlot(this.ammoSlot)==null){
			Entity entity=this.riddenByEntity;
			if(entity instanceof EntityPlayer){
				((EntityPlayer)entity).addChatMessage(new ChatComponentText("No Gunpowder!"));
			}
			return;
		}
		if(this.getStackInSlot(0)==null){
			Entity entity=this.riddenByEntity;
			if(entity instanceof EntityPlayer){
				((EntityPlayer)entity).addChatMessage(new ChatComponentText("No Ammo!"));
			}
			return;
		}
		
		if(this.rotationPitch<0)this.rotationPitch+=360;
		if(this.rotationYawHead<0)this.rotationYawHead+=360;
		double l=this.lengthOfBarrel*MathHelper.cos(this.rotationPitch*(float)Math.PI/180)/5;

		double x= this.posX-l*MathHelper.sin(this.rotationYawHead*(float)Math.PI/180)
				+this.rotationOffsetX*MathHelper.sin(this.rotationYaw*(float)Math.PI/180);
		double y= this.posY-this.lengthOfBarrel*MathHelper.sin(this.rotationPitch*(float)Math.PI/180)/5+this.getMountedYOffset();
		double z= this.posZ+l*MathHelper.cos(this.rotationYawHead*(float)Math.PI/180)
				-this.rotationOffsetZ*MathHelper.cos(this.rotationYaw*(float)Math.PI/180);

		int meta=0;
		if(this.rotationPitch>45 && this.rotationPitch<135){
			meta=0;
		}else if(this.rotationPitch>225 && this.rotationPitch<315){
			meta=1;
		}else{
			if(this.rotationYawHead>45 && this.rotationYawHead<135){
				meta=4;
			}else if(this.rotationYawHead>=135 && this.rotationYawHead<225){
				meta=2;
			}else if(this.rotationYawHead>=225 && this.rotationYawHead<315){
				meta=5;
			}else{
				meta=3;
			}
		}
		int fx=MathHelper.floor_double(x),fy=MathHelper.floor_double(y),fz=MathHelper.floor_double(z);
		EnumFacing face=BlockDispenser.getFacing(meta);
		int bx=fx-face.getFrontOffsetX();
		int by=fy-face.getFrontOffsetY();
		int bz=fz-face.getFrontOffsetZ();
		
		if(!(this.worldObj.getBlockState(new BlockPos(bx, by, bz)).getBlock() instanceof BlockAir)){
			Entity entity=this.riddenByEntity;
			if(entity instanceof EntityPlayer){
				((EntityPlayer)entity).addChatMessage(new ChatComponentText("A block is blocking fire!"));
			}
			return;
		}
		
		//Remember chunks and compare later
		/*
		int cxm=(bx-8)>>4;
		int cym=(by-8)>>4;
		int czm=(bz-8)>>4;
		
		ExtendedBlockStorage[] chunks=new ExtendedBlockStorage[8];
		
		chunks[0]=this.worldObj.getChunkFromChunkCoords(cxm, czm).getBlockStorageArray()[cym];
		chunks[1]=this.worldObj.getChunkFromChunkCoords(cxm+1, czm).getBlockStorageArray()[cym];
		chunks[2]=this.worldObj.getChunkFromChunkCoords(cxm+1, czm+1).getBlockStorageArray()[cym];
		chunks[3]=this.worldObj.getChunkFromChunkCoords(cxm, czm+1).getBlockStorageArray()[cym];
		chunks[4]=this.worldObj.getChunkFromChunkCoords(cxm, czm).getBlockStorageArray()[cym+1];
		chunks[5]=this.worldObj.getChunkFromChunkCoords(cxm+1, czm).getBlockStorageArray()[cym+1];
		chunks[6]=this.worldObj.getChunkFromChunkCoords(cxm+1, czm+1).getBlockStorageArray()[cym+1];
		chunks[7]=this.worldObj.getChunkFromChunkCoords(cxm, czm+1).getBlockStorageArray()[cym+1];
		
		for(int i=0;i<4;i++){
			ExtendedBlockStorage chunk=new ExtendedBlockStorage(cym<<4,false);
			chunk.setBlockLSBArray(chunks[i].getBlockLSBArray());
			chunk.setBlockMSBArray(chunks[i].getBlockMSBArray());
		}
		for(int i=4;i<8;i++){
			ExtendedBlockStorage chunk=new ExtendedBlockStorage((cym+1)<<4,false);
			chunk.setBlockLSBArray(chunks[i].getBlockLSBArray());
			chunk.setBlockMSBArray(chunks[i].getBlockMSBArray());
		}
		 */
		//dispense
		BlockPos posf=new BlockPos(fx, fy, fz);
		BlockPos posb=new BlockPos(bx, by, bz);
		
		Block block=this.worldObj.getBlockState(posf).getBlock();
		
		IBlockState state=Blocks.dispenser.getDefaultState().withProperty(BlockDispenser.FACING, face);
		this.worldObj.setBlockState(posb,state);
		TileEntity t=this.worldObj.getTileEntity(posb);
		if(t instanceof TileEntityDispenser){
			TileEntityDispenser te=(TileEntityDispenser) t;
			ItemStack stackAmmo=this.getStackInSlot(0);
			te.setInventorySlotContents(0, stackAmmo);
			List entityListCopy=new ArrayList(this.worldObj.loadedEntityList);

			//int entityNum=this.worldObj.loadedEntityList.size();
			List<Entity> firedEntityList=new ArrayList();
			
			for(int i=0;i<((this.calibre/5)*(this.calibre/5)*2);i++){
				/**Make a list of what are fired*/
				
				Blocks.dispenser.updateTick(this.worldObj,posb,state, this.worldObj.rand);
				
				
				/*
				ExtendedBlockStorage[] chunksL=new ExtendedBlockStorage[8];
				
				chunksL[0]=this.worldObj.getChunkFromChunkCoords(cxm, czm).getBlockStorageArray()[cym];
				chunksL[1]=this.worldObj.getChunkFromChunkCoords(cxm+1, czm).getBlockStorageArray()[cym];
				chunksL[2]=this.worldObj.getChunkFromChunkCoords(cxm+1, czm+1).getBlockStorageArray()[cym];
				chunksL[3]=this.worldObj.getChunkFromChunkCoords(cxm, czm+1).getBlockStorageArray()[cym];
				chunksL[4]=this.worldObj.getChunkFromChunkCoords(cxm, czm).getBlockStorageArray()[cym+1];
				chunksL[5]=this.worldObj.getChunkFromChunkCoords(cxm+1, czm).getBlockStorageArray()[cym+1];
				chunksL[6]=this.worldObj.getChunkFromChunkCoords(cxm+1, czm+1).getBlockStorageArray()[cym+1];
				chunksL[7]=this.worldObj.getChunkFromChunkCoords(cxm, czm+1).getBlockStorageArray()[cym+1];
				
				for(int j=0;j<8;j++){
					for(int cx=0;cx<16;cx++){
						for(int cy=0;cy<16;cy++){
							for(int cz=0;cz<16;cz++){
								if(chunks[j].getBlockByExtId(cx, cy, cz) != chunksL[j].getBlockByExtId(cx, cy, cz)){
									firedEntityList.add(new EntityFallingBlock(cxm+))
								}
							}
						}
					}
				}
				*/
			}

			if((stackAmmo!=null && stackAmmo.stackSize<=0)||te.getStackInSlot(0)==null){
				this.setInventorySlotContents(0, null);
			}
			te.setInventorySlotContents(0, null);
			this.worldObj.setBlockToAir(posb);
			
			
			Block block1=this.worldObj.getBlockState(posf).getBlock();
			if(block != block1){

				EntityFallingBlockEx entity=new EntityFallingBlockEx(this.worldObj,fx+0.5, fy+0.5, fz+0.5,state);
				NBTTagCompound nbt=new NBTTagCompound();
				TileEntity tileentity=this.worldObj.getTileEntity(posf);
				if(tileentity!=null){
					tileentity.writeToNBT(nbt);
				}
				entity.tileEntityData=nbt;
				entity.doDropItem=false;
				this.worldObj.spawnEntityInWorld(entity);
				((WorldServer)this.worldObj).getEntityTracker().sendToAllTrackingEntity
				(entity,new S0EPacketSpawnObject(entity, 70, Block.getIdFromBlock(entity.func_145805_f())));
				this.worldObj.setBlockToAir(posf);
				//System.out.println(block1.getLocalizedName());
			}
			
			for(int i=0;i<this.worldObj.loadedEntityList.size();i++){
				Entity e=(Entity) this.worldObj.loadedEntityList.get(i);
				if(!((i<entityListCopy.size() && e==entityListCopy.get(i)) || entityListCopy.contains(e))){
					if(e instanceof EntityItem ){
						EntityItem entityitem=(EntityItem)e;
						if(entityitem.getEntityItem().getItem() instanceof ItemBlock){
							ItemStack stack=entityitem.getEntityItem();
							ItemBlock item=(ItemBlock)stack.getItem();
							if(item.getBlock().canPlaceBlockAt(this.worldObj, posf) &&
									this.worldObj.setBlockState(posf, item.getBlock().getDefaultState())){


								EntityFallingBlockEx entity=new EntityFallingBlockEx(this.worldObj,fx+0.5, fy+0.5, fz+0.5,this.worldObj.getBlockState(posf));
								NBTTagCompound nbt=new NBTTagCompound();
								TileEntity tileentity=this.worldObj.getTileEntity(posf);
								if(tileentity!=null){
									tileentity.writeToNBT(nbt);
								}
								entity.tileEntityData=nbt;
								entity.doDropItem=false;
								this.worldObj.spawnEntityInWorld(entity);
								((WorldServer)this.worldObj).getEntityTracker().func_151248_b
								(entity,new S0EPacketSpawnObject(entity, 70, Block.getIdFromBlock(entity.func_145805_f())));
								this.worldObj.setBlockToAir(posf);
								this.worldObj.loadedEntityList.set(i, entity);
								e.setDead();
								((WorldServer)this.worldObj).getEntityTracker().func_151248_b
								(e,new S13PacketDestroyEntities(new int[]{e.getEntityId()}));
								//System.out.println(block1.getLocalizedName());
								firedEntityList.add(entity);
								
							}
						}
					}else{
						firedEntityList.add(e);
					}
				}
			}
			
			
			
			if(firedEntityList.size()==0){
				Entity entity=this.riddenByEntity;
				if(entity instanceof EntityPlayer){
					((EntityPlayer)entity).addChatMessage(new ChatComponentText("Something is blocking fire!"));
				}
				return;
			}
			this.reloadtime=0;
			this.playSound("random.explode",1.0f,1.0f-((float)this.calibre/80));
			WorldServer worldserver=(WorldServer)this.worldObj;
			 if (this.calibre >= 15)
		        {
				 	worldserver.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, x,y,z, 1.0D, 0.0D, 0.0D,new int[0]);
		        }
		        else
		        {
		        	worldserver.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x,y,z, 1.0D, 0.0D, 0.0D,new int[0]);
		        }
			
			/**Calculate their volume and decide speed*/
			double volumeSum=0;
			sum:{
				for(Entity e:firedEntityList){
					double width=e.width<0.2 ? 0.2:e.width;
					double height=e.height<0.2 ? 0.2:e.height;
					if(width>(double)this.calibre/10 || height>(double)this.calibre/10){
						volumeSum=64;
						break sum;
					}
					volumeSum+=width*width*height;
				}
				if(volumeSum>32)volumeSum=32;
				if(volumeSum<0.25)volumeSum=0.25;
			}
			volumeSum=Math.sqrt(volumeSum);
			double speed=this.bulletSpeed*MathHelper.sqrt_float(this.getStackInSlot(this.ammoSlot).stackSize)/MathHelper.sqrt_double(volumeSum);
			
			this.setSlotNull(this.ammoSlot);
			//System.out.println("volume:"+volumeSum+" speed:"+speed);
			
			for(int i=0;i<firedEntityList.size();i++){
				Entity entity=(Entity)firedEntityList.get(i);
				if(entity instanceof EntityArrow){
					EntityArrow arrow=(EntityArrow) entity;
					arrow.setDamage(arrow.getDamage()*speed*1.5);
				}else {}
				Random r=this.worldObj.rand;
				
				entity.setPositionAndRotation(x+MathHelper.clamp_double(r.nextGaussian(), -1, 1)*(double)this.calibre/20, y+MathHelper.clamp_double(r.nextGaussian(), -1, 1)*(double)this.calibre/20,
						z+MathHelper.clamp_double(r.nextGaussian(), -1, 1)*(double)this.calibre/20, this.rotationYawHead, this.rotationPitch);
				
				float yaw=(float) (this.rotationYawHead+(MathHelper.clamp_double(r.nextGaussian(), -1, 1))*this.precision);
				float pitch=(float) (this.rotationPitch+(MathHelper.clamp_double(r.nextGaussian(), -1, 1))*this.precision);

				double ml=speed*MathHelper.cos(pitch*(float)Math.PI/180);

				double mx= -ml*MathHelper.sin(yaw*(float)Math.PI/180);
				double my= -speed*MathHelper.sin(pitch*(float)Math.PI/180);
				double mz= ml*MathHelper.cos(yaw*(float)Math.PI/180);

				entity.motionX=mx;
				entity.motionY=my;
				entity.motionZ=mz;

				entity.rotationPitch=this.rotationPitch;
				entity.rotationYaw=this.rotationYawHead;
				
				((WorldServer)this.worldObj).getEntityTracker().func_151248_b
				(entity, new S18PacketEntityTeleport(entity));
				((WorldServer)this.worldObj).getEntityTracker().func_151248_b
				(entity, new S12PacketEntityVelocity(entity.getEntityId(), entity.motionX, entity.motionY, entity.motionZ));

				//System.out.println(entity.toString());
				
			}
			
			

		}
		
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		{
			NBTTagList tagList = tagCompound.getTagList("AmmoList", 10);
			for (int i = 0; i < tagList.tagCount(); i++) {
				NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
				byte slot = tag.getByte("Slot");
				if (slot >= 0 && slot < this.ammoSlot) {
					this.inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
				}
			}
		}
		{
			NBTTagList tagList = tagCompound.getTagList("GunPowderList", 10);
			for (int i = 0; i < tagList.tagCount(); i++) {
				NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
				byte slot = tag.getByte("Slot");
				if (slot >= this.ammoSlot && slot < this.ammoSlot+this.gunpowderSlot) {
					this.inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
				}
			}
		}
		this.inventory[this.inventory.length-1]=ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Fuel"));
		
		this.calibre=tagCompound.getInteger("Calibre");
		this.lengthOfBarrel=tagCompound.getInteger("Barrel");
		this.engine=tagCompound.getInteger("Engine");
		this.motor=tagCompound.getInteger("Motor");
		this.armor=tagCompound.getInteger("Armor");
		this.fuelLeft=tagCompound.getInteger("FuelLeft");
		this.design=tagCompound.getInteger("Design");
		this.rotationYawHead=tagCompound.getFloat("YawHead");
		this.updateDataWatcher();
		this.setStats();
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		{
			NBTTagList itemList = new NBTTagList();
			for (int i = 0; i < this.ammoSlot; i++) {
				ItemStack stack = this.inventory[i];
				if (stack != null) {
					NBTTagCompound tag = new NBTTagCompound();
					tag.setByte("Slot", (byte) i);
					stack.writeToNBT(tag);
					itemList.appendTag(tag);
				}
			}
			tagCompound.setTag("AmmoList", itemList);
		}
		{
			NBTTagList itemList = new NBTTagList();
			for (int i = this.ammoSlot; i < this.ammoSlot+this.gunpowderSlot; i++) {
				ItemStack stack = this.inventory[i];
				if (stack != null) {
					NBTTagCompound tag = new NBTTagCompound();
					tag.setByte("Slot", (byte) i);
					stack.writeToNBT(tag);
					itemList.appendTag(tag);
				}
			}
			tagCompound.setTag("GunPowderList", itemList);
		}
		{

			if(this.inventory[this.inventory.length-1]!=null){
				NBTTagCompound nbt=new NBTTagCompound();
				this.inventory[this.inventory.length-1].writeToNBT(nbt);
				tagCompound.setTag("Fuel", nbt);
			}

		}
		tagCompound.setInteger("Calibre",this.calibre);
		tagCompound.setInteger("Barrel",this.lengthOfBarrel);
		tagCompound.setInteger("Engine",this.engine);
		tagCompound.setInteger("Motor",this.motor);
		tagCompound.setInteger("Armor",this.armor);
		tagCompound.setInteger("FuelLeft", this.fuelLeft);
		tagCompound.setInteger("Design",this.design);
		tagCompound.setFloat("YawHead", this.rotationYawHead);
	}
	
	public ModelCannon getThisModel(RenderCannon renderer){
		if(this.model==null){
			this.calibre=this.dataWatcher.getWatchableObjectInt(20);
			this.lengthOfBarrel=this.dataWatcher.getWatchableObjectInt(21);
			this.motor=this.dataWatcher.getWatchableObjectInt(22);
			this.engine=this.dataWatcher.getWatchableObjectInt(23);
			this.armor=this.dataWatcher.getWatchableObjectInt(24);
			this.design=this.dataWatcher.getWatchableObjectInt(26);
			this.setStats();
			this.model=new ModelCannon((int)(this.lengthOfBarrel*16/10),(int)(this.calibre*16/10),0,renderer);
		}
		return this.model;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (this.inventory[index] != null)
        {
            ItemStack itemstack = this.inventory[index];
            this.inventory[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for(int i=0;i<this.inventory.length;i++){
			this.inventory[i]=null;
		}
	}

	@Override
	public ItemStack getCurrentArmor(int slotIn) {
		return null;
	}

	@Override
	public ItemStack[] getInventory() {
		return this.inventory;
	}

	/*
	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName()?this.customName:"container.cannon";

	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.customName!=null;
	}
*/
	
}
