package nomoreslimes;

import java.util.Map;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@Mod(modid="NoMoreSlimes", name="NoMoreSlimes", version="1_0")
public class NoMoreSlimesCore
{

  @Mod.Instance("SpawnChange")
  public static NoMoreSlimesCore instance;
  
  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(new NoMoreEventHandler());
  }
  
  @NetworkCheckHandler
  public boolean accept(Map<String, String>modList, Side side) {
     return true;
  }
}