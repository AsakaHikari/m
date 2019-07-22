package nooremod.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
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
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="FireSoundMod",name="FireSoundMod",version="Alpha1_0")
public class FireSoundCore {
	@Instance("FireSoundMod")
	public static FireSoundCore instance;
	public static EventCaller eventCaller;
	public static List<NoiseOfItem> noisyItemList=new ArrayList();

	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
		cfg.load();
		Property ItemsP=cfg.get("system", "Items which make some noise when be fired", new String[]{""});
		cfg.save();
		
		eventCaller=new EventCaller();
		 FMLCommonHandler.instance().bus().register(eventCaller);
		
		 for(String str:ItemsP.getStringList()){
			 NoiseOfItem noiseitem=new NoiseOfItem();
			 if(noiseitem.makeFromString(str)){
				 noisyItemList.add(noiseitem);
			 }
		 }
	}

	@EventHandler
	public void init(FMLInitializationEvent e){

	}
}
