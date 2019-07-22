package cannonmod.item.recipe;

import cannonmod.core.CannonCore;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeArtyArmor implements IRecipe{
	public ItemStack result;

	@Override
	public boolean matches(InventoryCrafting inv, World p_77569_2_) {
		ItemStack stackArty=null;
		int arty=0,armor=0;
		for(int i=0;i<inv.getSizeInventory();i++){
			ItemStack stack=inv.getStackInSlot(i);
			if(stack==null)continue;
			if(stack.getItem()==CannonCore.itemArty){
				if(arty>0){
					return false;
				}else{
					stackArty=stack;
					arty++;
				}
			}else if(stack.getItem()==CannonCore.itemArmor){
				armor++;
			}else {
				return false;
			}
		}
		
		if(arty<1||armor<1)return false;
		
		this.result=stackArty.copy();
		NBTTagCompound nbt=this.result.getTagCompound();
		nbt.setInteger("Armor", nbt.getInteger("Armor")+armor);
		this.result.setTagCompound(nbt);
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
