package waterrebillion;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid="waterRebillion",name="waterRebillion")
public class WRCore {
	@Instance("waterRebillion")
	protected WRCore instance;
	
	 @EventHandler
	 public void preInit(FMLPreInitializationEvent event) {
		 TickRegistry.registerScheduledTickHandler(new WaterInfect(), Side.SERVER);
		 TickRegistry.registerScheduledTickHandler(new WaterDamage(),Side.SERVER);
	 }
	 @EventHandler
	 public void init(FMLInitializationEvent event) {
		 
	 }
}
