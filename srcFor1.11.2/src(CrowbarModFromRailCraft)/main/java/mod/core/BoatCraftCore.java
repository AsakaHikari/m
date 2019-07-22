package mod.core;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import mods.railcraft.api.carts.CartTools;
import mods.railcraft.common.carts.LinkageHandler;
import mods.railcraft.common.carts.LinkageManager;
import mods.railcraft.common.items.CrowbarHandler;
import mods.railcraft.common.items.ItemCrowbar;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.Side;
@Mod(modid="crowbarmodfromrailcraft",name="crowbarmodfromrailcraft")
@EventBusSubscriber
public class BoatCraftCore {
	@Instance("crowbarmodfromrailcraft")
	protected static BoatCraftCore instance;
	public static Item itemShell;
	@SidedProxy(clientSide="mod.core.ShellCoreClient",serverSide="mod.core.ShellCoreProxy")
	public static ShellCoreProxy proxy;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemShell), new Object[] { "III", "IRI", "III", 
			Character.valueOf('I'), 
			"gemLapis", 
			Character.valueOf('R'), 
		"blockGold" }));

		EntityRegistry.registerModEntity(new ResourceLocation("crowbarmodfromrailcraft:chunk_loader"), EntityShell.class, "ChunkLoader", 0, this, 80, 4, true);
		
		CartTools.linkageManager=new LinkageManager();
		MinecraftForge.EVENT_BUS.register(CrowbarHandler.instance());
		MinecraftForge.EVENT_BUS.register(LinkageHandler.getInstance());
		ForgeChunkManager.setForcedChunkLoadingCallback(this, new ShellChunkManager());
		if (event.getSide() == Side.CLIENT){
			ModelLoader.setCustomModelResourceLocation(ItemCrowbar.getItemObj(), 0, new ModelResourceLocation(ItemCrowbar.getItemObj().getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemShell, 0, new ModelResourceLocation(itemShell.getRegistryName(), "inventory"));
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.registerRenderer();
	}

	@SubscribeEvent
	protected static void registerItems(RegistryEvent.Register<Item> event){
		itemShell=new ItemShell().setUnlocalizedName("chunk_loader").setRegistryName("chunk_loader");
		event.getRegistry().register(itemShell);
		ItemCrowbar.registerItem(event);
	}

}
