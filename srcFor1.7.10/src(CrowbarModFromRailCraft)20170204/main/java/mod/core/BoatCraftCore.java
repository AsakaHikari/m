package mod.core;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import mods.railcraft.api.carts.CartTools;
import mods.railcraft.common.carts.LinkageHandler;
import mods.railcraft.common.carts.LinkageManager;
import mods.railcraft.common.items.CrowbarHandler;
import mods.railcraft.common.items.ItemCrowbar;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.*;
@Mod(modid="CrowbarModFromRailcraft",name="CrowbarModFromRailcraft")
public class BoatCraftCore {
	@Instance("CrowbarModFromRailcraft")
	protected static BoatCraftCore instance;
	public static ItemCrowbar itemBoatRope;
	public static Item itemShell;
	@SidedProxy(clientSide="mod.core.ShellCoreClient",serverSide="mod.core.ShellCoreProxy")
	public static ShellCoreProxy proxy;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		itemShell=new ItemShell().setTextureName("railcraft:chunk_loader").setUnlocalizedName("chunk_loader");
		GameRegistry.registerItem(itemShell,"chunk_loader");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemShell), new Object[] { "III", "IRI", "III", 
	        Character.valueOf('I'), 
	        "gemLapis", 
	        Character.valueOf('R'), 
	        "blockGold" }));
	    
		EntityRegistry.registerModEntity(EntityShell.class, "ChunkLoader", 0, this, 80, 4, true);
		proxy.registerRenderer();
		CartTools.linkageManager=new LinkageManager();
		ItemCrowbar.registerItem();
		MinecraftForge.EVENT_BUS.register(CrowbarHandler.instance());
		MinecraftForge.EVENT_BUS.register(LinkageHandler.getInstance());
		ForgeChunkManager.setForcedChunkLoadingCallback(this, new ShellChunkManager());
	}
	
}
