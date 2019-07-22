package namapumpkin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@Mod(modid="PumpkinBuilderMOD", name="PumpkinBuilderMOD", version="1_0")
public class PumpkinCore
{
  @Mod.Instance("PumpkinBuilderMOD")
  public static PumpkinCore instance;
  
  public static BlockSlab pumpkin_slab;
  public static BlockSlab double_pumpkin_slab;
  public static Block pumpkin_stairs;
  
  @Mod.EventHandler
  public void preinit(FMLPreInitializationEvent event) {
	  pumpkin_slab=new BlockPumpkinSlab(false);
	  GameRegistry.registerBlock(pumpkin_slab,null, "pumpkin_slab").setHardness(1.0F).setResistance(10.0F).setStepSound(Block.soundTypeWood).setBlockName("pumpkinSlab");
	  double_pumpkin_slab=new BlockPumpkinSlab(true);
	  GameRegistry.registerBlock(double_pumpkin_slab,null, "double_pumpkin_slab").setHardness(1.0F).setResistance(10.0F).setStepSound(Block.soundTypeWood).setBlockName("doublePumpkinSlab");
	  /*try {
		  Constructor<BlockStairs> con=BlockStairs.class.getDeclaredConstructor(Block.class,int.class);
		  con.setAccessible(true);
		  pumpkin_stairs=con.newInstance(Blocks.pumpkin,0);
	  } catch (Exception e) {
		  e.printStackTrace();
	  }*/
	  pumpkin_stairs=new BlockPumpkinStairs(Blocks.pumpkin,0).setBlockName("stairsPumpkin");
	  GameRegistry.registerBlock(pumpkin_stairs, "pumpkin_stairs");
	  GameRegistry.registerItem(new ItemPumpkinSlab(pumpkin_slab,pumpkin_slab,double_pumpkin_slab,false), "pumpkin_slab");
	  GameRegistry.registerItem(new ItemPumpkinSlab(double_pumpkin_slab,pumpkin_slab,double_pumpkin_slab,true), "double_pumpkin_slab");
	  
	  GameRegistry.addRecipe(new ItemStack(pumpkin_slab,6), "XXX",'X',Blocks.pumpkin);
	  GameRegistry.addRecipe(new ItemStack(pumpkin_stairs,4),"X  ","XX ","XXX",'X',Blocks.pumpkin);
	  GameRegistry.addRecipe(new ItemStack(pumpkin_stairs,4),"  X"," XX","XXX",'X',Blocks.pumpkin);
  }
  
}