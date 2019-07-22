package mod.core;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

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

@Mod(modid="redstoneObsidian",name="redstoneObsidian",version="1_0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class RedstoneObsidianCore {
	@Instance("redstoneObsidian")
	public static RedstoneObsidianCore instance;
	public static Block blockRSObsidian;
	public static int blockRSObsidianID;
	public static Block blockActiveRSObsidian;
	public static int blockActiveRSObsidianID;
	public static Block blockAnytimeActiveRSObsidian;
	public static int blockAnytimeActiveRSObsidianID;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
		try
		{
			cfg.load();
			Property RSO  = cfg.getBlock("RedStoneObsidian", 3300);
			Property RSOA=cfg.getBlock("RedStoneObsidianActive", 3301);
			Property RSOAA=cfg.getBlock("RedStoneObsidianActiveAnytime",3302);
			this.blockRSObsidianID  = RSO.getInt();
			this.blockActiveRSObsidianID=RSOA.getInt();
			this.blockAnytimeActiveRSObsidianID=RSOAA.getInt();
		}
		catch (Exception e1)
		{
			FMLLog.log(Level.SEVERE, e1, "Error Message");
		}
		finally
		{
			cfg.save();
		}

		this.blockRSObsidian=new BlockRSObsidian(this.blockRSObsidianID,false);
		this.blockRSObsidian.setTextureName("rsobsidian:RSObsidian");
		this.blockActiveRSObsidian=new BlockRSObsidian(this.blockActiveRSObsidianID,true);
		this.blockActiveRSObsidian.setTextureName("rsobsidian:RSObsidianActivated");
		this.blockAnytimeActiveRSObsidian=new BlockRSObsidianAT(this.blockAnytimeActiveRSObsidianID);
		this.blockAnytimeActiveRSObsidian.setTextureName("rsobsidian:RSObsidianActivatedAnytime");
		GameRegistry.registerBlock(blockRSObsidian, "RedStoneObsidian");
		GameRegistry.registerBlock(blockActiveRSObsidian, "RedStoneObsidianActive");
		GameRegistry.registerBlock(blockAnytimeActiveRSObsidian, "RedStoneObsidianActiveAnytime");
		
		GameRegistry.addRecipe(new ItemStack(this.blockRSObsidian,6), new Object[]{"XXX","YYY","XXX",'X',Block.obsidian,'Y',Item.redstone});
		GameRegistry.addRecipe(new ItemStack(this.blockAnytimeActiveRSObsidian,6), new Object[]{"XXX","YZY","XXX",'X',Block.obsidian,'Y',Item.redstone,'Z',Block.torchRedstoneActive});
		
	}
	
	 @EventHandler
	    public void init(FMLInitializationEvent event) {
		 LanguageRegistry.addName(this.blockRSObsidian, "RedStoneObsidian");
		 LanguageRegistry.addName(this.blockAnytimeActiveRSObsidian, "RedStoneObsidianAT");
	 }
}
