package cannonmod.entity;


import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFallingBlockEx extends Entity
{
	public IBlockState fallTile;
    public int age;
    public boolean doDropItem;
    private boolean crushWhenLand;
    private boolean doHurtEntity;
    private int fallDamageMax;
    private float falldamage;
    public NBTTagCompound tileEntityData;
    protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.<BlockPos>createKey(EntityFallingBlock.class, DataSerializers.BLOCK_POS);

    public EntityFallingBlockEx(World p_i1706_1_)
    {
        super(p_i1706_1_);
        this.doDropItem = true;
        this.fallDamageMax = 40;
        this.falldamage = 2.0F;
        this.setOrigin(new BlockPos(this));
    }

    public void setOrigin(BlockPos p_184530_1_)
    {
        this.dataManager.set(ORIGIN, p_184530_1_);
    }
    
    @SideOnly(Side.CLIENT)
    public BlockPos getOrigin()
    {
        return (BlockPos)this.dataManager.get(ORIGIN);
    }

    
    
    public EntityFallingBlockEx(World p_i45319_1_, double p_i45319_2_, double p_i45319_4_, double p_i45319_6_, IBlockState p_i45319_8_)
    {
        super(p_i45319_1_);
        this.doDropItem = true;
        this.fallDamageMax = 40;
        this.falldamage = 2.0F;
        this.fallTile=p_i45319_8_;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        //this. = this.height / 2.0F;
        this.setPosition(p_i45319_2_, p_i45319_4_, p_i45319_6_);
        
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit() {
    	this.dataManager.register(ORIGIN, BlockPos.ORIGIN);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
    	if(this.worldObj.isRemote)return;
        if (this.fallTile.getBlock().getMaterial(fallTile) == Material.AIR)
        {
            this.setDead();
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            ++this.age;
            this.motionY -= 0.03999999910593033D;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;

            if (!this.worldObj.isRemote)
            {
                int i = MathHelper.floor_double(this.posX);
                int j = MathHelper.floor_double(this.posY);
                int k = MathHelper.floor_double(this.posZ);
                BlockPos blockpos1 = new BlockPos(this);
                if (this.onGround)
                {
                    this.motionX *= 0.699999988079071D;
                    this.motionZ *= 0.699999988079071D;
                    this.motionY *= -0.5D;

                    if (this.worldObj.getBlockState(blockpos1).getBlock() != Blocks.PISTON_EXTENSION)
                    {
                        this.setDead();

                        if(!(BlockFalling.canFallThrough(this.worldObj.getBlockState(new BlockPos(i, j - 1, k))) && this.motionX*this.motionX+this.motionZ+this.motionZ>0.1)){


                        	if (!this.crushWhenLand && this.worldObj.setBlockState(blockpos1,this.fallTile))
                        	{

                        		if (this.fallTile.getBlock() instanceof BlockFalling)
                        		{
                        			((BlockFalling)this.fallTile.getBlock()).onEndFalling(this.worldObj, blockpos1 );
                        		}

                        		if (this.tileEntityData != null && this.fallTile.getBlock() instanceof ITileEntityProvider)
                        		{
                        			TileEntity tileentity = this.worldObj.getTileEntity(blockpos1);

                        			if (tileentity != null)
                        			{
                        				NBTTagCompound nbttagcompound = new NBTTagCompound();
                        				tileentity.writeToNBT(nbttagcompound);
                        				Iterator iterator = this.tileEntityData.getKeySet().iterator();

                        				while (iterator.hasNext())
                        				{
                        					String s = (String)iterator.next();
                        					NBTBase nbtbase = this.tileEntityData.getTag(s);

                        					if (!s.equals("x") && !s.equals("y") && !s.equals("z"))
                        					{
                        						nbttagcompound.setTag(s, nbtbase.copy());
                        					}
                        				}

                        				tileentity.readFromNBT(nbttagcompound);
                        				tileentity.markDirty();
                        			}
                        		}
                        	}
                        	else if (this.doDropItem && !this.crushWhenLand)
                        	{
                        		this.entityDropItem(new ItemStack(this.fallTile.getBlock(), 1, this.fallTile.getBlock().damageDropped(this.fallTile)), 0.0F);
                        	}
                        }
                    }
                }
                else if (this.age > 100 && !this.worldObj.isRemote && (j < 1 || j > 256) || this.age > 600)
                {
                    if (this.doDropItem)
                    {
                        this.entityDropItem(new ItemStack(this.fallTile.getBlock(), 1, this.fallTile.getBlock().damageDropped(this.fallTile)), 0.0F);
                    }

                    this.setDead();
                }
            }
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    public void fall(float distance, float damageMultiplier)
    {
        if (this.doHurtEntity)
        {
            int i = MathHelper.ceiling_float_int(distance - 1.0F);
            Block block = this.fallTile.getBlock();
            if (i > 0)
            {
                ArrayList arraylist = new ArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
                boolean flag = block == Blocks.ANVIL;
                DamageSource damagesource = flag ? DamageSource.anvil : DamageSource.fallingBlock;
                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext())
                {
                    Entity entity = (Entity)iterator.next();
                    entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor_float((float)i * this.falldamage), this.fallDamageMax));
                }

                if (flag && (double)this.rand.nextFloat() < 0.05000000074505806D + (double)i * 0.05D)
                {
                    int j = ((Integer)this.fallTile.getValue(BlockAnvil.DAMAGE)).intValue();
                    ++j;

                    if (j > 2)
                    {
                        this.crushWhenLand = false;
                    }
                    else
                    {
                        this.fallTile = this.fallTile.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(j));
                    }
                }
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
    	Block block = this.fallTile != null ? this.fallTile.getBlock() : Blocks.AIR;
        ResourceLocation resourcelocation = (ResourceLocation)Block.REGISTRY.getNameForObject(block);
        p_70014_1_.setString("Block", resourcelocation == null ? "" : resourcelocation.toString());
        p_70014_1_.setByte("Data", (byte)block.getMetaFromState(this.fallTile));
        p_70014_1_.setByte("Time", (byte)this.age);
        p_70014_1_.setBoolean("DropItem", this.doDropItem);
        p_70014_1_.setBoolean("HurtEntities", this.doHurtEntity);
        p_70014_1_.setFloat("FallHurtAmount", this.falldamage);
        p_70014_1_.setInteger("FallHurtMax", this.fallDamageMax);

        if (this.tileEntityData != null)
        {
            p_70014_1_.setTag("TileEntityData", this.tileEntityData);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
    	 int i = p_70037_1_.getByte("Data") & 255;

         if (p_70037_1_.hasKey("Block", 8))
         {
             this.fallTile = Block.getBlockFromName(p_70037_1_.getString("Block")).getStateFromMeta(i);
         }
         else if (p_70037_1_.hasKey("TileID", 99))
         {
             this.fallTile = Block.getBlockById(p_70037_1_.getInteger("TileID")).getStateFromMeta(i);
         }
         else
         {
             this.fallTile = Block.getBlockById(p_70037_1_.getByte("Tile") & 255).getStateFromMeta(i);
         }
        this.age = p_70037_1_.getByte("Time") & 255;
        Block block = this.fallTile.getBlock();

        if (p_70037_1_.hasKey("HurtEntities", 99))
        {
            this.doHurtEntity = p_70037_1_.getBoolean("HurtEntities");
            this.falldamage = p_70037_1_.getFloat("FallHurtAmount");
            this.fallDamageMax = p_70037_1_.getInteger("FallHurtMax");
        }
        else if (block == Blocks.ANVIL)
        {
            this.doHurtEntity = true;
        }

        if (p_70037_1_.hasKey("DropItem", 99))
        {
            this.doDropItem = p_70037_1_.getBoolean("DropItem");
        }

        if (p_70037_1_.hasKey("TileEntityData", 10))
        {
            this.tileEntityData = p_70037_1_.getCompoundTag("TileEntityData");
        }

        if (block.getMaterial(fallTile) == Material.AIR)
        {
            block = Blocks.SAND;
        }
    }

    public void func_145806_a(boolean p_145806_1_)
    {
        this.doHurtEntity = p_145806_1_;
    }

    public void addEntityCrashInfo(CrashReportCategory p_85029_1_)
    {
        super.addEntityCrashInfo(p_85029_1_);
        Block block = this.fallTile.getBlock();
        p_85029_1_.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(block)));
        p_85029_1_.addCrashSection("Immitating block data", Integer.valueOf(block.getMetaFromState(this.fallTile)));
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public World func_145807_e()
    {
        return this.worldObj;
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire()
    {
        return false;
    }

    public Block func_145805_f()
    {
        return this.fallTile.getBlock();
    }
    
    public IBlockState getBlock(){
    	return this.fallTile;
    }
}