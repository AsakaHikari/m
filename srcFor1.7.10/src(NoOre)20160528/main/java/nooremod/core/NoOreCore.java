package nooremod.core;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="noOreMod",name="noOreMod",version="Alpha1_0")
public class NoOreCore {
	@Instance("noOreMod")
	public static NoOreCore instance;

	public static EventCaller eventCaller;
	public static int[] dimensions;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
		cfg.load();
		Property dimensionsP=cfg.get("system", "dimensions in which ores are reduced/deleted", new int[]{0,1});
		
		cfg.save();
		dimensions=dimensionsP.getIntList();
		eventCaller=new EventCaller();
		MinecraftForge.ORE_GEN_BUS.register(eventCaller);
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e){
		
	}
}
