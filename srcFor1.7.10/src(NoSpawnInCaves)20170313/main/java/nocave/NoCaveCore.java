package nocave;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="NoSpawnInCaves",version="1_0",name="NoSpawnInCaves")
public class NoCaveCore {
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e){
		
		MinecraftForge.EVENT_BUS.register(new CancelCaveEvent());
	}
}
