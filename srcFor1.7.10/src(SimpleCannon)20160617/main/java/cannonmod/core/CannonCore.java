package cannonmod.core;

import org.lwjgl.input.Keyboard;

import cannonmod.entity.EntityCannon;
import cannonmod.entity.EntityFallingBlockEx;
import cannonmod.item.*;
import cannonmod.item.recipe.RecipeArty;
import cannonmod.item.recipe.RecipeArtyArmor;
import cannonmod.item.recipe.RecipeArtyCamo;
import cannonmod.item.recipe.RecipeBarrel;
import cannonmod.item.recipe.RecipeBarrelExpand;
import cannonmod.item.recipe.RecipeCannon;
import cannonmod.item.recipe.RecipeChassis;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="simpleArtilleryMod",name="CustomizableArtilleryMod",version="Alpha1_0_2")
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
	public static EventCaller eventCaller;
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("simpleArtilleryMod");

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
		
		EntityRegistry.registerModEntity(EntityFallingBlockEx.class,"FallingBlockEx",1,this,80,4,true);
		EntityRegistry.registerModEntity(EntityCannon.class, "Artillery", 0, this, 80, 4, true);
		this.proxy.init();
		itemArty=new ItemArty().setUnlocalizedName("Artillery");
		GameRegistry.registerItem(itemArty,"Artillery");
		
		itemArmor=new ItemArmorPlate().setUnlocalizedName("ArmorPlate").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("simplecannon:armor");
		GameRegistry.registerItem(itemArmor,"ArmorPlate");
		
		itemCannon=new ItemCannon().setUnlocalizedName("Cannon").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("simplecannon:cannon");
		GameRegistry.registerItem(itemCannon,"Cannon");
		
		itemBarrel=new ItemBarrel().setUnlocalizedName("Barrel").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("simplecannon:barrel");
		GameRegistry.registerItem(itemBarrel,"Barrel");
		
		itemCarriage=new ItemCarriage().setUnlocalizedName("Carriage").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("simplecannon:carriage");
		GameRegistry.registerItem(itemCarriage,"Carriage");
		
		itemEngine=new ItemEngine().setUnlocalizedName("Engine").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("simplecannon:engineM1");
		GameRegistry.registerItem(itemEngine,"Engine");
		
		itemTrack=new ItemTrack().setUnlocalizedName("Track").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("simplecannon:track");
		GameRegistry.registerItem(itemTrack,"Track");
		
		itemChassis=new ItemChassis().setUnlocalizedName("Chassis").setCreativeTab(CreativeTabs.tabMaterials).setTextureName("simplecannon:chassis");
		GameRegistry.registerItem(itemChassis,"Chassis");
		
		
		if(gunpowderP.getBoolean()){
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.gunpowder,3),"XY","YX",'X',new ItemStack(Items.coal,1,OreDictionary.WILDCARD_VALUE),'Y',"dustRedstone"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.gunpowder,3),"YX","XY",'X',new ItemStack(Items.coal,1,OreDictionary.WILDCARD_VALUE),'Y',"dustRedstone"));
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemArmor,2),"XXX","XXX",'X',Blocks.heavy_weighted_pressure_plate));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemBarrel,2,0x00000a05),"XXX","   ","XXX",'X',"ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemTrack,2),"XXX","XYX","XXX",'X',Blocks.iron_bars,'Y',"blockIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemCarriage,1,0),"X X","YYY",'X',"ingotIron",'Y',Blocks.heavy_weighted_pressure_plate));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemCarriage,1,1)," X ","LIR","SCS",'X',new ItemStack(itemCarriage,1,0),'L',"blockLapis",'I',"blockIron"
				,'R',"blockRedstone",'S',Items.repeater,'C',Items.comparator));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemCarriage,1,2)," X ","LIR","SCS",'X',new ItemStack(itemCarriage,1,1),'L',"blockLapis",'I',"blockIron"
				,'R',"blockRedstone",'S',Items.repeater,'C',Items.comparator));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemCarriage,1,3)," X ","LIR","SCS",'X',new ItemStack(itemCarriage,1,2),'L',"blockLapis",'I',"blockIron"
				,'R',"blockRedstone",'S',Items.repeater,'C',Items.comparator));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemEngine,1,0),"GPG","PIP","PDP",'I',"ingotIron",'C',Blocks.unpowered_comparator
				,'P',Blocks.piston,'G',"ingotGold",'D',Items.cauldron));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemEngine,1,1),"PDP","PIP","PCP",'P',Blocks.piston,'D',"gemDiamond",'I',"blockIron",'C',Items.cauldron));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemEngine,1,2),"FFF","ECE","DDD",'F',Blocks.iron_bars,'E',new ItemStack(itemEngine,1,1)
		,'C',Items.comparator,'D',"gemDiamond"));
		
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
			GameRegistry.addShapelessRecipe(result,barrel,Items.cauldron);
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
			GameRegistry.addRecipe(new ShapedOreRecipe(result,"XXX","XAX","XXX",'X',Blocks.sand,'A',arty));
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
			GameRegistry.addRecipe(new ShapedOreRecipe(result,"XXX","XAX","XXX",'X',Items.snowball,'A',arty));
			GameRegistry.addRecipe(new ShapedOreRecipe(result,"XXX","XAX","XXX",'X',Blocks.snow,'A',arty));
			GameRegistry.addRecipe(new ShapedOreRecipe(result,"XXX","XAX","XXX",'X',Blocks.snow_layer,'A',arty));
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
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e){
		
	}
}
