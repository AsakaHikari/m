package mod.bacteria.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class IllusionSlot extends Slot {

	public IllusionSlot(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	/*
	@Override
	 public ItemStack getStack()
    {
		System.out.println("called!!!");
        return this.inventory.getStackInSlot(this.getSlotIndex());
    }
	*/
	@Override
	public void putStack(ItemStack par1ItemStack)
    {
		
        this.inventory.setInventorySlotContents(this.getSlotIndex(), par1ItemStack);
        this.onSlotChanged();
    }
	@Override
	 public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return false;
    }
}
