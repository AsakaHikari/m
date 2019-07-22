/*    */package torchboots;

/*    */
/*    */import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.Mod;
/*    */
import cpw.mods.fml.common.Mod.EventHandler;
/*    */
import cpw.mods.fml.common.Mod.Instance;
/*    */
import cpw.mods.fml.common.event.FMLInitializationEvent;
/*    */
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameData;
/*    */
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
/*    */
import net.minecraft.creativetab.CreativeTabs;
/*    */
import net.minecraft.init.Blocks;
/*    */
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
/*    */
import net.minecraft.item.ItemArmor.ArmorMaterial;
/*    */
import net.minecraft.item.ItemStack;
/*    */
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
/*    */
import net.minecraftforge.common.util.EnumHelper;

/*    */
/*    */@Mod(modid = "torchBootsMod", name = "torchBootsMod", version = "Alpha1_1")
/*    */public class TorchBootsCore
/*    */{
	/*    */
	/*    */@Mod.Instance("torchBootsMod")
	/*    */public static TorchBootsCore instance;
			public static List<Item> triggerItems;
	/*    */public static Item itemtorchboots;
	/* 28 */public static ItemArmor.ArmorMaterial TORCH = EnumHelper
			.addArmorMaterial("torchArmorMaterial", 1000, new int[] { 0, 0, 0,0 }, 0);

	/*    */
	/*    */@Mod.EventHandler
	/*    */public void preInit(FMLPreInitializationEvent e)
	/*    */{
		triggerItems=new ArrayList();
		Configuration cfg=new Configuration(e.getSuggestedConfigurationFile());
		cfg.load();
		Property itemsP=cfg.get("system", "ITEMS to put automatically when you wearing torch boots", new String[]{"minecraft:torch"},
				"seperate itemIDs with Newline");
		cfg.save();
		for(String s:itemsP.getStringList()){
			Item item=tryFindingItem(s);
			if(item !=null){
				System.out.println(item.getUnlocalizedName()+" put");
				triggerItems.add(item);
			}
		}
		
		/* 41 */itemtorchboots = new ItemTorchBoots(TORCH, 0, 3)
				.setUnlocalizedName("torch_boots")
				.setTextureName("torchboots:torch_boots")
				.setCreativeTab(CreativeTabs.tabCombat);
		/* 42 */GameRegistry.registerItem(itemtorchboots, "torch_boots");
		/*    */
		/* 44 */GameRegistry.addRecipe(new ItemStack(itemtorchboots),
				new Object[] { "X X", "X X", Character.valueOf('X'),
						Blocks.torch });
		/*    */}

	/*    */
	/*    */@Mod.EventHandler
	/*    */public void init(FMLInitializationEvent e)
	/*    */{
		}
	public Item tryFindingItem(String s){
		 Item item = GameData.getItemRegistry().getObject(s);
	        if (item != null)
	        {
	            return item;
	        }
	        
	        Block block = GameData.getBlockRegistry().getObject(s);
	        if (block != Blocks.air)
	        {
	            return Item.getItemFromBlock(block);
	        }
	        return null;
	 }
	/*    */
}
