package gvcguns;


import java.io.File;





import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;


@Mod(
		modid	= "GVCEntity",
		name	= "GVCEntity",
		version	= "1.7.x-srg-1"
		)
public class GVCEntityPlus {

	//private static final ToolMaterial IRON = null;

	//@SidedProxy(clientSide = "mmm.FN5728Guns.ProxyClient", serverSide = "mmm.lib.ProxyCommon")
	//public static ProxyCommon proxy;
	
	//public static boolean isArmorPiercing = true; 
	//public static boolean UnlimitedInfinity = false;
	@SidedProxy(clientSide = "gvcguns.client.ClientProxyGVC", serverSide = "gvcguns.CommonSideProxyGVC")
	public static CommonSideProxyGVC proxy;
	public static boolean isDebugMessage = true;
	public static boolean cfg_SpwanMob = true;
	
	
	public static Item fn_boxegg;
	public static Item fn_targetegg;
	public static Item fn_sentryegg;
	public static Item fn_sentryaagegg;
	public static int GVCVillagerProfession = 5;
	
	public static GVCVillagerTrade villager;
	
	


	
	protected static File configFile;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent pEvent) {
		configFile = pEvent.getSuggestedConfigurationFile();
		Configuration lconf = new Configuration(configFile);
		lconf.load();
		isDebugMessage	= lconf.get("RefinedMilitaryShovelReplica", "isDebugMessage", true).getBoolean(true);
		cfg_SpwanMob	= lconf.get("RefinedMilitaryShovelReplica", "cfg_exprotion", true).getBoolean(true);
		lconf.save();
		
		
		
		
		fn_boxegg	= new GVCItemBoxEgg().setUnlocalizedName("boxEgg").setTextureName("gvcguns:boxegg");
		GameRegistry.registerItem(fn_boxegg, "boxEgg");
		fn_targetegg	= new GVCItemTargetEgg().setUnlocalizedName("TargetEgg").setTextureName("gvcguns:targetegg");
		GameRegistry.registerItem(fn_targetegg, "TargetEgg");
		fn_sentryegg	= new GVCItemEggSentry(0).setUnlocalizedName("SentryEgg").setTextureName("gvcguns:sentryegg");
		GameRegistry.registerItem(fn_sentryegg, "SentryEgg");
		fn_sentryaagegg	= new GVCItemEggSentry(1).setUnlocalizedName("SentryAAGEgg").setTextureName("gvcguns:sentryaagegg");
		GameRegistry.registerItem(fn_sentryaagegg, "SentryAAGEgg");
	}



	public static void Debug(String pText, Object... pData) {
		if (isDebugMessage) {
			System.out.println(String.format("GVCEntity-" + pText, pData));
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent pEvent) {
		villager = new GVCVillagerTrade();
		//VillagerRegistry.instance().registerVillageTradeHandler(1, new GVCVillagerTrade());
		VillagerRegistry.instance().registerVillagerId(GVCVillagerProfession);
		VillagerRegistry.instance().registerVillageTradeHandler(GVCVillagerProfession, villager);
		
		GameRegistry.addRecipe(new ItemStack(fn_targetegg, 1),
				"r", 
				"e",
				'e', Items.egg,
				'r', Items.redstone
			);

		GameRegistry.addRecipe(new ItemStack(fn_sentryegg, 1),
				" i ", 
				"ib ",
				"i i",
				'i', Items.iron_ingot,
				'b', Blocks.iron_block
			);
		GameRegistry.addRecipe(new ItemStack(fn_sentryegg, 1),
				" i ", 
				"ib ",
				"i i",
				'i', Items.gold_ingot,
				'b', Blocks.iron_block
			);
		
		BiomeGenBase[] biomeList = null;
		biomeList = new BiomeGenBase[]{
				BiomeGenBase.desert,
				BiomeGenBase.plains,
				BiomeGenBase.savanna,
				//BiomeGenBase.mushroomIsland,
				BiomeGenBase.forest,
				BiomeGenBase.birchForest,
				BiomeGenBase.swampland,
				BiomeGenBase.taiga,
				BiomeGenBase.extremeHills,
				BiomeGenBase.hell,
				BiomeGenBase.sky,
				BiomeGenBase.jungle,
				BiomeGenBase.stoneBeach,
				BiomeGenBase.mesa,
				BiomeGenBase.megaTaiga,
		};
		
		
		
		
		
		EntityRegistry.registerModEntity(GVCEntityBox.class, "Box", 0, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityTarget.class, "Target", 1, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntitySentry.class, "Sentry", 2, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntitySentryBullet.class, "SentryBullet", 3, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntitySentryAAG.class, "Sentry", 4, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntitySentryBulletAAG.class, "SentryBulletAAG", 5, this, 250, 1, false);
		
		if(cfg_SpwanMob == true){
		  for(BiomeGenBase biome : biomeList)
		  {
			if(biome!=null)
			{
		EntityRegistry.addSpawn(GVCEntityBox.class, 20, 2, 6, EnumCreatureType.monster, biome);
		/*EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.desert});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.extremeHills});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.forest});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.taiga});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.swampland});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.hell});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.sky});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.jungle});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.savanna});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.coldTaiga});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.mesa});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.megaTaiga});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.stoneBeach});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.coldBeach});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.birchForest});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.coldTaiga});*/
		//EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster);
			}
		  }
		}
		/*if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
		{ 
			RenderingRegistry.registerEntityRenderingHandler(GVCEntityBox.class, new GVCRenderBox()); 
			RenderingRegistry.registerEntityRenderingHandler(GVCEntityTarget.class, new GVCRenderTarget()); 
			VillagerRegistry.instance().registerVillagerSkin(GVCVillagerProfession, new ResourceLocation("gvcguns:textures/entity/mob/gvcVillager.png"));
		}*/
		
		BlockDispenser.dispenseBehaviorRegistry.putObject(this.fn_targetegg,new  IBehaviorDispenseItem(){
		       @Override
		       public ItemStack dispense(IBlockSource var1, ItemStack var2){
		              World world = var1.getWorld();//World
		              /*Item item = var2.getItem();*/ //Item
		              IPosition iposition = BlockDispenser.func_149939_a(var1);//IPosition
		              double x = iposition.getX();//
		              double y = iposition.getY();
		              double z = iposition.getZ();//
		              //world.spawnEntityInWorld(new GVCEntityTarget(world));
		              //int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		              GVCEntityTarget entityskeleton = new GVCEntityTarget(world);
		              entityskeleton.setLocationAndAngles(x, y, z,  0, 0);
		              world.spawnEntityInWorld(entityskeleton);
		              return var2.splitStack(var2.stackSize-1);
		       }
		});
		
		//BlockDispenser.dispenseBehaviorRegistry.putObject(fn_targetegg, new GVCBehaviorDispenseTarget());

	}
	

}
