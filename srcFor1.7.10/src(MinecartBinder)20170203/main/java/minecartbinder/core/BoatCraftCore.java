package minecartbinder.core;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import minecartbinder.item.ItemBoatRope;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.*;
@Mod(modid="boatCraft",name="boatCraft")
public class BoatCraftCore {
	@Instance("boatCraft")
	protected BoatCraftCore instance;
	public static ItemBoatRope itemBoatRope;
	public static Map UUIDMap=new HashMap<UUID,Integer>();
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		
		itemBoatRope=new ItemBoatRope();
		GameRegistry.registerItem(itemBoatRope,"BoatRope");
		
		int boatWithHandleId=EntityRegistry.findGlobalUniqueEntityId();
		
		LanguageRegistry.addName(itemBoatRope,"boatRope");
		
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new onBoatSpawnEvent());
	}
	
}
