package mod.bacteria.tileentity;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GenomeChangerTileEntity extends TileEntity implements IInventory{
	
	private ItemStack[] inv;
	
	public GenomeChangerTileEntity(){
		inv=new ItemStack[1];
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
     
     public void readToPacket(ByteArrayDataInput data) {
    	/* System.out.println("received!!!");
 		//アイテムの読み込み
 		for (int i = 0; i < inv.length; i++) {
 			int id = data.readInt();
 			int stacksize = data.readByte();
 			int metadata = data.readInt();
  
 			if (id != 0 && stacksize != 0) {
 				
 				inv[i]=new ItemStack(id, stacksize, metadata);
 			} else {
 				inv[i]=null;
 			}
 		}*/
 	}
  
 	public void writeToPacket(DataOutputStream dos) {
 		/*
 		try {
 			//アイテムの書き込み
 			for (int i = 0; i < this.inv.length; i++) {
 				int id = inv[i] != null ? inv[i].itemID : 0;
 				int stacksize = inv[i] != null ? inv[i].stackSize : 0;
 				int metadata = inv[i] != null ? inv[i].getItemDamage() : 0;
 				
 				dos.writeInt(id);
 				dos.writeByte(stacksize);
 				dos.writeInt(metadata);
 			}
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 		*/
 	}
 	
 	public void receiveItemData(ByteArrayDataInput data){
 		 
 	     
 	     byte nbtNum;
 	     byte nbtValue;
 	     
 	     nbtNum = data.readByte();
		 nbtValue= data.readByte();
 	     
 		NBTTagCompound nbt=inv[0].getTagCompound();
 		String str="";
 		switch(nbtNum){
 		case 0:
 			str="str";
 			break;
 		case 1:
 			str="spd";
 			break;
 		case 2:
 			str="inc";
 			break;
 		case 3:
 			str="therm";
 			break;
 		case 4:
 			str="envUp";
 			break;
 		case 5:
 			str="envDown";
 			break;
 		case 6:
 			str="air";
 			break;
 		case 7:
 			str="effID";
 			break;
 		case 8:
 			str="effLevel";
 			break;
 		
 		}
 		nbt.setByte(str, nbtValue);
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
	
	return "GenomeChangerTileEntity";
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
	
		return itemstack==null?true:(itemstack.getItemName().equals("item.bacteria"));
	}
}