/*    */package torchboots;

/*    */
/*    */import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.common.Mod;
/*    */
import net.minecraftforge.fml.common.Mod.EventHandler;
/*    */
import net.minecraftforge.fml.common.Mod.Instance;
/*    */
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*    */
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameData;
/*    */
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
/*    */
import net.minecraft.creativetab.CreativeTabs;
/*    */
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
/*    */
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
/*    */
import net.minecraft.item.ItemArmor.ArmorMaterial;
/*    */
import net.minecraft.item.ItemStack;
/*    */
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
/*    */
import net.minecraftforge.common.util.EnumHelper;

/*    */
/*    */@Mod(modid = "torchBootsMod", name = "torchBootsMod", version = "Alpha1_0")
/*    */public class TorchBootsCore
/*    */{
	/*    */
	/*    */@Mod.Instance("torchBootsMod")
	/*    */public static TorchBootsCore instance;
			public static List<Item> triggerItems;
	/*    */public static Item itemtorchboots;
	/* 28 */public static ItemArmor.ArmorMaterial TORCH = EnumHelper
			.addArmorMaterial("torchArmorMaterial", "torchboots:torch_boots", 1000, new int[] { 0, 0, 0,0 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);

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
		
		/* 41 */itemtorchboots = new ItemTorchBoots(TORCH, 0, EntityEquipmentSlot.FEET)
				.setUnlocalizedName("torch_boots")
				//.setTextureName("torchboots:torch_boots")
				.setCreativeTab(CreativeTabs.COMBAT);
		/* 42 */GameRegistry.registerItem(itemtorchboots, "torch_boots");
		/*    */
		/* 44 */GameRegistry.addRecipe(new ItemStack(itemtorchboots),
				new Object[] { "X X", "X X", Character.valueOf('X'),
						Blocks.TORCH });
        if(e.getSide().isClient()){
            ModelLoader.setCustomModelResourceLocation(itemtorchboots, 0, new ModelResourceLocation("torchboots:torch_boots", "inventory"));
        }
		/*    */}

	/*    */
	/*    */@Mod.EventHandler
	/*    */public void init(FMLInitializationEvent e)
	/*    */{
		}
	public Item tryFindingItem(String s){
		ResourceLocation r=new ResourceLocation(s);
		 Item item = GameData.getItemRegistry().getObject(r);
	        if (item != null)
	        {
	            return item;
	        }
	        
	        Block block = GameData.getBlockRegistry().getObject(r);
	        if (block != Blocks.AIR)
	        {
	            return Item.getItemFromBlock(block);
	        }
	        return null;
	 }
	/*    */
}
