package regulararmy.core;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import regulararmy.entity.*;
import regulararmy.entity.command.RegularArmyLeader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="monsterRegularArmy",name="monsterRegularArmy",version="ALPHA1_5_4")
public class MonsterRegularArmyCore {
	@Instance("monsterRegularArmy")
	public static MonsterRegularArmyCore instance;
	public static Block blockBase;
	public static Block blockMonster;
	public static Item itemLetterOfProclamation;
	public static Item itemLetterOfPeace;
	public static RegularArmyLeader[] leaders=new RegularArmyLeader[256];
	public static int leadersNum=-1;
	public static long lastTimeMillis;
	public static int laggyTimer=0;
	public static boolean logBlock;
	public static boolean logEntity;
	public static boolean logRegion;
	public static List<Class> entityList=new ArrayList();

	public static List<String> entityIDList=new ArrayList();
	
	@SidedProxy(clientSide="regulararmy.core.RenderClient",serverSide="regulararmy.core.RenderProxy")
	public static RenderProxy proxy;
	
	public static Block[] blocksDoNotDrop;
	public static byte[] blocksDoNotDropMeta;
	Property blocksDoNotDropP;
	public static Item[] weapons;
	public static int[] weaponsDamage;
	public static int[] weaponsTier;
	Property weaponsAndTiersP;
	public static Item[] helms;
	public static int[] helmsDamage;
	public static int[] helmsTier;
	Property helmsAndTiersP;
	public static Item[] chests;
	public static int[] chestsDamage;
	public static int[] chestsTier;
	Property chestsAndTiersP;
	public static Item[] legs;
	public static int[] legsDamage;
	public static int[] legsTier;
	Property legsAndTiersP;
	public static Item[] boots;
	public static int[] bootsDamage;
	public static int[] bootsTier;
	Property bootsAndTiersP;
	
	public static Item[] results;
	public static int[] resultsDamage;
	public static int[] resultsTier;
	Property resultsAndTiersP;
	public static int spawnRange;
	public static int maxWave;
	public static int firstWave;
	public static int waveMobVanish;
	public static int maxHP;
	public static int spawnInterval;
	public static int waveInterval;
	public static int searchSpawnWaveInterval;
	public static double unitMultiplier;
	public static boolean isBowgun;
	public static boolean isMachinebow;
	public static boolean doTargetPlayers;
	public static int maxSpawnHeight;
	public static int minSpawnHeight;
	public static boolean doDropItem;
	public static int noAttackWhenLaggy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		
		List<MRAEntityData> dataList=new ArrayList();
		dataList.add(new MRAEntityData(EntitySniperSkeleton.class, 2f, 20f, 1.2f, 1f, 6, 4, 9999,3, "EntitySniperSkeletonMRA"));
		dataList.add(new MRAEntityData(EntitySkeletonR.class,1f,10f,1.2f,1f,0,0,9999,3, "EntitySkeletonMRA"));
		dataList.add(new MRAEntityData(EntityEngineer.class,3f,-1f,1.2f,0.4f,0,0,9999,-1,"EntityEngineerMRA"));
		dataList.add(new MRAEntityData(EntityScouter.class,1f,0f,1.2f,0f,0,0,9999,0, "EntityScouterMRA"));
		dataList.add(new MRAEntityData(EntityFastZombie.class,0.5f,1f,1.2f,1f,0,0,9999,4,"EntityFastZombieMRA"));
		dataList.add(new MRAEntityData(EntityCreeperR.class,3f,20f,1.2f,0.5f,10,3,30,2,"EntityCreeperMRA"));
		dataList.add(new MRAEntityData(EntityZombieSpearer.class,0.5f,3f,1.2f,1f,0,0,9999,4,"EntityZombieSpearerMRA"));
		dataList.add(new MRAEntityData(EntityZombieLongSpearer.class,0.5f,3f,1.2f,1f,0,0,9999,4,"EntityZombieLongSpearerMRA"));
		dataList.add(new MRAEntityData(EntityCatapult.class,3f,30f,1.2f,1f,15,6,50,1,"EntityCatapultMRA"));
		dataList.add(new MRAEntityData(EntityCannon.class,3f,30f,1.2f,1f,30,10,70,1,"EntityCannonMRA"));
		EntityRegistry.registerGlobalEntityID(EntityStone.class, "EntityStoneMRA", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityStone.class, "EntityStoneMRA", MRAEntityData.nextId++, MonsterRegularArmyCore.instance, 80, 1, true);
		
		Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
		cfg.load();

