package gvcguns;
 
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
 
public class GVCContainer extends Container {
    //座標でGUIを開くか判定するためのもの。
    int xCoord, yCoord, zCorrd;
    
    private IInventory inventoryPlayer; 
    private GVCTileEntityGunBox tileEntity; 

    
    public GVCContainer(IInventory inventoryPlayer, int x, int y, int z, GVCTileEntityGunBox tileEntity) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCorrd = z;
        this.inventoryPlayer = inventoryPlayer; 
        this.tileEntity      = tileEntity; 

        
        
        /* 
         		 * 3*3のインベントリ 
         		 */ 
         		for (int col = 0; col < 3; ++col) 
         		{ 
         			for (int row = 0; row < 3; ++row) 
         			{ 
         				this.addSlotToContainer(new Slot(this.tileEntity, row + col * 3,  62 + row * 18, 17 + col * 18)); 
         			} 
         		} 
         
         
         		/* 
         		 *  3*9のプレイヤーインベントリ 
         		 */ 
         		for (int col = 0; col < 3; ++col) 
         		{ 
         			for (int row = 0; row < 9; ++row) 
         			{ 
         				this.addSlotToContainer(new Slot(this.inventoryPlayer, row + col * 9 + 9, 8 + row * 18, 84 + col * 18)); 
         			} 
         		} 
         
         
         		/* 
         		 *  1*9のプレイヤーインベントリ 
         		 */ 
         		for (int row = 0; row < 9; ++row) 
         		{ 
         			this.addSlotToContainer(new Slot(this.inventoryPlayer, row, 8 + row * 18, 142)); 
         		} 

        
    }
 
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        //もし、ブロックとの位置関係でGUI制御したいなら、こちらを使う
//        return player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCorrd + 0.5D) <= 64D;
        return tileEntity.isUseableByPlayer(player);
    }
    
    
    /* 
    	 * Shift+左クリックしたときの処理を行うメソッド. 
     	 * 引数はPlayerとスロット番号(左上から0) 
     	 * 戻り値は引数の移動後のスロットのItemStack(全部移動したならnull, 同種のItemStackがあって、スタックサイズの変更だけなら変更したスタックサイズのItemStackを返す) 
     	 */ 
        @Override 
     	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) 
     	{ 
     		Slot slot = (Slot)this.inventorySlots.get(slotIndex); 
     		ItemStack srcItemStack = null; 
     
     
     		if (slot != null && slot.getHasStack()) 
     		{ 
     			ItemStack destItemStack = slot.getStack(); 
     			srcItemStack = destItemStack.copy(); 
     
     
     			if (slotIndex < 9 && !this.mergeItemStack(destItemStack, 9, 45, false)) 
     			{ 
     				return null; 
     			} 
     
     
     			if (slotIndex >= 9 && !this.mergeItemStack(destItemStack, 0, 9, false)) 
     			{ 
     				return null; 
     			} 
     
     
     			if (destItemStack.stackSize == 0) 
     			{ 
     				slot.putStack((ItemStack)null); 
     			} 
     			else 
     			{ 
     				slot.onSlotChanged(); 
     			} 
     		} 
     
         		return srcItemStack; 
    	} 


    
}