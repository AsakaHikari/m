package alwaysnight;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="AlwaysNight",version="1.1",name="AlwaysNight")
public class AlwaysNightCore {
	public static int day;
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e){
		Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
		cfg.load();
		Property dayP=cfg.get("system", "The tick that the dawn will never come from", 58000);

		cfg.save();
		day=dayP.getInt();
		FMLCommonHandler.instance().bus().register(new ChangeNightEvent());
	}
}
