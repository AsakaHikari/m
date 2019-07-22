package satisfybyone;

import net.minecraft.block.Block;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "IpponManzocMod", name = "IpponManzocMod", version = "1.0")
public class ManzocCore {
	@Instance(value = "IpponManzocMod")
	public static ManzocCore instance;

	@SidedProxy(clientSide = "satisfybyone.CoreProxyClient", serverSide = "satisfybyone.CoreProxy")
	public static CoreProxy proxy;

	public static Item itemBar;
	public static Item itemCart;
	public static Block blockBar;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// ITEMS
		itemBar = new ItemManzocBar(20, 20.0f, false);
		GameRegistry.registerItem(
				itemBar.setUnlocalizedName("ManzocBar")
						.setTextureName("manzoc:bar")
						.setCreativeTab(CreativeTabs.tabFood), "ManzocBar");
		itemCart = new ItemMinecart(0);
		GameRegistry.registerItem(
				itemCart.setUnlocalizedName("ManzocCart")
						.setTextureName("manzoc:cart")
						.setCreativeTab(CreativeTabs.tabTransport),
				"ManzocCart");

		// BLOCKS
		blockBar = new BlockManzoc().setBlockName("ManzocBarBlock")
				.setBlockTextureName("manzoc:blockManzoc")
				.setCreativeTab(CreativeTabs.tabBlock);
		GameRegistry.registerBlock(blockBar, "ManzocBarBlock");
		// ENTITIES
		int id = 0;
		EntityRegistry.registerModEntity(EntityManzocCart.class, "ManzocCart",
				id, this, 80, 1, true);// id is an internal mob id, you can
										// start at 0 and continue adding them
										// up.
		id++;

		// RECIPES
		GameRegistry.addShapelessRecipe(new ItemStack(itemBar),
				Blocks.hay_block, new ItemStack(Items.dye, 1, 3),
				new ItemStack(Items.dye, 1, 3), Items.sugar, Items.sugar,
				Items.sugar);
		GameRegistry.addRecipe(new ItemStack(blockBar), "XXX", "XXX", "XXX",
				'X', itemBar);
		GameRegistry.addRecipe(new ItemStack(itemCart), "X X", "XXX", 'X',
				blockBar);
		GameRegistry.addShapelessRecipe(new ItemStack(itemBar, 9), blockBar);
		GameRegistry.addShapelessRecipe(new ItemStack(blockBar, 5), itemCart);
		
		ManzocEventServerHandler eventHandler=new ManzocEventServerHandler();
		FMLCommonHandler.instance().bus().register(eventHandler);
		proxy.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}
}
