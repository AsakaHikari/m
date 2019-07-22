package regulararmy.core;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import regulararmy.entity.*;
import regulararmy.entity.command.RegularArmyLeader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="monsterRegularArmy",name="monsterRegularArmy",version="1_0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class MonsterRegularArmyCore {
	@Instance("monsterRegularArmy")
	public static MonsterRegularArmyCore instance;
	public static Block blockBase;
	public static int blockBaseID;
	public static Item itemLetterOfProclamation;
	public static int itemLetterOfProclamationID;
	public static RegularArmyLeader[] leaders=new RegularArmyLeader[256];
	public static int leadersNum=-1;
	public static List<Class> entityList=new ArrayList();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
		try
		{
			cfg.load();
			Property blockProp = cfg.getBlock("monsterRegularArmy", 1701);
			Property itemProp = cfg.getItem("monsterRegularArmy", 12001);
			
			blockBaseID=blockProp.getInt();
			itemLetterOfProclamationID=itemProp.getInt();
		}
		catch (Exception e1)
		{
			FMLLog.log(Level.SEVERE, e1, "Error Message");
		}
		finally
		{
			cfg.save();
		}
		blockBase=new BlockBase(blockBaseID);
		blockBase.setUnlocalizedName("BaseBlock");
		GameRegistry.registerBlock(blockBase,"BaseBlock");
		itemLetterOfProclamation=new ItemLetterOfProclamation(itemLetterOfProclamationID);
		itemLetterOfProclamation.setUnlocalizedName("LetterOfProclamation");
		GameRegistry.registerItem(itemLetterOfProclamation,"LetterOfProclamation");

		int id =0;
		EntityRegistry.registerGlobalEntityID(EntitySniperSkeleton.class, "EntitySniperSkeleton", EntityRegistry.findGlobalUniqueEntityId(), new Color(200,200,200).getRGB(), new Color(0,0,0).getRGB());
		EntityRegistry.registerModEntity(EntitySniperSkeleton.class, "EntitySniperSkeleton", id, this, 80, 1, true);
		RenderingRegistry.registerEntityRenderingHandler(EntitySniperSkeleton.class,new RenderSniperSkeleton());
		id++;
		EntityRegistry.registerGlobalEntityID(EntityEngineer.class,"EntityEngineer",EntityRegistry.findGlobalUniqueEntityId(), new Color(200,200,200).getRGB(), new Color(0,0,0).getRGB());
		EntityRegistry.registerModEntity(EntityEngineer.class,"EntityEngineer",id,this,80,1,true);
		RenderingRegistry.registerEntityRenderingHandler(EntityEngineer.class, new RenderZombie());
		id++;
		EntityRegistry.registerGlobalEntityID(EntityScouter.class,"EntityScouter",EntityRegistry.findGlobalUniqueEntityId(), new Color(200,200,200).getRGB(), new Color(0,0,0).getRGB());
		EntityRegistry.registerModEntity(EntityScouter.class,"EntityScouter",id,this,80,1,true);
		RenderingRegistry.registerEntityRenderingHandler(EntityScouter.class, new RenderZombie());
		id++;
		
		EntityRegistry.registerGlobalEntityID(EntityFastZombie.class,"EntityFastZombie",EntityRegistry.findGlobalUniqueEntityId(), new Color(200,200,200).getRGB(), new Color(0,0,0).getRGB());
		EntityRegistry.registerModEntity(EntityFastZombie.class,"EntityFastZombie",id,this,80,1,true);
		RenderingRegistry.registerEntityRenderingHandler(EntityFastZombie.class, new RenderZombie());
		id++;
		
		LanguageRegistry.instance().addStringLocalization("item.LetterOfProclamation.name", "en_US","LetterOfProclamation");
		
		LanguageRegistry.instance().addStringLocalization("tile.BaseBlock.name","en_US","BaseBlock");
		
		LanguageRegistry.instance().addStringLocalization("entity.EntitySniperSkeleton.name", "en_US","SniperSkeleton");
		LanguageRegistry.instance().addStringLocalization("entity.EntityEngineer.name","en_US","ZombieEngineer");
		LanguageRegistry.instance().addStringLocalization("entity.EntityScouter.name","en_US","ZombieScouter");
		
		
		entityList.add(EntitySniperSkeleton.class);
		entityList.add(EntityEngineer.class);
		entityList.add(EntityFastZombie.class);
		entityList.add(EntityScouter.class);
		RegularArmyEventHandler handler=new RegularArmyEventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		TickRegistry.registerScheduledTickHandler(handler, Side.SERVER);
	}
	
	 @EventHandler
	    public void init(FMLInitializationEvent event) {
		
	 }
}
