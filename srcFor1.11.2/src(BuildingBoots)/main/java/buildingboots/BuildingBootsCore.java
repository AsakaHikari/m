/*    */package buildingboots;

/*    */
/*    */import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
/*    */
import net.minecraftforge.fml.common.Mod.EventHandler;
/*    */
import net.minecraftforge.fml.common.Mod.Instance;
/*    */
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*    */
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameData;
/*    */
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
/*    */
import net.minecraft.creativetab.CreativeTabs;
/*    */
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
/*    */
import net.minecraftforge.common.util.EnumHelper;

/*    */
/*    */@Mod(modid = "buildingbootsmod", name = "BuildingBootsMod", version = "Alpha1_2")
@EventBusSubscriber
/*    */public class BuildingBootsCore{
	/*    */
	/*    */@Mod.Instance("buildingbootsmod")
	/*    */public static BuildingBootsCore instance;
			//public static List<Item> triggerItems;
	/*    */public static Item itembuildingboots_0;
	public static Item itembuildingboots_1;
	public static Item itembuildingboots_2;
	/* 28 */public static ItemArmor.ArmorMaterial BUILDING1 = EnumHelper
			.addArmorMaterial("BuildingArmorMaterial","buildingbootsmod:building_boots_0", 1000, new int[] { 0, 0, 0,0 }, 0,SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	public static ItemArmor.ArmorMaterial BUILDING2 = EnumHelper
			.addArmorMaterial("BuildingArmorMaterial","buildingbootsmod:building_boots_1", 1000, new int[] { 0, 0, 0,0 }, 0,SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	public static ItemArmor.ArmorMaterial BUILDING3 = EnumHelper
			.addArmorMaterial("BuildingArmorMaterial","buildingbootsmod:building_boots_2", 1000, new int[] { 0, 0, 0,0 }, 0,SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);

	/*    */	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("buildingbootsmod");
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
		

		GameRegistry.addRecipe(new ItemStack(itembuildingboots_0), "X X","XYX",'X',Blocks.COBBLESTONE,'Y',Items.IRON_BOOTS);
		GameRegistry.addRecipe(new ItemStack(itembuildingboots_1), "X X","XYX",'X',Blocks.STONEBRICK,'Y',itembuildingboots_0);
		GameRegistry.addRecipe(new ItemStack(itembuildingboots_2), "X X","XYX",'X',Blocks.BRICK_BLOCK,'Y',itembuildingboots_1);
		INSTANCE.registerMessage(MessageCannonGuiHandler.class,MessageCannonGui.class, 0, Side.SERVER);
		if (e.getSide().isClient()) {
            ModelLoader.setCustomModelResourceLocation(itembuildingboots_0, 0, new ModelResourceLocation("buildingbootsmod" + ":" + "building_boots_0", "inventory"));
            ModelLoader.setCustomModelResourceLocation(itembuildingboots_1, 0, new ModelResourceLocation("buildingbootsmod" + ":" + "building_boots_1", "inventory"));
            ModelLoader.setCustomModelResourceLocation(itembuildingboots_2, 0, new ModelResourceLocation("buildingbootsmod" + ":" + "building_boots_2", "inventory"));
        }
	}
	
	@SubscribeEvent
	protected static void registerItems(RegistryEvent.Register<Item> event){
		itembuildingboots_0 = new ItemBuildingBoots(BUILDING1, 0, EntityEquipmentSlot.FEET,0)
		.setUnlocalizedName("building_boots_0")
		//.setTextureName("buildingboots:building_boots_0")
		.setCreativeTab(CreativeTabs.COMBAT)
		.setRegistryName("buildingbootsmod", "building_boots_0");
		itembuildingboots_1 = new ItemBuildingBoots(BUILDING2, 0, EntityEquipmentSlot.FEET,1)
		.setUnlocalizedName("building_boots_1")
		//.setTextureName("buildingboots:building_boots_1")
		.setCreativeTab(CreativeTabs.COMBAT)
		.setRegistryName("buildingbootsmod", "building_boots_1");
		itembuildingboots_2 = new ItemBuildingBoots(BUILDING3, 0, EntityEquipmentSlot.FEET,2)
		.setUnlocalizedName("building_boots_2")
		//.setTextureName("buildingboots:building_boots_2")
		.setCreativeTab(CreativeTabs.COMBAT)
		.setRegistryName("buildingbootsmod", "building_boots_2");
		event.getRegistry().registerAll(itembuildingboots_0,itembuildingboots_1,itembuildingboots_2);
		
	}

	/*    */
	/*    */@Mod.EventHandler
	/*    */public void init(FMLInitializationEvent e)
	/*    */{
		}
}
	
