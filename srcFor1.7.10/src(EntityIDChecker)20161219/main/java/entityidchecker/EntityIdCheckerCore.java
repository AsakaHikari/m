package entityidchecker;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "recipe_sample")
public class EntityIdCheckerCore {
	public static Item checkerItem;
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		checkerItem=new CheckerItem().setUnlocalizedName("checkerItem").setCreativeTab(CreativeTabs.tabTools);
		GameRegistry.registerItem(checkerItem, "checkerItem");
	}

}
