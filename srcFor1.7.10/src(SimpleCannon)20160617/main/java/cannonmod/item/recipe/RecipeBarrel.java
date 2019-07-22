package cannonmod.item.recipe;

import cannonmod.core.CannonCore;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeBarrel implements IRecipe{
	public ItemStack result;

	@Override
	public boolean matches(InventoryCrafting inv, World p_77569_2_) {
		int calibre=-1;
		int barrel=0;
		int numBarrel=0;
		
		NBTTagCompound nbt=new NBTTagCompound();
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				
				ItemStack stack=inv.getStackInRowAndColumn(j, i);
				if(stack==null)continue;
				if(stack.getItem()==CannonCore.itemBarrel){
					if(calibre==-1){
						calibre=(stack.getItemDamage()&0x000000ff);
					}else if(calibre!=(stack.getItemDamage()&0x000000ff)){
						return false;
					}
					barrel+=((stack.getItemDamage()&0x0000ff00)>>8);
					if(barrel>160){
						return false;
					}
					numBarrel++;
				}
			}
			if(calibre!=-1){
				break;
			}
		}
		/*
		for(int i=0;i<inv.getSizeInventory();i++){
			ItemStack stack=inv.getStackInSlot(i);
			if(stack==null){
				continue;
					
			}
			if(stack.getItem()==CannonCore.itemBarrel){
				if(calibre==-1){
					calibre=(stack.getItemDamage()&0x000000ff);
				}else if(calibre!=(stack.getItemDamage()&0x000000ff)){
					return false;
				}
				barrel+=((stack.getItemDamage()&0x0000ff00)>>8);
				if(barrel>120){
					return false;
				}
				numBarrel++;
			}else{
				return false;
			}
		}
		*/
		if(numBarrel>1){
			this.result=new ItemStack(CannonCore.itemBarrel,1,(barrel<<8)+calibre);
			return true;
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		return this.result.copy();
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.result;
	}

}