		blocksDoNotDropP= cfg.get("Monsters Config", "Blocks which do not drops when broken by engineers", 
				"stone;0,grass,dirt,cobblestone,sand","write as \"NAME;META\" . you don't have to write MetaDataValue.");
		weaponsAndTiersP=cfg.get("Monsters Config", "Weapons which zombies takes and tiers", 
				"wooden_axe;0-0,wooden_sword-1,iron_shovel-1,stone_sword-2,iron_sword-3,diamond_sword-4,diamond_sword-5",
				"write as \"NAME;DamageValue-TIER\" you don't have to write DamageValue \n tier:0-5 0 is common and 5 is rare. ex)iron shovel:1 diamond sword:4");
		helmsAndTiersP=cfg.get("Monsters Config", "Helms which zombies takes and tiers",
				"golden_helmet-0,leather_helmet-1,chainmail_helmet-2,iron_helmet-3,diamond_helmet-4,diamond_helmet-5",
				"write as \"NAME;DamageValue-TIER\" you don't have to write DamageValue \n tier:0-5 0 is common and 5 is rare.");
		chestsAndTiersP=cfg.get("Monsters Config", "Chestplates which zombies takes and tiers",
				"golden_chestplate-0,leather_chestplate-1,chainmail_chestplate-2,iron_chestplate-3,diamond_chestplate-4,diamond_chestplate-5",
				"write as \"NAME;DamageValue-TIER\" you don't have to write DamageValue \n tier:0-5 0 is common and 5 is rare.");
		legsAndTiersP=cfg.get("Monsters Config", "Leggings which zombies takes and tiers",
				"golden_leggings-0,leather_leggings-1,chainmail_leggings-2,iron_leggings-3,diamond_leggings-4,diamond_leggings-5",
				"write as \"NAME;DamageValue-TIER\" you don't have to write DamageValue \n tier:0-5 0 is common and 5 is rare.");
		bootsAndTiersP=cfg.get("Monsters Config", "Boots which zombies takes and tiers",
				"golden_boots-0,leather_boots-1,chainmail_boots-2,iron_boots-3,diamond_boots-4,diamond_boots-5",
				"write as \"NAME;DamageValue-TIER\" you don't have to write DamageValue \n tier:0-5 0 is common and 5 is rare.");
		resultsAndTiersP=cfg.get("System Config", "Results which the base commits and tiers",
				"iron_ingot-4,redstone-3,gold_ingot-8,diamond-12",
				"write as \"NAME;DamageValue-TIER\" you don't have to write DamageValue \n tier:0 or higher.0 is common and higher is rare. ex)iron_ingot:4 diamond:12 \n "
				+ "At default, you get iron ingot once by 40 seconds in wave 10 and by 10 seconds in wave 40 on average. \n"
				+ "In case of diamond, you get it once by 2 minutes in wave 10 and by 30 seconds in wave 40 on average.");
		Property spawnRangeP=cfg.get("System Config","The distance between Base and monsters' SpawnPoint","40");
		Property maxWaveP=cfg.get("System Config","Wave num when the war ends","40");
		Property firstWaveP=cfg.get("System Config","Wave num when the war starts","1");
		cfg.get("System Config","Wave after monsters vanish",2,"Not available now");
		Property waveMobVanishP=cfg.get("System Config","v2 Wave after monsters vanish",4);
		Property maxHPP=cfg.get("System Config","HP of the Base. default:10000","10000");
		Property spawnIntervalP=cfg.get("System Config", "Time interval between the next monster unit spawns.(tick) default:200", 200,"Not available now");
		Property waveIntervalP=cfg.get("System Config", "Time interval between the next wave begins.(tick) default:600", 600);
		Property unitMultiplierP=cfg.get("System Config", "Unit multiplier. For example,when it is set to 2.0, twice many units will spawn. default:1.0", 1.0);
		Property isBowgunP=cfg.get("Monsters Config","Whether SniperSkeletons use bowgun or not","false");
		Property isMachinebowP=cfg.get("Monsters Config","Whether Skeletons use machinebow or not","false");
		Property doTargetPlayersP=cfg.get("Monsters Config","Whether monsters target players or not","true");
		Property maxSpawnHeightP=cfg.get("System Config","Max height where monsters spawn",256);
		Property minSpawnHeightP=cfg.get("System Config","Min height where monsters spawn",1);
		Property doDropItemP=cfg.get("System Config","Whether monsters drop items",true);
		Property doBlockDisappearP=cfg.get("System Config", "Whether monster blocks disappear automatically", true);
		Property noAttackWhenLaggyP=cfg.get("System Config","Whether monster stop attacking when the server is laggy. This value is maximum FPS. default:5",
				5,"Set this 0 to disable this.");
		Property logBlockP=cfg.get("System Config","Whether the game output debug log of learning blocks' dangerousness",false);
		Property logEntityP=cfg.get("System Config","Whether the game output debug log of learning entities' dangerousness",false);
		Property logRegionP=cfg.get("System Config","Whether the game output debug log of learning areas' dangerousness",false);
		
