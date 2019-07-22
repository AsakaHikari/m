package boatCraft.core;
import boatCraft.item.*;
import boatCraft.entity.*;
import boatCraft.renderer.*;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.*;
@Mod(modid="boatCraft",name="boatCraft")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class BoatCraftCore {
	@Instance("boatCraft")
	protected BoatCraftCore instance;
	public static ItemModBoat itemModBoat;
	public static ItemBoatRope itemBoatRope;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		itemModBoat=new ItemModBoat(19001);
		GameRegistry.registerItem(itemModBoat,"ModBoat");
		itemBoatRope=new ItemBoatRope(19002);
		GameRegistry.registerItem(itemBoatRope,"BoatRope");
		
		int boatWithHandleId=EntityRegistry.findGlobalUniqueEntityId();
		
		EntityRegistry.registerGlobalEntityID(EntityBoatWithBelayingPin.class,"BoatWithBelayingPin",boatWithHandleId);
		
		EntityRegistry.registerModEntity(EntityBoatWithBelayingPin.class,"BoatWithBelayingPin",0,this,80,1,true);
		
		LanguageRegistry.instance().addStringLocalization("entity.BoatWithBelayingPin.name","en_US","BoatWithBelayingPin");
		
		LanguageRegistry.addName(new ItemStack(itemModBoat,1,0),"boatWithBelayingPin");
		LanguageRegistry.addName(itemBoatRope,"boatRope");
		
		RenderingRegistry.registerEntityRenderingHandler(EntityBoatWithBelayingPin.class,new RenderModBoat
				(new ResourceLocation("boatcraft:textures/entity/boatWithBelayingPin.png")));
		
		
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new onBoatSpawnEvent());
	}
}
