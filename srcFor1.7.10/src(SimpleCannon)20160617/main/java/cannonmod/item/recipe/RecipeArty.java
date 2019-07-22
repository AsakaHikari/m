package cannonmod.item.recipe;

import java.util.List;

import cannonmod.core.CannonCore;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeArty implements IRecipe {
	
	public ItemStack result;

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		boolean cannon = false,chassis = false;
		NBTTagCompound nbt=new NBTTagCompound();
		for(int i=0;i<inv.getSizeInventory();i++){
			ItemStack stack=inv.getStackInSlot(i);
			if(stack==null)continue;
			if(!cannon&&stack.getItem()==CannonCore.itemCannon){
				cannon=true;
				nbt.setInteger("Calibre", stack.getTagCompound().getInteger("Calibre"));
				nbt.setInteger("Barrel", stack.getTagCompound().getInteger("Barrel"));
			}else if(!chassis&&stack.getItem()==CannonCore.itemChassis){
				chassis=true;
				nbt.setInteger("Engine",(stack.getItemDamage()&0x000000ff));
				nbt.setInteger("Motor", ((stack.getItemDamage()&0x0000ff00)>>8));
			}else{
				return false;
			}
		}
		if(!(cannon&&chassis))return false;
		this.result=new ItemStack(CannonCore.itemArty);
		this.result.setTagCompound(nbt);
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return result.copy();
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}
	
}
