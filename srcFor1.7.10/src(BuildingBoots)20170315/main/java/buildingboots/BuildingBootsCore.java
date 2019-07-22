/*    */package buildingboots;

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
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameData;
/*    */
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
/*    */
import net.minecraft.creativetab.CreativeTabs;
/*    */
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
/*    */@Mod(modid = "BuildingBootsMod", name = "BuildingBootsMod", version = "Alpha1_2")
/*    */public class BuildingBootsCore{
	/*    */
	/*    */@Mod.Instance("BuildingBootsMod")
	/*    */public static BuildingBootsCore instance;
			//public static List<Item> triggerItems;
	/*    */public static Item itembuildingboots_0;
	public static Item itembuildingboots_1;
	public static Item itembuildingboots_2;
	/* 28 */public static ItemArmor.ArmorMaterial BUILDING = EnumHelper
			.addArmorMaterial("BuildingArmorMaterial", 1000, new int[] { 0, 0, 0,0 }, 0);

	/*    */	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("BuildingBootsMod");
	/*    */@Mod.EventHandler
	/*    */public void preInit(FMLPreInitializationEvent e)
	/*    */{
		//triggerItems=new ArrayList();
		Configuration cfg=new Configuration(e.getSuggestedConfigurationFile());
		cfg.load();
		//Property itemsP=cfg.get("system", "ITEMS to put automatically when you wearing torch boots", new String[]{"minecraft:torch"},
				//"seperate itemIDs with Newline");
		cfg.save();
		/*
		for(String s:itemsP.getStringList()){
			Item item=tryFindingItem(s);
			if(item !=null){
				System.out.println(item.getUnlocalizedName()+" put");
				triggerItems.add(item);
			}
		}
		 */
		itembuildingboots_0 = new ItemBuildingBoots(BUILDING, 0, 3,0)
		.setUnlocalizedName("building_boots_0")
		.setTextureName("buildingboots:building_boots_0")
		.setCreativeTab(CreativeTabs.tabCombat);
		GameRegistry.registerItem(itembuildingboots_0, "building_boots_0");
		
		itembuildingboots_1 = new ItemBuildingBoots(BUILDING, 0, 3,1)
		.setUnlocalizedName("building_boots_1")
		.setTextureName("buildingboots:building_boots_1")
		.setCreativeTab(CreativeTabs.tabCombat);
		GameRegistry.registerItem(itembuildingboots_1, "building_boots_1");
		
		itembuildingboots_2 = new ItemBuildingBoots(BUILDING, 0, 3,2)
		.setUnlocalizedName("building_boots_2")
		.setTextureName("buildingboots:building_boots_2")
		.setCreativeTab(CreativeTabs.tabCombat);
		GameRegistry.registerItem(itembuildingboots_2, "building_boots_2");
		
		GameRegistry.addRecipe(new ItemStack(itembuildingboots_0), "X X","XYX",'X',Blocks.cobblestone,'Y',Items.iron_boots);
		GameRegistry.addRecipe(new ItemStack(itembuildingboots_1), "X X","XYX",'X',Blocks.stonebrick,'Y',itembuildingboots_0);
		GameRegistry.addRecipe(new ItemStack(itembuildingboots_2), "X X","XYX",'X',Blocks.brick_block,'Y',itembuildingboots_1);
		INSTANCE.registerMessage(MessageCannonGuiHandler.class,MessageCannonGui.class, 0, Side.SERVER);
	}

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
