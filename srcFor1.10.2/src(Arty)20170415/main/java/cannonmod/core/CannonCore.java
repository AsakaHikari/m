package cannonmod.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import cannonmod.entity.EntityCannon;
import cannonmod.entity.EntityFallingBlockEx;
import cannonmod.entity.EntityTracer;
import cannonmod.item.*;
import cannonmod.item.recipe.RecipeArty;
import cannonmod.item.recipe.RecipeArtyArmor;
import cannonmod.item.recipe.RecipeArtyCamo;
import cannonmod.item.recipe.RecipeBarrel;
import cannonmod.item.recipe.RecipeBarrelExpand;
import cannonmod.item.recipe.RecipeCannon;
import cannonmod.item.recipe.RecipeChassis;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/**a130*/
@Mod(modid="simpleArtilleryMod",name="CustomizableArtilleryMod",version="Alpha1_3_0")
public class CannonCore {
	@Instance("simpleArtilleryMod")
	public static CannonCore instance;
	@SidedProxy(clientSide="cannonmod.core.RenderProxyClient",serverSide="cannonmod.core.RenderProxy")
	public static RenderProxy proxy;
	
	public static double cannonHealth;
	public static double bulletSpeed;
	public static double cannonSpeed;
	
	public static KeyBinding buttonCannonGui;
	public static Item itemArty;
	public static Item itemArmor;
	public static Item itemCannon;
	public static Item itemBarrel;
	public static Item itemCarriage;
	public static Item itemEngine;
	public static Item itemTrack;
	public static Item itemChassis;
	/**a110*/
	public static Item itemLoader;
	/**a120*/
	public static Item itemForDebug;
	
	public static EventCaller eventCaller;
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("simpleArtilleryMod");

	public static SoundEvent cannonRun;
	/**a130*/
	public static List<EntityTracer> tracersList=new ArrayList();
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		Configuration cfg=new Configuration(e.getSuggestedConfigurationFile());
		cfg.load();
		Property healthP=cfg.get("system", "Max HP of artilleries (Default:70)", 70.0);
		Property bsP=cfg.get("system", "Bullet speed (Default:0.06)", 0.06);
		Property csP=cfg.get("system", "Artilleries' speed (Default:300.0)", 300.0);
		Property gunpowderP=cfg.get("system", "Enable gunpowder recipe", true);
		cfg.save();
		
		cannonHealth=healthP.getDouble();
		bulletSpeed=bsP.getDouble();
		cannonSpeed=csP.getDouble();
		
		cannonRun=registerSound("simplecannon.run");
		
		/**a130*/
		EntityRegistry.registerModEntity(EntityCannon.class, "Artillery", 0, this, 80, 4, true);
		EntityRegistry.registerModEntity(EntityFallingBlockEx.class,"FallingBlockEx",1,this,80,4,true);
		EntityRegistry.registerModEntity(EntityTracer.class, "Tracer", 2, this, 80, 4, true);
		
		this.proxy.init();
		itemArty=new ItemArty().setUnlocalizedName("Artillery");
		GameRegistry.registerItem(itemArty,"Artillery");
		
		itemArmor=new ItemArmorPlate().setUnlocalizedName("ArmorPlate").setCreativeTab(CreativeTabs.MATERIALS);
		GameRegistry.registerItem(itemArmor,"ArmorPlate");
		
		itemCannon=new ItemCannon().setUnlocalizedName("Cannon").setCreativeTab(CreativeTabs.MATERIALS);
		GameRegistry.registerItem(itemCannon,"Cannon");
		
		itemBarrel=new ItemBarrel().setUnlocalizedName("Barrel").setCreativeTab(CreativeTabs.MATERIALS);
		GameRegistry.registerItem(itemBarrel,"Barrel");
		
		itemCarriage=new ItemCarriage().setUnlocalizedName("Carriage").setCreativeTab(CreativeTabs.MATERIALS);
		GameRegistry.registerItem(itemCarriage,"Carriage");
		
		itemEngine=new ItemEngine().setUnlocalizedName("Engine").setCreativeTab(CreativeTabs.MATERIALS);
		GameRegistry.registerItem(itemEngine,"Engine");
		
		itemTrack=new ItemTrack().setUnlocalizedName("Track").setCreativeTab(CreativeTabs.MATERIALS);
		GameRegistry.registerItem(itemTrack,"Track");
		
		itemChassis=new ItemChassis().setUnlocalizedName("Chassis").setCreativeTab(CreativeTabs.MATERIALS);
		GameRegistry.registerItem(itemChassis,"Chassis");
		
		/**a110*/
		itemLoader=new ItemLoader().setUnlocalizedName("Loader").setCreativeTab(CreativeTabs.MATERIALS);
		GameRegistry.registerItem(itemLoader,"Loader");
		
