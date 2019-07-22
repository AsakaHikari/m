package core;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
 
@Mod(modid = ModMoreFighting.modid, name = ModMoreFighting.modid, version = "1.0")
public class ModMoreFighting {
	public static final String modid = "adddrop";
	 
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new LivingAttackEventHandler());
		
	}
}
