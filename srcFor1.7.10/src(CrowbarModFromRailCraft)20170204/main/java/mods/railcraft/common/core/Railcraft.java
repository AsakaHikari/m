package mods.railcraft.common.core;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Ints;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;

import java.io.File;
import java.util.Iterator;

import mods.railcraft.common.carts.LinkageManager;
import net.minecraft.block.Block;
import net.minecraft.command.CommandHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.apache.logging.log4j.Level;


public final class Railcraft
{
  public static final String MOD_ID = "Railcraft";
  public static final String VERSION = "9.12.2.0";
  public static final String MC_VERSION = "[1.7.10,1.8)";

  @Mod.Instance("Railcraft")
  public static Railcraft instance;

  public static Railcraft getMod()
  {
    return instance;
  }

  public static String getModId() {
    return "Railcraft";
  }

  public static String getVersion() {
    return "9.12.2.0";
  }


  @Mod.EventHandler
  public void serverCleanUp(FMLServerStoppingEvent event) {
    LinkageManager.reset();
  }

}