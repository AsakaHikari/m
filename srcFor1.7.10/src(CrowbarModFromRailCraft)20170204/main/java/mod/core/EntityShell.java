package mod.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

public class EntityShell extends Entity {
	public ForgeChunkManager.Ticket ticket;
	public List<ChunkCoordIntPair> forcedChunkList=new ArrayList();
	public EntityShell(World p_i1582_1_) {
		super(p_i1582_1_);
	}
	@Override
	public void onUpdate(){
		super.onUpdate();
		this.motionX*=0.9;
		this.motionY-=0.03;
		this.motionZ*=0.9;
		if(!this.worldObj.isRemote){
			this.loadChunksAround();
			//System.out.println("loaded");
			List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.5,0.5,0.5));
			for(Entity e:list){
				if (e != this.ridingEntity)
	            {
	                if (e instanceof EntityMinecart &&((EntityMinecart) e).func_145817_o()==Blocks.air && this.ridingEntity == null && e.riddenByEntity == null)
	                {
	                	//System.out.println("ridden!");
	                    this.mountEntity(e);
	                }
	            }
			}
		}
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}
	
	public void loadChunksAround(){
		if(!this.forcedChunkList.isEmpty()){
			this.releaseForcedChunks();
		}
		if(this.ridingEntity!=null){
			this.makeTicket();
		}
		

	}

	public void makeTicket(){
		if(this.ticket==null){
			ForgeChunkManager.Ticket chunkTicket=ForgeChunkManager.requestTicket(BoatCraftCore.instance, this.worldObj, ForgeChunkManager.Type.ENTITY);
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
		for(int x=xChunk-1;x<=xChunk+1;x++){
			for(int z=zChunk-1;z<=zChunk+1;z++){
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
	protected void entityInit() {
		this.setSize(0.68f, 0.68f);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean canBePushed(){
		return true;
	}
	@Override
	 public boolean canBeCollidedWith(){
		return !this.isDead;
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

				if (par1DamageSource.getDamageType().equals("player"))
				{
					if(!this.worldObj.isRemote&&
							!((EntityPlayer)par1DamageSource.getSourceOfDamage()).capabilities.isCreativeMode){
						this.entityDropItem(new ItemStack(BoatCraftCore.itemShell), 0.0f);
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
}
