package newMinecart;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
@Mod(modid = "newMinecartMod", name = "newMinecartMod", version = "1.2")
public class Core {
	    @Instance(value = "newMinecartMod")
	    public static Core instance;
	    public static Item minecartItem;
	    public static Item minecartChestItem;
	    @SidedProxy(clientSide="newMinecart.CoreProxyClient",serverSide="newMinecart.CoreProxy")
	    public static CoreProxy proxy;

	    @EventHandler
	    public void preInit(FMLPreInitializationEvent event)
	    {
	    }

	    @EventHandler
	    public void init(FMLInitializationEvent event)
	    {
	        int id =0;
	        EntityRegistry.registerModEntity(EntityMinecartEmpty.class, "NewMinecart", id, this, 80, 1, true);//id is an internal mob id, you can start at 0 and continue adding them up.
	        id++;
	        EntityRegistry.registerModEntity(EntityMinecartChest.class, "NewMinecartChest", id, this, 80, 1, true);
	        id++;
	        
	        
	       minecartItem = new ItemMinecart(0);
	        minecartItem.setUnlocalizedName("newminecart");
			minecartItem.setTextureName("newminecart:minecart_normal");
			
			minecartChestItem = new ItemMinecart(1);
			minecartChestItem.setUnlocalizedName("newminecartchest");
			minecartChestItem.setTextureName("newminecart:minecart_chest");
			Block block = (new BlockRailDetector()).setHardness(0.7F).setStepSound(Block.soundTypeMetal).setBlockName("newDetectorRail").setBlockTextureName("newminecart:rail_detector");
			block = GameRegistry.registerBlock(block, "newDetectorRail");
			GameRegistry.registerItem(minecartItem, "newminecart");
			GameRegistry.registerItem(minecartChestItem, "newminecartchest");
			GameRegistry.addShapelessRecipe(new ItemStack(minecartItem), Items.minecart);
			GameRegistry.addShapelessRecipe(new ItemStack(block),Blocks.detector_rail);
			GameRegistry.addShapelessRecipe( new ItemStack(Items.minecart),minecartItem);
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.detector_rail),block);
			GameRegistry.addShapelessRecipe(new ItemStack(minecartChestItem),Items.chest_minecart);
			GameRegistry.addShapelessRecipe(new ItemStack(Items.chest_minecart),minecartChestItem);
			GameRegistry.addShapelessRecipe(new ItemStack(minecartChestItem),minecartItem,Blocks.chest);
			
			proxy.init();
			if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				
		        }
			LanguageRegistry.instance().addStringLocalization("entity.NewMinecart.name", "en_US","New Minecart");//set a more readable name for the entity in given language
			LanguageRegistry.instance().addStringLocalization("entity.NewMinecartChest.name", "en_US","New Minecart with Chest");//set a more readable name for the entity in given language
			LanguageRegistry.instance().addStringLocalization("item.newminecart.name", "en_US","New Minecart");
			LanguageRegistry.instance().addStringLocalization("item.newminecartchest.name", "en_US","New Minecart with Chest");
			LanguageRegistry.instance().addStringLocalization("tile.newDetectorRail.name", "en_US","New Detector Rail");
	    }

}
