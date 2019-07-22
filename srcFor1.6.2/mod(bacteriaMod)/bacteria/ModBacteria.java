package mod.bacteria;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.event.world.*;
import mod.bacteria.item.*;
import mod.bacteria.block.*;
import mod.bacteria.tileentity.*;

@Mod(modid = "Bacteria Craft", name = "Bacteria Craft",version="Alpha1.6.2_0")
@NetworkMod(clientSideRequired=true, serverSideRequired=false,channels={"GCBact","UMBact"},packetHandler = PacketHandler.class)

public class ModBacteria {
	public static Item bacteria;
	public static int bacteriaID = 28520;
	public static Item emptySchale;
	public static int emptySchaleID = 28521;
	public static Item schaleWithAger;
	public static int schaleWithAgerID = 28522;
	
	public static Block genomeChanger;
	public static int genomeChangerID =801;
	public static Block U_MChanger;
	public static int U_MChangerID=802;
	public static Block Incubator;
	public static int IncubatorID=803;
	
	public static int GenomeChangerGuiID = 1000;
	public static int U_MChangerGuiID = 1001;
	public static int IncubatorGuiID = 1002;
	
	@Instance("Bacteria Craft")
	public static ModBacteria instance;
	
	
	 
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    	
      	bacteria = new Bacteria(bacteriaID);
    		GameRegistry.registerItem(bacteria, "bacteria");
        
       	emptySchale = new EmptySchale(emptySchaleID);
    		GameRegistry.registerItem(emptySchale, "empty schale");
    		
      	schaleWithAger = new SchaleWithAger(schaleWithAgerID);
       		GameRegistry.registerItem(schaleWithAger, "schale with ager");
       		
       	genomeChanger = new BlockGenomeChanger(genomeChangerID, Material.rock);
    		GameRegistry.registerBlock(genomeChanger, "genome changer");
    		
    	U_MChanger= new BlockU_MChanger(U_MChangerID, Material.rock);
    		GameRegistry.registerBlock(U_MChanger, "U_M changer");
    		
    	Incubator=new BlockIncubator(IncubatorID,Material.rock);
    		GameRegistry.registerBlock(Incubator,"incubator");
    		
    		GameRegistry.addShapelessRecipe
    		(new ItemStack(schaleWithAger, 1), 
    				new ItemStack(emptySchale),new ItemStack(Item.slimeBall),new ItemStack(Item.sugar));


    }
    
    @EventHandler
    public void Init(FMLInitializationEvent event){
    	LanguageRegistry.addName(bacteria, "bacteria");
		LanguageRegistry.instance().addNameForObject(bacteria, "ja_JP", "ç◊ã€");

    	LanguageRegistry.addName(emptySchale, "empty schale");
		LanguageRegistry.instance().addNameForObject(emptySchale, "ja_JP", "ãÛÇÃÉVÉÉÅ[Éå");
		
		LanguageRegistry.addName(schaleWithAger, "schale with ager");
		LanguageRegistry.instance().addNameForObject(schaleWithAger, "ja_JP", "ÉVÉÉÅ[ÉåÇ∆ä¶ìV");
		
		LanguageRegistry.addName(genomeChanger, "genome changer");
		LanguageRegistry.instance().addNameForObject(genomeChanger, "ja_JP", "à‚ì`éqëÄçÏëïíu");
		
		LanguageRegistry.addName(U_MChanger, "U_M changer");
		LanguageRegistry.instance().addNameForObject(U_MChanger, "ja_JP", "U_MëÄçÏëïíu");
		
		LanguageRegistry.addName(Incubator, "incubator");
		LanguageRegistry.instance().addNameForObject(Incubator, "ja_JP", "î|ó{ëïíu");
		
		//TileEntityÇÃìoò^
		GameRegistry.registerTileEntity(GenomeChangerTileEntity.class, "TileEntityGenomeChanger");
		GameRegistry.registerTileEntity(U_MChangerTileEntity.class, "TileEntityU_MChanger");
		GameRegistry.registerTileEntity(IncubatorTileEntity.class, "TileEntityIncubator");
		
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		 
		
    }
    

    
}
