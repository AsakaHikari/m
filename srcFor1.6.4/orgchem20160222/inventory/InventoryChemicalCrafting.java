package orgchem.inventory;

import java.util.ArrayList;
import java.util.List;

import orgchem.core.OrgChemCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryChemicalCrafting implements IInventory {
	/** List of the stacks in the crafting matrix. */
    private ItemStack[] stackList=new ItemStack[3];


    /**
     * Class containing the callbacks for the events on_GUIClosed and on_CraftMaxtrixChanged.
     */
    private Container eventHandler;

    public InventoryChemicalCrafting(Container par1Container)
    {
        this.eventHandler = par1Container;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.stackList.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return par1 >= this.getSizeInventory() ? null : this.stackList[par1];
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "container.crafting";
    }

    /**
     * If this returns false, the inventory name will be used as an unlocalized name, and translated into the player's
     * language. Otherwise it will be used directly.
     */
    public boolean isInvNameLocalized()
    {
        return false;
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.stackList[par1] != null)
        {
            ItemStack itemstack = this.stackList[par1];
            this.stackList[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.stackList[par1] != null)
        {
            ItemStack itemstack;

            if (this.stackList[par1].stackSize <= par2)
            {
                itemstack = this.stackList[par1];
                this.stackList[par1] = null;
                this.eventHandler.onCraftMatrixChanged(this);
                return itemstack;
            }
            else
            {
                itemstack = this.stackList[par1].splitStack(par2);

                if (this.stackList[par1].stackSize == 0)
                {
                    this.stackList[par1] = null;
                }

                this.eventHandler.onCraftMatrixChanged(this);
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.stackList[par1] = par2ItemStack;
        this.eventHandler.onCraftMatrixChanged(this);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged() {}

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    public void openChest() {}

    public void closeChest() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }
    
    public ItemStack getCraftResult(){
    	ItemStack itemStack=new ItemStack(OrgChemCore.itemBeaker);
    	if(stackList[0]!=null&&stackList[0].getItem()==OrgChemCore.itemBeaker&&stackList[1]!=null&&stackList[1].getItem()==OrgChemCore.itemBeaker&&stackList[2]==null){
    		NBTTagCompound nbt=(NBTTagCompound) stackList[0].getTagCompound().copy();
    		NBTTagCompound nbt2=stackList[1].getTagCompound();
    		int[] compounds=nbt.getIntArray("compounds");
			int[] quantity=nbt.getIntArray("quantity");
			int totalAmount=0;
			for(int i=0;i<quantity.length;i++){
				totalAmount+=quantity[i];
			}
			List<Integer> compoundsList=new ArrayList();
			List<Integer> quantityList=new ArrayList();
			for(int i=0;i<compounds.length;i++){
				compoundsList.add(compounds[i]);
				quantityList.add(quantity[i]);
			}
    		int[] compounds2=nbt2.getIntArray("compounds");
			int[] quantity2=nbt2.getIntArray("quantity");
			byte light=nbt2.getByte("light");
			int totalAmount2=0;
			for(int i=0;i<quantity2.length;i++){
				totalAmount2+=quantity2[i];
			}
			for(int i=0;i<compounds2.length;i++){
				if(compoundsList.contains(compounds2[i])){
					int id=compoundsList.indexOf(compounds2[i]);
					quantityList.set(id, (quantityList.get(id)+quantity2[i])/2);
				}else{
					compoundsList.add(compounds2[i]);
					quantityList.add(quantity2[i]/2);
				}
			}
			int[] newCompounds=new int[compoundsList.size()];
			for(int i=0;i<compoundsList.size();i++){
				newCompounds[i]=compoundsList.get(i);
			}
			int[] newQuantity=new int[quantityList.size()];
			for(int i=0;i<quantityList.size();i++){
				newQuantity[i]=quantityList.get(i);
			}
			nbt.setIntArray("compounds", newCompounds);
			nbt.setIntArray("quantity", newQuantity);
			nbt.setShort("temperature", (short) ((nbt.getShort("temperature")*totalAmount+nbt2.getShort("temperature")*totalAmount2)/(totalAmount=totalAmount2)));
			nbt.setByte("light", (byte) ((nbt.getByte("light")+nbt2.getByte("light"))/2));
			itemStack.stackSize=2;
			itemStack.setTagCompound(nbt);
			return itemStack;
    	}else if(stackList[0]!=null&&stackList[0].getItem()==OrgChemCore.itemBeaker&&stackList[2]!=null&&stackList[2].getItem()==OrgChemCore.itemBeaker&&stackList[1]==null){
    		NBTTagCompound nbt=(NBTTagCompound) stackList[0].getTagCompound().copy();
    		NBTTagCompound nbt2=stackList[1].getTagCompound();
    		int[] compounds=nbt.getIntArray("compounds");
			int[] quantity=nbt.getIntArray("quantity");
			int totalAmount=0;
			for(int i=0;i<quantity.length;i++){
				totalAmount+=quantity[i];
			}
			List<Integer> compoundsList=new ArrayList();
			List<Integer> quantityList=new ArrayList();
			for(int i=0;i<compounds.length;i++){
				compoundsList.add(compounds[i]);
				quantityList.add(quantity[i]);
			}
    		int[] compounds2=nbt2.getIntArray("compounds");
			int[] quantity2=nbt2.getIntArray("quantity");
			byte light=nbt2.getByte("light");
			int totalAmount2=0;
			for(int i=0;i<quantity2.length;i++){
				totalAmount2+=quantity2[i];
			}
			for(int i=0;i<compounds2.length;i++){
				if(compoundsList.contains(compounds2[i])){
					int id=compoundsList.indexOf(compounds2[i]);
					quantityList.set(id, (quantityList.get(id)+quantity2[i]));
				}else{
					compoundsList.add(compounds2[i]);
					quantityList.add(quantity2[i]);
				}
			}
			int[] newCompounds=new int[compoundsList.size()];
			for(int i=0;i<compoundsList.size();i++){
				newCompounds[i]=compoundsList.get(i);
			}
			int[] newQuantity=new int[quantityList.size()];
			for(int i=0;i<quantityList.size();i++){
				newQuantity[i]=quantityList.get(i);
			}
			nbt.setIntArray("compounds", newCompounds);
			nbt.setIntArray("quantity", newQuantity);
			nbt.setShort("temperature", (short) ((nbt.getShort("temperature")*totalAmount+nbt2.getShort("temperature")*totalAmount2)/(totalAmount=totalAmount2)));
			nbt.setByte("light", (byte) ((nbt.getByte("light")+nbt2.getByte("light"))));
			itemStack.setTagCompound(nbt);
			return itemStack;
    	}
    	return null;
    }
}
