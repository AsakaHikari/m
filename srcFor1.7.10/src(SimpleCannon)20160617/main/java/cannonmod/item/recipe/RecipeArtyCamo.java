package cannonmod.item.recipe;

import java.util.ArrayList;
import java.util.List;

import cannonmod.core.CannonCore;
import cannonmod.item.ItemArty;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeArtyCamo implements IRecipe {
	
	public ItemStack result;

	@Override
	public boolean matches(InventoryCrafting inv, World p_77569_2_) {
		List[] camoItemsLists=new List[ItemArty.camoList.length];
		camoItemsLists[0]=new ArrayList<ItemStack>(OreDictionary.getOres("dyeGray"));
		camoItemsLists[1]=new ArrayList<ItemStack>(OreDictionary.getOres("treeLeaves"));
		camoItemsLists[2]=new ArrayList<ItemStack>(OreDictionary.getOres("sandstone"));
		camoItemsLists[2].add(new ItemStack(Blocks.sand));
		camoItemsLists[3]=new ArrayList<ItemStack>(OreDictionary.getOres("stone"));
		camoItemsLists[4]=new ArrayList<ItemStack>();
		camoItemsLists[4].add(new ItemStack(Items.snowball));
		camoItemsLists[4].add(new ItemStack(Blocks.snow));
		camoItemsLists[4].add(new ItemStack(Blocks.snow_layer));
		
		int camo=-1;
		ItemStack camoStack=null;
		camo:{
			ItemStack stack=inv.getStackInRowAndColumn(0, 1);
			if(stack==null){
				return false;
			}
			for(int i=0;i<camoItemsLists.length;i++){
				List<ItemStack> list=camoItemsLists[i];
				for(ItemStack s:list){
					if(OreDictionary.itemMatches(s, stack, false)){
						camo=i;
						camoStack=s;
						break camo;
					}
				}
			}
		}
		if(camo==-1)return false;
		
		ItemStack stackArty=inv.getStackInRowAndColumn(1, 1);
		if(stackArty==null||stackArty.getItem()!=CannonCore.itemArty || stackArty.getTagCompound().getInteger("Design")==camo){
			return false;
		}
		
		{
			ItemStack stack=inv.getStackInRowAndColumn(2, 1);
			if(stack==null||!OreDictionary.itemMatches(camoStack,stack,false)){
				return false;
			}
		}
		
		for(int i=0;i<3;i++){
			ItemStack stack=inv.getStackInRowAndColumn(i, 0);
			if(stack==null||!OreDictionary.itemMatches(camoStack,stack,false)){
				return false;
			}
		}
		for(int i=0;i<3;i++){
			ItemStack stack=inv.getStackInRowAndColumn(i, 2);
			if(stack==null||!OreDictionary.itemMatches(camoStack,stack,false)){
				return false;
			}
		}
		
		this.result=stackArty.copy();
		this.result.getTagCompound().setInteger("Design", camo);
		
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		return this.result;
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
