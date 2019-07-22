package mod.bacteria.tileentity;



import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;

public class U_MChangerContainer extends Container{
	protected U_MChangerTileEntity tileEntity;
	
	public U_MChangerContainer(InventoryPlayer inventoryPlayer,U_MChangerTileEntity te){
        tileEntity = te;

        //the Slot constructor takes the IInventory and the slot number in that it binds to
        //and the x-y coordinates it resides on-screen

        	addSlotToContainer(new Slot(tileEntity,0,152,11));
        	addSlotToContainer(new IllusionSlot(tileEntity,1,101,15));
        	
        //commonly used vanilla code that adds the player's inventory
        bindPlayerInventory(inventoryPlayer);
}

@Override
public boolean canInteractWith(EntityPlayer player) {
        return tileEntity.isUseableByPlayer(player);
}

protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
    for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                    addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                                    8 + j * 18, 84 + i * 18));
            }
    }

    for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
    }
}

@Override
public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
	
    ItemStack stack = null;
    Slot slotObject = (Slot) inventorySlots.get(slot);

    //null checks and checks if the item can be stacked (maxStackSize > 1)
    if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            //merges the item into player inventory since its in the tileEntity
            if (slot < 2) {
                    if (!this.mergeItemStack(stackInSlot, 2, 36, true)) {
                            return null;
                    }
            }
            //places it into the tileEntity is possible since its in the player inventory
            else if (!this.mergeItemStack(stackInSlot, 0, 1, false)) {
                    return null;
            }

            if (stackInSlot.stackSize == 0) {
                    slotObject.putStack(null);
            } else {
                    slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == stack.stackSize) {
                    return null;
            }
            slotObject.onPickupFromSlot(player, stackInSlot);
    }
    return stack;
}



@Override
public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer){
	ItemStack itemstack;
	
	
	if(par1==1){
		Slot slot=(Slot)this.inventorySlots.get(par1);
		slot.putStack(null);
		this.inventorySlots.set(par1,slot);
	}
	itemstack=super.slotClick(par1, par2, par3, par4EntityPlayer);
	
	if(par1==0){
		if(((Slot)this.inventorySlots.get(par1)).getStack()==null?
			true:!((Slot)this.inventorySlots.get(par1)).getStack().getItemName().equals("item.bacteria")){
		Slot slot=(Slot)this.inventorySlots.get(1);
		slot.putStack(null);
		this.inventorySlots.set(1, slot);
		}
	}
	
	return itemstack;
}




}
