package mod.core;

import java.io.File;
import java.io.IOException;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="shellMod",name="shellMod",version="1.6.4_0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ShellCore {
	@Instance("shellMod")
	public static ShellCore instance;
	public static int tezId;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		
	}
	
	 @EventHandler
	    public void init(FMLInitializationEvent event) {
		 
		 
		 int tezId =EntityRegistry.findGlobalUniqueEntityId();
		 EntityRegistry.registerGlobalEntityID(EntityShell.class, "Shell",tezId , 0x00000000, 0x00ffffff);
		 EntityRegistry.registerModEntity(EntityShell.class, "Shell", 0, this, 80, 1, true);
		 LanguageRegistry.instance().addStringLocalization("entity.Shell.name", "en_US", "Tezkatlipoca");
		 //id is an internal mob id, you can start at 0 and continue adding them up.
		 RenderingRegistry.registerEntityRenderingHandler(EntityShell.class, new RenderShell(1.0f));
		 //set a more readable name for the entity in given language
		 TickRegistry.registerScheduledTickHandler(new EventHookShell(), Side.SERVER);
		
		 //MinecraftForge.EVENT_BUS.register(new EventHookShell());
	 }
}
