package mod.entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import mod.core.ItemShell;
import mod.core.ShellCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class EntityShell extends Entity
{
	private double prevMotionX,prevMotionY,prevMotionZ;
	public double destruction;
	public double reflection;
	public double damage;
	public EntityLivingBase setter=null;
	public float eyeHeight;
	public ForgeChunkManager.Ticket ticket;
	public List<ChunkCoordIntPair> forcedChunkList=new ArrayList();

	public EntityShell(World par1World)
	{
		super(par1World);
		this.dataWatcher.addObject(11, (float)this.posX-MathHelper.floor_double(this.posX));
		this.dataWatcher.addObject(12, (float)this.posY-MathHelper.floor_double(this.posY));
		this.dataWatcher.addObject(13, (float)this.posZ-MathHelper.floor_double(this.posZ));
		this.dataWatcher.addObject(14, MathHelper.floor_double(this.posX));
		this.dataWatcher.addObject(15, MathHelper.floor_double(this.posY));
		this.dataWatcher.addObject(16, MathHelper.floor_double(this.posZ));
	}


	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		super.onUpdate();

		if(worldObj.isRemote){
			this.posX=(double)this.dataWatcher.getWatchableObjectFloat(11)+(double)this.dataWatcher.getWatchableObjectInt(14);
			this.posY=(double)this.dataWatcher.getWatchableObjectFloat(12)+(double)this.dataWatcher.getWatchableObjectInt(15);
			this.posZ=(double)this.dataWatcher.getWatchableObjectFloat(13)+(double)this.dataWatcher.getWatchableObjectInt(16);
			return;
		}
		this.prevMotionX=this.motionX;
		this.prevMotionY=this.motionY;
		this.prevMotionZ=this.motionZ;

		if (MathHelper.abs((float) this.motionX) < 0.0001f) {
			this.motionX = 0;
		}
		if (MathHelper.abs((float) this.motionY) < 0.0001f) {
			this.motionY = 0;
		}
		if (MathHelper.abs((float) this.motionZ) < 0.0001f) {
			this.motionZ = 0;
		}


		int j=1+
				(int)((Math.max(Math.max(Math.abs(motionX),Math.abs(motionY)),Math.max(Math.abs(motionY),Math.abs(motionZ)))
						-Math.max(Math.max(Math.abs(motionX),Math.abs(motionY)),Math.max(Math.abs(motionY),Math.abs(motionZ)))%0.9)/0.9);
		//System.out.println("j="+j);

		//System.out.println("position:"+posX+","+posY+","+posZ);
		for(int i=1;i<=j;i++){

			this.onSplitUpdate(i, j);
			int minX = MathHelper.floor_double(this.boundingBox.minX - 0.0D);
			int minY = MathHelper.floor_double(this.boundingBox.minY - 0.0D);
			int minZ = MathHelper.floor_double(this.boundingBox.minZ - 0.0D);
			int maxX = MathHelper.floor_double(this.boundingBox.maxX + 0.0D);
			int maxY = MathHelper.floor_double(this.boundingBox.maxY + 0.0D);
			int maxZ = MathHelper.floor_double(this.boundingBox.maxZ + 0.0D);


			//System.out.println(maxX+","+maxY+","+maxZ+","+minX+","+minY+","+minZ+",");        
			boolean xflag=false;
			boolean yflag=false;
			boolean zflag=false;


			if (this.worldObj.checkChunksExist(minX, minY, minZ, maxX, maxY, maxZ))
			{
				for (int x = minX; x <= maxX; ++x)
				{
					for (int y = minY; y <= maxY; ++y)
					{
						for (int z = minZ; z <= maxZ; ++z)
						{
							if(!(this.worldObj.isAirBlock(x, y, z))){

								Block target=this.worldObj.getBlock(x, y, z);
								target.setBlockBoundsBasedOnState(worldObj, x, y, z);
								AxisAlignedBB tAABB=target.getCollisionBoundingBoxFromPool(worldObj, 0, 0, 0);
								if(tAABB!=null){
									if(this.boundingBox.intersectsWith(AxisAlignedBB.getBoundingBox(tAABB.minX+x,tAABB.minY+y,tAABB.minZ+z,
											tAABB.maxX+x,tAABB.maxY+y,tAABB.maxZ+z))){
										double relativeX,relativeY,relativeZ;
										relativeX=this.posX-x-(tAABB.maxX+tAABB.minX)/2;
										relativeY=this.getCentreYOffset()-y-(tAABB.maxY+tAABB.minY)/2;
										relativeZ=this.posZ-z-(tAABB.maxZ+tAABB.minZ)/2;
										//System.out.println(relativeX+","+relativeY+","+relativeZ);

										relativeX*=(tAABB.maxY-tAABB.minY)*(tAABB.maxZ-tAABB.minZ);
										relativeY*=(tAABB.maxX-tAABB.minX)*(tAABB.maxZ-tAABB.minZ);
										relativeZ*=(tAABB.maxY-tAABB.minY)*(tAABB.maxX-tAABB.minX);
										double hardness;
										if(target.getBlockHardness(worldObj, x, y, z)==-1){
											hardness=Float.MAX_VALUE;
										}else{
											hardness=1.5*(target.getBlockHardness(worldObj, x, y, z)+(2.0-target.getBlockHardness(worldObj, x, y, z))/2)/this.destruction/ShellCore.destructionRate;
										}

										try{
											if(Math.abs(relativeX)>=Math.abs(relativeY)&&Math.abs(relativeX)>=Math.abs(relativeZ)){
												//System.out.println("CollidedX("+k1+","+l1+","+i2+")");
												if(!((target instanceof BlockDynamicLiquid)||(target instanceof BlockStaticLiquid))&&!xflag){
													if(motionX>0&&relativeX<0){
														this.onCollidedWithBlock(x, y, z,hardness, ForgeDirection.WEST,tAABB);
														if(this.motionX>hardness){
															this.onBreakBlock(x, y, z,hardness, ForgeDirection.WEST,tAABB);
														}else{
															xflag=true;
															this.onFailedToBreakBlock(x, y, z,hardness, ForgeDirection.WEST,tAABB);
														}
													}else if(motionX<=0&&relativeX>=0){
														this.onCollidedWithBlock(x, y, z,hardness, ForgeDirection.EAST,tAABB);
														if(-this.motionX>hardness){
															
															this.onBreakBlock(x, y, z,hardness, ForgeDirection.EAST,tAABB);
														}else{
															xflag=true;
															
															this.onFailedToBreakBlock(x, y, z,hardness, ForgeDirection.EAST,tAABB);
														}
													}
												}
											}else if(Math.abs(relativeY)>=Math.abs(relativeX)&&Math.abs(relativeY)>=Math.abs(relativeZ)){
												//System.out.println("CollidedY("+k1+","+l1+","+i2+")");
												if(!((target instanceof BlockDynamicLiquid)||(target instanceof BlockStaticLiquid))&&!yflag){
													if(motionY>0&&relativeY<0){
														this.onCollidedWithBlock(x, y, z,hardness, ForgeDirection.DOWN,tAABB);
														if(this.motionY>hardness){
															
															this.onBreakBlock(x, y, z,hardness, ForgeDirection.DOWN,tAABB);
														}else{
															yflag=true;
															
															this.onFailedToBreakBlock(x, y, z,hardness, ForgeDirection.DOWN,tAABB);
														}
													}else if(motionY<=0&&relativeY>=0){
														this.onCollidedWithBlock(x, y, z,hardness, ForgeDirection.UP,tAABB);
														if(-this.motionY>hardness){
															
															this.onBreakBlock(x, y, z,hardness, ForgeDirection.UP,tAABB);
														}else{
															yflag=true;
															
															this.onFailedToBreakBlock(x, y, z,hardness, ForgeDirection.UP,tAABB);
														}
													}
												}
											}else if(Math.abs(relativeZ)>=Math.abs(relativeY)&&Math.abs(relativeZ)>=Math.abs(relativeX)){

												//System.out.println("CollidedZ("+k1+","+l1+","+i2+")");
												if(!((target instanceof BlockDynamicLiquid)||(target instanceof BlockStaticLiquid))&&!zflag){
													if(motionZ>0&&relativeZ<0){
														this.onCollidedWithBlock(x, y, z,hardness, ForgeDirection.NORTH,tAABB);
														if(this.motionZ>hardness){
															
															this.onBreakBlock(x, y, z,hardness, ForgeDirection.NORTH,tAABB);
														}else{
															zflag=true;
															
															this.onFailedToBreakBlock(x, y, z,hardness, ForgeDirection.NORTH,tAABB);
														}
													}else if(motionZ<=0&&relativeZ>=0){
														this.onCollidedWithBlock(x, y, z,hardness, ForgeDirection.SOUTH,tAABB);
														if(-this.motionZ>hardness){
															
															this.onBreakBlock(x, y, z,hardness, ForgeDirection.SOUTH,tAABB);
														}else{
															zflag=true;
															
															this.onFailedToBreakBlock(x, y, z,hardness, ForgeDirection.SOUTH,tAABB);
														}
													}
												}
											}

										}catch(NullPointerException e){

										}
									}
								}
							}
						}
					}
				}
			}

			/*
        this.boundingBox.offset(motionX/j, motionY/j, motionZ/j);
        this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
        this.posY = (this.boundingBox.minY + this.boundingBox.maxY)/2.0D;
        this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
			 */
			this.setPosition(this.posX+this.motionX/j,this.posY+this.motionY/j, this.posZ+this.motionZ/j);

			List entityList=worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox);
			if(!entityList.isEmpty()){

				for(int i1=0;i1<entityList.size();i1++){
					if(entityList.get(i1) instanceof Entity){

						Entity entity=(Entity)entityList.get(i1);
						//System.out.println("collided with"+entity.getClass().getSimpleName());
						this.onCollidedWithEntity(entity);
					}
				}

			}
			if(!yflag){
				this.onGround=false;
			}
			try
			{
				this.func_145775_I();
			}
			catch (Throwable throwable)
			{
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity tile collision");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
				this.addEntityCrashInfo(crashreportcategory);
				throw new ReportedException(crashreport);
			}

		}

		if(!this.onGround){
			this.motionY -= 0.03;
		}else{
			this.motionX*=0.9;
			this.motionZ*=0.9;
		}
		if(this.posY<192){
			this.motionX *= 0.98D;
			this.motionY *= 0.98D;
			this.motionZ *= 0.98D;
		}

		//System.out.println("motion:"+motionX+","+motionY+","+motionZ);
		//System.out.println(this.onGround);


		//System.out.println("position:"+posX+","+posY+","+posZ);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.dataWatcher.updateObject(11, (float)(this.posX-MathHelper.floor_double(this.posX)));
		this.dataWatcher.updateObject(12, (float)(this.posY-MathHelper.floor_double(this.posY)));
		this.dataWatcher.updateObject(13, (float)(this.posZ-MathHelper.floor_double(this.posZ)));
		this.dataWatcher.updateObject(14, MathHelper.floor_double(this.posX));
		this.dataWatcher.updateObject(15, MathHelper.floor_double(this.posY));
		this.dataWatcher.updateObject(16, MathHelper.floor_double(this.posZ));
		this.loadChunksAround();
	}

	public void loadChunksAround(){

		if(this.motionX*this.motionX+this.motionY*this.motionY+this.motionZ*this.motionZ>0.5){
			this.makeTicket();
		}else if(!this.forcedChunkList.isEmpty()){
			this.releaseForcedChunks();
		}

	}

	public void makeTicket(){
		if(this.ticket==null){
			ForgeChunkManager.Ticket chunkTicket=ForgeChunkManager.requestTicket(ShellCore.instance, this.worldObj, ForgeChunkManager.Type.ENTITY);
			if (chunkTicket != null)
			{
				chunkTicket.getModData();
				chunkTicket.setChunkListDepth(12);
				chunkTicket.bindEntity(this);
				this.setTicket(chunkTicket);
				//System.out.println("not null!");
			}else{
				//System.out.println("null!");
			}

		}
		forceChunkLoading((MathHelper.floor_double(this.posX))>>4, (MathHelper.floor_double(this.posZ))>>4);
	}

	public void setTicket(ForgeChunkManager.Ticket ticket){
		this.ticket=ticket;
	}
	public void forceChunkLoading(int xChunk, int zChunk) {
		if (this.ticket == null) {
			return;
		}
		List<ChunkCoordIntPair> coordsLoad=new ArrayList();
		int xmod=this.motionX<0?0:1;
		int zmod=this.motionZ<0?0:1;
		for(int x=xChunk-1+xmod;x<=xChunk+xmod;x++){
			for(int z=zChunk-1+zmod;z<=zChunk+zmod;z++){
				coordsLoad.add(new ChunkCoordIntPair(x, z));

			}
		}
		for(int i=0;i<coordsLoad.size();i++){
			ChunkCoordIntPair chunk=coordsLoad.get(i);
			if(this.forcedChunkList.contains(chunk)){
				this.forcedChunkList.remove(chunk);
			}else{
				//System.out.println("forced a chunk "+chunk.chunkXPos*16+"~"+(chunk.chunkXPos*16+16)+","+chunk.chunkZPos*16+"~"+(chunk.chunkZPos*16+16));
				ForgeChunkManager.forceChunk(this.ticket, chunk);
			}
		}
		for(int i=0;i<this.forcedChunkList.size();i++){
			ForgeChunkManager.unforceChunk(ticket, this.forcedChunkList.get(i));
		}
		this.forcedChunkList=coordsLoad;
	}

	public void releaseForcedChunks(){
		for(int i=0;i<this.forcedChunkList.size();i++){
			ForgeChunkManager.unforceChunk(ticket, this.forcedChunkList.get(i));
		}
		this.forcedChunkList.clear();
	}


	@Override
	public void setSize(float width,float height){
		super.setSize(width, height);
		this.eyeHeight=height/2;
	}

	@Override
	public float getEyeHeight(){
		return this.eyeHeight;
	}

	@Override
	public boolean canBePushed(){
		return true;
	}

	public AxisAlignedBB getCollisionBox(Entity par1Entity)
	{
		return par1Entity.boundingBox;
	}

	public AxisAlignedBB getBoundingBox(){
		return this.boundingBox;
	}


	public double getCentreYOffset(){
		return this.posY+this.height/2;
	}

	/**
	 * Return the motion factor for this projectile. The factor is multiplied by the original motion.
	 */
	protected float getMotionFactor()
	{
		return 0.98F;
	}


	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("reflection", this.reflection);
		nbt.setDouble("damage", this.damage);
		nbt.setDouble("destruction", this.destruction);

	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		this.reflection=nbt.getDouble("reflection");
		this.damage=nbt.getDouble("damage");
		this.destruction=nbt.getDouble("destruction");
	}
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
	{
		if (this.isEntityInvulnerable())
		{
			return false;
		}
		else
		{
			if(!worldObj.isRemote){
				this.setBeenAttacked();

				if (par1DamageSource.getDamageType().equals("player")&&MathHelper.sqrt_double(Math.pow(motionX, 2.0)+Math.pow(motionY, 2.0)+Math.pow(motionZ, 2.0))<0.5)
				{
					if(!this.worldObj.isRemote&&
							!((EntityPlayer)par1DamageSource.getSourceOfDamage()).capabilities.isCreativeMode){
						this.entityDropItem(new ItemStack(ShellCore.itemShell,1,ItemShell.getMetadataFromEntity(this)), 0.0f);
					}
					this.setDead();
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return false;
	}

	public boolean isEnteringUnloadedChunk(){
		AxisAlignedBB aabb=this.boundingBox;
		return !this.worldObj.checkChunksExist(MathHelper.floor_double(aabb.minX),MathHelper.floor_double(aabb.minY),MathHelper.floor_double(aabb.minZ),
				MathHelper.ceiling_double_int(aabb.maxX),MathHelper.ceiling_double_int(aabb.maxY),MathHelper.ceiling_double_int(aabb.maxZ));
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	public boolean canBeCollidedWith()
	{
		return true;
	}


	/**
	 * Called when the entity is attacked.
	 */


	@SideOnly(Side.CLIENT)
	public float getShadowSize()
	{
		return 0.0F;
	}



	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float par1)
	{
		return 15728880;
	}


	@Override
	protected void entityInit() {

	}

	/**
	 * called when this broke a block
	 * @param x block xcoord
	 * @param y block ycoord
	 * @param z block zcoord
	 * @param hardness hardness of the block(treated)
	 * @param fd side which this collided
	 * @param tAABB AxisAlignedBoundingBox of the block
	 */
	public void onBreakBlock(int x,int y,int z,double hardness,ForgeDirection fd,AxisAlignedBB tAABB){
		switch(fd){
		case WEST:
			this.motionX-=hardness;
			this.worldObj.func_147480_a(x, y, z, true);
			break;
		case EAST:
			this.motionX+=hardness;
			this.worldObj.func_147480_a(x, y, z, true);
			break;
		case DOWN:
			this.motionY-=hardness;
			this.worldObj.func_147480_a(x, y, z, true);
			break;
		case UP:
			this.motionY+=hardness;
			this.worldObj.func_147480_a(x, y, z, true);
			break;
		case NORTH:
			this.motionZ-=hardness;
			this.worldObj.func_147480_a(x, y, z, true);
			break;
		case SOUTH:
			this.motionZ+=hardness;
			this.worldObj.func_147480_a(x, y, z, true);
			break;
		default:
			break;
		
		}
	}

	/**
	 * called when this failed to break a block
	 * @param x block xcoord
	 * @param y block ycoord
	 * @param z block zcoord
	 * @param hardness hardness of the block(treated)
	 * @param fd side which this collided
	 * @param tAABB AxisAlignedBoundingBox of the block
	 */
	public void onFailedToBreakBlock(int x,int y,int z,double hardness,ForgeDirection fd,AxisAlignedBB tAABB){
		switch(fd){
		
		case WEST:
			this.boundingBox.minX=x+tAABB.minX-(this.boundingBox.maxX-this.boundingBox.minX);
			this.boundingBox.maxX=x+tAABB.minX;
			this.motionX*=-this.reflection;
			break;
		case EAST:
			this.boundingBox.maxX=x+tAABB.maxX+(this.boundingBox.maxX-this.boundingBox.minX);
			this.boundingBox.minX=x+tAABB.maxX;
			this.motionX*=-this.reflection;
			break;
		case DOWN:
			this.boundingBox.minY=y+tAABB.minY-(this.boundingBox.maxY-this.boundingBox.minY);
			this.boundingBox.maxY=y+tAABB.minY;
			this.motionY*=-this.reflection;
			break;
		case UP:
			this.boundingBox.maxY=y+tAABB.maxY+(this.boundingBox.maxY-this.boundingBox.minY);
			this.boundingBox.minY=y+tAABB.maxY;
			this.motionY*=-this.reflection;
			break;
		case NORTH:
			this.boundingBox.minZ=z+tAABB.minZ-(this.boundingBox.maxZ-this.boundingBox.minZ);
			this.boundingBox.maxZ=z+tAABB.minZ;
			this.motionZ*=-this.reflection;
			break;
		case SOUTH:
			this.boundingBox.maxZ=z+tAABB.maxZ+(this.boundingBox.maxZ-this.boundingBox.minZ);
			this.boundingBox.minZ=z+tAABB.maxZ;
			this.motionZ*=-this.reflection;
			break;
		default:
			break;
		
		}
		if(fd.equals(ForgeDirection.UP)){
			if(!this.onGround&&this.motionY<MathHelper.sqrt_double(this.reflection)/5){
				this.onGround=true;
				this.motionY=0;
			}
		}
	}

	/**
	 * called when this collided with a block
	 * @param x block xcoord
	 * @param y block ycoord
	 * @param z block zcoord
	 * @param hardness hardness of the block(treated)
	 * @param fd side which this collided
	 * @param tAABB AxisAlignedBoundingBox of the block
	 */
	public void onCollidedWithBlock(int x,int y,int z,double hardness,ForgeDirection fd,AxisAlignedBB tAABB){
	}

	/**
	 * called when this collided with an entity
	 * @param entity entity collided with
	 */
	public void onCollidedWithEntity(Entity entity){
		double relativeMotionX=this.motionX-entity.motionX;
		double relativeMotionY=this.motionY-entity.motionY;
		double relativeMotionZ=this.motionZ-entity.motionZ;
		double relativeSpeedSquare=relativeMotionX*relativeMotionX+relativeMotionY*relativeMotionY+relativeMotionZ*relativeMotionZ;
		if(relativeSpeedSquare>0.25){

			double damage=this.damage*ShellCore.damageRate*(MathHelper.sqrt_double(relativeSpeedSquare)-0.5);
			entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.setter), (float)damage);

		}
		if (entity != this.riddenByEntity && entity.canBePushed())
		{
			if(entity instanceof EntityShell){
				double motionX_=this.motionX;
				double motionY_=this.motionY;
				double motionZ_=this.motionZ;
				this.motionX=entity.motionX;
				this.motionY=entity.motionY;
				this.motionZ=entity.motionZ;
				entity.motionX=motionX_;
				entity.motionY=motionY_;
				entity.motionZ=motionZ_;
			}else{
				double motionX_=this.motionX;
				double motionY_=this.motionY;
				double motionZ_=this.motionZ;
				this.motionX=this.motionX/5*4+entity.motionX/5;
				this.motionY=this.motionY/5*4+entity.motionY/5;
				this.motionZ=this.motionZ/5*4+entity.motionZ/5;
				entity.motionX=motionX_;
				entity.motionY=motionY_;
				entity.motionZ=motionZ_;
			}

			entity.applyEntityCollision(this);
		}
	}

	/**
	 * called on update split to update the destroy of blocks precisely*/
	public void onSplitUpdate(int times,int maxSplit){
	}
}
