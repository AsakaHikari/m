/*    */ package de.sanandrew.mods.betterboat;
/*    */ 
/*    */ import cpw.mods.fml.common.Mod;
/*    */ import cpw.mods.fml.common.Mod.EventHandler;
/*    */ import cpw.mods.fml.common.Mod.Instance;
/*    */ import cpw.mods.fml.common.SidedProxy;
/*    */ import cpw.mods.fml.common.event.FMLInitializationEvent;
/*    */ import cpw.mods.fml.common.event.FMLPreInitializationEvent;
/*    */ import cpw.mods.fml.common.eventhandler.EventBus;
/*    */ import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
/*    */ import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
/*    */ import cpw.mods.fml.common.registry.EntityRegistry;
/*    */ import cpw.mods.fml.relauncher.Side;
/*    */ import de.sanandrew.mods.betterboat.entity.EntityBetterBoat;
/*    */ import de.sanandrew.mods.betterboat.entity.EntitySpawnHandler;
/*    */ import de.sanandrew.mods.betterboat.network.PacketSendBoatPos;

/*    */ import java.util.Map;

/*    */ import net.minecraft.entity.EntityList;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ 
/*    */ @Mod(modid="betterboat", version="11.4514", name="Better Boat_kai")
/*    */ public class BetterBoat
/*    */ {
	/*    */   public static final String MOD_ID = "betterboat";
	/*    */   public static final String VERSION = "1.1.0";
	/*    */   public static final String MOD_CHANNEL = "BetterBoatNWCH";
	/*    */   public static SimpleNetworkWrapper network;
	/*    */   private static final String MOD_PROXY_CLIENT = "de.sanandrew.mods.betterboat.client.ClientProxy";
	/*    */   private static final String MOD_PROXY_COMMON = "de.sanandrew.mods.betterboat.CommonProxy";
	/*    */ 
	/*    */   @Mod.Instance("betterboat")
	/*    */   public static BetterBoat instance;
	/*    */ 
	/*    */   @SidedProxy(modId="betterboat", clientSide="de.sanandrew.mods.betterboat.client.ClientProxy", serverSide="de.sanandrew.mods.betterboat.CommonProxy")
	/*    */   public static CommonProxy proxy;
	/*    */ 
	/*    */   @Mod.EventHandler
	/*    */   public void preInit(FMLPreInitializationEvent event)
	/*    */   {
		/* 45 */     network = NetworkRegistry.INSTANCE.newSimpleChannel("BetterBoatNWCH");
		/*    */ 
		/* 47 */    this.network.registerMessage(BoatPosMessageHandler.class, PacketSendBoatPos.class, 0, Side.SERVER);
		/*    */ 
		/* 49 */     MinecraftForge.EVENT_BUS.register(new EntitySpawnHandler());
	/*    */   }
	/*    */ 
	/*    */   @Mod.EventHandler
	/*    */   public void init(FMLInitializationEvent event)
	/*    */   {
		/* 55 */     EntityRegistry.registerModEntity(EntityBetterBoat.class, "betterBoat", 0, instance, 80, 3, true);
		/* 56 */     EntityList.stringToClassMapping.put("Boat", EntityBetterBoat.class);
		/*    */ 
		/* 58 */     proxy.registerRenderers();
	/*    */   }
	
/*    */ }



/* Location:           C:\Users\na0aya2e\雑多なプログラム\jd-gui-0.3.5.windows\minecraftModsToAnalyze\BetterBoat-1.7.10-1.1.0.deobf.jar
 * Qualified Name:     de.sanandrew.mods.betterboat.BetterBoat
 * JD-Core Version:    0.6.2
 */