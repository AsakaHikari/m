package gvcmob;

import gvcguns.GVCItemAK74;
import gvcguns.GVCblockCamp;

import java.io.File;
import java.util.Random;





import net.minecraft.block.Block;
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
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.MinecraftForge;
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
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;


@Mod(
		modid	= "GVCMob",
		name	= "GVCMob",
		version	= "1.7.x-srg-1-fixed"
		)
public class GVCMobPlus {

	//private static final ToolMaterial IRON = null;

	//@SidedProxy(clientSide = "mmm.FN5728Guns.ProxyClient", serverSide = "mmm.lib.ProxyCommon")
	//public static ProxyCommon proxy;
	
	@SidedProxy(clientSide = "gvcmob.ClientProxyGVCM", serverSide = "gvcmob.CommonSideProxyGVCM")
	public static CommonSideProxyGVCM proxy;
	//public static boolean isArmorPiercing = true; 
	//public static boolean UnlimitedInfinity = false;
	public static boolean isDebugMessage = true;
	
	public static boolean cfg_setCamp = true;
	
	public static boolean cfg_modeGorC = true;
	
	public static int cfg_creatCamp;
	public static double cfg_guerrillasrach;
	public static double cfg_guerrillaspawngroup;
	public static int cfg_guerrillaspawnnomal;
	public static int cfg_guerrillaspawntank;
	public static int cfg_flagspawnlevel;
	public static boolean cfg_canspawnguerrilla;
	public static boolean cfg_canspawnsolider;
	public static boolean cfg_cansetIED;
	
	public static Block fn_Gcamp;
	public static Block fn_Gcamp2;
	public static Block fn_DGcamp;
	
	public static Item fn_guerrillaegg;
	public static Item fn_guerrillabegg;
	public static Item fn_guerrillaspegg;
	public static Item fn_guerrillarpgegg;
	public static Item fn_guerrillasgegg;
	public static Item fn_guerrillamgegg;
	public static Item fn_guerrillapegg;
	public static Item fn_gkegg;
	public static Item fn_tankegg;
	public static Item fn_apcegg;
	public static Item fn_heliegg;
	public static Item fn_jeepegg;
	public static Item fn_aagegg;
	
	
	public static Item fn_mobspawner_egg;
	public static Item fn_mobspawner2_egg;
	
	public static Item fn_flagegg;
	
	public static Item fn_pmcegg;
	public static Item fn_pmcspegg;
	public static Item fn_pmcmgegg;
	public static Item fn_pmcrpgegg;
	public static Item fn_pmctankegg;
	public static Item fn_pmcheliegg;
	