		spawnRange=spawnRangeP.getInt();
		maxWave=maxWaveP.getInt();
		firstWave=firstWaveP.getInt();
		waveMobVanish=waveMobVanishP.getInt();
		maxHP=maxHPP.getInt();
		isBowgun=isBowgunP.getBoolean();
		isMachinebow=isMachinebowP.getBoolean();
		doTargetPlayers=doTargetPlayersP.getBoolean();
		spawnInterval=spawnIntervalP.getInt();
		waveInterval=waveIntervalP.getInt();
		unitMultiplier=unitMultiplierP.getDouble();
		maxSpawnHeight=maxSpawnHeightP.getInt();
		minSpawnHeight=minSpawnHeightP.getInt();
		doDropItem=doDropItemP.getBoolean();
		noAttackWhenLaggy=noAttackWhenLaggyP.getInt();
		logBlock=logBlockP.getBoolean();
		logEntity=logEntityP.getBoolean();
		logRegion=logRegionP.getBoolean();
		
		cfg.save();
		
		Configuration cfgMonster=new Configuration(new File(e.getModConfigurationDirectory(),"monsterRegularArmy_Monsters.cfg"));
		cfgMonster.load();
		for(MRAEntityData data:dataList){
			Property bsw=cfgMonster.get(data.unlocalizedName, "Basic spawn weight of this entity", data.basicWeight);
			Property ctt=cfgMonster.get(data.unlocalizedName, "Number of wave that this entities spawn most", data.centreTier);
			Property mint=cfgMonster.get(data.unlocalizedName,"Number of wave that this entities spawn at least",data.minTier);
			Property maxt=cfgMonster.get(data.unlocalizedName,"Number of wave that this entities spawn at most",data.maxTier);
			Property numb=cfgMonster.get(data.unlocalizedName,"Number of unit of this monster",data.numberOfMember,
					"In case of this value is negative, these monsters are followed by other monsters");
			MRAEntityData newData=new MRAEntityData(data.entityClass,data.crowdCostPerBlock,data.fightRange,data.jumpHeight,(float)bsw.getDouble(),
					ctt.getInt(),mint.getInt(),maxt.getInt(),numb.getInt(),data.unlocalizedName);
			newData.activateThisData();
		}
		cfgMonster.save();
		
		blockBase=new BlockBase().setBlockName("BaseBlock").setBlockTextureName("monsterregulararmy:base");
		GameRegistry.registerBlock(blockBase,"BaseBlock");
		blockMonster=new BlockMonster().setBlockName("MonsterBlock").setBlockTextureName("monsterregulararmy:monster");
		GameRegistry.registerBlock(blockMonster, "MonsterBlock");
		blockMonster.setTickRandomly(doBlockDisappearP.getBoolean());
		itemLetterOfProclamation=new ItemLetterOfProclamation().setUnlocalizedName("LetterOfProclamation").setTextureName("monsterregulararmy:letter");
		GameRegistry.registerItem(itemLetterOfProclamation,"LetterOfProclamation");
		itemLetterOfPeace=new ItemLetterOfPeace().setUnlocalizedName("LetterOfPeace").setTextureName("monsterregulararmy:letter_peace");
		GameRegistry.registerItem(itemLetterOfPeace,"LetterOfPeace");
		
		GameRegistry.addShapelessRecipe(new ItemStack(itemLetterOfProclamation),Items.paper,Items.iron_sword);
		GameRegistry.addRecipe(new ItemStack(itemLetterOfPeace),"GLG",'L',Items.paper,'G',Items.gold_nugget);
		GameRegistry.addRecipe(new ItemStack(blockBase,1,0),"IBI", 'I',Items.iron_sword,'B',Blocks.iron_block);
		
		proxy.init();
		
