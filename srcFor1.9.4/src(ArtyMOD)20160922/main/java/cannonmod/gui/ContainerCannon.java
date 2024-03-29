package cannonmod.gui;

import cannonmod.entity.EntityCannon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerCannon extends Container {
	public EntityCannon entityCannon;

	public ContainerCannon(InventoryPlayer invPlayer,EntityCannon entity){
		this.entityCannon=entity;
		this.addSlotToContainer(new SlotLoaded(entity,0,17,11));
		
		int slotsSoFar=this.inventorySlots.size();
		for(int i=0;i<(entityCannon.ammoSlot-1)/4+1;i++){
			for(int j=0;j<4;j++){
				if(i*4+j>=entityCannon.ammoSlot-1)break;
				this.addSlotToContainer(new Slot(entity,1+i*4+j,8+18*i,35+j*18));
			}
		}
		
		slotsSoFar=this.inventorySlots.size();
		this.addSlotToContainer(new SlotLoaded(entity,9,84,11));
		slotsSoFar++;
		for(int i=0;i<(entityCannon.gunpowderSlot-1)/4+1;i++){
			for(int j=0;j<4;j++){
				if(i*4+j>=entityCannon.gunpowderSlot-1)break;
				this.addSlotToContainer(new Slot(entity,10+i*4+j,75+18*i,35+j*18));
			}
		}
		slotsSoFar=this.inventorySlots.size();
		this.addSlotToContainer(new Slot(entity,18,152,82));
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
			}
		}
		for (int i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 168));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return this.entityCannon.isUseableByPlayer(p_75145_1_);
	}

	public int getAmmoSlotFrom(){
		return 0;
	}

	public int getGunPowderSlotFrom(){
		return this.getAmmoSlotFrom()+this.entityCannon.ammoSlot;
	}

	public int getFuelSlot(){
		return this.getGunPowderSlotFrom()+this.entityCannon.gunpowderSlot;
	}

	public int getPlayerInventorySlotFrom(){
		return this.getFuelSlot()+1;
	}
	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
	public ItemStack transferStackInSlot(EntityPlayer player, int num)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(num);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (num <this.getPlayerInventorySlotFrom())
			{
				if (!this.mergeItemStack(itemstack1, this.getPlayerInventorySlotFrom(), this.getPlayerInventorySlotFrom()+36, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else
			{
				if (itemstack.getItem() ==Items.GUNPOWDER)
				{
					for(int i=this.getGunPowderSlotFrom()+1;i<this.getFuelSlot();i++){
						Slot s=this.getSlot(i);
						if(s.getStack()==null){
							s.putStack(itemstack);
							s.onSlotChanged();
							itemstack1.stackSize=0;
							break;
						}
					}
				}
				else if (TileEntityFurnace.isItemFuel(itemstack1))
				{
					if (!this.mergeItemStack(itemstack1, this.getFuelSlot(), this.getFuelSlot()+1, false))
					{
						return null;
					}
				}else{
					if (!this.mergeItemStack(itemstack1, this.getAmmoSlotFrom()+1, this.getGunPowderSlotFrom(), false))
					{
						if (num >= this.getPlayerInventorySlotFrom() && num < this.getPlayerInventorySlotFrom() +27)
						{
							if (!this.mergeItemStack(itemstack1, this.getPlayerInventorySlotFrom() +27, this.getPlayerInventorySlotFrom() +36, false))
							{
								return null;
							}
						}
						else if (num >=  this.getPlayerInventorySlotFrom() +27 && num < this.getPlayerInventorySlotFrom() +36 && 
								!this.mergeItemStack(itemstack1, this.getPlayerInventorySlotFrom(), this.getPlayerInventorySlotFrom() +27, false))
						{
							return null;
						}
					}
				}

			}
			

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}
}
