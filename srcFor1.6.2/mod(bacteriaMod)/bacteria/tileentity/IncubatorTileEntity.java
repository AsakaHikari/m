package mod.bacteria.tileentity;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.*;
import net.minecraft.network.*;
import net.minecraft.tileentity.TileEntity;

public class IncubatorTileEntity extends TileEntity implements ISidedInventory{
	
	//�R�Ď���
		public int burnTime;
	 
		public int currentItemBurnTime;
	 
		//��������
		public int cookTime;
	 
		private static final int[] slots_top = new int[] {0,1};
		private static final int[] slots_middle = new int[] {2,3};
		private static final int[] slots_bottom = new int[] {4,5};
		private static final int[] slots_sides = new int[] {6};
	 
		public ItemStack[] sampleItemStacks = new ItemStack[7];
	 
		@Override
		public void readFromNBT(NBTTagCompound par1NBTTagCompound)
		{
			super.readFromNBT(par1NBTTagCompound);
	 
			//�A�C�e���̓ǂݍ���
			NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
			this.sampleItemStacks = new ItemStack[this.getSizeInventory()];
	 
			for (int i = 0; i < nbttaglist.tagCount(); ++i)
			{
				NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
				byte b0 = nbttagcompound1.getByte("Slot");
	 
				if (b0 >= 0 && b0 < this.sampleItemStacks.length)
				{
					this.sampleItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
				}
			}
	 
			//�R�Ď��Ԃ⒲�����ԂȂǂ̓ǂݍ���
			this.burnTime = par1NBTTagCompound.getShort("BurnTime");
			this.cookTime = par1NBTTagCompound.getShort("CookTime");
			this.currentItemBurnTime = getItemBurnTime(this.sampleItemStacks[1]);
	 
		}
	 
		@Override
		public void writeToNBT(NBTTagCompound par1NBTTagCompound)
		{
			super.writeToNBT(par1NBTTagCompound);
	 
			//�R�Ď��Ԃ⒲�����ԂȂǂ̏�������
			par1NBTTagCompound.setShort("BurnTime", (short)this.burnTime);
			par1NBTTagCompound.setShort("CookTime", (short)this.cookTime);
	 
			//�A�C�e���̏�������
			NBTTagList nbttaglist = new NBTTagList();
	 
			for (int i = 0; i < this.sampleItemStacks.length; ++i)
			{
				if (this.sampleItemStacks[i] != null)
				{
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte)i);
					this.sampleItemStacks[i].writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}
	 