		RegularArmyEventHandler handler=new RegularArmyEventHandler();
		FMLCommonHandler.instance().bus().register(handler);
		MinecraftForge.EVENT_BUS.register(handler);
		ForgeChunkManager.setForcedChunkLoadingCallback(this,new ShellChunkManager());
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		registerBlocksFromString(blocksDoNotDropP.getString());
		registerWeaponsAndTierFromString(weaponsAndTiersP.getString());
		registerResultsAndTierFromString(resultsAndTiersP.getString());
		registerHelmsAndTierFromString(helmsAndTiersP.getString());
		registerChestsAndTierFromString(chestsAndTiersP.getString());
		registerLegsAndTierFromString(legsAndTiersP.getString());
		registerBootsAndTierFromString(bootsAndTiersP.getString());
	}

	 public void registerBlocksFromString(String s){
		 String[] elements=s.split(",");
		 Block[] blocks=new Block[elements.length];
		 byte[] metas=new byte[elements.length];
		 for(int i=0;i<elements.length;i++){
			 String[] elms2=elements[i].split(";");
			 blocks[i]=tryFindingBlock(elms2[0]);
			 if(elms2.length==1){
				 metas[i]=-1;
			 }else{
				 metas[i]=Byte.parseByte(elms2[1]);
			 }
			 
		 }
		 blocksDoNotDrop=blocks;
		 blocksDoNotDropMeta=metas;
	 }
	 
	 public void registerWeaponsAndTierFromString(String s){
		 String[] elements=s.split(",");
		 weapons=new Item[elements.length];
		 weaponsDamage=new int[elements.length];
		 weaponsTier=new int[elements.length];
		 for(int i=0;i<elements.length;i++){
			 String[] elms2=elements[i].split("-");
			 if(elms2.length!=2){
				 System.out.println(elements[i]+" at weapons tier config is wrong!");
			 }
			 String[] elms3=elms2[0].split(";");
			 
			 if(elms3.length==1){
				 weapons[i]=tryFindingItem(elms2[0]);
				 weaponsDamage[i]=0;
				 weaponsTier[i]=Integer.parseInt(elms2[1]);
				 if(weapons[i]==null){
					 System.out.println("Weapons named '" +elms2[0]+"' do not exist!");
				 }
			 }else{
				 weapons[i]=tryFindingItem(elms3[0]);
				 weaponsDamage[i]=Integer.parseInt(elms3[1]);
				 weaponsTier[i]=Integer.parseInt(elms2[1]);
				 if(weapons[i]==null){
					 System.out.println("Weapons named '" +elms3[0]+"' do not exist!");
				 }
			 }
			 
		 }
	 }

	 public void registerHelmsAndTierFromString(String s){
		 String[] elements=s.split(",");
		 helms=new Item[elements.length];
		 helmsDamage=new int[elements.length];
		 helmsTier=new int[elements.length];
		 for(int i=0;i<elements.length;i++){
			 String[] elms2=elements[i].split("-");
			 if(elms2.length!=2){
				 System.out.println(elements[i]+" at armors tier config is wrong!");
			 }
			 String[] elms3=elms2[0].split(";");

			 if(elms3.length==1){
				 helms[i]=tryFindingItem(elms2[0]);
				 helmsDamage[i]=0;
				 helmsTier[i]=Integer.parseInt(elms2[1]);
				 if(helms[i]==null){
					 System.out.println("Helmets named '" +elms2[0]+"' do not exist!");
				 }
			 }else{
				 helms[i]=tryFindingItem(elms3[0]);
				 helmsDamage[i]=Integer.parseInt(elms3[1]);
				 helmsTier[i]=Integer.parseInt(elms2[1]);
				 if(helms[i]==null){
					 System.out.println("Helmets named '" +elms3[0]+"' do not exist!");
				 }
			 }

		 }
	 }

	 public void registerChestsAndTierFromString(String s){
		 String[] elements=s.split(",");
		 chests=new Item[elements.length];
		 chestsDamage=new int[elements.length];
		 chestsTier=new int[elements.length];
		 for(int i=0;i<elements.length;i++){
			 String[] elms2=elements[i].split("-");
			 if(elms2.length!=2){
				 System.out.println(elements[i]+" at armors tier config is wrong!");
			 }
			 String[] elms3=elms2[0].split(";");

			 if(elms3.length==1){
				 chests[i]=tryFindingItem(elms2[0]);
				 chestsDamage[i]=0;
				 chestsTier[i]=Integer.parseInt(elms2[1]);
				 if(chests[i]==null){
					 System.out.println("Chestplates named '" +elms2[0]+"' do not exist!");
				 }
			 }else{
				 chests[i]=tryFindingItem(elms3[0]);
				 chestsDamage[i]=Integer.parseInt(elms3[1]);
				 chestsTier[i]=Integer.parseInt(elms2[1]);
				 if(chests[i]==null){
					 System.out.println("Chestplates named '" +elms3[0]+"' do not exist!");
				 }
			 }

		 }
	 }

	 public void registerLegsAndTierFromString(String s){
		 String[] elements=s.split(",");
		 legs=new Item[elements.length];
		 legsDamage=new int[elements.length];
		 legsTier=new int[elements.length];
		 for(int i=0;i<elements.length;i++){
			 String[] elms2=elements[i].split("-");
			 if(elms2.length!=2){
				 System.out.println(elements[i]+" at armors tier config is wrong!");
			 }
			 String[] elms3=elms2[0].split(";");

			 if(elms3.length==1){
				 legs[i]=tryFindingItem(elms2[0]);
				 legsDamage[i]=0;
				 legsTier[i]=Integer.parseInt(elms2[1]);
				 if(legs[i]==null){
					 System.out.println("Leggings named '" +elms2[0]+"' do not exist!");
				 }
			 }else{
				 legs[i]=tryFindingItem(elms3[0]);
				 legsDamage[i]=Integer.parseInt(elms3[1]);
				 legsTier[i]=Integer.parseInt(elms2[1]);
				 if(legs[i]==null){
					 System.out.println("Leggings named '" +elms3[0]+"' do not exist!");
				 }
			 }

		 }
	 }

	 public void registerBootsAndTierFromString(String s){
		 String[] elements=s.split(",");
		 boots=new Item[elements.length];
		 bootsDamage=new int[elements.length];
		 bootsTier=new int[elements.length];
		 for(int i=0;i<elements.length;i++){
			 String[] elms2=elements[i].split("-");
			 if(elms2.length!=2){
				 System.out.println(elements[i]+" at armors tier config is wrong!");
			 }
			 String[] elms3=elms2[0].split(";");

			 if(elms3.length==1){
				 boots[i]=tryFindingItem(elms2[0]);
				 bootsDamage[i]=0;
				 bootsTier[i]=Integer.parseInt(elms2[1]);
				 if(boots[i]==null){
					 System.out.println("Boots named '" +elms2[0]+"' do not exist!");
				 }
			 }else{
				 boots[i]=tryFindingItem(elms3[0]);
				 bootsDamage[i]=Integer.parseInt(elms3[1]);
				 bootsTier[i]=Integer.parseInt(elms2[1]);
				 if(boots[i]==null){
					 System.out.println("Boots named '" +elms3[0]+"' do not exist!");
				 }
			 }
			 
		 }
	 }

	 public void registerResultsAndTierFromString(String s){
		 String[] elements=s.split(",");
		 results=new Item[elements.length];
		 resultsDamage=new int[elements.length];
		 resultsTier=new int[elements.length];
		 for(int i=0;i<elements.length;i++){
			 String[] elms2=elements[i].split("-");
			 if(elms2.length!=2){
				 System.out.println(elements[i]+" at results tier config is wrong!");
			 }
			 String[] elms3=elms2[0].split(";");
			 
			 if(elms3.length==1){
				 results[i]=tryFindingItem(elms2[0]);
				 resultsDamage[i]=0;
				 resultsTier[i]=Integer.parseInt(elms2[1]);
				 if(results[i]==null){
					 System.out.println("Item named '" +elms2[0]+"' do not exist!");
				 }
			 }else{
				 results[i]=tryFindingItem(elms3[0]);
				 resultsDamage[i]=Integer.parseInt(elms3[1]);
				 resultsTier[i]=Integer.parseInt(elms2[1]);
				 if(results[i]==null){
					 System.out.println("Item named '" +elms3[0]+"' do not exist!");
				 }
			 }
			 
		 }
	 }
	 
	 public Block tryFindingBlock(String s){
		 return Block.getBlockFromName(s);
	 }
	 
	 public Item tryFindingItem(String s){
		 Item item = GameData.getItemRegistry().getObject(s);
	        if (item != null)
	        {
	            return item;
	        }
	        
	        Block block = GameData.getBlockRegistry().getObject(s);
	        if (block != Blocks.air)
	        {
	            return Item.getItemFromBlock(block);
	        }
	        return null;
	 }
	 
}
