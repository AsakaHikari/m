package cannonmod.item.recipe;

import cannonmod.core.CannonCore;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeChassis implements IRecipe {
	public ItemStack result;

	@Override
	public boolean matches(InventoryCrafting inv, World p_77569_2_) {
		int carriage=0,track=0,engine=0;
		int carriageLvl=-1,engineLvl=0;
		for(int i=0;i<inv.getSizeInventory();i++){
			ItemStack stack=inv.getStackInSlot(i);
			if(stack==null)continue;
			if(stack.getItem()==CannonCore.itemCarriage){
				if(carriage>0){
					return false;
				}else{
					carriageLvl=stack.getItemDamage();
					carriage++;
				}
			}else if(stack.getItem()==CannonCore.itemEngine){
				if(engine>0){
					return false;
				}else{
					engineLvl=stack.getItemDamage()+1;
					engine++;
				}
			}else if(stack.getItem()==CannonCore.itemTrack){
				if(track>1){
					return false;
				}else{
					track++;
				}
			}else {
				return false;
			}
		}
		
		if(carriage<1||track<2)return false;
		this.result=new ItemStack(CannonCore.itemChassis,1,(carriageLvl<<8)+engineLvl);
		
		return true;
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
	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
	}
	
}
