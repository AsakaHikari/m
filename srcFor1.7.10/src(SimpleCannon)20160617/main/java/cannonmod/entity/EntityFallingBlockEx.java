package cannonmod.entity;


import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityFallingBlockEx extends Entity
{
    private Block block;
    public int blockMetadata;
    public int age;
    public boolean doDropItem;
    private boolean crushWhenLand;
    private boolean doHurtEntity;
    private int fallDamageMax;
    private float falldamage;
    public NBTTagCompound tileEntityData;
    private static final String __OBFID = "CL_00001668";

    public EntityFallingBlockEx(World p_i1706_1_)
    {
        super(p_i1706_1_);
        this.doDropItem = true;
        this.fallDamageMax = 40;
        this.falldamage = 2.0F;
    }

    public EntityFallingBlockEx(World p_i45318_1_, double p_i45318_2_, double p_i45318_4_, double p_i45318_6_, Block p_i45318_8_)
    {
        this(p_i45318_1_, p_i45318_2_, p_i45318_4_, p_i45318_6_, p_i45318_8_, 0);
    }

    public EntityFallingBlockEx(World p_i45319_1_, double p_i45319_2_, double p_i45319_4_, double p_i45319_6_, Block p_i45319_8_, int p_i45319_9_)
    {
        super(p_i45319_1_);
        this.doDropItem = true;
        this.fallDamageMax = 40;
        this.falldamage = 2.0F;
        this.block = p_i45319_8_;
        this.blockMetadata = p_i45319_9_;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(p_i45319_2_, p_i45319_4_, p_i45319_6_);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = p_i45319_2_;
        this.prevPosY = p_i45319_4_;
        this.prevPosZ = p_i45319_6_;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit() {}

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
        if (this.block.getMaterial() == Material.air)
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

                if (this.onGround)
                {
                    this.motionX *= 0.699999988079071D;
                    this.motionZ *= 0.699999988079071D;
                    this.motionY *= -0.5D;

                    if (this.worldObj.getBlock(i, j, k) != Blocks.piston_extension)
                    {
                        this.setDead();

                        if(!(BlockFalling.func_149831_e(this.worldObj, i, j - 1, k) && this.motionX*this.motionX+this.motionZ+this.motionZ>0.1)){


                        	if (!this.crushWhenLand && this.worldObj.setBlock(i, j, k, this.block, this.blockMetadata, 3))
                        	{

                        		if (this.block instanceof BlockFalling)
                        		{
                        			((BlockFalling)this.block).func_149828_a(this.worldObj, i, j, k, this.blockMetadata);
                        		}

                        		if (this.tileEntityData != null && this.block instanceof ITileEntityProvider)
                        		{
                        			TileEntity tileentity = this.worldObj.getTileEntity(i, j, k);

                        			if (tileentity != null)
                        			{
                        				NBTTagCompound nbttagcompound = new NBTTagCompound();
                        				tileentity.writeToNBT(nbttagcompound);
                        				Iterator iterator = this.tileEntityData.func_150296_c().iterator();

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
                        		this.entityDropItem(new ItemStack(this.block, 1, this.block.damageDropped(this.blockMetadata)), 0.0F);
                        	}
                        }
                    }
                }
                else if (this.age > 100 && !this.worldObj.isRemote && (j < 1 || j > 256) || this.age > 600)
                {
                    if (this.doDropItem)
                    {
                        this.entityDropItem(new ItemStack(this.block, 1, this.block.damageDropped(this.blockMetadata)), 0.0F);
                    }

                    this.setDead();
                }
            }
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float p_70069_1_)
    {
        if (this.doHurtEntity)
        {
            int i = MathHelper.ceiling_float_int(p_70069_1_ - 1.0F);

            if (i > 0)
            {
                ArrayList arraylist = new ArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox));
                boolean flag = this.block == Blocks.anvil;
                DamageSource damagesource = flag ? DamageSource.anvil : DamageSource.fallingBlock;
                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext())
                {
                    Entity entity = (Entity)iterator.next();
                    entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor_float((float)i * this.falldamage), this.fallDamageMax));
                }

                if (flag && (double)this.rand.nextFloat() < 0.05000000074505806D + (double)i * 0.05D)
                {
                    int j = this.blockMetadata >> 2;
                    int k = this.blockMetadata & 3;
                    ++j;

                    if (j > 2)
                    {
                        this.crushWhenLand = true;
                    }
                    else
                    {
                        this.blockMetadata = k | j << 2;
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
        p_70014_1_.setByte("Tile", (byte)Block.getIdFromBlock(this.block));
        p_70014_1_.setInteger("TileID", Block.getIdFromBlock(this.block));
        p_70014_1_.setByte("Data", (byte)this.blockMetadata);
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
        if (p_70037_1_.hasKey("TileID", 99))
        {
            this.block = Block.getBlockById(p_70037_1_.getInteger("TileID"));
        }
        else
        {
            this.block = Block.getBlockById(p_70037_1_.getByte("Tile") & 255);
        }

        this.blockMetadata = p_70037_1_.getByte("Data") & 255;
        this.age = p_70037_1_.getByte("Time") & 255;

        if (p_70037_1_.hasKey("HurtEntities", 99))
        {
            this.doHurtEntity = p_70037_1_.getBoolean("HurtEntities");
            this.falldamage = p_70037_1_.getFloat("FallHurtAmount");
            this.fallDamageMax = p_70037_1_.getInteger("FallHurtMax");
        }
        else if (this.block == Blocks.anvil)
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

        if (this.block.getMaterial() == Material.air)
        {
            this.block = Blocks.sand;
        }
    }

    public void func_145806_a(boolean p_145806_1_)
    {
        this.doHurtEntity = p_145806_1_;
    }

    public void addEntityCrashInfo(CrashReportCategory p_85029_1_)
    {
        super.addEntityCrashInfo(p_85029_1_);
        p_85029_1_.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(this.block)));
        p_85029_1_.addCrashSection("Immitating block data", Integer.valueOf(this.blockMetadata));
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
        return this.block;
    }
}