			par1NBTTagCompound.setTag("Items", nbttaglist);
	 
		}
	 
		@Override
		public Packet getDescriptionPacket() {
	        NBTTagCompound nbtTagCompound = new NBTTagCompound();
	        this.writeToNBT(nbtTagCompound);
	        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
		}
	 
		@Override
	    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
	        this.readFromNBT(pkt.customParam1);
	    }
	 
		//���܂ǂ̏���
		@SideOnly(Side.CLIENT)
		public int getCookProgressScaled(int par1)
		{
			return this.cookTime * par1 / 200;
		}
	 
		//���܂ǂ̏���
		@SideOnly(Side.CLIENT)
		public int getBurnTimeRemainingScaled(int par1)
		{
			if (this.currentItemBurnTime == 0)
			{
				this.currentItemBurnTime = 200;
			}
	 
			return this.burnTime * par1 / this.currentItemBurnTime;
		}
	 
		//���܂ǂ̏���
		public boolean isBurning()
		{
			return this.burnTime > 0;
		}
	 
		//�X�V���ɌĂяo�����
		//���܂ǂ̏���
		public void updateEntity()
		{
			boolean flag = this.burnTime > 0;
			boolean flag1 = false;
	 
			if (this.burnTime > 0)
			{
				--this.burnTime;
			}
	 
			if (!this.worldObj.isRemote)
			{
				if (this.burnTime == 0 && this.canSmelt())
				{
					this.currentItemBurnTime = this.burnTime = getItemBurnTime(this.sampleItemStacks[1]);
	 
					if (this.burnTime > 0)
					{
						flag1 = true;
	 
						if (this.sampleItemStacks[1] != null)
						{
							--this.sampleItemStacks[1].stackSize;
	 
							if (this.sampleItemStacks[1].stackSize == 0)
							{
								this.sampleItemStacks[1] = this.sampleItemStacks[1].getItem().getContainerItemStack(this.sampleItemStacks[1]);
							}
						}
					}
				}
	 
				if (this.isBurning() && this.canSmelt())
				{
					++this.cookTime;
	 
					if (this.cookTime == 200)
					{
						this.cookTime = 0;
						this.smeltItem();
						flag1 = true;
					}
				}
				else
				{
					this.cookTime = 0;
				}
	 
				if (flag != this.burnTime > 0)
				{
					flag1 = true;
				}
			}
	 
			if (flag1)
			{
				this.onInventoryChanged();
			}
		}
	 
		//���܂ǂ̏���
		private boolean canSmelt()
		{
			if (this.sampleItemStacks[0] == null)
			{
				return false;
			}
			else
			{
				ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.sampleItemStacks[0]);
				if (itemstack == null) return false;
				if (this.sampleItemStacks[2] == null) return true;
				if (!this.sampleItemStacks[2].isItemEqual(itemstack)) return false;
				int result = this.sampleItemStacks[2].stackSize + itemstack.stackSize;
				return (result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize());
			}
		}
	 
		//���܂ǂ̏���
		public void smeltItem()
		{
			if (this.canSmelt())
			{
				ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.sampleItemStacks[0]);
	 
				if (this.sampleItemStacks[2] == null)
				{
					this.sampleItemStacks[2] = itemstack.copy();
				}
				else if (this.sampleItemStacks[2].isItemEqual(itemstack))
				{
					this.sampleItemStacks[2].stackSize += itemstack.stackSize;
				}
	 
				--this.sampleItemStacks[0].stackSize;
	 
				if (this.sampleItemStacks[0].stackSize <= 0)
				{
					this.sampleItemStacks[0] = null;
				}
			}
		}
	 
		//���܂ǂ̏���
		public static int getItemBurnTime(ItemStack par0ItemStack)
		{
			if (par0ItemStack == null)
			{
				return 0;
			}
			else
			{
				int i = par0ItemStack.getItem().itemID;
				Item item = par0ItemStack.getItem();
	 
				if (par0ItemStack.getItem() instanceof ItemBlock && Block.blocksList[i] != null)
				{
					Block block = Block.blocksList[i];
	 
					if (block == Block.woodSingleSlab)
					{
						return 150;
					}
	 
					if (block.blockMaterial == Material.wood)
					{
						return 300;
					}
	 
					
				}
	 
				if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) return 200;
				if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) return 200;
				if (item instanceof ItemHoe && ((ItemHoe) item).getMaterialName().equals("WOOD")) return 200;
				if (i == Item.stick.itemID) return 100;
				if (i == Item.coal.itemID) return 1600;
				if (i == Item.bucketLava.itemID) return 20000;
				if (i == Block.sapling.blockID) return 100;
				if (i == Item.blazeRod.itemID) return 2400;
				return GameRegistry.getFuelValue(par0ItemStack);
			}
		}
	 
		//���܂ǂ̏���
		public static boolean isItemFuel(ItemStack par0ItemStack)
		{
			return getItemBurnTime(par0ItemStack) > 0;
		}
	 
		// �X���b�g��
		@Override
		public int getSizeInventory() {
			return this.sampleItemStacks.length;
		}
	 
		// �C���x���g�����̔C�ӂ̃X���b�g�ɂ���A�C�e�����擾
		@Override
		public ItemStack getStackInSlot(int par1) {
			return this.sampleItemStacks[par1];
		}
	 
		@Override
		public ItemStack decrStackSize(int par1, int par2) {
			if (this.sampleItemStacks[par1] != null)
			{
				ItemStack itemstack;
	 
				if (this.sampleItemStacks[par1].stackSize <= par2)
				{
					itemstack = this.sampleItemStacks[par1];
					this.sampleItemStacks[par1] = null;
					return itemstack;
				}
				else
				{
					itemstack = this.sampleItemStacks[par1].splitStack(par2);
	 
					if (this.sampleItemStacks[par1].stackSize == 0)
					{
						this.sampleItemStacks[par1] = null;
					}
	 
					return itemstack;
				}
			}
			else
			{
				return null;
			}
		}
	 
		@Override
		public ItemStack getStackInSlotOnClosing(int par1) {
			if (this.sampleItemStacks[par1] != null)
			{
				ItemStack itemstack = this.sampleItemStacks[par1];
				this.sampleItemStacks[par1] = null;
				return itemstack;
			}
			else
			{
				return null;
			}
		}
	 
		// �C���x���g�����̃X���b�g�ɃA�C�e��������
		@Override
		public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
			this.sampleItemStacks[par1] = par2ItemStack;
	 
			if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
			{
				par2ItemStack.stackSize = this.getInventoryStackLimit();
			}
		}
	 
		// �C���x���g���̖��O
		@Override
		public String getInvName() {
			return "Sample";
		}
	 
		// ������Ή����ǂ���
		@Override
		public boolean isInvNameLocalized() {
			return false;
		}
	 
		// �C���x���g�����̃X�^�b�N���E�l
		@Override
		public int getInventoryStackLimit() {
			return 64;
		}
	 
		@Override
		public void onInventoryChanged() {
			this.onInventoryChanged();
		}
	 
		// par1EntityPlayer��TileEntity���g���邩�ǂ���
		@Override
		public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
			return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
		}
	 
		@Override
		public void openChest() {}
	 
		@Override
		public void closeChest() {}
	 
		@Override
		public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
			return par1 == 2 ? false : (par1 == 1 ? this.isItemFuel(par2ItemStack) : true);
		}
	 
		//�z�b�p�[�ɃA�C�e���̎󂯓n��������ۂ̗D��x
		@Override
		public int[] getAccessibleSlotsFromSide(int par1) {
			return par1 == 0 ? slots_bottom : (par1 == 1 ? slots_top : slots_sides);
		}
	 
		//�z�b�p�[����A�C�e���������邩�ǂ���
		@Override
		public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
			return this.isItemValidForSlot(par1, par2ItemStack);
		}
	 
		//�אڂ���z�b�p�[�ɃA�C�e���𑗂�邩�ǂ���
		@Override
		public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
			return par3 != 0 || par1 != 1 || par2ItemStack.itemID == Item.bucketEmpty.itemID;
		}

}
