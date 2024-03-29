package gvcguns;
 
import java.util.Random;
 
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

public class GVCVillagerTrade implements IVillageTradeHandler {
 
	@Override
	public void manipulateTradesForVillager(EntityVillager villager,MerchantRecipeList recipeList, Random random) {
		
			recipeList.add(new MerchantRecipe( new ItemStack(Items.emerald, 5, 0), GVCGunsPlus.fn_ak74));
			recipeList.add(new MerchantRecipe( new ItemStack(Items.emerald, 2, 0), new ItemStack(GVCGunsPlus.fn_magazine, 2, 0)));
			
			recipeList.add(new MerchantRecipe( new ItemStack(Items.emerald, 2, 0), GVCGunsPlus.fn_box));
			recipeList.add(new MerchantRecipe( new ItemStack(Items.emerald, 7, 0), GVCGunsPlus.fn_m16a4));
			recipeList.add(new MerchantRecipe( new ItemStack(Items.emerald, 7, 0), GVCGunsPlus.fn_g36));
			
			recipeList.add(new MerchantRecipe( new ItemStack(GVCGunsPlus.fn_ak74, 1, 0), new ItemStack(Items.emerald, 4, 0)));
		//}
	}
}