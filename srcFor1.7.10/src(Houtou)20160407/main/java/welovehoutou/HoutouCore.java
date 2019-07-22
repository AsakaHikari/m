package welovehoutou;

import com.maverick.minekawaii.MainTest;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "houtouMod", name = "houtouMod", version = "1.0")
public class HoutouCore {
	@Instance(value = "houtouMod")
	public static HoutouCore instance;
	public static Item itemHoutou=new ItemFood(5,4.0f,false);
	public static BlockHoutouedCauldron houtouedCauldron=new BlockHoutouedCauldron();
	public static int houtouedCauldronRenderID;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		houtouedCauldronRenderID=RenderingRegistry.getNextAvailableRenderId();
		GameRegistry.registerItem(itemHoutou.setTextureName("houtou:houtou").setUnlocalizedName("Houtou"), "Houtou");
		GameRegistry.registerBlock(houtouedCauldron, "HoutouedCauldron").setBlockTextureName("cauldron").setBlockName("HoutouedCauldron");
		RenderingRegistry.registerBlockHandler(new RenderHoutouCauldron());
	}
	
	@EventHandler
    public void init(FMLInitializationEvent event)
    {
		itemHoutou.setCreativeTab(MainTest.tabMyMod);
		GameRegistry.addShapelessRecipe(new ItemStack(itemHoutou), 
				MainTest.cookednoodles,Blocks.pumpkin,Blocks.pumpkin,Blocks.pumpkin,Items.carrot);
		GameRegistry.addShapelessRecipe(new ItemStack(MainTest.sushi),MainTest.ricebowl,new ItemStack(Items.fish,1,0));
		GameRegistry.addShapelessRecipe(new ItemStack(MainTest.sushi),MainTest.ricebowl,new ItemStack(Items.fish,1,1));
		GameRegistry.addShapelessRecipe(new ItemStack(MainTest.sushi),MainTest.ricebowl,new ItemStack(Items.fish,1,3));
		MinecraftForge.EVENT_BUS.register(new HoutouEventHandler());
    }
}
