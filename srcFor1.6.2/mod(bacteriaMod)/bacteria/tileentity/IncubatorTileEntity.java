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
	
	//燃焼時間
		public int burnTime;
	 
		public int currentItemBurnTime;
	 
		//調理時間
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
	 
			//アイテムの読み込み
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
	 
			//燃焼時間や調理時間などの読み込み
			this.burnTime = par1NBTTagCompound.getShort("BurnTime");
			this.cookTime = par1NBTTagCompound.getShort("CookTime");
			this.currentItemBurnTime = getItemBurnTime(this.sampleItemStacks[1]);
	 
		}
	 
		@Override
		public void writeToNBT(NBTTagCompound par1NBTTagCompound)
		{
			super.writeToNBT(par1NBTTagCompound);
	 
			//燃焼時間や調理時間などの書き込み
			par1NBTTagCompound.setShort("BurnTime", (short)this.burnTime);
			par1NBTTagCompound.setShort("CookTime", (short)this.cookTime);
	 
			//アイテムの書き込み
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
	 
		//かまどの処理
		@SideOnly(Side.CLIENT)
		public int getCookProgressScaled(int par1)
		{
			return this.cookTime * par1 / 200;
		}
	 
		//かまどの処理
		@SideOnly(Side.CLIENT)
		public int getBurnTimeRemainingScaled(int par1)
		{
			if (this.currentItemBurnTime == 0)
			{
				this.currentItemBurnTime = 200;
			}
	 
			return this.burnTime * par1 / this.currentItemBurnTime;
		}
	 
		//かまどの処理
		public boolean isBurning()
		{
			return this.burnTime > 0;
		}
	 
		//更新時に呼び出される
		//かまどの処理
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
	 
		//かまどの処理
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
	 
		//かまどの処理
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
	 
		//かまどの処理
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
	 
		//かまどの処理
		public static boolean isItemFuel(ItemStack par0ItemStack)
		{
			return getItemBurnTime(par0ItemStack) > 0;
		}
	 
		// スロット数
		@Override
		public int getSizeInventory() {
			return this.sampleItemStacks.length;
		}
	 
		// インベントリ内の任意のスロットにあるアイテムを取得
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
	 
		// インベントリ内のスロットにアイテムを入れる
		@Override
		public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
			this.sampleItemStacks[par1] = par2ItemStack;
	 
			if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
			{
				par2ItemStack.stackSize = this.getInventoryStackLimit();
			}
		}
	 
		// インベントリの名前
		@Override
		public String getInvName() {
			return "Sample";
		}
	 
		// 多言語対応かどうか
		@Override
		public boolean isInvNameLocalized() {
			return false;
		}
	 
		// インベントリ内のスタック限界値
		@Override
		public int getInventoryStackLimit() {
			return 64;
		}
	 
		@Override
		public void onInventoryChanged() {
			this.onInventoryChanged();
		}
	 
		// par1EntityPlayerがTileEntityを使えるかどうか
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
	 
		//ホッパーにアイテムの受け渡しをする際の優先度
		@Override
		public int[] getAccessibleSlotsFromSide(int par1) {
			return par1 == 0 ? slots_bottom : (par1 == 1 ? slots_top : slots_sides);
		}
	 
		//ホッパーからアイテムを入れられるかどうか
		@Override
		public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
			return this.isItemValidForSlot(par1, par2ItemStack);
		}
	 
		//隣接するホッパーにアイテムを送れるかどうか
		@Override
		public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
			return par3 != 0 || par1 != 1 || par2ItemStack.itemID == Item.bucketEmpty.itemID;
		}

}
