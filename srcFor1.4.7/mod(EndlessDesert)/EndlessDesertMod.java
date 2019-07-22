package mod;

import net.minecraft.src.*;
import net.minecraft.world.WorldType;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.NetworkMod;
 
import cpw.mods.fml.common.event.FMLInitializationEvent;
 
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(
	modid="EndlessDesertMod",
	name="EndlessDesertMod",
	version="1.4.7_0"
)
@NetworkMod(
		clientSideRequired = true,
		serverSideRequired = true
	)

public class EndlessDesertMod {
	public static WorldType tutorialWorld = new WorldTypeCustom(15, "DESERT");
	@Mod.Init
	public void init(FMLInitializationEvent event)
	{
		LanguageRegistry.instance().addStringLocalization("DESERT", "en_US", "DESERT");
	}
}
