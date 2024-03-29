package farview;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.VboRenderList;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
@Mod(modid = Core.modid, name = Core.modid, version = "alpha1.0.5",useMetadata=true)

@EventBusSubscriber
public class Core {
	public static final String modid="farview";
	    @Instance(value = modid)
	    public static Core instance;
	    public static Item minecartItem;
	    public static Item minecartChestItem;
	    @SidedProxy(clientSide="farview.CoreProxyClient",serverSide="farview.CoreProxy")
	    public static CoreProxy proxy;
	    public static Method MregisterBlock;
	    public static Class NSDWClazz;
	    public static Field Flocked;
	    public static Field Fpatterns;
	    public static Field FrenderContainer;
	    public static Field FrenderChunks;
	    public static Field FrenderInfos;
	    public static Class CLRIClazz;
	    public static Field FrenderChunk;
	    public static Field FviewFrustum;
	    public static Field FrendererUpdateCount;
	    public static Method MsetupArrayPointers;
	    public static Method MgetFOVModifier;
	    public static Method MapplyBobbing;
	    public static Method MhurtCameraEffect;
	    public static Method  MorientCamera;
	    @Mod.Metadata
	    public static ModMetadata modMetadata;


	    //public static final DataParameter<Float> MAX_SPEED = EntityDataManager.<Float>createKey(EntityMinecart.class, DataSerializers.FLOAT);

	    @ObjectHolder(modid)
	    public static class ITEMS{

	        public static Item new_minecart;
	        public static Item new_chest_minecart;
	        public static Item new_detector_rail;
	    }

	    @ObjectHolder(modid)

	    public static class BLOCKS{

	        public static Block new_detector_rail;

	    }


		public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Core.modid);
	    @NetworkCheckHandler
	    public boolean accept(Map<String, String>modList, Side side) {
	       return true;
	    }

	    @EventHandler
	    public void preInit(FMLPreInitializationEvent event)
	    {
	    	modMetadata.autogenerated=false;
	    	proxy.init();
	    	/*
	    	try {
				NSDWClazz=Class.forName("net.minecraftforge.registries.NamespacedDefaultedWrapper");
			} catch (ClassNotFoundException e1) {
				// TODO �����������ꂽ catch �u���b�N
				e1.printStackTrace();
			}
	    	Flocked=ReflectionHelper.findField(NSDWClazz, "locked");
	    	Flocked.setAccessible(true);

	    	MregisterBlock=ReflectionHelper.findMethod(Block.class,"registerBlock","func_176219_a",int.class,String.class,Block.class);
	    	MregisterBlock.setAccessible(true);
	    	try {
	    		Flocked.set(Block.REGISTRY, false);

	    		MregisterBlock.invoke(null, 27, "golden_rail", (new BlockRailPoweredAlter()).setHardness(0.7F).setUnlocalizedName("goldenRail"));
	    		MregisterBlock.invoke(null, 28, "detector_rail", (new BlockRailDetectorAlter()).setHardness(0.7F).setUnlocalizedName("detectorRail"));
	    		MregisterBlock.invoke(null,66, "rail", (new BlockRailAlter()).setHardness(0.7F).setUnlocalizedName("rail"));
	    		MregisterBlock.invoke(null,157, "activator_rail", (new BlockRailPoweredAlter()).setHardness(0.7F).setUnlocalizedName("activatorRail"));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
	    	Fpatterns=ReflectionHelper.findField(TileEntityBanner.class,"field_175118_f","patterns");
	    	Fpatterns.setAccessible(true);

	    	//EntityMinecart.defaultMaxSpeedAirLateral=Float.MAX_VALUE;
	    	//EntityMinecart.defaultDragAir=1;

	    	ITEMS.new_minecart=new ItemMinecart(0).setRegistryName("new_minecart").setUnlocalizedName("new_minecart");
	        ITEMS.new_chest_minecart=new ItemMinecart(1).setRegistryName("new_chest_minecart").setUnlocalizedName("new_chest_minecart");
	        BLOCKS.new_detector_rail=(new NewBlockRailDetector()).setRegistryName("new_detector_rail").setHardness(0.7F).setUnlocalizedName("new_detector_rail").setCreativeTab(CreativeTabs.TRANSPORTATION);
	        ITEMS.new_detector_rail=new ItemBlock(BLOCKS.new_detector_rail).setRegistryName("new_detector_rail");
	        /*
	        if (event.getSide() == Side.CLIENT){
	            ModelLoader.setCustomModelResourceLocation(ITEMS.new_minecart, 0, new ModelResourceLocation(ITEMS.new_minecart.getRegistryName(), "inventory"));
	            ModelLoader.setCustomModelResourceLocation(ITEMS.new_chest_minecart, 0, new ModelResourceLocation(ITEMS.new_chest_minecart.getRegistryName(), "inventory"));
	            ModelLoader.setCustomModelResourceLocation(ITEMS.new_detector_rail, 0, new ModelResourceLocation(ITEMS.new_detector_rail.getRegistryName(), "inventory"));

	        }
	        */

	        INSTANCE.registerMessage(MessageHandlerServer.class, MessageHandlerServer.Message.class, 0, Side.SERVER);

	    }


	    @EventHandler
	    public void init(FMLInitializationEvent event)
	    {/*
	        int id =0;
	        EntityRegistry.registerModEntity(new ResourceLocation(modid,"NewMinecart"),EntityMinecartEmpty.class, "NewMinecart", id, this, 80, 1, true);
	        id++;
	        EntityRegistry.registerModEntity(new ResourceLocation(modid,"NewMinecartChest"),EntityMinecartChest.class, "NewMinecartChest", id, this, 80, 1, true);
	        id++;

			proxy.init();
			*/
	    }
	    /*
	    @SubscribeEvent
	    protected static void registerItems(RegistryEvent.Register<Item> event){

	        event.getRegistry().registerAll(
	        		ITEMS.new_minecart,ITEMS.new_chest_minecart,ITEMS.new_detector_rail
	        );
	    }
	    @SubscribeEvent
	    protected static void registerBlocks(RegistryEvent.Register<Block> event){
	        event.getRegistry().registerAll(
	        		BLOCKS.new_detector_rail
	        );
	    }
*/
	    @Config(modid = Core.modid,name=Core.modid+"Config")
	    @Config.LangKey("config")
	    public static class Cfg{
	    	@RangeInt(min=0)
	    	@Comment("The range of chunks that rendered by farview render. Setting bigger number enable you to see further.")
		    public static int maxRenderChunkDistance=20;

	    	@RangeInt(min=0)
	    	@Comment("The speed of chunk loading. Bigger number makes chunk loading faster but too high speed may disturb Vanilla chunk loading.")
		    public static int chunkLoadingSpeed=10;

	    	@Comment("This represents how far the fog starts. The number bigger, the fog further. Setting negative number makes fog disabled.")
		    public static int fogStartChunk=12;
	    }
}