		/**a120*/
		itemForDebug=new ItemForDebug().setUnlocalizedName("ItemForDebug").setCreativeTab(CreativeTabs.MISC);
		//GameRegistry.registerItem(itemForDebug,"ItemForDebug");
		
		if (e.getSide().isClient()) {
			ModelLoader.setCustomModelResourceLocation(itemArty,0,new ModelResourceLocation( "simplecannon"+ ":" + "arty", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemArmor,0,new ModelResourceLocation( "simplecannon"+ ":" + "armor", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemCannon,0,new ModelResourceLocation( "simplecannon"+ ":" + "cannon", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemBarrel,0,new ModelResourceLocation( "simplecannon"+ ":" + "barrel", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemCarriage,0,new ModelResourceLocation( "simplecannon"+ ":" + "carriage", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemCarriage,1,new ModelResourceLocation( "simplecannon"+ ":" + "carriageM1", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemCarriage,2,new ModelResourceLocation( "simplecannon"+ ":" + "carriageM2", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemCarriage,3,new ModelResourceLocation( "simplecannon"+ ":" + "carriageM3", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemEngine,0,new ModelResourceLocation( "simplecannon"+ ":" + "engineM1", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemEngine,1,new ModelResourceLocation( "simplecannon"+ ":" + "engineM2", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemEngine,2,new ModelResourceLocation( "simplecannon"+ ":" + "engineM3", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemTrack,0,new ModelResourceLocation( "simplecannon"+ ":" + "track", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemChassis,0,new ModelResourceLocation( "simplecannon"+ ":" + "chassis", "inventory"));
			/**a110*/
			ModelLoader.setCustomModelResourceLocation(itemLoader,0,new ModelResourceLocation( "simplecannon"+ ":" + "loaderM1", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemLoader,1,new ModelResourceLocation( "simplecannon"+ ":" + "loaderM2", "inventory"));
			ModelLoader.setCustomModelResourceLocation(itemLoader,2,new ModelResourceLocation( "simplecannon"+ ":" + "loaderM3", "inventory"));
		}
		if(gunpowderP.getBoolean()){
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.GUNPOWDER,3),"XY","YX",'X',new ItemStack(Items.COAL,1,OreDictionary.WILDCARD_VALUE),'Y',"dustRedstone"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.GUNPOWDER,3),"YX","XY",'X',new ItemStack(Items.COAL,1,OreDictionary.WILDCARD_VALUE),'Y',"dustRedstone"));
		}
		/**a110*/
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemLoader,1,0),"P  ","PB ","URU",'P',Blocks.PISTON,'B',Items.BREWING_STAND,'U',Items.BUCKET,'R',"record"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemLoader,1,1),"PBD","UBU","URU",'P',Blocks.PISTON,'D',"gemDiamond",'B',Items.BREWING_STAND,'U',Items.CAULDRON,'R',"record"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemLoader,1,2),"PNU","NUR","URD",'P',Blocks.PISTON,'D',"gemDiamond",'U',Items.CAULDRON,'R',"record",'N',"blockRedstone"));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemArmor,2),"XXX","XXX",'X',Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemBarrel,2,0x00000a05),"XXX","   ","XXX",'X',"ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemTrack,2),"XXX","XYX","XXX",'X',Blocks.IRON_BARS,'Y',"blockIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemCarriage,1,0),"X X","YYY",'X',"ingotIron",'Y',Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemCarriage,1,1)," X ","LIR","SCS",'X',new ItemStack(itemCarriage,1,0),'L',"blockLapis",'I',"blockIron"
				,'R',"blockRedstone",'S',Items.REPEATER,'C',Items.COMPARATOR));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemCarriage,1,2)," X ","LIR","SCS",'X',new ItemStack(itemCarriage,1,1),'L',"blockLapis",'I',"blockIron"
				,'R',"blockRedstone",'S',Items.REPEATER,'C',Items.COMPARATOR));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemCarriage,1,3)," X ","LIR","SCS",'X',new ItemStack(itemCarriage,1,2),'L',"blockLapis",'I',"blockIron"
				,'R',"blockRedstone",'S',Items.REPEATER,'C',Items.COMPARATOR));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemEngine,1,0),"GPG","PIP","PDP",'I',"ingotIron",'C',Blocks.UNPOWERED_COMPARATOR
				,'P',Blocks.PISTON,'G',"ingotGold",'D',Items.CAULDRON));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemEngine,1,1),"PDP","PIP","PCP",'P',Blocks.PISTON,'D',"gemDiamond",'I',"blockIron",'C',Items.CAULDRON));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemEngine,1,2),"FFF","ECE","DDD",'F',Blocks.IRON_BARS,'E',new ItemStack(itemEngine,1,1)
		,'C',Items.COMPARATOR,'D',"gemDiamond"));
		
		{
			ItemStack result=new ItemStack(itemArty);
			result.setTagCompound(new NBTTagCompound());
			GameRegistry.addShapelessRecipe(result,itemCannon,itemChassis);
		}
		
		{
			ItemStack result=new ItemStack(itemBarrel,1,0x00ff);
			GameRegistry.addRecipe(result,"XXX",'X',new ItemStack(itemBarrel,1,0xffff));
		}
		
		{
			ItemStack result=new ItemStack(itemBarrel,1,0x00ff);
			GameRegistry.addRecipe(result,"XX",'X',new ItemStack(itemBarrel,1,0xffff));
		}
		
		{
			ItemStack result=new ItemStack(itemBarrel,1,0xff00);
			GameRegistry.addRecipe(result,"X","X","X",'X',new ItemStack(itemBarrel,1,0xffff));
		}
		
		{
			ItemStack result=new ItemStack(itemBarrel,1,0xff00);
			GameRegistry.addRecipe(result,"X","X",'X',new ItemStack(itemBarrel,1,0xffff));
		}
		{
			ItemStack result=new ItemStack(itemCannon);
			result.setTagCompound(new NBTTagCompound());
			ItemStack barrel=new ItemStack(itemBarrel,1,0xffff);
			GameRegistry.addShapelessRecipe(result,barrel,Items.CAULDRON);
		}
		{
			ItemStack result=new ItemStack(itemChassis,1,0xfefe);
			GameRegistry.addShapelessRecipe(result, itemEngine,itemChassis,itemTrack,itemTrack);
		}
		{
			ItemStack result=new ItemStack(itemChassis,1,0xfe00);
			GameRegistry.addShapelessRecipe(result, itemChassis,itemTrack,itemTrack);
		}
		{
			ItemStack result=new ItemStack(itemArty);
			NBTTagCompound nbt=new NBTTagCompound();
			nbt.setInteger("Calibre", 0xfe);
			result.setTagCompound(nbt);
			ItemStack arty=new ItemStack(itemArty);
			NBTTagCompound nbt1=new NBTTagCompound();
			nbt1.setInteger("Calibre", 0xff);
			nbt1.setInteger("Design", 0xff);
			arty.setTagCompound(nbt1);
			GameRegistry.addShapelessRecipe(result, arty,itemArmor);
			GameRegistry.addShapelessRecipe(result, arty,itemArmor, itemArmor);
			GameRegistry.addShapelessRecipe(result, arty,itemArmor, itemArmor, itemArmor);
			GameRegistry.addShapelessRecipe(result, arty,itemArmor, itemArmor, itemArmor, itemArmor);
			GameRegistry.addShapelessRecipe(result, arty,itemArmor, itemArmor, itemArmor, itemArmor, itemArmor);
			GameRegistry.addShapelessRecipe(result, arty,itemArmor, itemArmor, itemArmor, itemArmor, itemArmor, itemArmor);
			GameRegistry.addShapelessRecipe(result, arty,itemArmor, itemArmor, itemArmor, itemArmor, itemArmor, itemArmor, itemArmor);
			GameRegistry.addShapelessRecipe(result, arty,itemArmor, itemArmor, itemArmor, itemArmor, itemArmor, itemArmor, itemArmor, itemArmor);
		}
		{
			ItemStack result=new ItemStack(itemArty);
			NBTTagCompound nbt=new NBTTagCompound();
			nbt.setInteger("Calibre", 0xff);
			nbt.setInteger("Design", 0);
			result.setTagCompound(nbt);
			ItemStack arty=new ItemStack(itemArty);
			NBTTagCompound nbt1=new NBTTagCompound();
			nbt1.setInteger("Calibre", 0xff);
			nbt1.setInteger("Design", 0xff);
			arty.setTagCompound(nbt1);
			GameRegistry.addRecipe(new ShapedOreRecipe(result,"XXX","XAX","XXX",'X',"dyeGray",'A',arty));
		}
		{
			ItemStack result=new ItemStack(itemArty);
			NBTTagCompound nbt=new NBTTagCompound();
			nbt.setInteger("Calibre", 0xff);
			nbt.setInteger("Design", 1);
			result.setTagCompound(nbt);
			ItemStack arty=new ItemStack(itemArty);
			NBTTagCompound nbt1=new NBTTagCompound();
			nbt1.setInteger("Calibre", 0xff);
			nbt1.setInteger("Design", 0xff);
			arty.setTagCompound(nbt1);
			GameRegistry.addRecipe(new ShapedOreRecipe(result,"XXX","XAX","XXX",'X',"treeLeaves",'A',arty));
		}
		{
			ItemStack result=new ItemStack(itemArty);
			NBTTagCompound nbt=new NBTTagCompound();
			nbt.setInteger("Calibre", 0xff);
			nbt.setInteger("Design", 2);
			result.setTagCompound(nbt);
			ItemStack arty=new ItemStack(itemArty);
			NBTTagCompound nbt1=new NBTTagCompound();
			nbt1.setInteger("Calibre", 0xff);
			nbt1.setInteger("Design", 0xff);
			arty.setTagCompound(nbt1);
			GameRegistry.addRecipe(new ShapedOreRecipe(result,"XXX","XAX","XXX",'X',"sandstone",'A',arty));
			GameRegistry.addRecipe(new ShapedOreRecipe(result,"XXX","XAX","XXX",'X',Blocks.SAND,'A',arty));
		}
		{
			ItemStack result=new ItemStack(itemArty);
			NBTTagCompound nbt=new NBTTagCompound();
			nbt.setInteger("Calibre", 0xff);
			nbt.setInteger("Design", 3);
			result.setTagCompound(nbt);
			ItemStack arty=new ItemStack(itemArty);
			NBTTagCompound nbt1=new NBTTagCompound();
			nbt1.setInteger("Calibre", 0xff);
			nbt1.setInteger("Design", 0xff);
			arty.setTagCompound(nbt1);
			GameRegistry.addRecipe(new ShapedOreRecipe(result,"XXX","XAX","XXX",'X',"stone",'A',arty));
		}
		{
			ItemStack result=new ItemStack(itemArty);
			NBTTagCompound nbt=new NBTTagCompound();
			nbt.setInteger("Calibre", 0xff);
			nbt.setInteger("Design", 4);
			result.setTagCompound(nbt);
			ItemStack arty=new ItemStack(itemArty);
			NBTTagCompound nbt1=new NBTTagCompound();
			nbt1.setInteger("Calibre", 0xff);
			nbt1.setInteger("Design", 0xff);
			arty.setTagCompound(nbt1);
			GameRegistry.addRecipe(new ShapedOreRecipe(result,"XXX","XAX","XXX",'X',Items.SNOWBALL,'A',arty));
			GameRegistry.addRecipe(new ShapedOreRecipe(result,"XXX","XAX","XXX",'X',Blocks.SNOW,'A',arty));
			GameRegistry.addRecipe(new ShapedOreRecipe(result,"XXX","XAX","XXX",'X',Blocks.SNOW_LAYER,'A',arty));
		}
		
		GameRegistry.addRecipe(new RecipeArty());
		RecipeSorter.register("cannonmod:arty", RecipeArty.class, RecipeSorter.Category.SHAPELESS, "");
		GameRegistry.addRecipe(new RecipeBarrel());
		RecipeSorter.register("cannonmod:barrel", RecipeBarrel.class, RecipeSorter.Category.SHAPED, "");
		GameRegistry.addRecipe(new RecipeBarrelExpand());
		RecipeSorter.register("cannonmod:barrelexpand", RecipeBarrelExpand.class, RecipeSorter.Category.SHAPED, "");
		GameRegistry.addRecipe(new RecipeCannon());
		RecipeSorter.register("cannonmod:cannon", RecipeCannon.class, RecipeSorter.Category.SHAPELESS, "");
		GameRegistry.addRecipe(new RecipeChassis());
		RecipeSorter.register("cannonmod:chassis", RecipeChassis.class, RecipeSorter.Category.SHAPELESS, "");
		GameRegistry.addRecipe(new RecipeArtyArmor());
		RecipeSorter.register("cannonmod:artyarmor", RecipeArty.class, RecipeSorter.Category.SHAPELESS, "");
		GameRegistry.addRecipe(new RecipeArtyCamo());
		RecipeSorter.register("cannonmod:artycamo", RecipeArtyCamo.class, RecipeSorter.Category.SHAPED, "");
		
		eventCaller=new EventCaller();
		MinecraftForge.EVENT_BUS.register(eventCaller);
		FMLCommonHandler.instance().bus().register(eventCaller);
		buttonCannonGui = new KeyBinding("key.cannongui", Keyboard.KEY_R, "key.categories.cannonmod");
		//buttonCannonFire = new KeyBinding("key.cannonfire", -99, "key.categories.cannonmod");

		// Register both KeyBindings to the ClientRegistry
		ClientRegistry.registerKeyBinding(buttonCannonGui);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		INSTANCE.registerMessage(MessageCannonGuiHandler.class, MessageCannonGui.class, 0, Side.SERVER);
		INSTANCE.registerMessage(MessageCannonParticleHandler.class, MessageCannonParticle.class, 1, Side.CLIENT);
		/**a130*/
		ForgeChunkManager.setForcedChunkLoadingCallback(this, new ShellChunkManager());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e){
		
	}

	private static SoundEvent registerSound(String soundName) {
		final ResourceLocation soundID = new ResourceLocation("simplecannon", soundName);
		return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
	}
	
}