	public static Item fn_soldieregg;
	public static Item fn_soldierspegg;
	public static Item fn_soldiermgegg;
	public static Item fn_soldierrpgegg;
	public static Item fn_soldiertankegg;
	public static Item fn_soldierheliegg;
	
	
	public static int bulletbase = 570;

	
	public static Item fn_g36;
	public static Item fn_m110;
	public static Item fn_mg36;
	public static Item fn_rpg7;
	
	
	protected static File configFile;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent pEvent) {
		configFile = pEvent.getSuggestedConfigurationFile();
		Configuration lconf = new Configuration(configFile);
		lconf.load();
		cfg_setCamp	= lconf.get("RefinedMilitaryShovelReplica", "cfg_setCamp", true).getBoolean(true);
		cfg_creatCamp	= lconf.get("RefinedMilitaryShovelReplica", "cfg_creatCamp", 1024).getInt(1024);
		cfg_modeGorC	= lconf.get("RefinedMilitaryShovelReplica", "cfg_ModeGorC", true).getBoolean(true);
		cfg_guerrillasrach	= lconf.get("RefinedMilitaryShovelReplica", "cfg_GuerrillaMobSrach", 40).getDouble(40);
		cfg_guerrillaspawnnomal	= lconf.get("RefinedMilitaryShovelReplica", "cfg_GuerrillaSpawnNomal", 5).getInt(5);
		cfg_guerrillaspawntank	= lconf.get("RefinedMilitaryShovelReplica", "cfg_GuerrillaSpawntank", 2).getInt(2);
		cfg_flagspawnlevel	= lconf.get("RefinedMilitaryShovelReplica", "cfg_FlagSpawnLevel", 180).getInt(180);
		cfg_canspawnguerrilla	= lconf.get("RefinedMilitaryShovelReplica", "cfg_CanSpawnGuerrilla", true).getBoolean(true);
		cfg_canspawnsolider	= lconf.get("RefinedMilitaryShovelReplica", "cfg_CanSpawnSolider", true).getBoolean(true);
		cfg_cansetIED	= lconf.get("RefinedMilitaryShovelReplica", "cfg_CansetIED", true).getBoolean(true);
		lconf.save();
		
		
		fn_g36 = GameRegistry.findItem("GVCGuns", "G36");
		fn_m110 = GameRegistry.findItem("GVCGuns", "M110");
		fn_mg36 = GameRegistry.findItem("GVCGuns", "MG36");
		fn_rpg7 = GameRegistry.findItem("GVCGuns", "RPG7");
		
		
		fn_Gcamp	= new GVCBlockGuerrillaCamp().setBlockName("GCamp").setBlockTextureName("gvcmob:camp");
		GameRegistry.registerBlock(fn_Gcamp, "Camp");
		fn_Gcamp2	= new GVCBlockGuerrillaCamp2().setBlockName("GCamp2").setBlockTextureName("gvcmob:camp");
		GameRegistry.registerBlock(fn_Gcamp2, "Camp2");
		
		
		fn_guerrillaegg	    = new GVCItemGuerrillaEgg(0).setUnlocalizedName("GuerrillaEgg").setTextureName("gvcmob:guerrillaegg");
		GameRegistry.registerItem(fn_guerrillaegg, "GuerrillaEgg");
		fn_guerrillabegg	= new GVCItemGuerrillaEgg(1).setUnlocalizedName("GuerrillaBEgg").setTextureName("gvcmob:guerrillabegg");
		GameRegistry.registerItem(fn_guerrillabegg, "GuerrillaBEgg");
		fn_guerrillaspegg	= new GVCItemGuerrillaEgg(2).setUnlocalizedName("GuerrillaspEgg").setTextureName("gvcmob:guerrillaspegg");
		GameRegistry.registerItem(fn_guerrillaspegg, "GuerrillaspEgg");
		fn_guerrillarpgegg	= new GVCItemGuerrillaEgg(3).setUnlocalizedName("GuerrillarpgEgg").setTextureName("gvcmob:guerrillarpgegg");
		GameRegistry.registerItem(fn_guerrillarpgegg, "GuerrillarpgEgg");
		fn_guerrillasgegg	= new GVCItemGuerrillaEgg(4).setUnlocalizedName("GuerrillaSGEgg").setTextureName("gvcmob:guerrillasgegg");
		GameRegistry.registerItem(fn_guerrillasgegg, "GuerrillaSGEgg");
		fn_guerrillamgegg	= new GVCItemGuerrillaEgg(5).setUnlocalizedName("GuerrillaMGEgg").setTextureName("gvcmob:guerrillamgegg");
		GameRegistry.registerItem(fn_guerrillamgegg, "GuerrillaMGEgg");
		//fn_guerrillapegg	= new GVCItemGuerrillaPEgg().setUnlocalizedName("GuerrillaPEgg").setTextureName("gvcmob:guerrillapegg");
		//GameRegistry.registerItem(fn_guerrillapegg, "GuerrillaPEgg");
		//GameRegistry.registerWorldGenerator(new WWorld(),10); 
		fn_gkegg	= new GVCItemGuerrillaEgg(6).setUnlocalizedName("GKEgg").setTextureName("gvcmob:gkegg");
		GameRegistry.registerItem(fn_gkegg, "GKEgg");
		fn_tankegg	= new GVCItemGuerrillaEgg(7).setUnlocalizedName("TankEgg").setTextureName("gvcmob:tankegg");
		GameRegistry.registerItem(fn_tankegg, "TankEgg");
		fn_apcegg	= new GVCItemGuerrillaEgg(8).setUnlocalizedName("APCEgg").setTextureName("gvcmob:apcegg");
		GameRegistry.registerItem(fn_apcegg, "APCEgg");
		fn_heliegg	= new GVCItemGuerrillaEgg(9).setUnlocalizedName("HeliEgg").setTextureName("gvcmob:heliegg");
		GameRegistry.registerItem(fn_heliegg, "HeliEgg");
		fn_jeepegg	= new GVCItemGuerrillaEgg(10).setUnlocalizedName("JeepEgg").setTextureName("gvcmob:jeepegg");
		GameRegistry.registerItem(fn_jeepegg, "JeepEgg");
		fn_aagegg	= new GVCItemGuerrillaEgg(11).setUnlocalizedName("AAGEgg").setTextureName("gvcmob:aagegg");
		GameRegistry.registerItem(fn_aagegg, "AAGEgg");
		
		
		fn_mobspawner_egg	= new GVCItemMobSpawnerEgg(0).setUnlocalizedName("MobSpawnerEgg").setTextureName("gvcmob:mobspawneregg");
		GameRegistry.registerItem(fn_mobspawner_egg, "MobSpawnerEgg");
		fn_mobspawner2_egg	= new GVCItemMobSpawnerEgg(1).setUnlocalizedName("MobSpawner2Egg").setTextureName("gvcmob:mobspawner2egg");
		GameRegistry.registerItem(fn_mobspawner2_egg, "MobSpawner2Egg");
		
		
		fn_flagegg	= new GVCItemGuerrillaFlagEgg().setUnlocalizedName("FlagEgg").setTextureName("gvcmob:flagegg");
		GameRegistry.registerItem(fn_flagegg, "FlagEgg");
		
		
		fn_pmcegg	= new GVCItemGuerrillaPMCEgg(0).setUnlocalizedName("PMCEgg").setTextureName("gvcmob:pmcegg");
		GameRegistry.registerItem(fn_pmcegg, "PMCEgg");
		fn_pmcspegg	= new GVCItemGuerrillaPMCEgg(1).setUnlocalizedName("PMCSPEgg").setTextureName("gvcmob:pmcspegg");
		GameRegistry.registerItem(fn_pmcspegg, "PMCSPEgg");
		fn_pmcmgegg	= new GVCItemGuerrillaPMCEgg(2).setUnlocalizedName("PMCMGEgg").setTextureName("gvcmob:pmcmgegg");
		GameRegistry.registerItem(fn_pmcmgegg, "PMCMGEgg");
		fn_pmcrpgegg	= new GVCItemGuerrillaPMCEgg(3).setUnlocalizedName("PMCRPGEgg").setTextureName("gvcmob:pmcrpgegg");
		GameRegistry.registerItem(fn_pmcrpgegg, "PMCRPGEgg");
		fn_pmctankegg	= new GVCItemGuerrillaPMCEgg(4).setUnlocalizedName("PMCTankEgg").setTextureName("gvcmob:pmctankegg");
		GameRegistry.registerItem(fn_pmctankegg, "PMCTankEgg");
		fn_pmcheliegg	= new GVCItemGuerrillaPMCEgg(5).setUnlocalizedName("PMCHeliEgg").setTextureName("gvcmob:pmcheliegg");
		GameRegistry.registerItem(fn_pmcheliegg, "PMCHeliEgg");
		
		fn_soldieregg	= new GVCItemGuerrillaSoldierEgg(0).setUnlocalizedName("SoldierEgg").setTextureName("gvcmob:solideregg");
		GameRegistry.registerItem(fn_soldieregg, "SoldierEgg");
		fn_soldierspegg	= new GVCItemGuerrillaSoldierEgg(1).setUnlocalizedName("SoldierSPEgg").setTextureName("gvcmob:soliderspegg");
		GameRegistry.registerItem(fn_soldierspegg, "SoldierSPEgg");
		fn_soldiermgegg	= new GVCItemGuerrillaSoldierEgg(2).setUnlocalizedName("SoldierMGEgg").setTextureName("gvcmob:solidermgegg");
		GameRegistry.registerItem(fn_soldiermgegg, "SoldierMGEgg");
		fn_soldierrpgegg	= new GVCItemGuerrillaSoldierEgg(3).setUnlocalizedName("SoldierRPGEgg").setTextureName("gvcmob:soliderrpgegg");
		GameRegistry.registerItem(fn_soldierrpgegg, "SoldierRPGEgg");
		fn_soldiertankegg	= new GVCItemGuerrillaSoldierEgg(4).setUnlocalizedName("SoldierTankEgg").setTextureName("gvcmob:solidertankegg");
		GameRegistry.registerItem(fn_soldiertankegg, "SoldierTankEgg");
		fn_soldierheliegg	= new GVCItemGuerrillaSoldierEgg(5).setUnlocalizedName("SoldierHeliEgg").setTextureName("gvcmob:soliderheliegg");
		GameRegistry.registerItem(fn_soldierheliegg, "SoldierHeliEgg");
		
	}



	public static void Debug(String pText, Object... pData) {
		if (isDebugMessage) {
			System.out.println(String.format("GVCMob-" + pText, pData));
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent pEvent) {
		
		EntityRegistry.registerModEntity(GVCEntityBulletBase.class, "BulletGuerrilla", bulletbase, this, 128, 5, true);
		EntityRegistry.registerModEntity(GVCEntityBulletGeRPG.class, "BulletGuerrillaRPG", bulletbase+1, this, 128, 5, true);
		EntityRegistry.registerModEntity(GVCEntityHeliBullet.class, "HeliBullet", bulletbase+2, this, 128, 5, true);
		
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
		
		
		
		
		//entity
		EntityRegistry.registerModEntity(GVCEntityGuerrilla.class, "Guerrilla", 0, this, 250, 1, false);
		LanguageRegistry.instance().addStringLocalization("entity.Guerrilla.name", "en_US", "Guerrilla");
		EntityRegistry.registerModEntity(GVCEntityGuerrillaSP.class, "Guerrillasp", 1, this, 250, 1, false);
		LanguageRegistry.instance().addStringLocalization("entity.Guerrillasp.name", "en_US", "GuerrillaSP");
		EntityRegistry.registerModEntity(GVCEntityGuerrillaRPG.class, "Guerrillarpg", 2, this, 250, 1, false);
		LanguageRegistry.instance().addStringLocalization("entity.Guerrillarpg.name", "en_US", "GuerrillaRPG");
		EntityRegistry.registerModEntity(GVCEntityGuerrillaSG.class, "Guerrillasg", 3, this, 250, 1, false);
		LanguageRegistry.instance().addStringLocalization("entity.Guerrillasg.name", "en_US", "GuerrillaSG");
		EntityRegistry.registerModEntity(GVCEntityGuerrillaMG.class, "Guerrillamg", 4, this, 250, 1, false);
		LanguageRegistry.instance().addStringLocalization("entity.Guerrillamg.name", "en_US", "GuerrillaMG");
		EntityRegistry.registerModEntity(GVCEntityGuerrillaBM.class, "Bommer", 5, this, 250, 1, false);
		LanguageRegistry.instance().addStringLocalization("entity.GuerrillaBM.name", "en_US", "Bommer");
		EntityRegistry.registerModEntity(GVCEntityGuerrillaP.class, "GuerrillaP", 6, this, 250, 1, false);
		LanguageRegistry.instance().addStringLocalization("entity.GuerrillaP.name", "en_US", "GuerrillaP");
		EntityRegistry.registerModEntity(GVCEntityGK.class, "GK", 7, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityTank.class, "Tank", 8, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityAPC.class, "APC", 9, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityHeli.class, "Heli", 10, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityJeep.class, "Jeep", 11, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityAAG.class, "Jeep", 12, this, 250, 1, false);
		
		EntityRegistry.registerModEntity(GVCEntityMobSpawner.class, "GVC_MobSpawner", 30, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityMobSpawner2.class, "GVC_MobSpawner2", 31, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityFlag.class, "Flag", 35, this, 250, 1, false);
		
		EntityRegistry.registerModEntity(GVCEntityPMC.class, "PMC", 40, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityPMCSP.class, "PMCSP", 41, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityPMCMG.class, "PMCMG", 42, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityPMCRPG.class, "PMCRPG", 43, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntitySoldier.class, "Solider", 44, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntitySoldierSP.class, "SoliderSP", 45, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntitySoldierMG.class, "SoliderMG", 46, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntitySoldierRPG.class, "SoliderRPG", 47, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntitySoldierTank.class, "SoliderTank", 48, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntitySoldierHeli.class, "SoliderHeli", 49, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityPMCTank.class, "PMCTank", 50, this, 250, 1, false);
		EntityRegistry.registerModEntity(GVCEntityPMCHeli.class, "PMCHeli", 51, this, 250, 1, false);

		
		
		//spawn
		for(BiomeGenBase biome : biomeList)
		{
			if(biome!=null)
			{
				if(cfg_canspawnguerrilla == true){
					EntityRegistry.addSpawn(GVCEntityGuerrilla.class, cfg_guerrillaspawnnomal*8, cfg_guerrillaspawnnomal*2, cfg_guerrillaspawnnomal*4, EnumCreatureType.monster, biome);
					EntityRegistry.addSpawn(GVCEntityGuerrillaSP.class, cfg_guerrillaspawnnomal*2, cfg_guerrillaspawnnomal, cfg_guerrillaspawnnomal*2, EnumCreatureType.monster, biome);
					EntityRegistry.addSpawn(GVCEntityGuerrillaRPG.class, cfg_guerrillaspawnnomal*2, cfg_guerrillaspawnnomal, cfg_guerrillaspawnnomal*2, EnumCreatureType.monster, biome);
					EntityRegistry.addSpawn(GVCEntityGuerrillaSG.class, cfg_guerrillaspawnnomal*2, cfg_guerrillaspawnnomal, cfg_guerrillaspawnnomal*2, EnumCreatureType.monster, biome);
					//EntityRegistry.addSpawn(GVCEntityGuerrillaMG.class, 10, 5, 10, EnumCreatureType.monster, biome);
					EntityRegistry.addSpawn(GVCEntityGuerrillaBM.class, cfg_guerrillaspawnnomal*2, cfg_guerrillaspawnnomal, cfg_guerrillaspawnnomal*2, EnumCreatureType.monster, biome);
					
					//EntityRegistry.addSpawn(GVCEntityAPC.class, cfg_guerrillaspawntank+1, cfg_guerrillaspawntank+1, (cfg_guerrillaspawntank+1)*2, EnumCreatureType.monster, biome);
					EntityRegistry.addSpawn(GVCEntityTank.class, cfg_guerrillaspawntank, cfg_guerrillaspawntank, cfg_guerrillaspawntank*2, EnumCreatureType.monster, biome);
					EntityRegistry.addSpawn(GVCEntityJeep.class, cfg_guerrillaspawntank+3, cfg_guerrillaspawntank+3, (cfg_guerrillaspawntank+3)*2, EnumCreatureType.monster, biome);
					if(biome!=BiomeGenBase.hell)
					EntityRegistry.addSpawn(GVCEntityHeli.class, cfg_guerrillaspawntank, cfg_guerrillaspawntank, cfg_guerrillaspawntank*2, EnumCreatureType.monster, biome);
							
		        }
				if(cfg_canspawnsolider == true){
					//EntityRegistry.addSpawn(GVCEntityPMC.class, 20, 5, 10, EnumCreatureType.monster, biome);
					EntityRegistry.addSpawn(GVCEntitySoldier.class, cfg_guerrillaspawnnomal*8, cfg_guerrillaspawnnomal*2, cfg_guerrillaspawnnomal*4, EnumCreatureType.monster, biome);
					EntityRegistry.addSpawn(GVCEntitySoldierSP.class, cfg_guerrillaspawnnomal*2, cfg_guerrillaspawnnomal, cfg_guerrillaspawnnomal*2, EnumCreatureType.monster, biome);
					EntityRegistry.addSpawn(GVCEntitySoldierMG.class, cfg_guerrillaspawnnomal*2, cfg_guerrillaspawnnomal, cfg_guerrillaspawnnomal*2, EnumCreatureType.monster, biome);
					EntityRegistry.addSpawn(GVCEntitySoldierRPG.class, cfg_guerrillaspawnnomal*2, cfg_guerrillaspawnnomal, cfg_guerrillaspawnnomal*2, EnumCreatureType.monster, biome);
					//EntityRegistry.addSpawn(GVCEntitySoldierTank.class, cfg_guerrillaspawntank, cfg_guerrillaspawntank, cfg_guerrillaspawntank*2, EnumCreatureType.monster, biome);
					if(biome!=BiomeGenBase.hell)
					EntityRegistry.addSpawn(GVCEntitySoldierHeli.class, cfg_guerrillaspawntank, cfg_guerrillaspawntank, cfg_guerrillaspawntank*2, EnumCreatureType.monster, biome);
		        }
		/*EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.desert});
		EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.extremeHills});
		*/
		//EntityRegistry.addSpawn(GVCEntityGuerrilla.class, 40, 5, 10, EnumCreatureType.monster);
			}
		}
		GameRegistry.addRecipe(new ItemStack(fn_pmcegg, 1),
				"bib",
				" e ",
				"bib",
				'b', Blocks.emerald_block,
				'i', fn_g36,
				'e', Items.egg
			);
		GameRegistry.addRecipe(new ItemStack(fn_pmcspegg, 1),
				"bib",
				" e ",
				"bib",
				'b', Blocks.emerald_block,
				'i', fn_m110,
				'e', Items.egg
			);
		GameRegistry.addRecipe(new ItemStack(fn_pmcmgegg, 1),
				"bib",
				" e ",
				"bib",
				'b', Blocks.emerald_block,
				'i', fn_mg36,
				'e', Items.egg
			);
		GameRegistry.addRecipe(new ItemStack(fn_pmcrpgegg, 1),
				"bib",
				" e ",
				"bib",
				'b', Blocks.emerald_block,
				'i', fn_rpg7,
				'e', Items.egg
			);
		/*
		GameRegistry.addRecipe(new ItemStack(fn_pmctankegg, 1),
				" e ",
				"bbb", 
				'b', Blocks.emerald_block,
				'e', Items.egg
			);
		GameRegistry.addRecipe(new ItemStack(fn_pmcheliegg, 1),
				"e",
				"b", 
				'b', Blocks.emerald_block,
				'e', Items.egg
			);
		
		*/
		GameRegistry.addRecipe(new ItemStack(fn_flagegg, 1),
				"ddd",
				"ded",
				"ddd", 
				'd', Items.diamond,
				'e', Items.egg
			);
		
		
		proxy.reisterRenderers();
		/*if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
		{ 
			RenderingRegistry.registerEntityRenderingHandler(GVCEntityBulletBase.class, new GVCRenderBulletBase());
			RenderingRegistry.registerEntityRenderingHandler(GVCEntityBulletGeRPG.class, new GVCRenderBulletBaseRPG());
			
			RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrilla.class, new GVCRenderGuerrilla());
			RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrillaSP.class, new GVCRenderGuerrillaSP());
			RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrillaRPG.class, new GVCRenderGuerrillaRPG());
			RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrillaSG.class, new GVCRenderGuerrillaSG());
			RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrillaMG.class, new GVCRenderGuerrillaMG());
			RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrillaBM.class, new GVCRenderGuerrillaBM());
			RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrillaP.class, new GVCRenderGuerrillaP());
		} */

		
		if(this.cfg_setCamp==true){
		GameRegistry.registerWorldGenerator(new WWorld(),10);
		
		//MinecraftForge.EVENT_BUS.register(new GVCEventHandlerCreatCamp());
		}
		
		
		BlockDispenser.dispenseBehaviorRegistry.putObject(this.fn_flagegg,new  IBehaviorDispenseItem(){
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
		              GVCEntityFlag entityskeleton = new GVCEntityFlag(world);
		              entityskeleton.setLocationAndAngles(x, y, z,  0, 0);
		              world.spawnEntityInWorld(entityskeleton);
		              return var2.splitStack(var2.stackSize-1);
		       }
		});
		
		int D = Short.MAX_VALUE;
		
		
	}
	
}
