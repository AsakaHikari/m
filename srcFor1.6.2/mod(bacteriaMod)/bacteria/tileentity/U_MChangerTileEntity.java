package mod.bacteria.tileentity;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class U_MChangerTileEntity extends TileEntity implements IInventory{
	
	public ItemStack[] inv;
	
	public U_MChangerTileEntity(){
		inv=new ItemStack[2];
	}
	 @Override
     public void readFromNBT(NBTTagCompound tagCompound) {
             super.readFromNBT(tagCompound);
             
             NBTTagList tagList = tagCompound.getTagList("Inventory");
             for (int i = 0; i < tagList.tagCount(); i++) {
                     NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
                     byte slot = tag.getByte("Slot");
                     if (slot >= 0 && slot < inv.length) {
                             inv[slot] = ItemStack.loadItemStackFromNBT(tag);
                     }
             }
     }

     @Override
     public void writeToNBT(NBTTagCompound tagCompound) {
             super.writeToNBT(tagCompound);
                             
             NBTTagList itemList = new NBTTagList();
             for (int i = 0; i < inv.length; i++) {
                     ItemStack stack = inv[i];
                     if (stack != null) {
                             NBTTagCompound tag = new NBTTagCompound();
                             tag.setByte("Slot", (byte) i);
                             stack.writeToNBT(tag);
                             itemList.appendTag(tag);
                     }
             }
             tagCompound.setTag("Inventory", itemList);
     }
     
   
 	
 	
 	public void receiveItemData(ByteArrayDataInput data){
 		 
 	     
 	     byte nbtNum;
 	     int nbtValue;
 	     
 	     nbtNum = data.readByte();
		 nbtValue= data.readInt();
 	     
		 
 		NBTTagCompound nbt=inv[0].getTagCompound();
 		switch(nbtNum){
 		case 0:
 			nbt.setInteger("use",nbtValue);
 			break;
 		case 1:
 			nbt.setInteger("urate",nbtValue);
 			break;
 		case 2:
 		case 3:
 		case 4:
 		case 5:
 			nbt.setInteger("make"+(nbtNum-2),nbtValue);
 			break;
 		case 6:
 		case 7:
 		case 8:
 		case 9:
 			nbt.setInteger("mrate"+(nbtNum-6),nbtValue);
 			break;
 		case 10:
 		case 11:
 		case 12:
 		case 13:
 			nbt.setInteger("mmax"+(nbtNum-10),nbtValue);
 			break;
 			
 		case 14:
 		case 15:
 		case 16:
 		case 17:
 			nbt.setInteger("mmin"+(nbtNum-14),nbtValue);
 			break;
 		
 		}
 	}
 	/*
 	@Override
	public Packet getDescriptionPacket()
	{
 		
		//パケットの取得
		return PacketHandler.getPacketTileEntity(this);
	}
	*/
 	
 	
@Override
public int getSizeInventory() {
	
	return inv.length;
}

@Override
public ItemStack getStackInSlot(int slot) {

	return inv[slot];
}

@Override
public ItemStack decrStackSize(int slot, int amt) {
	 ItemStack stack = getStackInSlot(slot);
     if (stack != null) {
             if (stack.stackSize <= amt) {
                     setInventorySlotContents(slot, null);
             } else {
                     stack = stack.splitStack(amt);
                     if (stack.stackSize == 0) {
                             setInventorySlotContents(slot, null);
                     }
             }
     }
     return stack;
}

@Override
public ItemStack getStackInSlotOnClosing(int slot) {
	 ItemStack stack = getStackInSlot(slot);
     if (stack != null) {
             setInventorySlotContents(slot, null);
     }
     return stack;
}

@Override
public void setInventorySlotContents(int slot, ItemStack itemstack) {
	if(!isItemValidForSlot(slot,itemstack))return;
	 inv[slot] = itemstack;
     if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
             itemstack.stackSize = getInventoryStackLimit();
     }       
	
}

@Override
public String getInvName() {
	
	return "U_MChangerTileEntity";
}

@Override
public boolean isInvNameLocalized() {
	
	return false;
}

@Override
public int getInventoryStackLimit() {
	
	return 64;
}

@Override
public boolean isUseableByPlayer(EntityPlayer entityplayer) {
	 return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
             entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
}

@Override
public void openChest() {
	
	
}

@Override
public void closeChest() {
	
	
}

@Override
public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
	
		return slot==0 ?itemstack==null?true:(itemstack.getItemName().equals("item.bacteria")) :true;
	}


}

