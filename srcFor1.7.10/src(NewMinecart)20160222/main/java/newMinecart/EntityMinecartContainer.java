package newMinecart;


import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityMinecartContainer extends EntityMinecartRapid
{
	public InventoryBasic inventory;
    /**
     * When set to true, the minecart will drop all items when setDead() is called. When false (such as when travelling
     * dimensions) it preserves its contents.
     */
    private boolean dropContentsWhenDead = true;
    private static final String __OBFID = "CL_00001674";

    public EntityMinecartContainer(World p_i1716_1_)
    {
        super(p_i1716_1_);
    }

    public EntityMinecartContainer(World p_i1717_1_, double p_i1717_2_, double p_i1717_4_, double p_i1717_6_)
    {
        super(p_i1717_1_, p_i1717_2_, p_i1717_4_, p_i1717_6_);
    }

    public void killMinecart(DamageSource p_94095_1_)
    {

        for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.inventory.getStackInSlot(i);

            if (itemstack != null)
            {
                float f = this.rand.nextFloat() * 0.8F + 0.1F;
                float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                while (itemstack.stackSize > 0)
                {
                    int j = this.rand.nextInt(21) + 10;

                    if (j > itemstack.stackSize)
                    {
                        j = itemstack.stackSize;
                    }

                    itemstack.stackSize -= j;
                    EntityItem entityitem = new EntityItem(this.worldObj, this.posX + (double)f, this.posY + (double)f1, this.posZ + (double)f2, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));
                    float f3 = 0.05F;
                    entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                    entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                    this.worldObj.spawnEntityInWorld(entityitem);
                }
            }
        }
        this.setDead();
    }




    /**
     * Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    public void travelToDimension(int p_71027_1_)
    {
        this.dropContentsWhenDead = false;
        super.travelToDimension(p_71027_1_);
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        if (this.dropContentsWhenDead)
        {
            for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = this.inventory.getStackInSlot(i);

                if (itemstack != null)
                {
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j = this.rand.nextInt(21) + 10;

                        if (j > itemstack.stackSize)
                        {
                            j = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j;
                        EntityItem entityitem = new EntityItem(this.worldObj, this.posX + (double)f, this.posY + (double)f1, this.posZ + (double)f2, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                        this.worldObj.spawnEntityInWorld(entityitem);
                    }
                }
            }
        }

        super.setDead();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        super.writeEntityToNBT(p_70014_1_);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
        {
            if (this.inventory.getStackInSlot(i) != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory.getStackInSlot(i).writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        p_70014_1_.setTag("Items", nbttaglist);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        super.readEntityFromNBT(p_70037_1_);
        NBTTagList nbttaglist = p_70037_1_.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventory.getSizeInventory())
            {
                this.inventory.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }
        }
    }

    /**
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer p_130002_1_)
    {
        if(net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, p_130002_1_))) return true;
        if (!this.worldObj.isRemote)
        {
            p_130002_1_.displayGUIChest(this.inventory);
        }

        return true;
    }

    protected void applyDrag()
    {
        int i = 15 - Container.calcRedstoneFromInventory(this.inventory);
        float f = 0.98F + (float)i * 0.001F;
        this.motionX *= (double)f;
        this.motionY *= 0.0D;
        this.motionZ *= (double)f;
    }
}